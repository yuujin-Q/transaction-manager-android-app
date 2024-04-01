package com.mog.bondoman.ui.pengaturan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mog.bondoman.R
import com.mog.bondoman.data.connection.SessionManager
import com.mog.bondoman.databinding.FragmentPengaturanBinding
import com.mog.bondoman.ui.login.LoginFragment

class PengaturanFragment : Fragment() {
    private var _binding: FragmentPengaturanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPengaturanBinding.inflate(inflater, container, false)

        sessionManager = SessionManager.getInstance(
            requireActivity().applicationContext.getSharedPreferences(
                SessionManager.PREF_KEY,
                Context.MODE_PRIVATE
            )
        )

        binding.logout.setOnClickListener {
            onNegativeClick()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNegativeClick() {
        sessionManager.removeAuthToken()

        findNavController().popBackStack(0, false)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, LoginFragment()).commit()
    }
}