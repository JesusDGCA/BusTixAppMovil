package com.example.appmovilbustix.utils

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

object QRCodeGenerator {
    fun generateQRCodeBitmap(text: String, width: Int = 512, height: Int = 512): ImageBitmap? {
        // No se puede generar un QR de un texto vacío
        if (text.isBlank()) {
            return null
        }

        try {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            return bmp.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}