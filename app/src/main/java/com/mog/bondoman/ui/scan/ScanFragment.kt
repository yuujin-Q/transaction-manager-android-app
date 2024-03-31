package com.mog.bondoman.ui.scan

import android.Manifest
import android.app.Activity
import android.widget.ImageView
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mog.bondoman.databinding.FragmentScanBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ScanViewModel

    private val CAMERA_PERMISSION_CODE = 101
    private val GALLERY_PERMISSION_CODE = 102

    private var currentImageUri: Uri? = null

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data") as Bitmap
            showImagePreviewDialog(imageBitmap)
        } else {
            Toast.makeText(requireContext(), "Failed to capture image", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            dispatchTakePictureIntent()
        } else {
            Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                currentImageUri = uri
                showImagePreviewDialog(uri)
            }
        } else {
            Toast.makeText(requireContext(), "Failed to open gallery", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ScanViewModel::class.java]

        binding.btnCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                dispatchTakePictureIntent()
            } else {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        binding.btnGallery.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            } else {
                requestGalleryPermission()
            }
        }
    }

    private fun requestGalleryPermission() {
        requestGalleryLauncher.launch(
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        )
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        requestGalleryLauncher.launch(galleryIntent)
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureLauncher.launch(takePictureIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun uploadImageToServer(imageBitmap: Bitmap) {
        // Simulate uploading image to server
        viewModel.uploadImage(imageBitmap)
        // Show result and ask for another scan
        showResultDialog()
    }

    private fun showImagePreviewDialog(imageBitmap: Bitmap?) {
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        alertDialogBuilder.setTitle("Image Preview")
        val imageView = ImageView(requireContext())

        if (imageBitmap != null) {
            // Set bitmap to the image view
            imageView.setImageBitmap(imageBitmap)
            alertDialogBuilder.setView(imageView)
        } else {
            Toast.makeText(requireContext(), "Bitmap is null", Toast.LENGTH_SHORT).show()
            return
        }

        alertDialogBuilder.setPositiveButton("Scan") { _, _ ->
            // Upload the image to the server
            uploadImageToServer(imageBitmap)
        }
        alertDialogBuilder.setNegativeButton("Cancel", null)
        alertDialogBuilder.show()
    }
    private fun showImagePreviewDialog(imageUri: Uri) {
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        alertDialogBuilder.setTitle("Image Preview")
        val imageView = ImageView(requireContext())

        try {
            val imageBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
            imageView.setImageBitmap(imageBitmap)
            alertDialogBuilder.setView(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show()
        }

        alertDialogBuilder.setPositiveButton("Scan") { _, _ ->
            try {
                val imageBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
                uploadImageToServer(imageBitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialogBuilder.setNegativeButton("Cancel", null)
        alertDialogBuilder.show()
    }



    private fun showResultDialog() {
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        alertDialogBuilder.setTitle("Scan Result")
        alertDialogBuilder.setMessage("Scan completed successfully.")
        alertDialogBuilder.setNegativeButton("Cancel", null)
        alertDialogBuilder.setNegativeButton("Save") { _, _ ->
            // Save to transaction list
            Toast.makeText(requireContext(), "Saved to transaction list", Toast.LENGTH_SHORT).show()
        }
        alertDialogBuilder.show()
    }
}
