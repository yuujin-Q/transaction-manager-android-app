package com.mog.bondoman.ui.twibbon

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mog.bondoman.R
import com.mog.bondoman.databinding.FragmentTwibbonBinding
import java.io.File
import java.io.IOException
import kotlin.math.min

class TwibbonFragment : Fragment() {
    private lateinit var cameraPreviewView: PreviewView  // Change to PreviewView
    private var _binding: FragmentTwibbonBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TwibbonViewModel
    private lateinit var controller: LifecycleCameraController

    private var imageCapture: ImageCapture? = null
    private lateinit var twibbonImageView: ImageView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTwibbonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TwibbonViewModel::class.java)

        super.onViewCreated(view, savedInstanceState)

        cameraPreviewView = view.findViewById<PreviewView>(R.id.camera_preview)!!
        twibbonImageView = view.findViewById<ImageView>(R.id.twibbon_image)!!

        viewModel = ViewModelProvider(this).get(TwibbonViewModel::class.java)

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

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

        val twibbonDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.wibu)
        twibbonImageView.setImageDrawable(twibbonDrawable)
        twibbonImageView.visibility = View.VISIBLE

        controller = LifecycleCameraController(requireContext()).apply {
            setEnabledUseCases(
                LifecycleCameraController.IMAGE_CAPTURE or
                        LifecycleCameraController.VIDEO_CAPTURE
            )
        }


        val takePhotoButton = view.findViewById<Button>(R.id.take_photo_button)

        takePhotoButton.setOnClickListener {
            imageCapture?.let { capture ->
                val externalMediaDir =
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val photoFile = File(
                    externalMediaDir,
                    "${System.currentTimeMillis()}.jpg"
                )
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                capture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(requireContext()),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            val savedUri = Uri.fromFile(photoFile)
                            showImagePreviewDialog(savedUri)
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Toast.makeText(
                                requireContext(),
                                "Failed to capture image: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
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
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        )
    }

    private fun showImagePreviewDialog(imageUri: Uri?) {
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        alertDialogBuilder.setTitle("Twibbon Preview")
        val imageView = ImageView(requireContext())

        if (imageUri != null) {
            try {
                val imageBitmap = BitmapFactory.decodeFile(imageUri.path)
                val twibbonBitmap = BitmapFactory.decodeResource(resources, R.drawable.wibu)
                val resultBitmap = overlayBitmaps(imageBitmap, twibbonBitmap)
                imageView.setImageBitmap(resultBitmap)
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
            showResultDialog()
        }
        alertDialogBuilder.setNegativeButton("Cancel", null)
        alertDialogBuilder.show()
    }

    // Fungsi untuk menggabungkan gambar foto dengan twibbon
    private fun overlayBitmaps(photoBitmap: Bitmap, twibbonBitmap: Bitmap): Bitmap {
        val photoWidth = photoBitmap.width
        val photoHeight = photoBitmap.height

        val matrix = Matrix()
        matrix.postRotate(90f)
        val rotatedPhotoBitmap =
            Bitmap.createBitmap(photoBitmap, 0, 0, photoWidth, photoHeight, matrix, true)

        val twibbonWidth = (min(rotatedPhotoBitmap.width, rotatedPhotoBitmap.height) * 0.8).toInt()
        val twibbonHeight =
            (twibbonWidth.toFloat() / twibbonBitmap.width * twibbonBitmap.height).toInt()

        val twibbonResized =
            Bitmap.createScaledBitmap(twibbonBitmap, twibbonWidth, twibbonHeight, false)

        val resultBitmap = Bitmap.createBitmap(
            rotatedPhotoBitmap.width,
            rotatedPhotoBitmap.height,
            rotatedPhotoBitmap.config
        )
        val canvas = Canvas(resultBitmap)

        val photoX = (resultBitmap.width - rotatedPhotoBitmap.width) / 2
        val photoY = (resultBitmap.height - rotatedPhotoBitmap.height) / 2
        canvas.drawBitmap(rotatedPhotoBitmap, photoX.toFloat(), photoY.toFloat(), null)

        val twibbonX = (resultBitmap.width - twibbonWidth) / 2
        val twibbonY = (resultBitmap.height - twibbonHeight) / 2
        canvas.drawBitmap(twibbonResized, twibbonX.toFloat(), twibbonY.toFloat(), null)

        return resultBitmap
    }


    private fun showResultDialog() {
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        alertDialogBuilder.setTitle("Twibbon")
        alertDialogBuilder.setMessage("Twibbon completed successfully.")
        alertDialogBuilder.setNegativeButton("Cancel", null)
        alertDialogBuilder.setNegativeButton("OK") { _, _ ->
            // Save to transaction list
            Toast.makeText(requireContext(), "Your Twibbon is AMAZING!", Toast.LENGTH_SHORT).show()
        }
        alertDialogBuilder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}