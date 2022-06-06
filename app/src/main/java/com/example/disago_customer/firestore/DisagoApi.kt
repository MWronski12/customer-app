package com.example.disago_customer.firestore

import android.util.Log
import com.example.disago_customer.firestore.documents.Customer
import com.example.disago_customer.firestore.documents.Driver
import com.example.disago_customer.firestore.documents.Ride
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception


interface DisagoApiInterface {

    // Customer interactions
    suspend fun createCustomer(customer: Customer, userId: String): DocumentReference?
    suspend fun getCustomer(userId: String): Customer?
    suspend fun listCustomerRides(userId: String): List<Ride?>
//    suspend fun updateCustomer()

    // Driver Interactions
    suspend fun createDriver(driver: Driver, userId: String): DocumentReference?
    suspend fun getDriver(userId: String): Driver?
    suspend fun listDriverRides(userId: String): List<Ride?>
//    suspend fun updateDriver(userId: String): Boolean
    //    suspend fun getDriversAverageReviewRating(driverId: String): String

    //
//    // Ride Interactions
    suspend fun createRide(ride: Ride): DocumentReference?
    suspend fun getRide(rideRef: DocumentReference): Ride?
    suspend fun updateRideStatus(rideId: String, status: String): Boolean
//
//    // Review Interactions
//    suspend fun createReview(ride: Ride): Review?
}


class DisagoApi(private val db: FirebaseFirestore) : DisagoApiInterface {

    // Call this function right after a new user signs up with firebase auth
    // It creates customer object in firestore with user profile data
    override suspend fun createCustomer(customer: Customer, userId: String): DocumentReference? {

        var result: DocumentReference? = null
        val customerRef = db.collection("customers").document(userId)

        customerRef
            .set(customer)
            .addOnSuccessListener {
                result = customerRef
                Log.d("DEBUG", "New customer with id=$userId created")
            }
            .addOnFailureListener { e ->
                Log.w("DEBUG", "Error creating customer:", e)
            }

        return result
    }

    override suspend fun getCustomer(userId: String): Customer? {

        var customer: Customer? = null

        db.collection("customers").document(userId)
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    customer = it.toObject(Customer::class.java)
                    Log.d("DEBUG", "Success fetching info for customer with id=$userId")
                } else {
                    Log.d("DEBUG", "Customer with id=$userId doesn't exist")
                }
            }
            .addOnFailureListener { e ->
                Log.d("DEBUG", "Error fetching customer: ${e.message}")
            }

        return customer
    }

    override suspend fun listCustomerRides(userId: String): List<Ride?> {

        val customerRef = db.collection("customers").document(userId)
        val result: MutableList<Ride> = mutableListOf()

        db.collection("rides")
            .whereEqualTo("customer", customerRef)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // TODO
                    try {
                        result.add(
                            Ride(
                                customer = document.data["customer"] as DocumentReference,
                                driver = document.data["driver"] as DocumentReference?,
                                originLocation = document.data["originLocation"] as String,
                                destinationLocation = document.data["destinationLocation"] as String,
                                date = document.data["date"] as Timestamp,
                                price = document.data["price"] as Double,
                                status = document.data["status"] as String
                            )
                        )
                        Log.d("DEBUG", "Ride ${document.id} => ${document.data} retrieved")
                    } catch (e: Exception) {
                        Log.d("DEBUG", "Failed to deserialize ride object with id=${document.id}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("DEBUG", "Error getting customer (id=$userId) rides: ", exception)
            }

        return result
    }

    // Call this function right after a new user signs up with firebase auth
    // It creates driver object in firestore with user profile data
    override suspend fun createDriver(driver: Driver, userId: String): DocumentReference? {

        var result: DocumentReference? = null
        val driverRef = db.collection("drivers").document(userId)

        driverRef
            .set(driver)
            .addOnSuccessListener {
                result = driverRef
                Log.d("DEBUG", "New driver with id=$userId created")
            }
            .addOnFailureListener { e ->
                Log.w("DEBUG", "Error creating driver:", e)
            }

        return result
    }

    override suspend fun getDriver(userId: String): Driver? {

        var driver: Driver? = null

        db.collection("drivers").document(userId)
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    driver = it.toObject(Driver::class.java)
                    Log.d("DEBUG", "Success fetching info for driver with id=$userId")
                } else {
                    Log.d("DEBUG", "Driver with id=$userId doesn't exist")
                }
            }
            .addOnFailureListener { e ->
                Log.d("DEBUG", "Error fetching driver: ${e.message}")
            }

        return driver
    }

    override suspend fun listDriverRides(userId: String): List<Ride?> {

        val driverRef = db.collection("drivers").document(userId)
        val result: MutableList<Ride> = mutableListOf()

        db.collection("rides")
            .whereEqualTo("driver", driverRef)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    try {
                        result.add(
                            Ride(
                                customer = document.data["customer"] as DocumentReference,
                                driver = document.data["driver"] as DocumentReference?,
                                originLocation = document.data["originLocation"] as String,
                                destinationLocation = document.data["destinationLocation"] as String,
                                date = document.data["date"] as Timestamp,
                                price = document.data["price"] as Double,
                                status = document.data["status"] as String
                            )
                        )
                        Log.d("DEBUG", "Ride ${document.id} => ${document.data} retrieved")
                    } catch (e: Exception) {
                        Log.d("DEBUG", "Failed to deserialize ride object with id=${document.id}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("DEBUG", "Error getting driver (id=$userId) rides: ", exception)
            }

        return result
    }


    override suspend fun createRide(ride: Ride): DocumentReference? {

        val ridesRef = db.collection("rides")

        return ridesRef
            .add(ride)
            .addOnSuccessListener { newRideRef ->
                Log.d("DEBUG", "New ride with id=${newRideRef.id} created")
            }
            .addOnFailureListener { e ->
                Log.d("DEBUG", "Failed to create Ride: ${e.message}")
            }
            .await()
    }

    override suspend fun getRide(rideRef: DocumentReference): Ride? {

        var ride: Ride? = null
        val rideSnapshot = rideRef.get().await()

        try {
            ride = Ride(
                customer = rideSnapshot.data!!["customer"] as DocumentReference,
                driver = rideSnapshot.data!!["driver"] as DocumentReference?,
                originLocation = rideSnapshot.data!!["originLocation"] as String,
                destinationLocation = rideSnapshot.data!!["destinationLocation"] as String,
                date = rideSnapshot.data!!["date"] as Timestamp,
                price = rideSnapshot.data!!["price"] as Double,
                status = rideSnapshot.data!!["status"] as String
            )
        } catch (e: Exception) {
            Log.d("DEBUG", "Error deserializing ride (id=${rideRef.id} object: ${e.message}")
        }

        return ride
    }

    override suspend fun updateRideStatus(rideId: String, status: String): Boolean {

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

