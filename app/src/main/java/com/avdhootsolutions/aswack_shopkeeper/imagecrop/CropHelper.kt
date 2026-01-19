package com.avdhootsolutions.aswack_shopkeeper.imagecrop

import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropFileUtils.getSmartFilePath
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropFileUtils.copyFile
import android.app.Activity
import android.content.Context
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropHelper
import android.os.Environment
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropHandler
import android.content.Intent
import android.net.Uri
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropParams
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropFileUtils
import android.os.Build
import androidx.core.content.FileProvider
import android.provider.MediaStore
import android.util.Log
import com.avdhootsolutions.aswack_shopkeeper.utilities.Utils
import java.io.File
import java.io.IOException
import java.lang.Exception

class CropHelper {
    private val context: Context? = null

    companion object {
        const val TAG = "CropHelper"
        var uriFile: File? = null

        /**
         * request code of Activities or Fragments
         * You will have to change the values of the request codes below if they conflict with your own.
         */
        const val REQUEST_CROP = 127
        const val REQUEST_CAMERA = 128
        const val REQUEST_CAMERA_BG = 130
        const val REQUEST_PICK = 129
        const val CROP_CACHE_FOLDER = "PhotoCropper"
        private var activity: Activity? = null
        fun generateUri(mContext: Context?): Uri {
            activity = mContext as Activity?
            val currentPath = Environment.getExternalStorageDirectory()
                .toString() + "/Android/data/com.avdhootsolutions.aswack_shopkeeper/CHATIMAGES"
            val tempDir = File(currentPath)
            //        File tempDir = new File(Environment.getExternalStorageDirectory() + File.separator + CROP_CACHE_FOLDER);
            // File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            // File tempDir = new File(sdCard.getAbsolutePath() + "/"+CROP_CACHE_FOLDER);
            if (!tempDir.exists()) {
                try {
                    val result = tempDir.mkdir()
                } catch (e: Exception) {
                    Log.e(TAG, "generateUri failed: $tempDir", e)
                }
            }
            //        String name = "app" + System.currentTimeMillis();
            val name = "app"
            try {
                uriFile = File.createTempFile(name, ".jpg", tempDir)
                Utils.setPrefData("temp_name", uriFile?.name, mContext)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return Uri.fromFile(uriFile)

            //String name = String.format("image-%d.jpg", System.currentTimeMillis());
            // return Uri.fromFile(tempDir).buildUpon().appendPath(name).build();
        }

        fun isPhotoReallyCropped(uri: Uri?): Boolean {
            val file = File(uri!!.path)
            val length = file.length()
            return length > 0
        }

        fun handleResult(handler: CropHandler?, requestCode: Int, resultCode: Int, data: Intent?) {
            if (handler == null) return
            if (resultCode == Activity.RESULT_CANCELED) {
                handler.onCancel()
            } else if (resultCode == Activity.RESULT_OK) {
                val cropParams = handler.getCropParams()
                if (cropParams == null) {
                    handler.onFailed("CropHandler's params MUST NOT be null!")
                    return
                }
                when (requestCode) {
                    REQUEST_PICK, REQUEST_CROP -> {
                        if (isPhotoReallyCropped(cropParams.uri)) {
                            Log.d(TAG, "Photo cropped!")
                            onPhotoCropped(handler, cropParams)

                        } else {
                            val context = handler.getCropParams()!!.context
                            if (context != null) {
                                if (data != null && data.data != null) {
                                    val path = getSmartFilePath(context, data.data!!)
                                    val result = copyFile(path, cropParams.uri!!.path!!)
                                    if (!result) {
                                        handler.onFailed("Copy file to cached folder failed")

                                    }
                                } else {
                                    handler.onFailed("Returned data is null $data")

                                }
                            } else {
                                handler.onFailed("CropHandler's context MUST NOT be null!")
                            }
                        }
                        if (cropParams.enable) {
                            // Send this Uri to Crop
                            val intent = buildCropFromUriIntent(cropParams)
                            handler.handleIntent(intent, REQUEST_CROP)
                        } else {
                            Log.d(TAG, "Photo cropped!")
                            onPhotoCropped(handler, cropParams)
                        }
                    }
                    REQUEST_CAMERA -> if (cropParams.enable) {
                        val intent = buildCropFromUriIntent(cropParams)
                        handler.handleIntent(intent, REQUEST_CROP)
                    } else {
                        Log.d(TAG, "Photo cropped!")
                        onPhotoCropped(handler, cropParams)
                    }
                }
            }
        }

        private fun onPhotoCropped(handler: CropHandler, cropParams: CropParams) {
            if (cropParams.compress) {
                val originUri = cropParams.uri
                //            Uri compressUri = CropHelper.generateUri(activity);
//            Log.d(TAG, "onPhotoCropped: " + compressUri);
//            CompressImageUtils.compressImageFile(cropParams, originUri, compressUri);
                if (originUri != null) {
                    handler.onCompressed(originUri)
                }
            } else {
                //handler.onPhotoCropped(cropParams.uri);
                /*    Uri originUri = cropParams.uri;
            Uri compressUri = CropHelper.generateUri(activity);
//            Log.d(TAG, "onPhotoCropped: " + compressUri);
            CompressImageUtils.compressImageFile(cropParams, originUri, compressUri);
//            handler.onCompressed(originUri);*/
                handler.onPhotoCropped(Uri.fromFile(uriFile))
            }
        }

        // None-Crop Intents
        fun buildGalleryIntent(params: CropParams, mActivity: Activity): Intent {
            val intent: Intent
            activity = mActivity
            if (params.enable) {
                intent = buildCropIntent(Intent.ACTION_GET_CONTENT, params)
            } else {
                intent = Intent(Intent.ACTION_GET_CONTENT)
                    .setType("image/*")
                //                    .putExtra(MediaStore.EXTRA_OUTPUT, params.uri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    //File imagePath = new File(String.valueOf(params.uri));
                    val contentUri = FileProvider.getUriForFile(mActivity,
                        mActivity.applicationContext.packageName + ".provider",
                        uriFile!!)
                    Log.d(TAG, "buildGalleryIntent: $contentUri")
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
                } else {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, params.uri)
                }
            }
            return intent
        }

