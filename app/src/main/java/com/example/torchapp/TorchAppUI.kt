package com.example.torchapp

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun KOAAppUI(
    resultText: String,
    imageBitmap: Bitmap?,
    onSelectImage: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = onSelectImage) {
            Text("Select X-ray Image")
        }

        imageBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "X-ray",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }

        Text(
            text = resultText,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewKOAAppUI() {
    KOAAppUI("Select an X-ray", null) {}
}
