package com.example.disago_customer.network

import android.util.Log
import com.example.disago_customer.network.directions_api_response.Distance
import com.example.disago_customer.network.directions_api_response.Duration
import com.example.disago_customer.network.directions_api_response.GoogleMapsApiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.Exception

private const val TAG = "GoogleMapsApiService"
private const val BASE_URL = "https://maps.googleapis.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit =
    Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()


interface GoogleMapsApiService {
    @GET("maps/api/directions/json")
    suspend fun get(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): GoogleMapsApiResponse
}


object GoogleMapsApi {
    val retrofitService: GoogleMapsApiService by lazy {
        retrofit.create(GoogleMapsApiService::class.java)
    }
}

// TODO: How to properly store and reference API keys?
private const val key = "AIzaSyA3nPbG6U73k9JF9a_2GmwQEcxVZgSrRyg"

/* MAIN FUNCTION TO USE API!

    Returns object containing all the relevant data for our application from the google maps API
    Pass origin and destination strings from the user input
    Use viewModelScope if You call this function from a ViewModel
    Use lifecycleScope if You call this function from an Activity

    EXAMPLE USAGE FOR LAUNCHING IN VIEW MODEL:

    private val price: Double? = null
    fun onRequestRide(origin: String, destination: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = getGoogleMapsApiResponseRelevantData(origin, destination)
            if (data != null) {
                withContext(Dispatchers.Main) {
                    _price.value = data.distance.value / 1000.0 * PRICE_PER_KM
                }
            }
        }
    } */
suspend fun getGoogleMapsApiResponseRelevantData(
    origin: String,
    destination: String
): GoogleMapsApiResponseRelevantData? {

    try {
        val response = GoogleMapsApi.retrofitService.get(origin, destination, key)

        // Check if Api call was successful and provided routes info
        if (response.routes.isEmpty() || response.status != "OK") {
            Log.d(TAG, "Google maps api call failed!")
            return null
        }

        // Check if Api provided distance and duration info
        if (response.routes[0]!!.legs[0].distance == null || response.routes[0]!!.legs[0].duration == null) {
            Log.d(TAG, "Google maps api call did not provide distance and duration info!")
            return null
        }
        Log.d(
            TAG,
            "distanceText=${response.routes[0]!!.legs[0].distance!!.text} distanceValue=${response.routes[0]!!.legs[0].distance!!.value}"
        )
        return GoogleMapsApiResponseRelevantData(
            distance = Distance(
                response.routes[0]!!.legs[0].distance!!.text,
                response.routes[0]!!.legs[0].distance!!.value
            ),
            duration = Duration(
                response.routes[0]!!.legs[0].duration!!.text,
                response.routes[0]!!.legs[0].duration!!.value
            ),
            endAddress = response.routes[0]!!.legs[0].endAddress,
            endLocation = response.routes[0]!!.legs[0].endLocation,
            startAddress = response.routes[0]!!.legs[0].startAddress,
            startLocation = response.routes[0]!!.legs[0].startLocation
        )
    } catch (e: Exception) {
        Log.d(TAG, "Google maps api call failed: ${e.message}")
        return null
    }
}
