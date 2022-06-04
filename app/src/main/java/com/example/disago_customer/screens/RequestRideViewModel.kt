package com.example.disago_customer.screens

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disago_customer.network.GoogleMapsApi
import kotlinx.coroutines.launch
import java.lang.Exception

class RequestRideViewModel: ViewModel() {

    companion object {
        private const val PRICE_PER_KM = 2
    }

    // TODO: How to properly store and reference API keys
    private val key = "AIzaSyA3nPbG6U73k9JF9a_2GmwQEcxVZgSrRyg"

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    fun onRequestRide(origin: String, destination: String) {
        getGoogleMapsApiResponse(origin, destination)

        Log.d("DEBUG", "onRequestRide called")
    }

    private fun getGoogleMapsApiResponse(origin: String, destination: String) {
        viewModelScope.launch {
            try {
                val result = GoogleMapsApi.retrofitService.get(origin, destination, key)
                // TODO: How to handle null assertion and varying API response format
                val price = result.routes[0].legs!![0]?.distance?.value!! / 1000 * PRICE_PER_KM
                _response.value = "Price: $price"

                Log.d("DEBUG", "Api called: " + _response.value.toString())
            } catch(e: Exception) {
                _response.value = "Price: ${e.message}$"

                Log.d("DEBUG", "Api call failed: " + e.message)
            }
        }
    }
}