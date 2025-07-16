package com.example.torchapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {

    private lateinit var module: Module
    private lateinit var photoFile: File
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions()

        try {
            module = Module.load(assetFilePath("mobilenetv3_pruned_quantized.pt"))
        } catch (e: Exception) {
            Toast.makeText(this, "Model load error: ${e.message}", Toast.LENGTH_LONG).show()
            return
        }

        setContent {
            var resultText by remember { mutableStateOf("Upload or capture an X-ray image") }
            var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
            val context = LocalContext.current

            val imagePicker = rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent()
            ) { uri ->
                uri?.let {
                    val stream = contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(stream)
                    imageBitmap = bitmap
                    resultText = predict(bitmap)
                }
            }

            val cameraLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    imageBitmap = bitmap
                    resultText = predict(bitmap)
                }
            }

            Scaffold { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("KOA X-ray Classifier", style = MaterialTheme.typography.headlineSmall)

                    imageBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "X-ray Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            imagePicker.launch("image/*")
                        }) {
                            Text("Upload Image")
                        }

                        Button(onClick = {
                            photoFile = createImageFile()
                            photoUri = FileProvider.getUriForFile(
                                this@MainActivity,
                                "${packageName}.provider",
                                photoFile
                            )
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                            cameraLauncher.launch(intent)
                        }) {
                            Text("Capture Image")
                        }
                    }

                    Text(resultText)
                }
            }
        }
    }
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    private fun predict(bitmap: Bitmap): String {
        val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        val rgbBitmap = toRGB(resized)

        return try {
            val tensor = TensorImageUtils.bitmapToFloat32Tensor(
                rgbBitmap,
                TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
                TensorImageUtils.TORCHVISION_NORM_STD_RGB
            )

            val output = module.forward(IValue.from(tensor)).toTensor()
            val scores = output.dataAsFloatArray
            val softmaxScores = softmax(scores)
            val classes = listOf("Normal", "Doubtful", "Minimal", "Moderate", "Severe")
            val maxIdx = softmaxScores.indices.maxByOrNull { softmaxScores[it] } ?: -1
            val confidence = softmaxScores[maxIdx] * 100

            "Prediction: ${classes[maxIdx]} (${String.format("%.2f", confidence)}%)"
        } catch (e: Exception) {
            "Prediction failed: ${e.message}"
        }
    }

    private fun toRGB(bitmap: Bitmap): Bitmap {
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val pixel = bitmap.getPixel(x, y)
                val gray = Color.red(pixel)
                val rgb = Color.rgb(gray, gray, gray)
                result.setPixel(x, y, rgb)
            }
        }
        return result
    }

    private fun softmax(logits: FloatArray): FloatArray {
        val max = logits.maxOrNull() ?: 0f
        val exps = logits.map { Math.exp((it - max).toDouble()) }
        val sum = exps.sum()
        return exps.map { (it / sum).toFloat() }.toFloatArray()
    }

    private fun assetFilePath(assetName: String): String {
        val file = File(filesDir, assetName)
        if (file.exists()) return file.absolutePath
        assets.open(assetName).use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    }

    private fun createImageFile(): File {
        val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val dir = getExternalFilesDir(null)
        return File.createTempFile("IMG_$time", ".jpg", dir)
    }

    private fun requestPermissions() {
        val permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (notGranted.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, notGranted.toTypedArray(), 1)
        }
    }
}
