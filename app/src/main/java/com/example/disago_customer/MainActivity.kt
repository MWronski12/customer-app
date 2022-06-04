package com.example.disago_customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.disago_customer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.requestRideButton.setOnClickListener {
            Log.d("DEBUG", "Button clicked")

            viewModel.onRequestRide(
                binding.pickupLocationEditText.text.toString(),
                binding.destinationLocationEditText.text.toString()
            )
        }
    }
}