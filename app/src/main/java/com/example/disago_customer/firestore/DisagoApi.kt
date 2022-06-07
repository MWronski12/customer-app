package com.example.disago_customer.firestore

import android.util.Log
import com.example.disago_customer.firestore.documents.Customer
import com.example.disago_customer.firestore.documents.Driver
import com.example.disago_customer.firestore.documents.Review
import com.example.disago_customer.firestore.documents.Ride
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.lang.Exception

private const val TAG = "DisagoApi"

interface DisagoApiInterface {

    // Customer interactions
    suspend fun createCustomer(customer: Customer, userId: String): DocumentReference
    suspend fun getCustomer(userId: String): Customer?
    suspend fun getCustomerRides(userId: String): List<Ride?>
    suspend fun updateCustomer(customer: Customer, userId: String)
    suspend fun updateCustomerBalance(userId: String, incrementValue: Double)

    // Driver Interactions
    suspend fun createDriver(driver: Driver, userId: String): DocumentReference
    suspend fun getDriver(userId: String): Driver?
    suspend fun getDriverRides(userId: String): List<Ride?>
    suspend fun updateDriver(driver: Driver, userId: String)
    suspend fun updateDriverBalance(userId: String, incrementValue: Double)

    // Ride Interactions
    suspend fun createRide(ride: Ride): DocumentReference
    suspend fun getRide(rideRef: DocumentReference): Ride?
    suspend fun updateRideStatus(rideId: String, status: String)

    // Review Interactions
    suspend fun createReview(ride: Ride, rating: Int): DocumentReference?
    suspend fun getDriversAverageReviewRating(driverRef: DocumentReference): String
}

// Works
class DisagoApi(private val db: FirebaseFirestore) : DisagoApiInterface {

    // Call this function right after a new user signs up with firebase auth
    // It creates customer object in firestore with user profile data
    override suspend fun createCustomer(customer: Customer, userId: String): DocumentReference {

        val customerRef = db.collection("customers").document(userId)

        customerRef
            .set(customer)
            .addOnSuccessListener {
                Log.d(TAG, "New customer with id=$userId created")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error creating customer:", e)
            }.await()

        return customerRef
    }

