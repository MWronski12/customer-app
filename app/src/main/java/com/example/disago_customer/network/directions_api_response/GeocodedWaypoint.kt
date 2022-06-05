package com.example.disago_customer.network.directions_api_response


import com.squareup.moshi.Json

data class GeocodedWaypoint(
    @Json(name = "geocoder_status")
    val geocoderStatus: String? = null,
    @Json(name = "place_id")
    val placeId: String? = null,
    @Json(name = "types")
    val types: List<String>? = null
)