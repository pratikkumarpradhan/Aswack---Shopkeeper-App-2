package com.avdhootsolutions.aswack_shopkeeper.activities

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import android.content.Intent
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.zhihu.matisse.Matisse
import android.content.pm.ActivityInfo
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.adapters.*
import com.avdhootsolutions.aswack_shopkeeper.enums.AddedItemEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropHandler
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropHelper
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropParams
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.avdhootsolutions.aswack_shopkeeper.models.SpareParts
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Prefrences
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.UpdateSparePartsCarAccesoriesDetailViewModel
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.activity__update_spare_parts.*
import kotlinx.android.synthetic.main.header_title.*
import java.io.*


class UpdateSparePartsCarAccesoriesActivity : AppCompatActivity(), ImagesAdapter.ICustomListListener,
    CropHandler {
    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var updateProductViewModel: UpdateSparePartsCarAccesoriesDetailViewModel

    /**
     * Adapter for our services
     */
    lateinit var imagesAdapter: ImagesAdapter

    lateinit var mCropParams: CropParams
    private var imagePath: String = ""
    private var currentPhotoPathName: String = ""
    lateinit var dialogProfilePicture: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__update_spare_parts)
        mContext = this@UpdateSparePartsCarAccesoriesActivity
        initView()

        onClickListener()
    }

    /**
     * All click events
     */
    private fun onClickListener() {
        iv_back!!.setOnClickListener { finish() }

        ivImage1.setOnClickListener {
            checkPermission()
            updateProductViewModel.selectImageNumber = 1
        }

        ivImage2.setOnClickListener {
            checkPermission()
            updateProductViewModel.selectImageNumber = 2
        }

        ivImage3.setOnClickListener {
            checkPermission()
            updateProductViewModel.selectImageNumber = 3
        }
        ivImage4.setOnClickListener {
            checkPermission()
            updateProductViewModel.selectImageNumber = 4
        }

        ivImage5.setOnClickListener {
            checkPermission()
            updateProductViewModel.selectImageNumber = 5
        }

        ivImage6.setOnClickListener {
            checkPermission()
            updateProductViewModel.selectImageNumber = 6
        }

        rlReview.setOnClickListener {

            if (!rbUsed.isChecked && !rbNew.isChecked) {
                Toast.makeText(mContext,
                    resources.getString(R.string.select_condition_vehicle),
                    Toast.LENGTH_SHORT).show()
            } else if (etProductSerialNumber.text.toString().isNullOrEmpty() ||
                etProductName.text.toString().isNullOrEmpty() ||
                etDescription.text.toString().isNullOrEmpty() ||
                etPrice.text.toString().isNullOrEmpty()
            ) {
                Toast.makeText(mContext,
                    resources.getString(R.string.fill_all_details),
                    Toast.LENGTH_SHORT).show()
            } else {

                progressBar.visibility = View.VISIBLE

                val spareParts = SpareParts()
                spareParts.product_id = updateProductViewModel.productList.id
                spareParts.seller_id = Helper().getLoginData(mContext).id
                spareParts.seller_company_id = updateProductViewModel.productList.seller_company_id
                spareParts.package_purchased_id = updateProductViewModel.productList.package_purchased_id
                spareParts.master_category_id = updateProductViewModel.productList.master_category_id
                spareParts.vehicle_category = updateProductViewModel.productList.vehicle_cat_id
                spareParts.product_name = etProductName.text.toString()
                spareParts.serial_number = etProductSerialNumber.text.toString()
                spareParts.description = etDescription.text.toString()
                spareParts.price = etPrice.text.toString()

                if (rbUsed.isChecked){
                    spareParts.product_condition = "0"
                }else{
                    spareParts.product_condition = "1"
                }

                spareParts.vehicle_company =
                    updateProductViewModel.brandTypeModelsLiveData.value?.get(spBrand.selectedItemPosition)?.vehicle_brand_id

                spareParts.vehicle_model_type =
                    updateProductViewModel.brandTypeModelsLiveData.value?.get(spBrand.selectedItemPosition)?.type_list?.get(
                        spType.selectedItemPosition)?.vehicle_type_id

                spareParts.vehicle_model_name =
                    updateProductViewModel.brandTypeModelsLiveData.value?.get(spBrand.selectedItemPosition)?.type_list?.get(
                        spType.selectedItemPosition)?.model_list?.get(spModel.selectedItemPosition)?.vehicle_model_id

                updateProductViewModel.apiUpdateProduct(spareParts)
            }


        }
    }

    private fun initView() {
        rvPhotos.setLayoutManager(GridLayoutManager(mContext, 3))
        rvPhotos.setHasFixedSize(true)
        tvReview.text = resources.getString(R.string.update)
        updateProductViewModel = ViewModelProvider(this).get(UpdateSparePartsCarAccesoriesDetailViewModel::class.java)
        updateProductViewModel.productList = Gson().fromJson(intent.getStringExtra(IntentKeyEnum.PRODUCT_LIST.name).toString(), ProductList::class.java)

        etProductName.setText(updateProductViewModel.productList.product_name)
        etPrice.setText(updateProductViewModel.productList.price)
        etDescription.setText(updateProductViewModel.productList.description)
        etProductSerialNumber.setText(updateProductViewModel.productList.serial_number)

        if (updateProductViewModel.productList.product_condition == "0"){
            rbUsed.isChecked = true
        }else if (updateProductViewModel.productList.product_condition == "1"){
            rbNew.isChecked = true
        }

        if (updateProductViewModel.productList.master_category_id == "6"){
            tvTitle.text = resources.getString(R.string.update_spare_parts_details)
        }else if (updateProductViewModel.productList.master_category_id == "7"){
            tvTitle.text = resources.getString(R.string.update_car_accessories_details)
        }


        setImages()

        val sellVehicle = SellVehicle()
        sellVehicle.category = updateProductViewModel.productList.vehicle_cat_id
        updateProductViewModel.apiGetBrandsTypesModels(sellVehicle)

        updateProductViewModel.brandTypeModelsLiveData.observe(this,
            androidx.lifecycle.Observer { brandsTypeModelsList ->
                val brandAdpter = BrandsAdapter(mContext,
                    brandsTypeModelsList)

                spBrand.adapter = brandAdpter

                for (i in brandsTypeModelsList.indices) {
                    if (brandsTypeModelsList[i].vehicle_brand_id ==
                        updateProductViewModel.productList.vehicle_company_id) {
                        spBrand.setSelection(i)
                    }
                }

                spBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long,
                    ) {

                        Log.e("positionSelected ", position.toString() + " jhuj")

                            updateProductViewModel.brandTypeModelsLiveData.value?.get(position)
                                ?.let {
                                    val typeAdapter =  TypesAdapter(mContext,
                                        it.type_list)

                                    spType.adapter = typeAdapter

                                    for (i in it.type_list.indices) {
                                        if (it.type_list[i].vehicle_type_id ==
                                            updateProductViewModel.productList.vehicle_type_id) {
                                           spType.setSelection(i)
                                        }
                                    }

                                }




                        spType.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    p1: View?,
                                    position: Int,
                                    p3: Long,
                                ) {

                                    Log.e("positionSelected ", position.toString() + " jhuj")


                                        updateProductViewModel.brandTypeModelsLiveData.value?.get(
                                            spBrand.selectedItemPosition)?.type_list?.get(position)
                                            ?.let {
                                                val modelAdapter =  ModelsAdapter(mContext,
                                                    it.model_list)
                                                spModel.adapter = modelAdapter

                                                for (i in it.model_list.indices) {
                                                    if (it.model_list[i].vehicle_model_id ==
                                                        updateProductViewModel.productList.vehicle_model_id) {
                                                        spModel.setSelection(i)
                                                    }
                                                }

                                            }


                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {

                                }

                            }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            })

        updateProductViewModel.successUpdateImageLiveData.observe(this,
            androidx.lifecycle.Observer { isScuccess ->
                progressBar.visibility = View.GONE
                Toast.makeText(mContext, "Image updated successfully", Toast.LENGTH_SHORT).show()
            })

        updateProductViewModel.successMessageLiveData.observe(this,
            androidx.lifecycle.Observer { isScuccess ->
                progressBar.visibility = View.GONE
                if (isScuccess) {
                    val intent = Intent(mContext, ThankYouForAddActivity::class.java)
                    intent.putExtra(IntentKeyEnum.IS_PRODUCT_UPDATED.name,true)
                    startActivity(intent)
                    finish()

                }
            })

        updateProductViewModel.errorMessage.observe(this, androidx.lifecycle.Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


        // tvSearch = findViewById(R.id.tvSearch);

        /*  tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, SearchCarListActivity.class));
            }
        });*/

        /*   spBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String imc_met = spBrand.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });*/
    }



    /**
     * Set images as per url
     */
    private fun setImages() {

        if (updateProductViewModel.productList.image1!!.isNotEmpty()){
            Picasso.get().load(updateProductViewModel.productList.image1)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(ivImage1);
        }
        if (updateProductViewModel.productList.image2!!.isNotEmpty()){
            Picasso.get().load(updateProductViewModel.productList.image2)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(ivImage2);

        }

        if (updateProductViewModel.productList.image3!!.isNotEmpty()){
            Picasso.get().load(updateProductViewModel.productList.image3)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(ivImage3);
        }

        if (updateProductViewModel.productList.image4!!.isNotEmpty()){
            Picasso.get().load(updateProductViewModel.productList.image4)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(ivImage4);

        }

        if (updateProductViewModel.productList.image5!!.isNotEmpty()){
            Picasso.get().load(updateProductViewModel.productList.image5)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(ivImage5);

        }

        if (updateProductViewModel.productList.image6!!.isNotEmpty()){
            Picasso.get().load(updateProductViewModel.productList.image6)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(ivImage6);
        }



    }

    private fun setImagesFromLocalDirectory(path: String) {
        if (updateProductViewModel.selectImageNumber == 1){
            Picasso.get().load(File(path))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivImage1);

            progressBar.visibility = View.VISIBLE
            val file = File(path)
            updateProductViewModel.apiUpdateImage(updateProductViewModel.productList.id!!, "1",file)
        }

        if (updateProductViewModel.selectImageNumber == 2){
            Picasso.get().load(File(path))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivImage2);

            progressBar.visibility = View.VISIBLE
            val file = File(path)
            updateProductViewModel.apiUpdateImage(updateProductViewModel.productList.id!!, "2",file)
        }

        if (updateProductViewModel.selectImageNumber == 3){
            Picasso.get().load(File(path))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivImage3);

            progressBar.visibility = View.VISIBLE
            val file = File(path)
            updateProductViewModel.apiUpdateImage(updateProductViewModel.productList.id!!, "3",file)
        }

        if (updateProductViewModel.selectImageNumber == 4){
            Picasso.get().load(File(path))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivImage4);

            progressBar.visibility = View.VISIBLE
            val file = File(path)
            updateProductViewModel.apiUpdateImage(updateProductViewModel.productList.id!!, "4",file)
        }

        if (updateProductViewModel.selectImageNumber == 5){
            Picasso.get().load(File(path))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivImage5);

            progressBar.visibility = View.VISIBLE
            val file = File(path)
            updateProductViewModel.apiUpdateImage(updateProductViewModel.productList.id!!, "5",file)
        }

        if (updateProductViewModel.selectImageNumber == 6){
            Picasso.get().load(File(path))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivImage6);

            progressBar.visibility = View.VISIBLE
            val file = File(path)
            updateProductViewModel.apiUpdateImage(updateProductViewModel.productList.id!!, "6",file)
        }
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
}