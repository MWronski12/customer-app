package com.example.disago_customer.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.disago_customer.R
import com.example.disago_customer.databinding.FragmentRequestRideBinding

class RequestRideFragment : Fragment() {

    private lateinit var viewModel: RequestRideViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val binding = DataBindingUtil.inflate<FragmentRequestRideBinding>(
            inflater,
            R.layout.fragment_request_ride, container, false
        )

        viewModel = ViewModelProvider(this)[RequestRideViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.requestRideButton.setOnClickListener {
            Log.d("DEBUG", "Button clicked")

            viewModel.onRequestRide(
                binding.pickupLocationEditText.text.toString(),
                binding.destinationLocationEditText.text.toString()
            )
        }

        return binding.root
    }
}