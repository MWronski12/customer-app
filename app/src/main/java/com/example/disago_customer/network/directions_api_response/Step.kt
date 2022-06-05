package com.example.disago_customer.network.directions_api_response


import com.squareup.moshi.Json

data class Step(
    @Json(name = "distance")
    val distance: DistanceX? = null,
    @Json(name = "duration")
    val duration: DurationX? = null,
    @Json(name = "end_location")
    val endLocation: EndLocationX? = null,
    @Json(name = "html_instructions")
    val htmlInstructions: String? = null,
    @Json(name = "maneuver")
    val maneuver: String? = null,
    @Json(name = "polyline")
    val polyline: Polyline? = null,
    @Json(name = "start_location")
    val startLocation: StartLocationX? = null,
    @Json(name = "travel_mode")
    val travelMode: String? = null
)