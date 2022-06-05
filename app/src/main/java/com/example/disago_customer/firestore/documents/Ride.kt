package com.example.disago_customer.firestore.documents

import com.example.disago_customer.firestore.fields.RideStatus
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue

data class Ride(
    val customer: DocumentReference,
    var driver: DocumentReference?,
    val date: FieldValue?,
    val originLocation: String,
    val destinationLocation: String,
    val price: Double,
    var status: String?
)