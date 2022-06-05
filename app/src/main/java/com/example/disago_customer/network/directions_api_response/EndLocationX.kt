package com.example.disago_customer.network.directions_api_response


import com.squareup.moshi.Json

data class EndLocationX(
    @Json(name = "lat")
    val lat: Double? = null,
    @Json(name = "lng")
    val lng: Double? = null
)