package com.example.disago_customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

<<<<<<< HEAD
    private var db = Firebase.firestore
=======
    val db = Firebase.firestore
>>>>>>> google_maps_API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

<<<<<<< HEAD
        val userRef = db.collection("customers").document("mZRxkL5MdDjyM34EXdd9")
        db.collection("test")
            .add(userRef)
            .addOnSuccessListener { documentReference ->
                Log.d("DEBUG", "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("DEBUG", "Error adding document", e)
=======
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("DEBUG", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("DEBUG", "Error getting documents.", exception)
>>>>>>> google_maps_API
            }
    }
}