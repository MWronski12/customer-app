package com.example.disago_customer.screens.request_ride

import android.util.Log
import androidx.lifecycle.*
import com.example.disago_customer.firestore.DisagoApi
import com.example.disago_customer.firestore.documents.Ride
import com.example.disago_customer.firestore.fields.RideStatus
import com.example.disago_customer.network.getGoogleMapsApiResponseRelevantData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val PRICE_PER_KM = 0.79

class RequestRideViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val disagoApi = DisagoApi(db)

    var originLocation: String = ""
    var destinationLocation: String = ""


    // For displaying price after fetching from google maps Api
    private val _price = MutableLiveData<Double>()
    val price: LiveData<Double>
        get() = _price
    val priceString = Transformations.map(price) { price ->
        "Price: ${String.format("%.2f", price)} €"
    }

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status
    val statusString = Transformations.map(status) { status ->
        "Status: $status"
    }


    fun onRequestRide(origin: String, destination: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = getGoogleMapsApiResponseRelevantData(origin, destination)
            if (data != null) {
                withContext(Dispatchers.Main) {
                    _price.value = data.distance.value / 1000.0 * PRICE_PER_KM
                }
            }
        }
    }

    fun onConfirmRideRequest(signedInUserId: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val ride = Ride(
                customer = db.collection("customers").document(signedInUserId),
                originLocation = originLocation,
                destinationLocation = destinationLocation,
                price = price.value!!
            )

            val newRide = disagoApi.createRide(ride)
            if (newRide != null) {
                Log.d("DEBUG", "Ride id: ${newRide.id}")
                attachRideStatusListener(newRide)
            } else {
                Log.d("DEBUG", "newRide=null")
            }
        }
    }

    private fun attachRideStatusListener(rideRef: DocumentReference) {
        rideRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("DEBUG", "Listen failed.", e)
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d("DEBUG", "Ride status changed: ${snapshot.data!!["status"]}")
                when (snapshot.data!!["status"]) {
                    RideStatus.REQUESTED -> onRideRequested(rideRef)
                    RideStatus.ACCEPTED -> onRideAccepted(rideRef)
                    RideStatus.ARRIVED -> onRideArrived(rideRef)
                    RideStatus.COMPLETED -> onRideCompleted(rideRef)
                    RideStatus.CANCELLED -> onRideCancelled(rideRef)
                }
            }
        }
    }

    private fun onRideRequested(rideRef: DocumentReference) {
        _status.value = RideStatus.REQUESTED
    }

    private fun onRideAccepted(rideRef: DocumentReference) {
        _status.value = RideStatus.ACCEPTED
    }

    private fun onRideArrived(rideRef: DocumentReference) {
        _status.value = RideStatus.ARRIVED
    }

    private fun onRideCompleted(rideRef: DocumentReference) {
        _status.value = RideStatus.COMPLETED
    }

    private fun onRideCancelled(rideRef: DocumentReference) {
        _status.value = RideStatus.CANCELLED
    }
}