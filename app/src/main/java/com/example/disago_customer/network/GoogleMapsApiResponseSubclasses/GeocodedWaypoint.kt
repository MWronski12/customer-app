package com.example.disago_customer.network.GoogleMapsApiResponseSubclasses

data class GeocodedWaypoint(
    val geocoder_status: String?,
    val place_id: String?,
    val types: List<String?>?
)