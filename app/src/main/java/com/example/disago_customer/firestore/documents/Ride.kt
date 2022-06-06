package com.example.disago_customer.firestore.documents

import com.example.disago_customer.firestore.fields.RideStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.IgnoreExtraProperties


data class Ride(
    var customer: DocumentReference,
    var driver: DocumentReference? = null,
    var originLocation: String,
    var destinationLocation: String,
    var date: Timestamp = Timestamp.now(),
    var price: Double,
    var status: String = RideStatus.ACCEPTED
)
