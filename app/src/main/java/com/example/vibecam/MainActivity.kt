package com.example.vibecam
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.vibecam.presets.Preset
import com.example.vibecam.presets.PresetRepository
import androidx.compose.runtime.Composable
import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private lateinit var previewView: PreviewView
    private var imageCapture: ImageCapture? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startCamera()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        previewView = PreviewView(this)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        setContent {

            var selectedPreset by remember {
                mutableStateOf(
                    PresetRepository.presets.first()
                )
            }

            CameraPreview(
                previewView = previewView,
                selectedPreset = selectedPreset,
                onPresetSelected = {
                    println("CLICKED: ${it.name}")
                    selectedPreset = it
                },
                onCaptureClick = {
                    takePhoto()
                }
            )
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }

            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()

            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture
            )
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val contentValues = ContentValues().apply {
            put(
                MediaStore.MediaColumns.DISPLAY_NAME,
                "VibeCam_${System.currentTimeMillis()}"
            )
            put(
                MediaStore.MediaColumns.MIME_TYPE,
                "image/jpeg"
            )
        }
        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ).build()
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageSavedCallback {

                    override fun onImageSaved(
                        outputFileResults: ImageCapture.OutputFileResults
                    ) {
                        runOnUiThread {
                            Toast.makeText(
                                this@MainActivity,
                                "PHOTO SAVED 📸",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onError(
                        exception: ImageCaptureException
                    ) {
                        exception.printStackTrace()
                    }
                }
        )
    }
}

@Composable
fun CameraPreview(
    previewView: PreviewView,
    selectedPreset: Preset,
    onPresetSelected: (Preset) -> Unit,
    onCaptureClick: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = selectedPreset.name,
                color = Color.White,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ISO ${selectedPreset.iso}",
                color = Color.White
            )

            Text(
                text = selectedPreset.shutterSpeed,
                color = Color.White
            )
        }

        LazyRow(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)
        ) {

            items(PresetRepository.presets) { preset ->

                Card(
                    onClick = {
                        onPresetSelected(preset)
                    },
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (preset.id == selectedPreset.id)
                                Color(0xFFFF7A00)
                            else
                                Color.White
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text = preset.name,
                        color =
                            if (preset.id == selectedPreset.id)
                                Color.White
                            else
                                Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        Button(
            onClick = onCaptureClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Text("Capture")
        }
    }
}