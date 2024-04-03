package com.mog.bondoman.ui.transaction.modify

import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mog.bondoman.ui.transaction.TransactionViewModel
import com.mog.bondoman.utils.LocationProvider

abstract class ModifyTransactionFragment : Fragment() {
    protected val transactionViewModel: TransactionViewModel by activityViewModels<TransactionViewModel>()
    protected lateinit var transactionInputViewModel: TransactionInputViewModel

    private val locationData = MutableLiveData<Location>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionInputViewModel = ViewModelProvider(this)[TransactionInputViewModel::class.java]
    }

    protected fun setFormInputListeners(
        transactionInputBinding: TransactionInputBinding,
        confirmButton: Button
    ) {
        // set observer for input validation
        transactionInputViewModel.transactionFormState.observe(viewLifecycleOwner,
            Observer { transactionFormState ->
                if (transactionFormState == null) {
                    return@Observer
                }
                confirmButton.isEnabled = transactionFormState.isDataValid
                TransactionInputViewModel.updateError(
                    transactionFormState,
                    transactionInputBinding,
                    this
                )
            })

        // check for data change
        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                transactionInputViewModel.transactionDataChanged(transactionInputBinding)
            }
        }
        transactionInputBinding.addTextChangedListener(afterTextChangedListener)
    }

    protected fun setLocationButtonOnClick(
        getLocationButton: ImageButton,
        transactionInputBinding: TransactionInputBinding
    ) {
        getLocationButton.setOnClickListener {
            insertLocationToInput(transactionInputBinding)
        }
    }

    private fun insertLocationToInput(
        transactionInputBinding: TransactionInputBinding
    ) {
        val context = requireContext()
        val activity = requireActivity()
        val locationProvider = LocationProvider.getInstance(context)
        val isLocationEnabled = locationProvider.checkForPermission(context, activity)

        if (!isLocationEnabled) {
            Toast.makeText(
                context,
                "Location Permission is disabled, try enabling it from setting",
                Toast.LENGTH_SHORT
            ).show()
            return
        } else {
            locationProvider.getCurrentLocation(context, activity, locationData)
            locationData.observe(this@ModifyTransactionFragment) {
                Log.d(
                    "ModifyTransactionFragment",
                    "location found: lat=${it.latitude}, long=${it.longitude}"
                )
                // TODO: make location human readable, use geocoder maybe
                val locationString = "${it.latitude},${it.longitude}"
                transactionInputBinding.locationEditText.setText(locationString)
            }
        }
    }
}