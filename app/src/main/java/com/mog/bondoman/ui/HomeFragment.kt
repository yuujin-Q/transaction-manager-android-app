package com.mog.bondoman.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.mog.bondoman.R
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

    private lateinit var binding: FragmentHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    //    TODO
    @Inject
    lateinit var sessionManager: SessionManager
    private val transactionViewModel: TransactionViewModel by activityViewModels<TransactionViewModel>()
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

        binding.appBarMain.toolbar.inflateMenu(R.menu.overflow)

        binding.navView.let {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_transaction, R.id.nav_scan, R.id.nav_graf, R.id.nav_settings
                ),
                binding.drawerLayout
            )

            binding.appBarMain.toolbar.setupWithNavController(navControl, appBarConfiguration)
            it.setupWithNavController(navControl)
        }

        binding.appBarMain.contentMain.bottomNavView?.let {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_transaction, R.id.nav_scan, R.id.nav_graf
                )
            )
            binding.appBarMain.toolbar.setupWithNavController(navControl, appBarConfiguration)
            it.setupWithNavController(navControl)
        }
    }

    override fun onStart() {
        super.onStart()

        val userId = sessionManager.fetchNim()
        transactionViewModel.setUserId(userId!!.toLong())
        CoroutineScope(Dispatchers.IO).launch {
            transactionViewModel.fetchData()
        }
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