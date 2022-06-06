package com.example.disago_customer.firestore.documents

import com.example.disago_customer.firestore.fields.DisabilityType

class Customer(
    var name: String,
    var surname: String,
    var email: String,
    var phoneNumber: String,
    var disabilityType: String,
    var balance: Double = 0.0
)