package com.example.disago_customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.disago_customer.firestore.DisagoApi
import com.example.disago_customer.firestore.documents.Customer
import com.example.disago_customer.firestore.fields.RideStatus
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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
        val id = "At1ZdTE8uZS0F59YOI61"
        val api = DisagoApi(db)
        api.getDriver(id)


    }
}