package com.mog.bondoman.ui.transaksi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mog.bondoman.databinding.FragmentTransaksiBinding

class TransaksiFragment : Fragment() {

    private var _binding: FragmentTransaksiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transaksiViewModel =
            ViewModelProvider(this)[TransaksiViewModel::class.java]

        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textTransaksi
        transaksiViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}