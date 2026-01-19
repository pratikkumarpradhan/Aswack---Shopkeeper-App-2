package com.avdhootsolutions.aswack_shopkeeper.imagecrop

import android.content.Intent
import android.net.Uri

interface CropHandler {
    fun onPhotoCropped(uri: Uri)
    fun onCompressed(uri: Uri)
    fun onCancel()
    fun onFailed(message: String)
    fun handleIntent(intent: Intent, requestCode: Int)
    fun getCropParams() : CropParams
}