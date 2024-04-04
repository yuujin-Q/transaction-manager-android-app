package com.mog.bondoman

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.mog.bondoman.ui.transaction.TransactionFragment

class TransactionReceiver(private val activity: MainActivity) : BroadcastReceiver() {
    companion object {
        const val EXTRA_TRANSACTION_TITLE = "TRANSACTION_TITLE"
        const val EXTRA_TRANSACTION_CATEGORY = "TRANSACTION_CATEGORY"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TransactionReceiver", "Transaction received")
        Toast.makeText(context, "ambatublow", Toast.LENGTH_SHORT).show()

        val args = intent.extras!!
        args.putInt(
            TransactionFragment.TRANSACTION_REQUEST_TYPE,
            TransactionFragment.REQUEST_ADD_TRANSACTION
        )
        val title = args.getString(EXTRA_TRANSACTION_TITLE)
        val category = args.getString(EXTRA_TRANSACTION_CATEGORY)

        Log.d("TransactionReceiver", title!!)
        Log.d("TransactionReceiver", category!!)

//        TODO navigate to add transaction fragment
//        val navController = activity.findNavController(R.id.nav_host_fragment_content_main)
//        navController.navigate(R.id.transactionFragment)
//        val mainNavView = activity.findViewById<View>(R.id.nav_host_fragment_content_main)
//        Log.d("navigationaaahhh", mainNavView.toString())
//        val childNavView = mainNavView.findViewById<View>(R.id.transactionFragment)
//        Log.d("navigationaaahhh", childNavView.toString())
//        childNavController.navigate(R.id.editTransactionFragment, args)

//        activity.findNavController(R.id.nav_host_fragment_content_main)
//            .navigatorProvider += Navigator().
    }
}