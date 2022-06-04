package com.example.disago_customer.firestore

import android.util.Log
import com.example.disago_customer.firestore.documents.Customer
import com.example.disago_customer.firestore.documents.Driver
import com.example.disago_customer.firestore.documents.Review
import com.example.disago_customer.firestore.documents.Ride
import com.example.disago_customer.firestore.fields.RideStatus
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


interface DisagoApiInterface {

    // Customer interactions
    fun getCustomer(userId: String): Customer?
    fun createCustomer(customer: Customer, userId: String): DocumentReference?

    // Driver Interactions
    fun getDriver(userId: String): Driver?
    fun createDriver(driver: Driver, userId: String): DocumentReference?

    //    fun getDriversAccumulatedRating(driverId: String): String
//
//    // Ride Interactions
    fun createRide(ride: Ride): DocumentReference?
    fun updateRideStatus(rideId: String, status: String): Boolean
//    fun listRides(): List<Ride?>
//
//    // Review Interactions
//    fun createReview(ride: Ride): Review?
}


class DisagoApi(private val db: FirebaseFirestore) : DisagoApiInterface {

    override fun getCustomer(userId: String): Customer? {

        var customer: Customer? = null

        db.collection("customers").document(userId)
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    customer = Customer(
                        it["name"].toString(),
                        it["surname"].toString(),
                        it["email"].toString(),
                        it["phoneNumber"].toString(),
                        it["disabilityType"].toString(),
                        it["balance"].toString().toDouble()
                    )
                    Log.d("DEBUG", "Customer: ${it.data}")
                } else {
                    Log.d("DEBUG", "No such document")
                }
            }
            .addOnFailureListener { e ->
                Log.d("DEBUG", "Error fetching customer: ${e.message}")
            }

        return customer
    }

    override fun getDriver(userId: String): Driver? {

        var driver: Driver? = null

        db.collection("drivers").document(userId)
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    driver = Driver(
                        it["name"].toString(),
                        it["surname"].toString(),
                        it["email"].toString(),
                        it["car"].toString(),
                        it["phoneNumber"].toString(),
                        it["balance"].toString().toDouble()
                    )
                    Log.d("DEBUG", "Driver: ${it.data}")
                } else {
                    Log.d("DEBUG", "No such document")
                }
            }
            .addOnFailureListener { e ->
                Log.d("DEBUG", "Error fetching customer: ${e.message}")
            }

        return driver
    }

    // Call this function right after a new user signs up with firebase auth
    // It creates customer object in firestore with user profile data
    override fun createCustomer(customer: Customer, userId: String): DocumentReference? {

        var result: DocumentReference? = null
        val customerRef = db.collection("customers").document(userId)

        customerRef
            .set(customer.toHashMap())
            .addOnSuccessListener {
                result = customerRef
                Log.d("DEBUG", "New customer with id=$userId created")
            }
            .addOnFailureListener { e ->
                Log.w("DEBUG", "Error creating customer:", e)
            }

        return result
    }

    // Call this function right after a new user signs up with firebase auth
    // It creates driver object in firestore with user profile data
    override fun createDriver(driver: Driver, userId: String): DocumentReference? {

        var result: DocumentReference? = null
        val driverRef = db.collection("customers").document(userId)

        driverRef
            .set(driver.toHashMap())
            .addOnSuccessListener {
                result = driverRef
                Log.d("DEBUG", "New driver with id=$userId created")
            }
            .addOnFailureListener { e ->
                Log.w("DEBUG", "Error creating driver:", e)
            }

        return result
    }

    override fun createRide(ride: Ride): DocumentReference? {

        var result: DocumentReference? = null

        db.collection("rides")
            .add(ride)
            .addOnSuccessListener { newRideRef ->
                result = newRideRef
                Log.d("DEBUG", "New ride with id=${newRideRef.id} created")
            }
            .addOnFailureListener { e ->
                Log.d("DEBUG", "Failed to create Ride: ${e.message}")
            }
        return result
    }

    override fun updateRideStatus(rideId: String, status: String): Boolean {

        val data = hashMapOf<String, Any>(
            "status" to status
        )
        var result = false

        db.collection("rides").document(rideId)
            .update(data)
            .addOnSuccessListener {
                result = true
                Log.d("DEBUG", "Ride with id=$rideId status updated to: $status")
            }
            .addOnFailureListener { e ->
                Log.d(
                    "DEBUG",
                    "Failed to update ride status: ${e.message}"
                )
            }
        return result
    }
}
