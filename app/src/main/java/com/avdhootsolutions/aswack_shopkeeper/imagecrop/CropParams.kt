package com.avdhootsolutions.aswack_shopkeeper.imagecrop

import android.content.Context
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropHelper
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropParams

class CropParams(var context: Context) {
    var uri: Uri? = null
    var type: String
    var outputFormat: String
    var crop: String
    var scale: Boolean
    var returnData: Boolean
    var noFaceDetection: Boolean
    var scaleUpIfNeeded: Boolean

    /**
     * Default is true, if set false, crop function will not work,
     * it will only pick up images from gallery or take pictures from camera.
     */

    var enable: Boolean

    /**
     * Default is false, if it is from capture and without crop, the image could be large
     * enough to trigger OOM, it is better to compress image while enable is false
     */
    @JvmField
    var compress: Boolean
    var rotateToCorrectDirection: Boolean
    @JvmField
    var compressWidth: Int
    @JvmField
    var compressHeight: Int
    @JvmField
    var compressQuality: Int
    @JvmField
    var aspectX: Int
    @JvmField
    var aspectY: Int
    @JvmField
    var outputX: Int
    @JvmField
    var outputY: Int
    fun refreshUri() {
        uri = CropHelper.generateUri(context)
        Log.d("CropHelper", "refreshUri: $uri")
    }

    companion object {
        const val CROP_TYPE = "image/*"
        val OUTPUT_FORMAT = Bitmap.CompressFormat.JPEG.toString()
        const val DEFAULT_ASPECT = 1
        const val DEFAULT_OUTPUT = 300
        const val DEFAULT_COMPRESS_WIDTH = 1000
        const val DEFAULT_COMPRESS_HEIGHT = 1000
        const val DEFAULT_COMPRESS_QUALITY = 90
    }

    init {
        type = CROP_TYPE
        outputFormat = OUTPUT_FORMAT
        crop = "true"
        scale = true
        returnData = false
        noFaceDetection = true
        scaleUpIfNeeded = true
        enable = true
        rotateToCorrectDirection = false
        compress = false
        compressQuality = DEFAULT_COMPRESS_QUALITY
        compressWidth = DEFAULT_COMPRESS_WIDTH
        compressHeight = DEFAULT_COMPRESS_HEIGHT
        aspectX = DEFAULT_ASPECT
        aspectY = DEFAULT_ASPECT
        outputX = DEFAULT_OUTPUT
        outputY = DEFAULT_OUTPUT
        refreshUri()
    }
}