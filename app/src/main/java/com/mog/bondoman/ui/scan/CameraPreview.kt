package com.mog.bondoman.ui.scan
//
//import android.content.Context
//import android.util.AttributeSet
//import androidx.camera.view.LifecycleCameraController
//import androidx.camera.view.PreviewView
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.lifecycleScope
//import kotlinx.coroutines.launch
//
//class CameraPreviewView @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : PreviewView(context, attrs, defStyleAttr) {
//
//    fun bindController(controller: LifecycleCameraController, lifecycleOwner: LifecycleOwner) {
//        controller.bindToLifecycle(lifecycleOwner)
//        lifecycleOwner.lifecycleScope.launch {
//            controller.cameraInfo.observe(lifecycleOwner) { cameraInfo ->
//                if (cameraInfo.cameraState == CameraState.INITIALIZED) {
//                    controller.setSurfaceProvider(surfaceProvider)
//                }
//            }
//        }
//    }
//}
