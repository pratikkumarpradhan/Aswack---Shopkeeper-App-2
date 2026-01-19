package com.avdhootsolutions.aswack_shopkeeper.activities

import android.Manifest
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import android.content.Intent
import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.adapters.ImagesAdapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.SellVehicleSpinnerAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropHandler
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropHelper
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropParams
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.models.Offer
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Prefrences
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.AddOfferViewModel
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.UpdateOfferViewModel
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import kotlinx.android.synthetic.main.activity_update_offer.*
import kotlinx.android.synthetic.main.header_title.*
import kotlinx.android.synthetic.main.header_title.tvTitle
import java.io.*
import java.util.*

class UpdateOfferActivity : AppCompatActivity(), ImagesAdapter.ICustomListListener, CropHandler {
    lateinit var mContext: Context

    lateinit var picker: DatePickerDialog

    lateinit var mCropParams: CropParams
    private var imagePath: String = ""
    private var currentPhotoPathName: String = ""
    lateinit var dialogProfilePicture: Dialog

    /**
     * Adapter for our services
     */
    lateinit var imagesAdapter: ImagesAdapter



    /**
     * View model
     */
    lateinit var updateOfferViewModel: UpdateOfferViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_offer)
        mContext = this@UpdateOfferActivity
        initView()

        clickListener()
    }

    /**
     * Click events
     */
    private fun clickListener() {


        ivImage1.setOnClickListener {
            checkPermission()
            updateOfferViewModel.selectImageNumber = 1
        }

        ivImage2.setOnClickListener {
            checkPermission()
            updateOfferViewModel.selectImageNumber = 2
        }

        ivImage3.setOnClickListener {
            checkPermission()
            updateOfferViewModel.selectImageNumber = 3
        }
        ivImage4.setOnClickListener {
            checkPermission()
            updateOfferViewModel.selectImageNumber = 4
        }

        ivImage5.setOnClickListener {
            checkPermission()
            updateOfferViewModel.selectImageNumber = 5
        }

        ivImage6.setOnClickListener {
            checkPermission()
            updateOfferViewModel.selectImageNumber = 6
        }


        tvUpdate.setOnClickListener {
            if (etOfferName.text.toString().isNullOrEmpty() ||
                etTitle.text.toString().isNullOrEmpty() ||
                etOfferPrice.text.toString().isNullOrEmpty() ||
                etOfferOriPrice.text.toString().isNullOrEmpty() ||
                etDescription.text.toString().isNullOrEmpty() ||
                tvOfferStartDateValue.text.toString().isNullOrEmpty() ||
                tvOfferEndDateValue.text.toString().isNullOrEmpty()
            ) {
                Toast.makeText(mContext,
                    resources.getString(R.string.fill_all_details),
                    Toast.LENGTH_SHORT).show()
            }  else {

                progressBar.visibility = View.VISIBLE

                val offer = Offer()
                offer.offer_id = updateOfferViewModel.selectedOffer.id
                offer.name = etOfferName.text.toString()
                offer.title = etTitle.text.toString()
                offer.offer_price = etOfferPrice.text.toString()
                offer.original_price = etOfferOriPrice.text.toString()
                offer.start_date = tvOfferStartDateValue.text.toString()
                offer.end_date = tvOfferEndDateValue.text.toString()
                offer.description = etDescription.text.toString()
                offer.seller_id = Helper().getLoginData(mContext).id
//                offer.company_seller_id = updateOfferViewModel.selectedOffer.seller_company_id
//                offer.seller_company_id = updateOfferViewModel.selectedOffer.seller_company_id
//                offer.seller_category_id = updateOfferViewModel.selectedOffer.seller_category_id
                updateOfferViewModel.updateOffer(offer)

            }
        }


        iv_back.setOnClickListener(View.OnClickListener { finish() })

        tvOfferStartDateValue.setOnClickListener(View.OnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val year = cldr[Calendar.YEAR]
            picker = DatePickerDialog(mContext!!,
                { view, year, monthOfYear, dayOfMonth -> tvOfferStartDateValue.setText(year.toString() + "/" + (monthOfYear + 1) + "/" + dayOfMonth.toString()) },
                year,
                month,
                day)
            picker!!.show()
        })
        tvOfferEndDateValue.setOnClickListener(View.OnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val year = cldr[Calendar.YEAR]
            picker = DatePickerDialog(mContext!!,
                { view, year, monthOfYear, dayOfMonth -> tvOfferEndDateValue.setText(year.toString() + "/" + (monthOfYear + 1) + "/" + dayOfMonth.toString()) },
                year,
                month,
                day)
            picker!!.show()
        })

    }

    private fun initView() {
        rvPhotos.setLayoutManager(GridLayoutManager(mContext, 3))
        rvPhotos.setHasFixedSize(true)

        updateOfferViewModel = ViewModelProvider(this).get(UpdateOfferViewModel::class.java)
        updateOfferViewModel.selectedOffer = Gson().fromJson(intent.getStringExtra(IntentKeyEnum.OFFER_DETAILS.name).toString(), Offer::class.java)

        if (updateOfferViewModel.selectedOffer.product_name.isNullOrEmpty()){
            tvProductName.visibility = View.GONE
            etProductName.visibility = View.GONE
        }else{
            etProductName.setText(updateOfferViewModel.selectedOffer.product_name)
        }


        etOfferName.setText(updateOfferViewModel.selectedOffer.name)
        etTitle.setText(updateOfferViewModel.selectedOffer.title)
        etOfferPrice.setText(updateOfferViewModel.selectedOffer.offer_price)
        etOfferOriPrice.setText(updateOfferViewModel.selectedOffer.original_price)
        tvOfferStartDateValue.text = updateOfferViewModel.selectedOffer.start_date
        tvOfferEndDateValue.text = updateOfferViewModel.selectedOffer.end_date
        etDescription.setText(updateOfferViewModel.selectedOffer.description)

        setImages()

        updateOfferViewModel.errorMessage.observe(this, androidx.lifecycle.Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })

        updateOfferViewModel.successUpdateImageLiveData.observe(this,
            androidx.lifecycle.Observer { isScuccess ->
                progressBar.visibility = View.GONE
                Toast.makeText(mContext, "Image updated successfully", Toast.LENGTH_SHORT).show()
            })


        updateOfferViewModel.successMessageLiveData.observe(this, androidx.lifecycle.Observer { isSuccess ->

                 progressBar.visibility = View.GONE
                Toast.makeText(mContext, resources.getString(R.string.offer_updated_Successfully), Toast.LENGTH_SHORT).show()
                finish()

        })


        tvTitle.setText(getString(R.string.update_offer))
    }


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

            compressImg?.let {

                setImagesFromLocalDirectory(it)
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
            val mediaStorageDir = File(
                Environment.getExternalStorageDirectory()
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

    companion object {
        private const val REQUEST_CODE_CHOOSE = 23
    }

    override fun onTypesSelected() {

    }


    private fun setImagesFromLocalDirectory(path: String) {
        if (updateOfferViewModel.selectImageNumber == 1){
            Picasso.get().load(File(path))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivImage1);

            progressBar.visibility = View.VISIBLE
            val file = File(path)
            updateOfferViewModel.apiUpdateImage(updateOfferViewModel.selectedOffer.id!!, "1",file)
        }

        if (updateOfferViewModel.selectImageNumber == 2){
            Picasso.get().load(File(path))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivImage2);

            progressBar.visibility = View.VISIBLE
            val file = File(path)
            updateOfferViewModel.apiUpdateImage(updateOfferViewModel.selectedOffer.id!!, "2",file)
        }

        if (updateOfferViewModel.selectImageNumber == 3){
            Picasso.get().load(File(path))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivImage3);

            progressBar.visibility = View.VISIBLE
            val file = File(path)
            updateOfferViewModel.apiUpdateImage(updateOfferViewModel.selectedOffer.id!!, "3",file)
        }

        if (updateOfferViewModel.selectImageNumber == 4){
            Picasso.get().load(File(path))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivImage4);

            progressBar.visibility = View.VISIBLE
            val file = File(path)
            updateOfferViewModel.apiUpdateImage(updateOfferViewModel.selectedOffer.id!!, "4",file)
        }

        if (updateOfferViewModel.selectImageNumber == 5){
            Picasso.get().load(File(path))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivImage5);

            progressBar.visibility = View.VISIBLE
            val file = File(path)
            updateOfferViewModel.apiUpdateImage(updateOfferViewModel.selectedOffer.id!!, "5",file)
        }

        if (updateOfferViewModel.selectImageNumber == 6){
            Picasso.get().load(File(path))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivImage6);

            progressBar.visibility = View.VISIBLE
            val file = File(path)
            updateOfferViewModel.apiUpdateImage(updateOfferViewModel.selectedOffer.id!!, "6",file)
        }
    }

    /**
     * Set images as per url
     */
    private fun setImages() {

        if (updateOfferViewModel.selectedOffer.image_1!!.isNotEmpty()){
            Picasso.get().load(updateOfferViewModel.selectedOffer.image_1)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(ivImage1);
        }
        if (updateOfferViewModel.selectedOffer.image_2!!.isNotEmpty()){
            Picasso.get().load(updateOfferViewModel.selectedOffer.image_2)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(ivImage2);

        }

        if (updateOfferViewModel.selectedOffer.image_3!!.isNotEmpty()){
            Picasso.get().load(updateOfferViewModel.selectedOffer.image_3)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(ivImage3);
        }

        if (updateOfferViewModel.selectedOffer.image_4!!.isNotEmpty()){
            Picasso.get().load(updateOfferViewModel.selectedOffer.image_4)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(ivImage4);

        }

        if (updateOfferViewModel.selectedOffer.image_5!!.isNotEmpty()){
            Picasso.get().load(updateOfferViewModel.selectedOffer.image_5)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(ivImage5);

        }

        if (updateOfferViewModel.selectedOffer.image_6!!.isNotEmpty()){
            Picasso.get().load(updateOfferViewModel.selectedOffer.image_6)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(ivImage6);
        }



    }
}