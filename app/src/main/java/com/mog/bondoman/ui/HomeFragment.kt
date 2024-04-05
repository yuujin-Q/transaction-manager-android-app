package com.mog.bondoman.ui

import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.mog.bondoman.R
import com.mog.bondoman.TransactionReceiver
import com.mog.bondoman.data.connection.SessionManager
import com.mog.bondoman.databinding.FragmentHomeBinding
import com.mog.bondoman.ui.transaction.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var transactionReceiver: TransactionReceiver
    private val transactionIntentFilter = IntentFilter("com.mog.bondoman.ADD_TRANSACTION")

    private lateinit var binding: FragmentHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var sessionManager: SessionManager
    private val transactionViewModel: TransactionViewModel by activityViewModels<TransactionViewModel>()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        transactionReceiver.attachFragment(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.requireActivity()
                .registerReceiver(
                    transactionReceiver,
                    transactionIntentFilter,
                    Context.RECEIVER_EXPORTED
                )
        } else {
            this.requireActivity().registerReceiver(transactionReceiver, transactionIntentFilter)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Home Frag", sessionManager.fetchAuthToken() ?: "no token")

        val navHostFrag =
            binding.appBarMain.contentMain.navHostFragmentContentMain.getFragment<NavHostFragment>()
        val navControl = navHostFrag.navController



        binding.appBarMain.contentMain.bottomNavView.let {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.transactionFragment, R.id.scanFragment, R.id.graphFragment
                )
            )
            binding.appBarMain.toolbar.setupWithNavController(navControl, appBarConfiguration)
            it.setupWithNavController(navControl)
        }

        sessionManager.isValidSession.observe(viewLifecycleOwner,
            Observer { isValid ->
                if (isValid) {
                    return@Observer
                } else {
                    findNavController().navigate(R.id.navigate_to_login)
                }
            })
    }

    override fun onStart() {
        super.onStart()

        val userId = sessionManager.fetchNim()
        transactionViewModel.setUserId(userId!!.toLong())
        CoroutineScope(Dispatchers.IO).launch {
            transactionViewModel.fetchData()
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().unregisterReceiver(transactionReceiver)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment HomeFragment.
         */
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}