    override suspend fun getCustomer(userId: String): Customer? {

        val document = db.collection("customers").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "Success fetching info for customer with id=${document.id}")
                } else {
                    Log.d(TAG, "Customer with id=$userId doesn't exist")
                }
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Error fetching customer: ${e.message}")
            }.await()

        var newCustomer: Customer? = null
        try {
            newCustomer = Customer(
                name = document.data!!["name"] as String,
                surname = document.data!!["surname"] as String,
                email = document.data!!["email"] as String,
                phoneNumber = document.data!!["phoneNumber"] as String,
                disabilityType = document.data!!["disabilityType"] as String,
                balance = document.data!!["balance"] as Double
            )
        } catch (e: Exception) {
            Log.d(TAG, "Failed to deserialize customer object")
        }

        return newCustomer
    }

    override suspend fun updateCustomer(customer: Customer, userId: String) {

        val customerRef = db.collection("customers").document(userId)
        customerRef
            .update(
                hashMapOf<String, Any>(
                    "name" to customer.name,
                    "surname" to customer.surname,
                    "email" to customer.email,
                    "phoneNumber" to customer.phoneNumber,
                    "disabilityType" to customer.disabilityType
                )
            )
            .addOnSuccessListener {
                Log.d(TAG, "Customer profile successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating customer profile: ", e)
            }.await()
    }

    override suspend fun updateCustomerBalance(userId: String, incrementValue: Double) {

        val customerRef = db.collection("customers").document(userId)

        customerRef
            .update("balance", FieldValue.increment(incrementValue))
            .addOnSuccessListener {
                Log.d(TAG, "Customer balance successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating customer balance: ", e)
            }.await()
    }

    override suspend fun getCustomerRides(userId: String): List<Ride?> {

        val customerRef = db.collection("customers").document(userId)

        val documents = db.collection("rides")
            .whereEqualTo("customer", customerRef)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                Log.d(TAG, "Successfully fetched ${documents.size()} rides")

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting customer (id=$userId) rides: ", exception)
            }.await()

        val result: MutableList<Ride> = mutableListOf()
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
                Log.d(TAG, "Ride with id=${document.id} deserialized")
            } catch (e: Exception) {
                Log.d(TAG, "Failed to deserialize ride with id=${document.id}")
            }
        }

        return result
    }

    // Call this function right after a new user signs up with firebase auth
    // It creates driver object in firestore with user profile data
    override suspend fun createDriver(driver: Driver, userId: String): DocumentReference {

        val driverRef = db.collection("drivers").document(userId)

        driverRef
            .set(driver)
            .addOnSuccessListener {
                Log.d(TAG, "New driver with id=$userId created")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error creating driver:", e)
            }.await()

        return driverRef
    }

    override suspend fun getDriver(userId: String): Driver? {

        val document = db.collection("drivers").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "Success fetching info for driver with id=${document.id}")
                } else {
                    Log.d(TAG, "Customer with id=$userId doesn't exist")
                }
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Error fetching driver: ${e.message}")
            }.await()

        var driver: Driver? = null

        try {
            driver = Driver(
                name = document.data!!["name"] as String,
                surname = document.data!!["surname"] as String,
                email = document.data!!["email"] as String,
                phoneNumber = document.data!!["phoneNumber"] as String,
                car = document.data!!["car"] as String,
                balance = document.data!!["balance"] as Double
            )
        } catch (e: Exception) {
            Log.d(TAG, "Failed to deserialize driver object: ${e.message}")
        }
        return driver
    }

    override suspend fun updateDriver(driver: Driver, userId: String) {

        val driverRef = db.collection("drivers").document(userId)
        driverRef
            .update(
                hashMapOf<String, Any>(
                    "name" to driver.name,
                    "surname" to driver.surname,
                    "email" to driver.email,
                    "phoneNumber" to driver.phoneNumber,
                    "car" to driver.car
                )
            )
            .addOnSuccessListener {
                Log.d(TAG, "Driver profile successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating driver profile: ", e)
            }.await()
    }

    override suspend fun updateDriverBalance(userId: String, incrementValue: Double) {

        val customerRef = db.collection("drivers").document(userId)

        customerRef
            .update("balance", FieldValue.increment(incrementValue))
            .addOnSuccessListener {
                Log.d(TAG, "Driver balance successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating driver balance: ", e)
            }.await()
    }

    override suspend fun getDriverRides(userId: String): List<Ride?> {

        val driverRef = db.collection("drivers").document(userId)

        val documents = db.collection("rides")
            .whereEqualTo("driver", driverRef)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                Log.d(TAG, "Successfully fetched ${documents.size()} rides")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting driver (id=$userId) rides: ", exception)
            }.await()


        val result: MutableList<Ride> = mutableListOf()
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
            } catch (e: Exception) {
                Log.d(TAG, "Failed to deserialize ride object with id=${document.id}")
            }
        }
        return result
    }


    override suspend fun createRide(ride: Ride): DocumentReference {

        val ridesRef = db.collection("rides")

        return ridesRef
            .add(ride)
            .addOnSuccessListener { newRideRef ->
                Log.d(TAG, "New ride with id=${newRideRef.id} created")
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Failed to create Ride: ${e.message}")
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
            Log.d(TAG, "Error deserializing ride (id=${rideRef.id} object: ${e.message}")
        }

        return ride
    }

    override suspend fun updateRideStatus(rideId: String, status: String) {

        val data = hashMapOf<String, Any>(
            "status" to status
        )

        db.collection("rides").document(rideId)
            .update(data)
            .addOnSuccessListener {
                Log.d(TAG, "Ride with id=$rideId status updated to: $status")
            }
            .addOnFailureListener { e ->
                Log.d(
                    TAG,
                    "Failed to update ride status: ${e.message}"
                )
            }.await()
    }

    override suspend fun createReview(ride: Ride, rating: Int): DocumentReference? {

        if (ride.driver == null) {
            throw Exception("Ride has to contain driver field!")
            return null
        }

        val review = Review(ride.customer, ride.driver!!, rating)

        val reviewsRef = db.collection("reviews")
        val newReviewRef = reviewsRef
            .add(review)
            .addOnSuccessListener {
                Log.d(TAG, "New review with id=${it.id} created!")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to create a review!")
            }.await()

        return newReviewRef
    }

    override suspend fun getDriversAverageReviewRating(driverRef: DocumentReference): String {

        val reviewsRef = db.collection("reviews")

        val documents = reviewsRef
            .whereEqualTo("driver", driverRef)
            .get()
            .addOnSuccessListener { documents ->
                Log.d(TAG, "Successfully fetched ${documents.size()} drivers rides")
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Failed to fetch drivers rides: ${e.message}")
            }.await()

        var sum = 0.0
        documents.forEach {
            sum += it.data["rating"] as Long
        }
        val avg = sum / documents.size()

        return String.format("%.1f", avg)
    }
}
