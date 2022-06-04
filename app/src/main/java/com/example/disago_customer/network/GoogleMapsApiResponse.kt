package com.example.disago_customer.network

import com.example.disago_customer.network.google_maps_api_response_subclasses.GeocodedWaypoint
import com.example.disago_customer.network.google_maps_api_response_subclasses.Route

data class GoogleMapsApiResponse(
    val geocoded_waypoints: List<GeocodedWaypoint>,
    val routes: List<Route>,
    val status: String
)