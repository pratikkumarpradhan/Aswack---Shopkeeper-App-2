package com.avdhootsolutions.aswack_shopkeeper.imagecrop

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.FileNotFoundException

object BitmapUtil {
    fun decodeUriAsBitmap(context: Context?, uri: Uri?): Bitmap? {
        if (context == null || uri == null) return null
        var bitmap: Bitmap? = null
        bitmap = try {
            BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        }
        return bitmap
    }
}