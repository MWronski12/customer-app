package com.example.disago_customer.firestore.documents

import com.google.firebase.firestore.DocumentReference

data class Review(
    val customer: DocumentReference,
    val driver: DocumentReference,
    val rating: Int
) {
    override fun toString(): String {
        return "Review={customer=$customer, driver=$driver, rating=$rating}"
    }
}