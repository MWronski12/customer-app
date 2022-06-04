package com.example.disago_customer.network.google_maps_api_response_subclasses

data class GeocodedWaypoint(
    val geocoder_status: String?,
    val place_id: String?,
    val types: List<String?>?
)