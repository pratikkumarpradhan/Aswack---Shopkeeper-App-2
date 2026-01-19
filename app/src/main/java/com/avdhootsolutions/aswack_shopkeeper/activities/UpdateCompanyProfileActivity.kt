package com.avdhootsolutions.aswack_shopkeeper.activities

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropHandler
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropParams
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import android.os.Build
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.view.Gravity
import android.content.Intent
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropHelper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Prefrences
import android.text.TextUtils
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog
import java.io.*
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.adapters.*
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.TimingEnum
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import com.squareup.picasso.Picasso
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.avdhootsolutions.aswack_shopkeeper.enums.FileTypeEnum
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.CompanyProfileViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_company_profile.*


class UpdateCompanyProfileActivity : AppCompatActivity(), CropHandler{
    lateinit var mContext: Context
    lateinit var mCropParams: CropParams
    private var imagePath: String = ""
    private var currentPhotoPathName: String = ""
    lateinit var dialogProfilePicture: Dialog

    /**
     * View model
     */
    lateinit var companyProfileViewModel: CompanyProfileViewModel



    lateinit var activityResultLauncher: ActivityResultLauncher<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_profile)
        mContext = this
        initView()

        clickListener()
    }

    private fun initView() {

        tvSubmit.text = resources.getString(R.string.update)
        ivEdit.visibility = View.GONE
        etcname.keyListener = null

        companyProfileViewModel = ViewModelProvider(this).get(CompanyProfileViewModel::class.java)

        companyProfileViewModel.companyId = intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()
        companyProfileViewModel.selectedCategoryFromHome = intent.getStringExtra(IntentKeyEnum.CAT_ID.name).toString()

        progressBar.visibility = View.VISIBLE
        var productList = ProductList()
        productList.seller_company_id = companyProfileViewModel.companyId

        companyProfileViewModel.getCompanyProfile(productList)

        companyProfileViewModel.companyDetailLiveData.observe(this, androidx.lifecycle.Observer { companyDetails ->
            progressBar.visibility = View.GONE
            Glide.with(mContext).load(companyDetails.image).placeholder(R.drawable.placeholder).into(ivLogo)
            etcname.setText(companyDetails.company_name)
            etcownername.setText(companyDetails.owner_name)
            etclandline.setText(companyDetails.company_phone)
            etcmobileno.setText(companyDetails.company_mobile)
            etcaltmobileno.setText(companyDetails.company_alt_mobile)
            etcemail.setText(companyDetails.company_email_id)
            etcwebsite.setText(companyDetails.company_website)
            ethouseno.setText(companyDetails.building_number)
            etcstreetnoname.setText(companyDetails.street_number)
            etcLandmark.setText(companyDetails.landmark)
            etcpincode.setText(companyDetails.pincode)
            etcdetails.setText(companyDetails.desciption)


        })


        rbtiming.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            when (radio) {
                radio_regular -> {
                    tlTimmings.visibility = View.VISIBLE
                }
                radio_emergancy -> {

                    tlTimmings.visibility = View.GONE
                    // some code
                }
            }
        }



        companyProfileViewModel.errorMessage.observe(this, androidx.lifecycle.Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


        companyProfileViewModel.successMessage.observe(this, androidx.lifecycle.Observer { success ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, resources.getString(R.string.company_profile_updated), Toast.LENGTH_SHORT).show()
            finish()
        })

        companyProfileViewModel.errorMessage.observe(this, androidx.lifecycle.Observer { error ->

            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })

        lblregistration.setText(getString(R.string.company_profile_page))
        mCropParams = CropParams(mContext)

        lblcupload.setOnClickListener(View.OnClickListener { checkPermission() })
    }

    private fun clickListener() {


        tvSubmit.setOnClickListener(View.OnClickListener {
            if (etcwebsite.text.toString().isNullOrEmpty() ||
                    etcname.text.toString().isNullOrEmpty() ||
                    etcownername.text.toString().isNullOrEmpty() ||
                    etcmobileno.text.toString().isNullOrEmpty() ||
                    etcemail.text.toString().isNullOrEmpty() ||
                    etcname.text.toString().isNullOrEmpty() ||
                    etcownername.text.toString().isNullOrEmpty() ||
                    etcpincode.text.toString().isNullOrEmpty()
                ) {
                    Toast.makeText(mContext,
                        resources.getString(R.string.fill_all_details),
                        Toast.LENGTH_SHORT).show()
                }   else {

                    progressBar.visibility = View.VISIBLE

                    val register = CompanyRegister()
                    register.company_id = companyProfileViewModel.companyDetailLiveData!!.value!!.id
//                    register.name = etcname.text.toString()
                    register.ownername = etcownername.text.toString()
                    register.email = etcemail.text.toString()
                    register.phone = etclandline.text.toString()
                    register.mobile = etcmobileno.text.toString()
                    register.amobile = etcaltmobileno.text.toString()
                    register.pincode = etcpincode.text.toString()
                    register.houseno = ethouseno.text.toString()
                    register.streetno = etcstreetnoname.text.toString()
                    register.landmark = etcLandmark.text.toString()
                    register.pincode = etcpincode.text.toString()
                    register.website = etcwebsite.text.toString()
                    register.detail = etcdetails.text.toString()


                if (companyProfileViewModel.filePath.isEmpty()){
                    companyProfileViewModel.apiCompanyUpdateWithFile(register, null, mContext)
                }else{
                    val file = File(companyProfileViewModel.filePath)
                    companyProfileViewModel.apiCompanyUpdateWithFile(register, file, mContext)
                }


                }

        })


        tvSelectLocation.setOnClickListener {
            val intent = Intent(mContext, MapActivity::class.java)
            mapResultLauncher.launch(intent)
        }


    }

    var mapResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult?> {
                override fun onActivityResult(result: ActivityResult?) {
                    if (result?.resultCode == Activity.RESULT_OK) {
                        // Here, no request code
                        val data: Intent? = result.data

                           companyProfileViewModel.latitude =
                               data?.getStringExtra(IntentKeyEnum.LATITUDE.name).toString()

                        companyProfileViewModel.longitude =
                            data?.getStringExtra(IntentKeyEnum.LONGITUDE.name).toString()

                        if (companyProfileViewModel.latitude.isNotEmpty()){
                            tvSelectLocation.text = resources.getString(R.string.location_fetched)
                        }


                    }
                }
            })


    /**
     * Check permission for external storage
     */
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            showProfilePicDialog()
        } else {
            if (ContextCompat.checkSelfPermission(mContext!!,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    mContext!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    mContext!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {
                showProfilePicDialog()
            } else {
                ActivityCompat.requestPermissions((mContext as Activity?)!!,
                    arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1)
            }
        }
    }

    private fun showProfilePicDialog() {
        mCropParams = CropParams((mContext as Activity?)!!)
        dialogProfilePicture = Dialog(mContext, R.style.picture_dialog_style)
        dialogProfilePicture.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogProfilePicture.setContentView(R.layout.dialog_choose_image)
        val wlmp = dialogProfilePicture.window?.attributes
        wlmp?.gravity = Gravity.BOTTOM
        wlmp?.width = WindowManager.LayoutParams.FILL_PARENT
        wlmp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialogProfilePicture.window?.attributes = wlmp
        dialogProfilePicture.show()
        val tvCamera = dialogProfilePicture.findViewById<TextView>(R.id.tvCamera)
        val tvGallery = dialogProfilePicture.findViewById<TextView>(R.id.tvGallery)
        val tvCancel = dialogProfilePicture.findViewById<TextView>(R.id.tvCancel)
        tvGallery.setOnClickListener {
            dialogProfilePicture.dismiss()
            openGallery()
        }
        tvCamera.setOnClickListener {
            dialogProfilePicture.dismiss()
            openCamera()
        }
        tvCancel.setOnClickListener { dialogProfilePicture.dismiss() }
    }

    fun openGallery() {
        mCropParams.refreshUri()
        mCropParams.enable = false
        mCropParams.compress = false
        val intent = CropHelper.buildGalleryIntent(mCropParams, mContext as Activity)
        startActivityForResult(intent, CropHelper.REQUEST_CROP)
    }

    private fun openCamera() {
        mCropParams.refreshUri()
        mCropParams.enable = false
        mCropParams.compress = false
        val intent = CropHelper.buildCameraIntent(mCropParams, mContext as Activity)
        startActivityForResult(intent, CropHelper.REQUEST_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropHelper.REQUEST_CROP) {
            CropHelper.handleResult(this, requestCode, resultCode, data)
        } else if (requestCode == CropHelper.REQUEST_CAMERA) {
            CropHelper.handleResult(this, requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showProfilePicDialog()
                } else {
                    // permission denied
                }
            }
        }
    }

    override fun onPhotoCropped(uri: Uri) {
        if (!mCropParams!!.compress) {
            imagePath = uri.path.toString()
            Log.d("onPhotoCropped", "imagePath==$imagePath")
            val tempFile = File(imagePath)
            Log.d("length", "onPhotoCropped: " + tempFile.length())
            if (tempFile.length() >= Prefrences.IMAGE_LIMIT) {
                val builder = AlertDialog.Builder(
                    mContext!!)
                builder.setMessage(getString(R.string.image_size_restriction_message))
                builder.setPositiveButton(getString(R.string.ok)) { dialog, which -> dialog.dismiss() }
                builder.show()
                builder.setCancelable(false)
            } else {
                setImage(imagePath)
            }
        }
    }

    private fun setImage(imagePath: String) {
        currentPhotoPathName = imagePath

      //  var mulPart: Part? = null
        if (currentPhotoPathName != null && !TextUtils.isEmpty(currentPhotoPathName)) {
            val compressImg = compressImage(currentPhotoPathName!!)

            Picasso.get().load(File(compressImg))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivLogo);

            ivLogo.visibility = View.VISIBLE
            lblcupload.text = "Uploaded"

            compressImg?.let {
                companyProfileViewModel.filePath = it
                Log.e("filePath ", companyProfileViewModel.filePath)
            }




            val compressfile = File(compressImg)
//            val file = File(compressImg)
//            val reqSpecificationImage: RequestBody =
//                create.create(parse.parse("multipart/form-data"), file)
//            mulPart = createFormData.createFormData("file", file.name, reqSpecificationImage)
            // RequestBody req_profile_id = RequestBody.create(MediaType.parse("text/plain"), Utils.getPrefData(Prefrences.USER_ID, mContext));

            // apiUpdateImage(req_profile_id, mulPart);


            //pass it like this
            //pass it like this
            val file = File(compressImg)
//            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//
//// MultipartBody.Part is used to send also the actual file name
//
//// MultipartBody.Part is used to send also the actual file name
//            val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestFile)
//
//// add another part within the multipart request
//
//// add another part within the multipart request
//            val fullName = RequestBody.create(MediaType.parse("multipart/form-data"), "Your Name")
//
//            service.updateProfile(id, fullName, body, other)



        }
    }

    override fun onCompressed(uri: Uri) {
        imagePath = uri.path.toString()
        setImage(imagePath)
    }

    override fun onCancel() {}
    override fun onFailed(message: String) {}
    override fun handleIntent(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }

    override fun onDestroy() {
        CropHelper.clearCacheDir()
        super.onDestroy()
    }

    override fun getCropParams(): CropParams {
        return mCropParams!!
    }

    fun compressImage(imageUri: String): String? {
        val u = Uri.fromFile(File(imageUri))
        var imageStream: InputStream? = null
        try {
            imageStream = mContext!!.contentResolver.openInputStream(u)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        var selectedImage = BitmapFactory.decodeStream(imageStream)
        selectedImage =
            getResizedBitmap(selectedImage, 1200) // 400 is for example, replace with desired size
        val exif: ExifInterface
        try {
            exif = ExifInterface(getRealPathFromURI(imageUri)!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0)
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            selectedImage = Bitmap.createBitmap(selectedImage, 0, 0,
                selectedImage.width, selectedImage.height, matrix,
                true)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var out: FileOutputStream? = null
        val filename = filename
        try {
            out = FileOutputStream(filename)

//          write the compressed bitmap at the destination specified by filename.
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 80, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return filename
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    val filename: String?
        get() {
            val mediaStorageDir = File(Environment.getExternalStorageDirectory()
                .toString() + "/Android/data/"
                    + mContext!!.applicationContext.packageName
                    + "/MyFolder")
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null
                }
            }
            val mediaFile: File
            val mImageName = System.currentTimeMillis().toString() + ".jpg"
            mediaFile = File(mediaStorageDir.path + File.separator + mImageName)
            return mediaFile.toString()
        }

    private fun getRealPathFromURI(contentURI: String): String? {
        val contentUri = Uri.parse(contentURI)
        val cursor = mContext!!.contentResolver.query(contentUri, null, null, null, null)
        return if (cursor == null) {
            contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(index)
        }
    }



    override fun onResume() {
        super.onResume()


    }

}