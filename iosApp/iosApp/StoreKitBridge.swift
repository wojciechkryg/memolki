import Foundation
import StoreKit
import Shared

final class StoreKitBridge: NSObject, IosStoreKitBridge {

    private var productCache: [String: Product] = [:]

    func products(productIds: [String], completion: @escaping ([BillingProduct]) -> Void) {
        Task {
            completion(await fetchProducts(productIds))
        }
    }

    func purchase(productId: String, completion: @escaping (KotlinBoolean) -> Void) {
        Task {
            completion(KotlinBoolean(bool: await performPurchase(productId)))
        }
    }

    func isPurchased(productId: String, completion: @escaping (KotlinBoolean) -> Void) {
        Task {
            completion(KotlinBoolean(bool: await checkEntitlement(productId)))
        }
    }

    private func fetchProducts(_ ids: [String]) async -> [BillingProduct] {
        let cached = ids.compactMap { productCache[$0] }
        if cached.count == ids.count {
            return cached.map(Self.billingProduct)
        }
        do {
            let products = try await Product.products(for: ids)
            products.forEach { productCache[$0.id] = $0 }
            return products.map(Self.billingProduct)
        } catch {
            return []
        }
    }

    private static func billingProduct(_ product: Product) -> BillingProduct {
        BillingProduct(
            id: product.id,
            formattedPrice: product.displayPrice,
            priceMicros: NSDecimalNumber(decimal: product.price * 1_000_000).int64Value,
            currencyCode: product.priceFormatStyle.currencyCode
        )
    }

    private func resolveProduct(_ productId: String) async throws -> Product? {
        if let cached = productCache[productId] { return cached }
        let product = try await Product.products(for: [productId]).first
        if let product { productCache[productId] = product }
        return product
    }

    private func performPurchase(_ productId: String) async -> Bool {
        do {
            guard let product = try await resolveProduct(productId) else { return false }
            let result = try await product.purchase()
            guard case .success(let verification) = result,
                  case .verified(let transaction) = verification else { return false }
            await transaction.finish()
            return true
        } catch {
            return false
        }
    }

    private func checkEntitlement(_ productId: String) async -> Bool {
        for await result in Transaction.currentEntitlements {
            if case .verified(let transaction) = result, transaction.productID == productId {
                return true
            }
        }
        return false
    }
}
