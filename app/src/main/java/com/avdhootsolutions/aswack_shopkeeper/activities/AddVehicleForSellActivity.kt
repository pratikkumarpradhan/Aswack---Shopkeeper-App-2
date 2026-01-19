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
import com.avdhootsolutions.aswack_shopkeeper.enums.UserTypeEnum
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropHandler
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropHelper
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropParams
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Prefrences
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.AddVehicleForSellViewModel
import kotlinx.android.synthetic.main.activity_sell_vehical.*
import kotlinx.android.synthetic.main.header_title.*
import java.io.*


class AddVehicleForSellActivity : AppCompatActivity(), ImagesAdapter.ICustomListListener,
    CropHandler {

    lateinit var mCropParams: CropParams
    private var imagePath: String = ""
    private var currentPhotoPathName: String = ""
    lateinit var dialogProfilePicture: Dialog



    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var addVehicleForSellViewModel: AddVehicleForSellViewModel

    /**
     * Adapter for our services
     */
    lateinit var imagesAdapter: ImagesAdapter


    /**
     * Adapter of the owners
     * Ex : First owner
     */
    lateinit var ownersAdapter: OwnersAdapter

    val PICK_IMAGE_MULTIPLE = 11;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_vehical)
        mContext = this@AddVehicleForSellActivity
        initView()

        onClickListener()
    }

    /**
     * All click events
     */
    private fun onClickListener() {

        rlLocation.setOnClickListener(View.OnClickListener {

            val intent = Intent(mContext, MapActivity::class.java)
            startActivity(intent)

        })
        rlPhoto.setOnClickListener(View.OnClickListener { checkPermission() })
        iv_back!!.setOnClickListener { finish() }

        rlReview.setOnClickListener {

            if (!rbAutomatic.isChecked && !rbManual.isChecked) {
                Toast.makeText(
                    mContext,
                    resources.getString(R.string.select_transmition_type),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (etKmDriven.text.toString().isEmpty() ||
                etAdTitle.text.toString().isEmpty() ||
                etDescribe.text.toString().isEmpty() ||
                etSetAPrice.text.toString().isEmpty() ||
                etMobileno.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    mContext,
                    resources.getString(R.string.fill_all_details),
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                progressBar.visibility = View.VISIBLE

                val sellVehicle = SellVehicle()
                sellVehicle.user_id = Helper().getLoginData(mContext).id
                sellVehicle.seller_company_id = addVehicleForSellViewModel.companyId
                sellVehicle.user_type = UserTypeEnum.SELLER.ordinal.toString()
                sellVehicle.contact_number = etMobileno.text.toString()
                sellVehicle.title = etAdTitle.text.toString()
                sellVehicle.description = etDescribe.text.toString()
                sellVehicle.price = etSetAPrice.text.toString()
                sellVehicle.driven_km = etKmDriven.text.toString()
                sellVehicle.vehicle_cat = addVehicleForSellViewModel.vehicleCategoryId
                sellVehicle.owners = spOwner.selectedItemPosition.toString()
                sellVehicle.package_purchased_id = addVehicleForSellViewModel.packageId

                sellVehicle.vehicle_brand =
                    addVehicleForSellViewModel.brandTypeModelsLiveData.value?.get(spBrand.selectedItemPosition)?.vehicle_brand_id

                sellVehicle.vehicle_type =
                    addVehicleForSellViewModel.brandTypeModelsLiveData.value?.get(spBrand.selectedItemPosition)?.type_list?.get(
                        spType.selectedItemPosition
                    )?.vehicle_type_id

                sellVehicle.vehicle_model =
                    addVehicleForSellViewModel.brandTypeModelsLiveData.value?.get(spBrand.selectedItemPosition)?.type_list?.get(
                        spType.selectedItemPosition
                    )?.model_list?.get(spModel.selectedItemPosition)?.vehicle_model_id

                sellVehicle.vehicle_year =
                    addVehicleForSellViewModel.yearListLiveData.value?.get(spYear.selectedItemPosition)?.id

                sellVehicle.vehicle_fuel =
                    addVehicleForSellViewModel.fuelListLiveData.value?.get(spFuel.selectedItemPosition)?.id

                if (rbAutomatic.isChecked) {
                    sellVehicle.transmission = "0"
                } else {
                    sellVehicle.transmission = "1"
                }

                addVehicleForSellViewModel.apiSellVehicleWithFile(sellVehicle, mContext)
            }


        }
    }

    private fun initView() {

        tvTitle.text = resources.getString(R.string.add_vehicle_details)
        rvPhotos.setLayoutManager(GridLayoutManager(mContext, 3))
        rvPhotos.setHasFixedSize(true)

        addVehicleForSellViewModel =
            ViewModelProvider(this).get(AddVehicleForSellViewModel::class.java)

        addVehicleForSellViewModel.companyId =
            intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()
        addVehicleForSellViewModel.packageId =
            intent.getStringExtra(IntentKeyEnum.PACKAGE_ID.name).toString()
        addVehicleForSellViewModel.getOwnersList()

        Log.e(
            "sellVehicleViewModel.ownersList ",
            addVehicleForSellViewModel.ownersList.size.toString() + " jkggjk"
        )

        ownersAdapter = OwnersAdapter(mContext, addVehicleForSellViewModel.ownersList)
        spOwner.adapter = ownersAdapter

        addVehicleForSellViewModel.vehicleCategoryId =
            intent.getStringExtra(IntentKeyEnum.VEHICLE_CATEGORY_ID.name).toString()

        val sellVehicle = SellVehicle()
        sellVehicle.category = addVehicleForSellViewModel.vehicleCategoryId
        addVehicleForSellViewModel.apiGetBrandsTypesModels(sellVehicle)

        addVehicleForSellViewModel.brandTypeModelsLiveData.observe(
            this,
            androidx.lifecycle.Observer { brandsTypeModelsList ->
                val brandAdpter = BrandsAdapter(
                    mContext,
                    brandsTypeModelsList
                )

                spBrand.adapter = brandAdpter

                spBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long,
                    ) {

                        Log.e("positionSelected ", position.toString() + " jhuj")

                        val typeAdapter =
                            addVehicleForSellViewModel.brandTypeModelsLiveData.value?.get(position)
                                ?.let {
                                    TypesAdapter(
                                        mContext,
                                        it.type_list
                                    )
                                }

                        spType.adapter = typeAdapter

                        spType.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    p1: View?,
                                    position: Int,
                                    p3: Long,
                                ) {

                                    Log.e("positionSelected ", position.toString() + " jhuj")

                                    val modelAdapter =
                                        addVehicleForSellViewModel.brandTypeModelsLiveData.value?.get(
                                            spBrand.selectedItemPosition
                                        )?.type_list?.get(position)
                                            ?.let {
                                                ModelsAdapter(
                                                    mContext,
                                                    it.model_list
                                                )
                                            }

                                    spModel.adapter = modelAdapter
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {

                                }

                            }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            })


        addVehicleForSellViewModel.fuelListLiveData.observe(
            this,
            androidx.lifecycle.Observer { fuelList ->
                val adapter = FuelsAdapter(
                    this,
                    fuelList
                )

                spFuel.adapter = adapter

            })

        addVehicleForSellViewModel.yearListLiveData.observe(
            this,
            androidx.lifecycle.Observer { yearList ->
                val adapter = YearsAdapter(
                    this,
                    yearList
                )

                spYear.adapter = adapter

            })

        addVehicleForSellViewModel.successMessageLiveData.observe(
            this,
            androidx.lifecycle.Observer { isScuccess ->
                progressBar.visibility = View.GONE
                if (isScuccess) {
                    val intent = Intent(mContext, ThankYouForAddActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            })

        addVehicleForSellViewModel.errorMessage.observe(this, androidx.lifecycle.Observer { error ->
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
    private fun setImagesIntoList(imagePath: String) {


                var removablePath = ArrayList<Int>()

                for (j in addVehicleForSellViewModel.imageList.indices) {
                    if (imagePath == addVehicleForSellViewModel.imageList[j]) {
                        removablePath.add(j)
                    }
                }

                for (z in removablePath.indices) {

                    addVehicleForSellViewModel.imageList.removeAt(removablePath[z])
                }

                addVehicleForSellViewModel.imageList.add(imagePath)


        imagesAdapter = ImagesAdapter(mContext, this)
        imagesAdapter.setList(addVehicleForSellViewModel.imageList)
        rvPhotos.adapter = imagesAdapter
    }


    companion object {
        private const val REQUEST_CODE_CHOOSE = 23
    }

    override fun onTypesSelected() {

    }
}