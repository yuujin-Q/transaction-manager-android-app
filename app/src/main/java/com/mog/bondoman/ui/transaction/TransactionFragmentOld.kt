//package com.mog.bondoman.ui.transaction
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.core.content.res.ResourcesCompat
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.mog.bondoman.databinding.FragmentTransformBinding
//import com.mog.bondoman.databinding.TransactionTransformBinding
//
///**
// * Fragment that demonstrates a responsive layout pattern where the format of the content
// * transforms depending on the size of the screen. Specifically this Fragment shows items in
// * the [RecyclerView] using LinearLayoutManager in a small screen
// * and shows items using GridLayoutManager in a large screen.
// */
//class TransactionFragmentOld : Fragment() {
//
//    private var _binding: FragmentTransformBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
//        _binding = FragmentTransformBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val recyclerView = binding.recyclerviewTransform
//        val adapter = TransformAdapter()
//        recyclerView.adapter = adapter
//        transactionViewModel.texts.observe(viewLifecycleOwner) {
//            adapter.submitList(it)
//        }
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    class TransformAdapter :
//        ListAdapter<String, TransformViewHolder>(object : DiffUtil.ItemCallback<String>() {
//
//            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
//                oldItem == newItem
//
//            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
//                oldItem == newItem
//        }) {
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransformViewHolder {
//            val binding = TransactionTransformBinding.inflate(LayoutInflater.from(parent.context))
//            return TransformViewHolder(binding)
//        }
//
//        override fun onBindViewHolder(holder: TransformViewHolder, position: Int) {
//            holder.textView.text = getItem(position)
//            holder.imageView.setImageDrawable(
//                ResourcesCompat.getDrawable(holder.imageView.resources, drawables[position], null)
//            )
//        }
//    }
//
//    class TransformViewHolder(binding: TransactionTransformBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        val imageView: ImageView = binding.imageViewItemTransform
//        val textView: TextView = binding.textViewItemTransform
//    }
//}