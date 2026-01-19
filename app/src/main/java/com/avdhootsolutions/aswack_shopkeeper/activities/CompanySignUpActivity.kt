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
import kotlinx.android.synthetic.main.activity_company_sign_up.*

import java.io.*
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.adapters.*
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.TimingEnum
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.CompanyRegistrationViewModel
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
import com.avdhootsolutions.aswack_shopkeeper.enums.CourierOwnerTypeEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.FileTypeEnum
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper


class CompanySignUpActivity : AppCompatActivity(), CropHandler,
    CompanyTypeAdapter.ICustomListListener, SubCategoryAdapter.ICustomListListener {
    lateinit var mContext: Context
    lateinit var mCropParams: CropParams
    private var imagePath: String = ""
    private var currentPhotoPathName: String = ""
    lateinit var dialogProfilePicture: Dialog

    /**
     * View model
     */
    lateinit var companyRegistrationViewModel: CompanyRegistrationViewModel

    /**
     * Adapter for our services
     */
    lateinit var companyTypeAdapter: CompanyTypeAdapter

    /**
     * Adapter for gara services
     */
    lateinit var garageSubCategoryAdapter: SubCategoryAdapter


    /**
     * Adapter for Insurance services
     */
    lateinit var insuranceSubCategoryAdapter: SubCategoryAdapter

    /**
     * Adapter for our services
     */
    lateinit var emeregancySubCategoryAdapter: SubCategoryAdapter

    /**
     * Adapter for our services
     */
    lateinit var breakdownSubCategoryAdapter: SubCategoryAdapter

    lateinit var timingAdapter: TimmingAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_sign_up)
        mContext = this
        initView()

        clickListener()
    }

    private fun initView() {

        rvOurServices.layoutManager = GridLayoutManager(mContext, 2)
        rvOurServices.setHasFixedSize(true)

        rvGarageSubCategory.layoutManager = GridLayoutManager(mContext, 2)
        rvGarageSubCategory.setHasFixedSize(true)

        rvEmergancyubCategory.layoutManager = GridLayoutManager(mContext, 2)
        rvEmergancyubCategory.setHasFixedSize(true)

        rvBreakDownSubCategory.layoutManager = GridLayoutManager(mContext, 2)
        rvBreakDownSubCategory.setHasFixedSize(true)

        rvInsuranceSubCategory.layoutManager = GridLayoutManager(mContext, 2)
        rvInsuranceSubCategory.setHasFixedSize(true)


        companyRegistrationViewModel =
            ViewModelProvider(this).get(CompanyRegistrationViewModel::class.java)

        timingAdapter = TimmingAdapter(mContext, companyRegistrationViewModel.timesList)
        spOpenningMonday.adapter = timingAdapter
        spClosingMonday.adapter = timingAdapter

        spOpenningTuesday.adapter = timingAdapter
        spClosingTuesday.adapter = timingAdapter

        spOpenningWednesday.adapter = timingAdapter
        spClosingWednesday.adapter = timingAdapter

        spOpenningThursday.adapter = timingAdapter
        spClosingThursday.adapter = timingAdapter

        spOpenningFriday.adapter = timingAdapter
        spClosingFriday.adapter = timingAdapter

        spOpenningSaturday.adapter = timingAdapter
        spClosingSaturday.adapter = timingAdapter

        spOpenningSunday.adapter = timingAdapter
        spClosingSunday.adapter = timingAdapter


        companyRegistrationViewModel.selectedCategoryFromHome =
            intent.getStringExtra(IntentKeyEnum.CAT_ID.name).toString()

        companyRegistrationViewModel.apiGetCountries()

        companyRegistrationViewModel.categoriesLiveData.observe(
            this,
            androidx.lifecycle.Observer { categories ->

                companyTypeAdapter = CompanyTypeAdapter(
                    mContext,
                    this,
                    companyRegistrationViewModel.selectedCategoryFromHome
                )
                companyTypeAdapter.setList(companyRegistrationViewModel.categoryList)
                rvOurServices.adapter = companyTypeAdapter
            })

        companyRegistrationViewModel.subCategoryListLiveData.observe(
            this,
            androidx.lifecycle.Observer { subCategories ->

                if (companyRegistrationViewModel.selectedCategoryFromHome == "3") {
                    tvGarageSubCategory.visibility = View.VISIBLE
                    rvGarageSubCategory.visibility = View.VISIBLE

                    tvSpecializeIn.visibility = View.VISIBLE
                    etSpecializeIn.visibility = View.VISIBLE
                }


                if (companyRegistrationViewModel.selectedCategoryFromHome == "4") {
                    tvInsuranceSubCategory.visibility = View.VISIBLE
                    rvInsuranceSubCategory.visibility = View.VISIBLE
                }

                if (companyRegistrationViewModel.selectedCategoryFromHome == "5") {
                    tvEmergancySubCategory.visibility = View.VISIBLE
                    rvEmergancyubCategory.visibility = View.VISIBLE

                    tvSpecializeIn.visibility = View.VISIBLE
                    etSpecializeIn.visibility = View.VISIBLE
                }

                if (companyRegistrationViewModel.selectedCategoryFromHome == "10") {
                    tvBreakDownSubCategory.visibility = View.VISIBLE
                    rvBreakDownSubCategory.visibility = View.VISIBLE
                }

                if (companyRegistrationViewModel.selectedCategoryFromHome == "12") {
                    tvCourierOwnerType.visibility = View.VISIBLE
                    rgCourierOwnerType.visibility = View.VISIBLE
                }

                if (subCategories[0].cat_id == "3") {
                    garageSubCategoryAdapter = SubCategoryAdapter(mContext, this)
                    garageSubCategoryAdapter.setList(companyRegistrationViewModel.garageSubCategoryList)
                    rvGarageSubCategory.adapter = garageSubCategoryAdapter
                }


                if (subCategories[0].cat_id == "4") {
                    insuranceSubCategoryAdapter = SubCategoryAdapter(mContext, this)
                    insuranceSubCategoryAdapter.setList(companyRegistrationViewModel.insuranceSubCategoryList)
                    rvInsuranceSubCategory.adapter = insuranceSubCategoryAdapter
                }

                if (subCategories[0].cat_id == "5") {
                    emeregancySubCategoryAdapter = SubCategoryAdapter(mContext, this)
                    emeregancySubCategoryAdapter.setList(companyRegistrationViewModel.emeragncySubCategoryList)
                    rvEmergancyubCategory.adapter = emeregancySubCategoryAdapter
                }

                if (subCategories[0].cat_id == "10") {
                    breakdownSubCategoryAdapter = SubCategoryAdapter(mContext, this)
                    breakdownSubCategoryAdapter.setList(companyRegistrationViewModel.breakdownSubCategoryList)
                    rvBreakDownSubCategory.adapter = breakdownSubCategoryAdapter
                }


            })


        rbFreelencer.setOnClickListener {  lblcownername.visibility = View.GONE
            etcownername.visibility = View.GONE
            lblcmobileno.text = resources.getString(R.string.mobile_no)
            lblcemail.text = resources.getString(R.string.email)
            lblcaltmobileno.text = resources.getString(R.string.alternate_mobile_number)
            lblcname.text = resources.getString(R.string.name)
            lblcwebsite.visibility = View.GONE
            etcwebsite.visibility = View.GONE
            lblcdetails.visibility = View.GONE
            etcdetails.visibility = View.GONE
            lblclogo.visibility = View.GONE
            lblcupload.visibility = View.GONE }


        rbCompany.setOnClickListener {
            lblcownername.visibility = View.VISIBLE
            etcownername.visibility = View.VISIBLE
            lblcmobileno.text = resources.getString(R.string.company_mobile_number)
            lblcemail.text = resources.getString(R.string.company_email_id)
            lblcaltmobileno.text = resources.getString(R.string.company_alternate_mobile_number)
            lblcname.text = resources.getString(R.string.company_name)
            lblcwebsite.visibility = View.VISIBLE
            etcwebsite.visibility = View.VISIBLE
            lblcdetails.visibility = View.VISIBLE
            etcdetails.visibility = View.VISIBLE
            lblclogo.visibility = View.VISIBLE
            lblcupload.visibility = View.VISIBLE
        }


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



        companyRegistrationViewModel.errorMessage.observe(
            this,
            androidx.lifecycle.Observer { error ->

                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
            })


        companyRegistrationViewModel.checkCategoryLiveData.observe(
            this,
            androidx.lifecycle.Observer { isChecked ->

                progressBar.visibility = View.GONE

                if (etcname.text.toString().isNullOrEmpty() ||
                    etcmobileno.text.toString().isNullOrEmpty() ||
                    etcemail.text.toString().isNullOrEmpty() ||
                    etcname.text.toString().isNullOrEmpty()
                ) {
                    Toast.makeText(
                        mContext,
                        resources.getString(R.string.fill_all_details),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (rbCompany.isChecked && etcownername.text.toString().isNullOrEmpty()) {
                    Toast.makeText(
                        mContext,
                        resources.getString(R.string.select_owner_name),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!radio_regular.isChecked && !radio_emergancy.isChecked) {
                    Toast.makeText(
                        mContext,
                        resources.getString(R.string.select_timing),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    val register = CompanyRegister()
                    register.seller_id = Helper().getLoginData(mContext).id
                    register.name = etcname.text.toString()
                    register.ownername = etcownername.text.toString()
                    register.email = etcemail.text.toString()
                    register.phone = etclandline.text.toString()
                    register.mobile = etcmobileno.text.toString()
                    register.amobile = etcaltmobileno.text.toString()
                    register.pincode = etcpincode.text.toString()
                    register.houseno = ethouseno.text.toString()
                    register.streetno = etcstreetnoname.text.toString()
                    register.landmark = etcsnoname.text.toString()
                    register.pincode = etcpincode.text.toString()
                    register.website = etcwebsite.text.toString()
                    register.detail = etcdetails.text.toString()
                    register.lat = companyRegistrationViewModel.latitude
                    register.long = companyRegistrationViewModel.longitude
                    register.specialize_in = etSpecializeIn.text.toString()

                    if (rbCompany.isChecked) {
                        register.company_type = CourierOwnerTypeEnum.COMPANY.ordinal.toString()

                    } else if (rbFreelencer.isChecked) {
                        register.company_type = CourierOwnerTypeEnum.FREELENCER.ordinal.toString()

                    }

                    var isGarageSelected = false
                    var isEmergancySelected = false
                    var isBreakdownSelected = false
                    var isInsuranceSelected = false
                    var isCourierSelected = false

                    for (i in companyRegistrationViewModel.categoryList.indices) {
                        if (companyRegistrationViewModel.categoryList[i].isChecked) {
                            register.type.add(companyRegistrationViewModel.categoryList[i].id!!)

                            if (companyRegistrationViewModel.categoryList[i].id == "3") {
                                isGarageSelected = true
                            }

                            if (companyRegistrationViewModel.categoryList[i].id == "4") {
                                isInsuranceSelected = true
                            }

                            if (companyRegistrationViewModel.categoryList[i].id == "5") {
                                isEmergancySelected = true
                            }

                            if (companyRegistrationViewModel.categoryList[i].id == "10") {
                                isBreakdownSelected = true
                            }

                            if (companyRegistrationViewModel.categoryList[i].id == "12") {
                                isCourierSelected = true
                            }


                        }
                    }

                    var isGarageSubCategrorySelected = false
                    var isEmergancySubCategrorySelected = false
                    var isBreakdownSubCategrorySelected = false
                    var isInsuranceSubCategrorySelected = false


                    if (isGarageSelected) {
                        for (i in companyRegistrationViewModel.garageSubCategoryList.indices) {
                            if (companyRegistrationViewModel.garageSubCategoryList[i].isChecked) {
                                isGarageSubCategrorySelected = true
                                register.garage_subcategory.add(companyRegistrationViewModel.garageSubCategoryList[i].id!!)
                            }
                        }
                    }


                    if (isEmergancySelected) {
                        for (i in companyRegistrationViewModel.emeragncySubCategoryList.indices) {
                            if (companyRegistrationViewModel.emeragncySubCategoryList[i].isChecked) {
                                isEmergancySubCategrorySelected = true
                                register.emergency_subcategory.add(companyRegistrationViewModel.emeragncySubCategoryList[i].id!!)
                            }
                        }
                    }


                    if (isBreakdownSelected) {
                        for (i in companyRegistrationViewModel.breakdownSubCategoryList.indices) {
                            if (companyRegistrationViewModel.breakdownSubCategoryList[i].isChecked) {
                                isBreakdownSubCategrorySelected = true
                                register.breakdown_subcategory.add(companyRegistrationViewModel.breakdownSubCategoryList[i].id!!)
                            }
                        }
                    }

                    if (isInsuranceSelected) {
                        for (i in companyRegistrationViewModel.insuranceSubCategoryList.indices) {
                            if (companyRegistrationViewModel.insuranceSubCategoryList[i].isChecked) {
                                isInsuranceSubCategrorySelected = true
                                register.insurance_subcategory.add(companyRegistrationViewModel.insuranceSubCategoryList[i].id!!)
                            }
                        }
                    }


                    if (radio_regular.isChecked) {
                        register.officetime = "regular"

                        tlTimmings.visibility = View.VISIBLE
                        register.times = ArrayList()
                        if (cbMonday.isChecked) {
                            val officeTimings = OfficeTimings()
                            officeTimings.day = cbMonday.text.toString()
                            officeTimings.open =
                                companyRegistrationViewModel.timesList[spOpenningMonday.selectedItemPosition]
                            officeTimings.close =
                                companyRegistrationViewModel.timesList[spClosingMonday.selectedItemPosition]

                            register.times.add(officeTimings)
                        }

                        if (cbTuesday.isChecked) {
                            val officeTimings = OfficeTimings()
                            officeTimings.day = cbTuesday.text.toString()
                            officeTimings.open =
                                companyRegistrationViewModel.timesList[spOpenningTuesday.selectedItemPosition]
                            officeTimings.close =
                                companyRegistrationViewModel.timesList[spClosingTuesday.selectedItemPosition]
                            register.times.add(officeTimings)
                        }


                        if (cbWednsday.isChecked) {
                            val officeTimings = OfficeTimings()
                            officeTimings.day = cbWednsday.text.toString()
                            officeTimings.open =
                                companyRegistrationViewModel.timesList[spOpenningWednesday.selectedItemPosition]
                            officeTimings.close =
                                companyRegistrationViewModel.timesList[spClosingWednesday.selectedItemPosition]
                            register.times.add(officeTimings)
                        }

                        if (cbThursday.isChecked) {
                            val officeTimings = OfficeTimings()
                            officeTimings.day = cbThursday.text.toString()
                            officeTimings.open =
                                companyRegistrationViewModel.timesList[spOpenningThursday.selectedItemPosition]
                            officeTimings.close =
                                companyRegistrationViewModel.timesList[spClosingThursday.selectedItemPosition]
                            register.times.add(officeTimings)
                        }

                        if (cbFriday.isChecked) {
                            val officeTimings = OfficeTimings()
                            officeTimings.day = cbFriday.text.toString()
                            officeTimings.open =
                                companyRegistrationViewModel.timesList[spOpenningFriday.selectedItemPosition]
                            officeTimings.close =
                                companyRegistrationViewModel.timesList[spClosingFriday.selectedItemPosition]
                            register.times.add(officeTimings)
                        }

                        if (cbSaturday.isChecked) {
                            val officeTimings = OfficeTimings()
                            officeTimings.day = cbSaturday.text.toString()
                            officeTimings.open =
                                companyRegistrationViewModel.timesList[spOpenningSaturday.selectedItemPosition]
                            officeTimings.close =
                                companyRegistrationViewModel.timesList[spClosingSaturday.selectedItemPosition]
                            register.times.add(officeTimings)
                        }

                        if (cbSunday.isChecked) {
                            val officeTimings = OfficeTimings()
                            officeTimings.day = cbSunday.text.toString()
                            officeTimings.open =
                                companyRegistrationViewModel.timesList[spOpenningSunday.selectedItemPosition]
                            officeTimings.close =
                                companyRegistrationViewModel.timesList[spClosingSunday.selectedItemPosition]
                            register.times.add(officeTimings)
                        }


                    } else if (radio_emergancy.isChecked) {

                        tlTimmings.visibility = View.GONE
                        register.times = ArrayList()
                        register.officetime = TimingEnum.EMERGENCY.ordinal.toString()
                    }

                    register.country =
                        companyRegistrationViewModel.countriesLiveData.value?.get(spCountry.selectedItemPosition)?.id
                    register.state =
                        companyRegistrationViewModel.statesLiveData.value?.get(spState.selectedItemPosition)?.id
                    register.city =
                        companyRegistrationViewModel.citiesLiveData.value?.get(spCity.selectedItemPosition)?.id

                    if (isGarageSelected && !isGarageSubCategrorySelected) {
                        Toast.makeText(
                            mContext,
                            resources.getString(R.string.select_subcategory_of_garage),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (isInsuranceSelected && !isInsuranceSubCategrorySelected) {
                        Toast.makeText(
                            mContext,
                            resources.getString(R.string.select_subcategory_of_insurance),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (isEmergancySelected && !isEmergancySubCategrorySelected) {
                        Toast.makeText(
                            mContext,
                            resources.getString(R.string.select_subcategory_of_emergancy),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (isEmergancySelected && !companyRegistrationViewModel.isLocationButtonClicked) {

                        Toast.makeText(
                            mContext,
                            resources.getString(R.string.please_select_company_location),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (isBreakdownSelected && !isBreakdownSubCategrorySelected) {
                        Toast.makeText(
                            mContext,
                            resources.getString(R.string.select_subcategory_of_breakdown),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (isCourierSelected && !rbCompany.isChecked && !rbFreelencer.isChecked) {
                        Toast.makeText(
                            mContext,
                            resources.getString(R.string.select_company_type_of_courier),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (register.type.size == 0) {
                        Toast.makeText(
                            mContext,
                            resources.getString(R.string.select_type),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (isGarageSelected && etSpecializeIn.text.toString().isEmpty()) {
                        Toast.makeText(
                            mContext,
                            resources.getString(R.string.enter_specialize_in),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (isEmergancySelected && etSpecializeIn.text.toString().isEmpty()) {
                        Toast.makeText(
                            mContext,
                            resources.getString(R.string.enter_specialize_in),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val intent = Intent(mContext, PackageListActivity::class.java)
                        intent.putExtra(
                            IntentKeyEnum.COMPANY_REGISTRATION_DATA.name,
                            Gson().toJson(register)
                        )
                        intent.putExtra(
                            IntentKeyEnum.FILE_PATH.name,
                            companyRegistrationViewModel.filePath
                        )
                        startActivity(intent)
                    }

                }
            })


        companyRegistrationViewModel.countriesLiveData.observe(
            this,
            androidx.lifecycle.Observer { countryList ->
                val adapter = Country1Adapter(
                    this,
                    countryList
                )

                spCountry.adapter = adapter

                spCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long,
                    ) {

                        val stateData = Register()
                        stateData.country =
                            companyRegistrationViewModel.countriesLiveData.value?.get(position)?.id
                        companyRegistrationViewModel.apiGetStateList(stateData)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            })

        companyRegistrationViewModel.statesLiveData.observe(
            this,
            androidx.lifecycle.Observer { stateList ->
                val adapter = StatesAdapter(
                    this,
                    stateList
                )

                spState.adapter = adapter


                spState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long,
                    ) {

                        val cityData = Register()
                        cityData.state =
                            companyRegistrationViewModel.statesLiveData.value?.get(position)?.id
                        companyRegistrationViewModel.apiGetCityList(cityData)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            })

        companyRegistrationViewModel.citiesLiveData.observe(
            this,
            androidx.lifecycle.Observer { cityList ->
                val adapter = CitiesAdapter(
                    this,
                    cityList
                )

                spCity.adapter = adapter
            })

        companyRegistrationViewModel.errorMessage.observe(
            this,
            androidx.lifecycle.Observer { error ->
                progressBar.visibility = View.GONE
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
            })

        lblregistration.text = getString(R.string.company_profile_page)
        mCropParams = CropParams(mContext)
        val adapter: ArrayAdapter<*> =
            ArrayAdapter.createFromResource(mContext!!, R.array.company_type, R.layout.spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spCompanyName.adapter = adapter


        lblcupload.setOnClickListener(View.OnClickListener { checkPermission() })
    }

    private fun clickListener() {
        tvSubmit.setOnClickListener(View.OnClickListener {

            val login = Login()
            login.seller_id = Helper().getLoginData(mContext).id

            for (i in companyRegistrationViewModel.categoryList.indices) {
                if (companyRegistrationViewModel.categoryList[i].isChecked) {
                    login.categoryList.add(companyRegistrationViewModel.categoryList[i].id!!)
                }
            }

            if (login.categoryList.size > 0) {
                progressBar.visibility = View.VISIBLE
                companyRegistrationViewModel.checkRegisteredCategories(login)
            } else {
                Toast.makeText(mContext, "Please select one category", Toast.LENGTH_SHORT).show()
            }

        })

        rbFreelencer


        rlLocation.setOnClickListener {

            companyRegistrationViewModel.isLocationButtonClicked = true

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

                        companyRegistrationViewModel.latitude =
                            data?.getStringExtra(IntentKeyEnum.LATITUDE.name).toString()

                        companyRegistrationViewModel.longitude =
                            data?.getStringExtra(IntentKeyEnum.LONGITUDE.name).toString()

                        if (companyRegistrationViewModel.latitude.isNotEmpty()) {
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

            Picasso.get().load(File(compressImg))
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(ivLogo);

            ivLogo.visibility = View.VISIBLE
            lblcupload.text = "Uploaded"

            compressImg?.let {
                companyRegistrationViewModel.filePath = it
                Log.e("filePath ", companyRegistrationViewModel.filePath)
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

    override fun onCatgeorySelected(categories: Categories, position: Int) {

        var isGarageSelected = false
        var isEmergancySelected = false
        var isBreakDownSelected = false
        var isInsuranceSelected = false
        var isCourierSelected = false

        for (i in companyRegistrationViewModel.categoryList.indices) {
            if (companyRegistrationViewModel.categoryList[i].id == "3" &&
                companyRegistrationViewModel.categoryList[i].isChecked
            ) {
                isGarageSelected = true
            }

            if (companyRegistrationViewModel.categoryList[i].id == "4" &&
                companyRegistrationViewModel.categoryList[i].isChecked
            ) {
                isInsuranceSelected = true
            }

            if (companyRegistrationViewModel.categoryList[i].id == "5" &&
                companyRegistrationViewModel.categoryList[i].isChecked
            ) {
                isEmergancySelected = true
            }

            if (companyRegistrationViewModel.categoryList[i].id == "10" &&
                companyRegistrationViewModel.categoryList[i].isChecked
            ) {
                isBreakDownSelected = true
            }

            if (companyRegistrationViewModel.categoryList[i].id == "12" &&
                companyRegistrationViewModel.categoryList[i].isChecked
            ) {
                isCourierSelected = true
            }
        }

        if (isGarageSelected) {
            tvGarageSubCategory.visibility = View.VISIBLE
            rvGarageSubCategory.visibility = View.VISIBLE

        } else if (!isGarageSelected) {
            tvGarageSubCategory.visibility = View.GONE
            rvGarageSubCategory.visibility = View.GONE

        }

        if (isInsuranceSelected) {
            tvInsuranceSubCategory.visibility = View.VISIBLE
            rvInsuranceSubCategory.visibility = View.VISIBLE
        } else if (!isGarageSelected) {
            tvInsuranceSubCategory.visibility = View.GONE
            rvInsuranceSubCategory.visibility = View.GONE
        }


        if (isEmergancySelected || isGarageSelected) {
            tvSpecializeIn.visibility = View.VISIBLE
            etSpecializeIn.visibility = View.VISIBLE
        } else {
            etSpecializeIn.setText("")
            tvSpecializeIn.visibility = View.GONE
            etSpecializeIn.visibility = View.GONE
        }

        if (isEmergancySelected) {
            tvEmergancySubCategory.visibility = View.VISIBLE
            rvEmergancyubCategory.visibility = View.VISIBLE
        } else if (!isEmergancySelected) {
            tvEmergancySubCategory.visibility = View.GONE
            rvEmergancyubCategory.visibility = View.GONE
        }

        if (isBreakDownSelected) {
            tvBreakDownSubCategory.visibility = View.VISIBLE
            rvBreakDownSubCategory.visibility = View.VISIBLE
        } else if (!isBreakDownSelected) {
            tvBreakDownSubCategory.visibility = View.GONE
            rvBreakDownSubCategory.visibility = View.GONE
        }


        if (isCourierSelected) {
            tvCourierOwnerType.visibility = View.VISIBLE
            rgCourierOwnerType.visibility = View.VISIBLE
        } else if (!isBreakDownSelected) {
            tvCourierOwnerType.visibility = View.GONE
            rgCourierOwnerType.visibility = View.GONE
        }


    }

    override fun onCatgeorySelected(subCategories: SubCategories, position: Int) {

    }


}