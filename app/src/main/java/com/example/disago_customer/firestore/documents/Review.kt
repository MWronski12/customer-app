package com.example.disago_customer.firestore.documents

data class Review(
    val customer: Customer,
    val driver: Driver,
    val rating: Int
)