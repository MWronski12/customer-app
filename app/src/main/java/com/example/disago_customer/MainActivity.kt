package com.example.disago_customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.disago_customer.firestore.DisagoApi
import com.example.disago_customer.firestore.documents.Customer
import com.example.disago_customer.firestore.documents.Driver
import com.example.disago_customer.firestore.fields.RideStatus
import com.example.disago_customer.network.getGoogleMapsApiResponseRelevantData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private val api = DisagoApi(db)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val docRef = db.collection("test").document("123")
//        GlobalScope.launch(Dispatchers.IO) {
//            docRef.set(hashMapOf<String, Any>("bro" to "eat sth!")).await()
//        }



//        val response = getGoogleMapsApiResponseRelevantData("Warszawa", "Krakow", lifecycleScope)
//
//        Log.d("DEBUG", "Distance string: ${response?.distance?.text}")
//        Log.d("DEBUG", "Distance value in meters: ${response?.distance?.value}")
//        Log.d("DEBUG", "Duration string: ${response?.duration?.text}")
//        Log.d("DEBUG", "Duration value in seconds: ${response?.duration?.value}")
//        Log.d("DEBUG", "Start address: ${response?.startAddress}")
//        Log.d("DEBUG", "End Address: ${response?.endAddress}")



//          CREATE CUSTOMER TEST
//        val id = "sldkfjnsdlkfgnj"
//        val customer = Customer("m", "w", "w", "w", "w", 10.0)
//
//        val api = DisagoApi(db)
//        api.createCustomer(customer, id)

//        FETCH CUSTOMER TEST
//        val id = "mZRxkL5MdDjyM34EXdd9"
//        val api = DisagoApi(db)
//        api.getCustomer(id)

//        FETCH DRIVER TEST
//        val id = "At1ZdTE8uZS0F59YOI61"
//        val api = DisagoApi(db)
//        api.getDriver(id)

    }
}