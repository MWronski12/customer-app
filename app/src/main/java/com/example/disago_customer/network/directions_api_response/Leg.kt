package com.example.disago_customer.network.directions_api_response


import com.squareup.moshi.Json

data class Leg(
    @Json(name = "distance")
    val distance: Distance? = null,
    @Json(name = "duration")
    val duration: Duration? = null,
    @Json(name = "end_address")
    val endAddress: String,
    @Json(name = "end_location")
    val endLocation: EndLocation,
    @Json(name = "start_address")
    val startAddress: String,
    @Json(name = "start_location")
    val startLocation: StartLocation,
    @Json(name = "steps")
    val steps: List<Step>? = null,
    @Json(name = "traffic_speed_entry")
    val trafficSpeedEntry: List<Any>? = null,
    @Json(name = "via_waypoint")
    val viaWaypoint: List<Any>? = null
)