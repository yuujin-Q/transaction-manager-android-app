package com.mog.bondoman.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mog.bondoman.TransactionReceiver
import com.mog.bondoman.data.connection.SessionManager
import com.mog.bondoman.databinding.FragmentSettingsBinding
import com.mog.bondoman.utils.Randomize
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.logout.setOnClickListener {
            onNegativeClick()
        }

        binding.kirimTransaksi.setOnClickListener {
            broadcastRandomTransaction()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNegativeClick() {
        sessionManager.removeAuthToken()
    }

    private fun broadcastRandomTransaction() {
        val intent = Intent("com.mog.bondoman.ADD_TRANSACTION")

        val args = Bundle().apply {
            this.putString(TransactionReceiver.EXTRA_TRANSACTION_TITLE, Randomize.string())
            this.putString(TransactionReceiver.EXTRA_TRANSACTION_CATEGORY, Randomize.string())
        }

        intent.putExtras(args)
        requireActivity().sendBroadcast(intent)
    }
}