package com.ritesh.weathersnap.data.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

data class CompressionResult(
    val originalPath: String,
    val compressedPath: String,
    val originalSizeBytes: Long,
    val compressedSizeBytes: Long
)

@Singleton
class ImageCompressor @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun compress(
        sourcePath: String,
        maxDimensionPx: Int = 1080,
        quality: Int = 80
    ): CompressionResult = withContext(Dispatchers.IO) {
        val source = File(sourcePath)
        val originalSize = source.length()

        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(sourcePath, bounds)

        val sampleSize = calculateSampleSize(bounds.outWidth, bounds.outHeight, maxDimensionPx)
        val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        val decoded = BitmapFactory.decodeFile(sourcePath, decodeOptions)
            ?: error("Failed to decode image at $sourcePath")
        val bitmap = applyExifRotation(decoded, sourcePath)

        val reportsDir = File(context.filesDir, "reports").apply { mkdirs() }
        val destination = File(reportsDir, "report_${System.currentTimeMillis()}.jpg")
        FileOutputStream(destination).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
        }
        bitmap.recycle()

        CompressionResult(
            originalPath = sourcePath,
            compressedPath = destination.absolutePath,
            originalSizeBytes = originalSize,
            compressedSizeBytes = destination.length()
        )
    }

    private fun applyExifRotation(bitmap: Bitmap, sourcePath: String): Bitmap {
        val orientation = runCatching {
            ExifInterface(sourcePath).getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        }.getOrDefault(ExifInterface.ORIENTATION_NORMAL)

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1f, 1f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.preScale(1f, -1f)
            else -> return bitmap
        }
        val rotated = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
        )
        if (rotated != bitmap) bitmap.recycle()
        return rotated
    }

    private fun calculateSampleSize(width: Int, height: Int, maxDim: Int): Int {
        var sample = 1
        var w = width
        var h = height
        while (w / 2 >= maxDim || h / 2 >= maxDim) {
            sample *= 2
            w /= 2
            h /= 2
        }
        return sample
    }
}
