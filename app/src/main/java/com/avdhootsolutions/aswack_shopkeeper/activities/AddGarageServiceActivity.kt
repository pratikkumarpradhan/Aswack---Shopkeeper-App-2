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
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
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
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropHandler
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropHelper
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropParams
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.avdhootsolutions.aswack_shopkeeper.models.SpareParts
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Prefrences
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.AddGarageViewModel
import kotlinx.android.synthetic.main.activity_add_garage.*
import kotlinx.android.synthetic.main.activity_add_garage.etDescription
import kotlinx.android.synthetic.main.activity_add_garage.etPrice
import kotlinx.android.synthetic.main.activity_add_garage.etProductName
import kotlinx.android.synthetic.main.activity_add_garage.progressBar
import kotlinx.android.synthetic.main.activity_add_garage.rlPhoto
import kotlinx.android.synthetic.main.activity_add_garage.rlReview
import kotlinx.android.synthetic.main.activity_add_garage.rvPhotos
import kotlinx.android.synthetic.main.activity_add_garage.spBrand
import kotlinx.android.synthetic.main.activity_tyre_service.*
import kotlinx.android.synthetic.main.header_title.*
import kotlinx.android.synthetic.main.header_title.tvTitle
import java.io.*


class AddGarageServiceActivity : AppCompatActivity(), ImagesAdapter.ICustomListListener,

    CropHandler {

    lateinit var mCropParams: CropParams
    private var imagePath: String = ""
    private var currentPhotoPathName: String = ""
    lateinit var dialogProfilePicture: Dialog
    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var addGarageViewModel: AddGarageViewModel

    /**
     * Adapter for images
     */
    lateinit var imagesAdapter: ImagesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_garage)
        mContext = this@AddGarageServiceActivity
        initView()

        onClickListener()
    }

    /**
     * All click events
     */
    private fun onClickListener() {

        rlPhoto.setOnClickListener { checkPermission() }
        iv_back!!.setOnClickListener { finish() }

        rlReview.setOnClickListener {

            if (etSpecializeIn.text.toString().isNullOrEmpty() ||
                etDescription.text.toString().isNullOrEmpty() ||
                etPrice.text.toString().isNullOrEmpty()
            ) {
                Toast.makeText(
                    mContext,
                    resources.getString(R.string.fill_all_details),
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                progressBar.visibility = View.VISIBLE

                val spareParts = SpareParts()
                spareParts.seller_id = Helper().getLoginData(mContext).id
                spareParts.seller_company_id = addGarageViewModel.companyId
                spareParts.package_purchased_id = addGarageViewModel.packageId
                spareParts.master_category_id = addGarageViewModel.mainCatId
                spareParts.vehicle_category = addGarageViewModel.vehicleCategoryId
                spareParts.product_name = etProductName.text.toString()
                spareParts.specialize_in = etSpecializeIn.text.toString()
                spareParts.description = etDescription.text.toString()
                spareParts.price = etPrice.text.toString()

                spareParts.vehicle_company =
                    addGarageViewModel.brandTypeModelsLiveData.value?.get(spBrand.selectedItemPosition)?.vehicle_brand_id

                addGarageViewModel.apiAddGarageWithFile(spareParts, mContext)
            }


        }
    }

    private fun initView() {

        tvTitle.text = resources.getString(R.string.add_garage_detail)

        rvPhotos.layoutManager = GridLayoutManager(mContext, 3)
        rvPhotos.setHasFixedSize(true)

        addGarageViewModel = ViewModelProvider(this).get(AddGarageViewModel::class.java)

        addGarageViewModel.companyId =
            intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()
        addGarageViewModel.vehicleCategoryId =
            intent.getStringExtra(IntentKeyEnum.VEHICLE_CATEGORY_ID.name).toString()
        addGarageViewModel.mainCatId =
            intent.getStringExtra(IntentKeyEnum.MAIN_CAT_ID.name).toString()
        addGarageViewModel.packageId =
            intent.getStringExtra(IntentKeyEnum.PACKAGE_ID.name).toString()

        val sellVehicle = SellVehicle()
        sellVehicle.category = addGarageViewModel.vehicleCategoryId
        addGarageViewModel.apiGetBrandsTypesModels(sellVehicle)

        addGarageViewModel.brandTypeModelsLiveData.observe(this,
            androidx.lifecycle.Observer { brandsTypeModelsList ->
                val brandAdpter = BrandsAdapter(
                    mContext,
                    brandsTypeModelsList
                )

                spBrand.adapter = brandAdpter
            })



        addGarageViewModel.successMessageLiveData.observe(this,
            androidx.lifecycle.Observer { isScuccess ->
                progressBar.visibility = View.GONE
                if (isScuccess) {
                    val intent = Intent(mContext, ThankYouForAddActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            })

        addGarageViewModel.errorMessage.observe(this, androidx.lifecycle.Observer { error ->
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

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            showProfilePicDialog()
        } else {
            if (ContextCompat.checkSelfPermission(
                    mContext!!,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    mContext!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    mContext!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showProfilePicDialog()
            } else {
                ActivityCompat.requestPermissions(
                    (mContext as Activity?)!!,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
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
                    mContext!!
                )
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

                setImagesIntoList(it)
            }
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
                ExifInterface.TAG_ORIENTATION, 0
            )
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
            selectedImage = Bitmap.createBitmap(
                selectedImage, 0, 0,
                selectedImage.width, selectedImage.height, matrix,
                true
            )
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
                        + "/MyFolder"
            )
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

    private fun setImagesIntoList(imagePath: String) {


        var removablePath = ArrayList<Int>()

        for (j in addGarageViewModel.imageList.indices) {
            if (imagePath == addGarageViewModel.imageList[j]) {
                removablePath.add(j)
            }
        }

        for (z in removablePath.indices) {

            addGarageViewModel.imageList.removeAt(removablePath[z])
        }

        addGarageViewModel.imageList.add(imagePath)


        imagesAdapter = ImagesAdapter(mContext, this)
        imagesAdapter.setList(addGarageViewModel.imageList)
        rvPhotos.adapter = imagesAdapter
    }

    companion object {
        private const val REQUEST_CODE_CHOOSE = 23
    }

    override fun onTypesSelected() {

    }
}