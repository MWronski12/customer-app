package com.example.disago_customer.network.GoogleMapsApiResponseSubclasses

data class Step(
    val distance: DistanceX?,
    val duration: DurationX?,
    val end_location: EndLocationX?,
    val html_instructions: String?,
    val maneuver: String?,
    val polyline: Polyline?,
    val start_location: StartLocationX?,
    val travel_mode: String?
)