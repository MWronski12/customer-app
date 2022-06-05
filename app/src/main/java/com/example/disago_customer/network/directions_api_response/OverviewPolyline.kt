package com.example.disago_customer.network.directions_api_response


import com.squareup.moshi.Json

data class OverviewPolyline(
    @Json(name = "points")
    val points: String? = null
)