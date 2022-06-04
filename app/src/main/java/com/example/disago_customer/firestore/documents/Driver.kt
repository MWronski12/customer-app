package com.example.disago_customer.firestore.documents

class Driver(
    var name: String,
    var surname: String,
    var email: String,
    var car: String,
    var phoneNumber: String,
    var balance: Double = 0.0
) {
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name,
            "surname" to surname,
            "email" to email,
            "car" to car,
            "phoneNumber" to phoneNumber,
            "balance" to balance
        )
    }
}
