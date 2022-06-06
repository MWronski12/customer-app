package com.example.disago_customer

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.disago_customer.firestore.DisagoApi
import com.example.disago_customer.firestore.documents.Customer
import com.example.disago_customer.firestore.documents.Driver
import com.example.disago_customer.firestore.documents.Ride
import com.example.disago_customer.firestore.fields.DisabilityType
import com.example.disago_customer.firestore.fields.RideStatus
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DisagoApiTest {

    private var db = Firebase.firestore
    private var api = DisagoApi(db)

    @Test
    @Throws(IOException::class)
    fun insertAndGetCustomer() {

        // Normally this would be signedInUserId
        // We want to createCustomer only after new user signs up
        // So we can store additional user data
        val testUserId = "testUserId"
        GlobalScope.launch(Dispatchers.IO) {
            val newCustomer = Customer(
                "test",
                "user",
                "testuser@gmail.com",
                "+420 666 666 666",
                DisabilityType.DWARFISM,
                69.00
            )

            val testCustomerRef = api.createCustomer(newCustomer, testUserId)
            assertEquals(testUserId, testCustomerRef?.id)

            val testCustomer = api.getCustomer(testUserId)
            assertEquals(newCustomer, testCustomer)
        }
    }

    @Test
    @Throws(IOException::class)
    fun insertAndGetDriver() {

        // Normally this would be signedInUserId
        // We want to createCustomer only after new user signs up
        // So we can store additional user data
        val testUserId = "testUserId"
        GlobalScope.launch(Dispatchers.IO) {
            val newDriver = Driver(
                "test",
                "user",
                "testuser@gmail.com",
                "Black Tesla Model X",
                "+420 666 666 666",
                69.00
            )

            val testDriverRef = api.createDriver(newDriver, testUserId)
            assertEquals(testUserId, testDriverRef?.id)

            val testDriver = api.getDriver(testUserId)
            assertEquals(newDriver, testDriver)
        }
    }

    @Test
    @Throws(IOException::class)
    fun insertGetAndUpdateRide() {

        // Ride can only be created by a user requesting it, so we have to create this user first
        val testUserId = "testUserId"
        GlobalScope.launch(Dispatchers.IO) {
            val newCustomer = Customer(
                "test",
                "user",
                "testuser@gmail.com",
                "+420 666 666 666",
                DisabilityType.DWARFISM,
                69.00
            )

            val testCustomerRef = api.createCustomer(newCustomer, testUserId)
            assertTrue(testCustomerRef != null)

            val newRide = Ride(
                testCustomerRef!!,
                null,
                FieldValue.serverTimestamp(),
                "Warsaw",
                "Cracow",
                420.00,
                RideStatus.REQUESTED
            )

            val testRideRef = api.createRide(newRide)
            assertTrue(testRideRef != null)

            val testRide = api.getRide(testRideRef!!)
            assertEquals(newRide, testRide)

            assertTrue(api.updateRideStatus(testRideRef.id, RideStatus.ACCEPTED))

            val updatedRide = api.getRide(testRideRef)
            assertTrue(updatedRide != null)
            assertEquals(updatedRide!!.status, RideStatus.ACCEPTED)
        }
    }
}