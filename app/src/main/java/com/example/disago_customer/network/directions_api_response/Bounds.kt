package com.example.disago_customer.network.directions_api_response


import com.squareup.moshi.Json

data class Bounds(
    @Json(name = "northeast")
    val northeast: Northeast? = null,
    @Json(name = "southwest")
    val southwest: Southwest? = null
)