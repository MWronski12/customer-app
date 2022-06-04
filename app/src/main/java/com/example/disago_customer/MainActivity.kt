package com.example.disago_customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userRef = db.collection("customers").document("mZRxkL5MdDjyM34EXdd9")
        db.collection("test")
            .add(userRef)
            .addOnSuccessListener { documentReference ->
                Log.d("DEBUG", "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("DEBUG", "Error adding document", e)
            }
    }
}