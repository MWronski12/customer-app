package com.example.disago_customer.screens.request_ride

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

            viewModel.originLocation = binding.pickupLocationEditText.text.toString()
            viewModel.destinationLocation = binding.destinationLocationEditText.text.toString()

            viewModel.onRequestRide(
                binding.pickupLocationEditText.text.toString(),
                binding.destinationLocationEditText.text.toString()
            )
        }

        // TODO: Use Firebase.auth.currentUser.id when implemented
        val signedInUserId = "mZRxkL5MdDjyM34EXdd9"
        binding.confirmRideRequestButton.setOnClickListener {
            // Cannot confirm ride if the price was not calculated
            if (viewModel.price.value == null) {
                return@setOnClickListener
            }
            viewModel.onConfirmRideRequest(signedInUserId)
        }

        return binding.root
    }
}