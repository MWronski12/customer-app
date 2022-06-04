package com.example.disago_customer.screens

import android.util.Log
import androidx.lifecycle.*
import com.example.disago_customer.firestore.DisagoApi
import com.example.disago_customer.firestore.documents.Ride
import com.example.disago_customer.firestore.fields.RideStatus
import com.example.disago_customer.network.GoogleMapsApi
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.Exception

const val PRICE_PER_KM = 0.79

class RequestRideViewModel: ViewModel() {

    private val db = Firebase.firestore
    private val disagoApi = DisagoApi(db)

    var originLocation: String? = null
    var destinationLocation: String? = null

    // TODO: How to properly store and reference API keys
    private val key = "AIzaSyA3nPbG6U73k9JF9a_2GmwQEcxVZgSrRyg"

    private val _price = MutableLiveData<Double>()
    private val price: LiveData<Double>
        get() = _price
    val priceString = Transformations.map(price) { price ->
        "Price: ${String.format("%.2f", price)} €"
    }

    fun onRequestRide(origin: String, destination: String) {
        getGoogleMapsApiResponse(origin, destination)

        Log.d("DEBUG", "onRequestRide called")
    }

    // TODO: Populate signedInUserId with Firebase.auth.currentUser
    val signedInUserId = "mZRxkL5MdDjyM34EXdd9"
    fun onConfirmRideRequest(signedInUserId: String) {

        // Fetch signedInUser data
        val customer = disagoApi.getCustomer(signedInUserId)

        val ride = Ride(
            db.collection("customers").document(signedInUserId),
            null,
            FieldValue.serverTimestamp(),
            // TODO: Better way to do this
            originLocation!!,
            destinationLocation!!,
            price.value!!,
            RideStatus.REQUESTED
        )

        val newRide = disagoApi.createRide(ride)

        if (newRide != null) {
            onRideRequested(newRide)
        } else {
            ride.status = RideStatus.CANCELLED
            onRideCancelled()
        }
    }

    private fun onRideRequested(rideRef: DocumentReference) {
    }

    private fun onRideAccepted() {

    }

    private fun onRideCancelled() {

    }

    private fun getGoogleMapsApiResponse(origin: String, destination: String) {
        viewModelScope.launch {
            try {
                val result = GoogleMapsApi.retrofitService.get(origin, destination, key)
                // TODO: How to handle null assertion and varying API response format
                _price.value = result.routes[0].legs!![0]?.distance?.value!! / 1000.0 * PRICE_PER_KM

                Log.d("DEBUG", "Price calculated: " + price.value.toString())
            } catch(e: Exception) {
                Log.d("DEBUG", "Google Maps Api Error: " + e.message)
            }
        }
    }
}