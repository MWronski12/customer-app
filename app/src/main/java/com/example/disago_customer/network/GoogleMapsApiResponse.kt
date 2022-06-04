package com.example.disago_customer.network

import com.example.disago_customer.network.GoogleMapsApiResponseSubclasses.GeocodedWaypoint
import com.example.disago_customer.network.GoogleMapsApiResponseSubclasses.Route

data class GoogleMapsApiResponse(
    val geocoded_waypoints: List<GeocodedWaypoint>,
    val routes: List<Route>,
    val status: String
)