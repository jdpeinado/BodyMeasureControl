package com.josedo.bodymeasurecontrol.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream

class ImageStorageManager {
    companion object {
        fun saveToInternalStorage(context: Context, bitmapImage: Bitmap, imageFileName: String): String {
            context.openFileOutput(imageFileName, Context.MODE_PRIVATE).use { fos ->
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 25, fos)
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
    }
}