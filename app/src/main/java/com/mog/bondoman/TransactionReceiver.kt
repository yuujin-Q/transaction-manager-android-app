package com.mog.bondoman

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

class TransactionReceiver : BroadcastReceiver() {
    companion object {
        const val EXTRA_TRANSACTION_TITLE = "TRANSACTION_TITLE"
        const val EXTRA_TRANSACTION_CATEGORY = "TRANSACTION_CATEGORY"
    }

    private lateinit var fragment: Fragment
    private val transactionNavController = MutableLiveData<NavController>()
    private var ongoingNavigation = false
    private var args: Bundle? = null

    fun attachFragment(fragment: Fragment) {
        this.fragment = fragment
        transactionNavController.observe(fragment.requireActivity()) {
            Log.d("TransactionRecv", "Navigation updated!")
            if (ongoingNavigation) {
                Log.d("TransactionRecv", "Navigating to addTransaction!")
                it.navigate(R.id.addTransactionFragment, args)
                ongoingNavigation = false
            }
        }
    }

    fun setDestinationNavController(navController: NavController) {
        Log.d("TransactionRecv", "Navigation updated?")
        transactionNavController.value = navController
    }

    override fun onReceive(context: Context, intent: Intent) {
        ongoingNavigation = true
        Toast.makeText(context, "Randomize", Toast.LENGTH_SHORT).show()

        args = intent.extras!!

        fragment.requireActivity()
            .findNavController(R.id.nav_host_fragment_content_main)
            .navigate(R.id.transactionFragment)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object TransactionReceiverProvider {
    @Singleton
    @Provides
    fun provideTransactionReceiver(): TransactionReceiver {
        return TransactionReceiver()
    }
}
