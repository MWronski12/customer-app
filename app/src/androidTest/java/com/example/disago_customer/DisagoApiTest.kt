package com.example.disago_customer

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.disago_customer.firestore.DisagoApi
import com.example.disago_customer.firestore.documents.Customer
import com.example.disago_customer.firestore.documents.Driver
import com.example.disago_customer.firestore.documents.Ride
import com.example.disago_customer.firestore.fields.DisabilityType
import com.example.disago_customer.firestore.fields.RideStatus
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


// THESE TESTS ARE NOT CONFIGURED PROPERLY
// BUT BODIES OF THE FUNCTIONS CAN BE USED AS A TEST IN MainActivity.onCreate
@RunWith(AndroidJUnit4::class)
class DisagoApiTest {

    private var db = Firebase.firestore
    private var api = DisagoApi(db)

    @Test
    @Throws(IOException::class)
    fun testCustomerRideInteractions() {

        val testUserId = "testuserid"
        val tag = "DISAGO_API_TEST"
        GlobalScope.launch(Dispatchers.IO) {
            val customer = Customer(
                "test",
                "user",
                "testuser@gmail.com",
                "Black Tesla Model X",
                DisabilityType.DWARFISM,
                69.00
            )

            val newCustomerRef = api.createCustomer(customer, testUserId)
            val newCustomer = api.getCustomer(newCustomerRef.id)
            Log.d(tag, "New customer: $newCustomer")

            customer.email = "changedemail@gmail.com"
            api.updateCustomer(customer, testUserId)
            val updatedCustomer = api.getCustomer(testUserId)
            Log.d(tag, "Updated customer: $updatedCustomer")

            api.updateCustomerBalance(testUserId, -10.0)
            Log.d(
                tag,
                "${newCustomer!!.balance} == ${api.getCustomer(testUserId)!!.balance} - 10.0"
            )

            val ride = Ride(
                customer = newCustomerRef,
                originLocation = "Warszawa",
                destinationLocation = "Krakow",
                price = 10.0
            )

            val newRideRef = api.createRide(ride)
            val newRide = api.getRide(newRideRef)
            Log.d(tag, "new ride: $newRide")

            val newCustomerRides = api.getCustomerRides(testUserId)
            Log.d(tag, "new ride (from list): ${newCustomerRides[0]}")

            api.updateRideStatus(newRideRef.id, RideStatus.ACCEPTED)
            Log.d(tag, "Accepted == ${api.getRide(newRideRef)!!.status}")
        }
    }

    @Test
    @Throws(IOException::class)
    fun testDriverRideInteractions() {

        val testUserId = "testuserid"
        val tag = "DISAGO_API_TEST"
        GlobalScope.launch(Dispatchers.IO) {
            val driver = Driver(
                "test",
                "user",
                "testuser@gmail.com",
                "Black Tesla Model X",
                DisabilityType.DWARFISM,
                69.00
            )

            val newDriverRef = api.createDriver(driver, testUserId)
            val newDriver = api.getDriver(newDriverRef.id)
            Log.d(tag, "New driver: $newDriver")

            driver.email = "changedemail@gmail.com"
            api.updateDriver(driver, testUserId)
            val updatedDriver = api.getDriver(testUserId)
            Log.d(tag, "Updated driver: $updatedDriver")

            api.updateDriverBalance(testUserId, -10.0)
            Log.d(tag, "${newDriver!!.balance} == ${api.getDriver(testUserId)!!.balance} - 10.0")

            val ride = Ride(
                customer = newDriverRef,
                driver = newDriverRef,
                originLocation = "Warszawa",
                destinationLocation = "Krakow",
                price = 10.0
            )

            val newRideRef = api.createRide(ride)
            val newRide = api.getRide(newRideRef)
            Log.d(tag, "new ride: $newRide")

            val newDriverRides = api.getDriverRides(testUserId)
            Log.d(tag, "new ride (from list): ${newDriverRides[0]}")

            api.updateRideStatus(newRideRef.id, RideStatus.ACCEPTED)
            Log.d(tag, "Accepted == ${api.getRide(newRideRef)!!.status}")
        }
    }

    @Test
    @kotlin.jvm.Throws(IOException::class)
    fun testReviewInteractions() {
        val tag = "DISAGO_API_TEST"
        val testDriverId = "testdriverid"
        val testCustomerId = "testcustomerid"
        GlobalScope.launch(Dispatchers.IO) {
            val customer = Customer(
                "test",
                "customer",
                "testuser@gmail.com",
                "+420 666 666 666",
                DisabilityType.DWARFISM,
                69.00
            )
            val driver = Driver(
                "test",
                "driver",
                "testuser@gmail.com",
                "Black Tesla Model X",
                "+420 666 666 666",
                69.00
            )

            val newDriverRef = api.createDriver(driver, testDriverId)
            val newCustomerRef = api.createCustomer(customer, testDriverId)
            val ride = Ride(
                customer = newCustomerRef,
                driver = newDriverRef,
                originLocation = "Warsaw",
                destinationLocation = "Cracow",
                price = 420.0
            )
            for (i in 1..5) {
                api.createReview(ride, i)
            }

            Log.d(tag, api.getDriversAverageReviewRating(newDriverRef))
        }
    }
}