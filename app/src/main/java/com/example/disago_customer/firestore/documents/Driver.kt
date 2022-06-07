package com.example.disago_customer.firestore.documents

data class Driver(
    var name: String,
    var surname: String,
    var email: String,
    var car: String,
    var phoneNumber: String,
    var balance: Double = 0.0
) {
    override fun toString(): String {
        return "Customer={" +
                "name=$name, " +
                "surname=$surname, " +
                "email=$email, " +
                "phoneNumber=$phoneNumber, " +
                "car=$car, " +
                "balance=$balance" +
                "}"
    }
}