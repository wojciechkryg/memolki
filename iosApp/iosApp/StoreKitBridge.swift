import Foundation
import StoreKit
import Shared

final class StoreKitBridge: NSObject, IosStoreKitBridge {

    private var productCache: [String: Product] = [:]
    private var pendingTransactions: [String: Transaction] = [:]
    private var transactionHandler: ((String, String) -> Void)?
    private var transactionListenerTask: Task<Void, Never>?

    func setTransactionHandler(handler: @escaping (String, String) -> Void) {
        transactionHandler = handler
        startTransactionListener()
    }

    func finishTransaction(transactionId: String) {
        guard let transaction = pendingTransactions.removeValue(forKey: transactionId) else { return }
        Task { await transaction.finish() }
    }

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

    private func startTransactionListener() {
        guard transactionListenerTask == nil else { return }
        transactionListenerTask = Task { [weak self] in
            for await result in Transaction.unfinished {
                self?.deliver(result)
            }
            for await result in Transaction.updates {
                self?.deliver(result)
            }
        }
    }

    // Kotlin grants the reward first and only then calls finishTransaction, so an
    // interrupted grant is redelivered through Transaction.unfinished on next launch.
    private func deliver(_ result: VerificationResult<Transaction>) {
        guard case .verified(let transaction) = result else { return }
        let transactionId = String(transaction.id)
        pendingTransactions[transactionId] = transaction
        transactionHandler?(transaction.productID, transactionId)
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
            switch result {
            case .success(let verification):
                deliver(verification)
                return true
            case .pending:
                // Ask-to-buy — the outcome arrives later through Transaction.updates.
                return true
            default:
                return false
            }
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
