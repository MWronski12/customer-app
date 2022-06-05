package com.example.disago_customer.network.directions_api_response


import com.squareup.moshi.Json

data class Route(
    @Json(name = "bounds")
    val bounds: Bounds? = null,
    @Json(name = "copyrights")
    val copyrights: String? = null,
    @Json(name = "legs")
    val legs: List<Leg>,
    @Json(name = "overview_polyline")
    val overviewPolyline: OverviewPolyline? = null,
    @Json(name = "summary")
    val summary: String? = null,
    @Json(name = "warnings")
    val warnings: List<Any>? = null,
    @Json(name = "waypoint_order")
    val waypointOrder: List<Any>? = null
)