package com.example.disago_customer.network

import com.example.disago_customer.network.directions_api_response.Distance
import com.example.disago_customer.network.directions_api_response.Duration
import com.example.disago_customer.network.directions_api_response.EndLocation
import com.example.disago_customer.network.directions_api_response.StartLocation

data class GoogleMapsApiResponseRelevantData(
    val distance: Distance,
    val duration: Duration,
    val endAddress: String,
    val endLocation: EndLocation,
    val startAddress: String,
    val startLocation: StartLocation
)
