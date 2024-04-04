package com.mog.bondoman.ui.scan

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mog.bondoman.R
import com.mog.bondoman.databinding.FragmentScanBinding
import android.graphics.Bitmap
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.IOException


class ScanFragment : Fragment() {

    private lateinit var cameraPreviewView: PreviewView  // Change to PreviewView
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ScanViewModel
    private lateinit var controller: LifecycleCameraController

    private var currentImageUri: Uri? = null

    private var imageCapture: ImageCapture? = null


    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data") as Bitmap
            showImagePreviewDialog(imageBitmap) // Show the image preview dialog
        } else {
            Toast.makeText(requireContext(), "Failed to capture image", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
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
        viewModel = ViewModelProvider(this).get(ScanViewModel::class.java)

        binding.btnCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                dispatchTakePictureIntent()
            } else {
                requestCameraPermission()
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
        super.onViewCreated(view, savedInstanceState)

        cameraPreviewView = view.findViewById<PreviewView>(R.id.camera_preview)!!

        viewModel = ViewModelProvider(this).get(ScanViewModel::class.java)

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()

            // Inisialisasi imageCapture
            imageCapture = ImageCapture.Builder()
                .build()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(cameraPreviewView.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))


        // Initialize controller
        controller = LifecycleCameraController(requireContext()).apply {
            setEnabledUseCases(
                LifecycleCameraController.IMAGE_CAPTURE or
                        LifecycleCameraController.VIDEO_CAPTURE
            )
        }

        //val cameraSwitchButton = view.findViewById<Button>(R.id.camera_switch_button)
        //val openGalleryButton = view.findViewById<Button>(R.id.open_gallery_button)
        val takePhotoButton = view.findViewById<Button>(R.id.take_photo_button)

//        cameraPreviewView.bindController(controller, viewLifecycleOwner)

//        cameraSwitchButton.setOnClickListener {
//            viewModel.toggleCamera()
//        }

        takePhotoButton.setOnClickListener {
            imageCapture?.let { capture ->
                // Membuat file untuk menyimpan gambar yang diambil
                val photoFile = File(requireContext().externalMediaDirs.firstOrNull(), "${System.currentTimeMillis()}.jpg")

                // Membuat konfigurasi untuk menyimpan foto
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                // Mengambil gambar menggunakan imageCapture
                capture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri = Uri.fromFile(photoFile)
                        showImagePreviewDialog(savedUri)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(requireContext(), "Failed to capture image: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } ?: run {
                Toast.makeText(requireContext(), "ImageCapture is null", Toast.LENGTH_SHORT).show()
            }
        }

        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                requireActivity(), CAMERAX_PERMISSIONS, 0
            )
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    companion object {
        val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        )
    }
    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
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

        alertDialogBuilder.setPositiveButton("Continue") { _, _ ->
            // Upload the image to the server
            uploadImageToServer(imageBitmap)
        }
        alertDialogBuilder.setNegativeButton("Cancel", null)
        alertDialogBuilder.show()
    }

    private fun showImagePreviewDialog(imageUri: Uri?) {
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        alertDialogBuilder.setTitle("Image Preview")
        val imageView = ImageView(requireContext())

        if (imageUri != null) {
            try {
                // Set image URI to the image view
                imageView.setImageURI(imageUri)
                alertDialogBuilder.setView(imageView)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            Toast.makeText(requireContext(), "Image URI is null", Toast.LENGTH_SHORT).show()
            return
        }

        alertDialogBuilder.setPositiveButton("Continue") { _, _ ->
            // Handle scanning process here
            // For example: pass the URI to a scanning function
            // scanImage(imageUri)
            showResultDialog()
        }
        alertDialogBuilder.setNegativeButton("Cancel", null)
        alertDialogBuilder.show()
    }

    private fun showResultDialog() {
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        alertDialogBuilder.setTitle("Scan")
        alertDialogBuilder.setMessage("Scan completed successfully.")
        alertDialogBuilder.setNegativeButton("Cancel", null)
        alertDialogBuilder.setNegativeButton("OK") { _, _ ->
            // Save to transaction list
            Toast.makeText(requireContext(), "Saved to transaction list", Toast.LENGTH_SHORT).show()
        }
        alertDialogBuilder.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