        fun buildCameraIntent(params: CropParams, mActivity: Activity): Intent {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activity = mActivity
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                //File imagePath = new File(String.valueOf(params.uri));
                val contentUri = FileProvider.getUriForFile(mActivity,
                    mActivity.applicationContext.packageName + ".provider",
                    uriFile!!)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, params.uri)
            }
            return intent
            //return new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, params.uri);
        }

        // Crop Intents
        private fun buildCropFromUriIntent(params: CropParams): Intent {
            return buildCropIntent("com.android.camera.action.CROP", params)
        }

        private fun buildCropIntent(action: String, params: CropParams): Intent {
            val intent = Intent(action)
                .putExtra("crop", "true")
                .putExtra("scale", params.scale)
                .putExtra("aspectX", params.aspectX)
                .putExtra("aspectY", params.aspectY)
                .putExtra("outputX", params.outputX)
                .putExtra("outputY", params.outputY)
                .putExtra("return-data", params.returnData)
                .putExtra("outputFormat", params.outputFormat)
                .putExtra("noFaceDetection", params.noFaceDetection)
                .putExtra("scaleUpIfNeeded", params.scaleUpIfNeeded)
            //                .putExtra(MediaStore.EXTRA_OUTPUT, params.uri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setDataAndType(params.uri, params.type)
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                //File imagePath = new File(String.valueOf(params.uri));
                val contentUri = FileProvider.getUriForFile(activity!!,
                    activity!!.applicationContext.packageName + ".provider",
                    File(params.uri.toString()))
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
            } else {
                intent.setDataAndType(params.uri, params.type)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, params.uri)
            }
            return intent
        }

        // Clear Cache
        fun clearCacheDir(): Boolean {
//        File cacheFolder = new File(Environment.getExternalStorageDirectory() + File.separator + CROP_CACHE_FOLDER);
            val cacheFolder = File(Environment.getExternalStorageDirectory()
                .toString() + "/Android/data/com.droid.aswack_shopkeeper/CHATIMAGES")
            if (cacheFolder.exists() && cacheFolder.listFiles() != null) {
                for (file in cacheFolder.listFiles()) {
                    val result = file.delete()
                    Log.d(TAG,
                        "Delete " + file.absolutePath + if (result) " succeeded" else " failed")
                }
                return true
            }
            return false
        }

        fun clearCachedCropFile(uri: Uri?): Boolean {
            if (uri == null) return false
            val file = File(uri.path)
            if (file.exists()) {
                val result = file.delete()
                Log.d(TAG, "Delete " + file.absolutePath + if (result) " succeeded" else " failed")
                return result
            }
            return false
        }
    }
}