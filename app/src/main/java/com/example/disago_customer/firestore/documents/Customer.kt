package com.example.disago_customer.firestore.documents

import com.example.disago_customer.firestore.fields.DisabilityType

class Customer(
//    val id: String,
    var name: String,
    var surname: String,
    var email: String,
    var phoneNumber: String,
    var disabilityType: String,
    var balance: Double = 0.0
) {
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf<String, Any>(
            "name" to name,
            "surname" to surname,
            "email" to email,
            "phoneNumber" to phoneNumber,
            "disabilityType" to disabilityType,
            "balance" to balance
        )
    }
}
