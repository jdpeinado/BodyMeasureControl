package com.josedo.bodymeasurecontrol.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import java.io.File
import java.io.FileInputStream
import kotlin.math.roundToInt

class ImageStorageManager {
    companion object {
        val RATIO = 0.1

        fun saveToInternalStorage(context: Context, bitmapImage: Bitmap, imageFileName: String): String {
            context.openFileOutput(imageFileName, Context.MODE_PRIVATE).use { fos ->
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 25, fos)
            }

            val thumbnal = ThumbnailUtils.extractThumbnail(bitmapImage, (bitmapImage.width*RATIO).roundToInt(), (bitmapImage.height*RATIO).roundToInt())
            val thumbnailFileName = getThumbnailFilename(imageFileName)
            context.openFileOutput(thumbnailFileName, Context.MODE_PRIVATE).use { fos ->
                thumbnal.compress(Bitmap.CompressFormat.JPEG, 25, fos)
            }

            return context.filesDir.absolutePath + File.separatorChar + imageFileName
        }

        fun getImageFromInternalStorage(imageFileName: String): Bitmap? {
            val file = File(imageFileName)
            return BitmapFactory.decodeStream(FileInputStream(file))
        }

        fun deleteImageFromInternalStorage(imageFileName: String): Boolean {
            val file = File(imageFileName)
            return file.delete()
        }

        fun getThumbnailFilename(imageFileName: String): String {
            return imageFileName.substring(0, imageFileName.lastIndexOf(".")) + "_thumbnail.jpeg"
        }
    }
}