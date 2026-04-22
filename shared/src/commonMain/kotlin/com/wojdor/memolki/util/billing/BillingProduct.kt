package com.wojdor.memolki.util.billing

data class BillingProduct(
    val id: String,
    val formattedPrice: String,
    val priceMicros: Long,
    val currencyCode: String
)
