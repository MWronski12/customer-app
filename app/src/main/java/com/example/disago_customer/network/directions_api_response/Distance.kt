package com.example.disago_customer.network.directions_api_response


import com.squareup.moshi.Json

data class Distance(
    @Json(name = "text")
    val text: String,
    @Json(name = "value")
    val value: Int
)