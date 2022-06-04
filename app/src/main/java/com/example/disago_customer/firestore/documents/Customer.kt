package com.example.disago_customer.firestore.documents

import com.example.disago_customer.firestore.fields.DisabilityType

data class Customer(
    var name: String,
    var surname: String,
    var email: String,
    var phoneNumber: String,
    var disabilityType: DisabilityType,
    var balance: Double = 0.0
)
