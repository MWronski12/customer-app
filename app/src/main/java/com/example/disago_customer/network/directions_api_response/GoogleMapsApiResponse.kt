package com.example.disago_customer.network.directions_api_response


import com.squareup.moshi.Json

data class GoogleMapsApiResponse(
    @Json(name = "geocoded_waypoints")
    val geocodedWaypoints: List<GeocodedWaypoint>,
    @Json(name = "routes")
    val routes: List<Route?>,
    @Json(name = "status")
    val status: String
)