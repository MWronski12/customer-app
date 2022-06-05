package com.example.disago_customer.network.directions_api_response


import com.squareup.moshi.Json

data class StartLocation(
    @Json(name = "lat")
    val lat: Double,
    @Json(name = "lng")
    val lng: Double,
)