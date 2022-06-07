package com.example.disago_customer.firestore.documents

import com.example.disago_customer.firestore.fields.DisabilityType

class Customer(
    var name: String,
    var surname: String,
    var email: String,
    var phoneNumber: String,
    var disabilityType: String,
    var balance: Double = 0.0
) {
    override fun toString(): String {
        return "Customer={" +
                "name=$name, " +
                "surname=$surname, " +
                "email=$email, " +
                "phoneNumber=$phoneNumber, " +
                "disabilityType=$disabilityType, " +
                "balance=$balance" +
                "}"
    }
}