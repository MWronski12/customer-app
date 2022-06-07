package com.example.disago_customer.firestore.documents

import com.example.disago_customer.firestore.fields.Coordinates
import com.example.disago_customer.firestore.fields.RideStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.IgnoreExtraProperties
import com.squareup.moshi.Json


data class Ride(
    var customer: DocumentReference,
    var driver: DocumentReference? = null,
    var originLocation: String,
    var destinationLocation: String,
    var date: Timestamp = Timestamp.now(),
    var price: Double,
    var status: String = RideStatus.REQUESTED,

//    If You want to implement the map, this can be used to track drivers position
//    var driversCoordinates: Coordinates? = null
) {
    override fun toString(): String {
        return "Ride={" +
                    "customer=${customer.id}, " +
                    "driver=${driver?.id}, " +
                    "originLocation=$originLocation, " +
                    "destinationLocation=$destinationLocation, " +
                    "date=${date.toDate()}, " +
                    "price=$price, " +
                    "status=$status" +
                "}"
    }
}
