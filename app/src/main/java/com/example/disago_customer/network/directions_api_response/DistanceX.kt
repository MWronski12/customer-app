package com.example.disago_customer.network.directions_api_response


import com.squareup.moshi.Json

data class DistanceX(
    @Json(name = "text")
    val text: String? = null,
    @Json(name = "value")
    val value: Int? = null
)