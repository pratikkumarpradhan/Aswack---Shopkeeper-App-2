package com.avdhootsolutions.aswack_shopkeeper.activities

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import android.content.Intent
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.adapters.*
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.UserTypeEnum
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.AddVehicleForSellViewModel
import kotlinx.android.synthetic.main.activity_sell_vehical.*
import kotlinx.android.synthetic.main.header_title.*


class AddVehicleForSellActivityCopy : AppCompatActivity(), ImagesAdapter.ICustomListListener {
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
        mContext = this@AddVehicleForSellActivityCopy
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

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                mContext!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (mContext as Activity?)!!,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                111
            )
        } else {
            showMenuDialogToSetProfilePic()
        }
    }

    private fun showMenuDialogToSetProfilePic() {
        if (ActivityCompat.checkSelfPermission(
                mContext!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startChoosing()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                112
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            112 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startChoosing()
                } else {
                    Toast.makeText(mContext, R.string.permission_denied, Toast.LENGTH_LONG).show()
                }
            }
            111 -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    showMenuDialogToSetProfilePic()
                } else {
                    Toast.makeText(mContext, R.string.permission_denied, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun startChoosing() {
        if (Build.VERSION.SDK_INT < 19) {
            var intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE
            )
        } else {
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK
            && null != data
        ) {
            if (data.clipData != null) {

                Log.e("clipdata ", " get")

                var count = data.clipData?.itemCount
                if (count != null) {
                    for (i in 0 until count) {
                        var imageUri: Uri? = data.clipData?.getItemAt(i)?.uri
                        if (imageUri != null) {
                            getPathFromURI(imageUri)
                        } else {
                            Log.e("imageUri is ", "null")
                        }
                    }
                } else {
                    Log.e("image count is ", "0")
                }
            } else if (data.data != null) {
                Log.e("data.data ", " get")
                var uri : Uri? = data.data

                if (data.data != null){
                    getPathFromURI(data.data!!)
                }


            }

        }
    }

    private fun getPathFromURI(uri: Uri) {

        var pathList = ArrayList<String>()

        if (uri.path != null) {
            var path: String = uri.path!!

            val databaseUri: Uri
            val selection: String?
            val selectionArgs: Array<String>?
            if (path.contains("/document/image:")) { // files selected from "Documents"
                databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                selection = "_id=?"
                selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
            } else { // files selected from all other sources, especially on Samsung devices
                databaseUri = uri
                selection = null
                selectionArgs = null
            }
            try {
                val projection = arrayOf(
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.ORIENTATION,
                    MediaStore.Images.Media.DATE_TAKEN
                ) // some example data you can query
                val cursor = contentResolver.query(
                    databaseUri,
                    projection, selection, selectionArgs, null
                )
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndex(projection[0])
                        var imagePath = cursor.getString(columnIndex)
                        // Log.e("path", imagePath);
                        pathList.add(imagePath)

                        Log.e("image path for multiple " , imagePath)
                    }

                    for (i in pathList.indices) {
                        pathList[i]?.let {

                            var removablePath = ArrayList<Int>()

                            for (j in addVehicleForSellViewModel.imageList.indices) {
                                if (it == addVehicleForSellViewModel.imageList[j]) {
                                    removablePath.add(j)
                                }
                            }

                            for (z in removablePath.indices) {

                                addVehicleForSellViewModel.imageList.removeAt(removablePath[z])
                            }

                            addVehicleForSellViewModel.imageList.add(it)
                        }

                    }

                    imagesAdapter = ImagesAdapter(mContext, this)
                    imagesAdapter.setList(addVehicleForSellViewModel.imageList)
                    rvPhotos.adapter = imagesAdapter

                    cursor.close()
                }

            } catch (e: Exception) {
                Log.e("TAG", e.message, e)
            }
        }


    }


    companion object {
        private const val REQUEST_CODE_CHOOSE = 23
    }

    override fun onTypesSelected() {

    }
}