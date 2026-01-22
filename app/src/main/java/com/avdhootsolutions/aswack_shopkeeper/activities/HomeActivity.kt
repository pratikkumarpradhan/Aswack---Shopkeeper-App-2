package com.avdhootsolutions.aswack_shopkeeper.activities

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.OurServicesAdapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.VehicleCategoryAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.CompanyStatusEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.Categories
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.HomeViewModel
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.custom_menu.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.row_our_service_list.*

class HomeActivity : AppCompatActivity(),
    OurServicesAdapter.ICustomListListener {
    lateinit var mContext: Context
    lateinit var dialog: Dialog

    var sellingArray = ArrayList<String>()
    var sliderArray = ArrayList<Int>()
    var isAllFabsVisible: Boolean = false

    /**
     * View model
     */
    lateinit var homeViewModel: HomeViewModel


    /**
     * Adapter for our services
     */
    lateinit var ourServicesAdapter: OurServicesAdapter

    var NotificationFromUserChat = ""


    /**
     * Dialog helper class
     */
    lateinit var dialogHelper: DialogHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mContext = this@HomeActivity
        initDrawer()

        init()

        clickListener()
    }

    fun init() {

        progressBar.visibility = View.VISIBLE

        dialogHelper = DialogHelper(mContext)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        if (intent.extras != null) {

            if (intent.hasExtra("screen_type") != null) {
                NotificationFromUserChat = intent.getStringExtra("screen_type").toString()
                Log.e("NotificationFromUserChat ", NotificationFromUserChat)

                when (NotificationFromUserChat) {
                    "CHAT_LIST_1" -> {
                        categoryClickEvent("1")
                    }
                    "CHAT_LIST_3" -> {
                        categoryClickEvent("3")
                    }
                    "CHAT_LIST_4" -> {
                        categoryClickEvent("4")
                    }
                    "CHAT_LIST_5" -> {
                        categoryClickEvent("5")
                    }
                    "CHAT_LIST_6" -> {
                        categoryClickEvent("6")
                    }
                    "CHAT_LIST_7" -> {
                        categoryClickEvent("7")
                    }
                    "CHAT_LIST_8" -> {
                        categoryClickEvent("8")
                    }
                    "CHAT_LIST_9" -> {
                        categoryClickEvent("9")
                    }
                    "CHAT_LIST_10" -> {
                        categoryClickEvent("10")
                    }
                    "CHAT_LIST_11" -> {
                        categoryClickEvent("11")
                    }
                    "CHAT_LIST_12" -> {
                        categoryClickEvent("12")
                    }
                }


            }

        }


        homeViewModel.sliderLiveData.observe(
            this,
            androidx.lifecycle.Observer { sliderList ->
                progressBar.visibility = View.GONE
//                rvSlider.adapter = SliderAdapter(mContext, sliderList)
                imageSlider.setImageList(sliderList, ScaleTypes.FIT)
            })

        homeViewModel.mainPageImageLiveData.observe(
            this,
            androidx.lifecycle.Observer { mainPageImage ->
                Glide.with(this).load(mainPageImage).into(ivMainImage)
            })

        homeViewModel.countriesLiveData.observe(
            this,
            androidx.lifecycle.Observer { countryList ->
                progressBar.visibility = View.GONE
                dialogHelper.showCountryListDialog(countryList, false)
            })


        homeViewModel.categoriesLiveData.observe(this, Observer { categories ->
            progressBar.visibility = View.GONE
            ourServicesAdapter = OurServicesAdapter(mContext, this)
            ourServicesAdapter.setList(categories)
            rvOurServices.adapter = ourServicesAdapter

            setCategoryData(categories)

        })

        homeViewModel.vehicleListLiveData.observe(this, Observer { categories ->

        })


        homeViewModel.successLogoutMessageLiveData.observe(this, Observer { isLogout ->
            progressBar.visibility = View.GONE
            Helper().clearLoginData(mContext)
            val i = Intent(mContext, LoginActivity::class.java)
// set the new task and clear flags
// set the new task and clear flags
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        })



        homeViewModel.companyStatusLiveData.observe(this, Observer { companyStatus ->

            when (companyStatus.status?.toInt()) {
                CompanyStatusEnum.ACCEPTED.ordinal -> {

                    val login = Login()
                    login.seller_id = Helper().getLoginData(mContext).id
                    login.company_id = companyStatus.company_id

                    homeViewModel.getPackageStatusForCategory(
                        login,
                        companyStatus.category_id ?: ""
                    )

//                    val intent = Intent(mContext, DashBoardActivity::class.java)
//                    intent.putExtra(IntentKeyEnum.CAT_ID.name, companyStatus.category_id)
//                    intent.putExtra(IntentKeyEnum.COMPANY_ID.name, companyStatus.company_id)
//                    startActivity(intent)
                }

                CompanyStatusEnum.REJECT.ordinal -> {

                }
                CompanyStatusEnum.PENDING.ordinal -> {

                    progressBar.visibility = View.GONE
                    val intent = Intent(mContext, ThankYouForRegistrationActivity::class.java)
                    intent.putExtra(IntentKeyEnum.CAT_ID.name, companyStatus.category_id)
                    if (!companyStatus.custom_request_code.isNullOrEmpty()) {
                        intent.putExtra(
                            IntentKeyEnum.CUSTOM_REQUEST_CODE.name,
                            companyStatus.custom_request_code
                        )
                    }
                    startActivity(intent)
                }

                CompanyStatusEnum.NOT_SENT.ordinal -> {
                    progressBar.visibility = View.GONE
                    if (companyStatus.category_id == "1" || companyStatus.category_id == "6"
                        || companyStatus.category_id == "7" || companyStatus.category_id == "9"
                        || companyStatus.category_id == "3" || companyStatus.category_id == "5"
                        || companyStatus.category_id == "10" || companyStatus.category_id == "4"
                        || companyStatus.category_id == "8" || companyStatus.category_id == "11"
                        || companyStatus.category_id == "12"
                    ) {
                        val intent = Intent(mContext, CompanySignUpActivity::class.java)
                        intent.putExtra(IntentKeyEnum.CAT_ID.name, companyStatus.category_id)
                        startActivity(intent)
                    }


                }

            }
        })


        homeViewModel.packageStatusLiveData.observe(this, Observer { packageStatus ->
            progressBar.visibility = View.GONE
            when (packageStatus.status?.toInt()) {
                CompanyStatusEnum.ACCEPTED.ordinal -> {

                    if (NotificationFromUserChat.isNotEmpty()) {
                        NotificationFromUserChat = ""
                        val intent = Intent(mContext, ChatListActivity::class.java)
                        intent.putExtra(IntentKeyEnum.COMPANY_ID.name, packageStatus.company_id)
                        startActivity(intent)
                    } else {
                        val intent = Intent(mContext, DashBoardActivity::class.java)
                        intent.putExtra(IntentKeyEnum.CAT_ID.name, packageStatus.category_id)
                        intent.putExtra(IntentKeyEnum.COMPANY_ID.name, packageStatus.company_id)
                        intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, packageStatus.id)
                        if (NotificationFromUserChat.isNotEmpty()) {
                            intent.putExtra(IntentKeyEnum.NOTIFICATION_FROM_CHAT.name, true)
                        }
                        startActivity(intent)
                    }


                }


                CompanyStatusEnum.PENDING.ordinal -> {
                    val intent = Intent(mContext, PackageListActivity::class.java)
                    intent.putExtra(IntentKeyEnum.CAT_ID.name, packageStatus.category_id)
                    intent.putExtra(IntentKeyEnum.DIRECT_PACKAGE.name, "1")
                    intent.putExtra(IntentKeyEnum.COMPANY_ID.name, packageStatus.company_id)
                    startActivity(intent)
                }


            }

        })


        homeViewModel.errorMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })

    }

    /**
     * Click events
     */
    private fun clickListener() {


        clSellVehicle.setOnClickListener {
            categoryClickEvent("1")
        }

        clBuyVehicle.setOnClickListener {

            dialogHelper.openBuySellDialog(
                1,
                "What you want you buy?",
                homeViewModel.vehicleList,
                object : VehicleCategoryAdapter.ICustomListListener {
                    override fun onCatgeorySelected(categories: Categories, position: Int) {
                        dialogHelper.dismissDialog()

                        val intent = Intent(mContext, BuyVehicalActivity::class.java)
                        intent.putExtra(IntentKeyEnum.CAT_ID.name, categories.id)
                        startActivity(intent)

                    }

                })


        }

        clEmergencyServices.setOnClickListener {
            categoryClickEvent("5")
        }

        clInsurance.setOnClickListener {
            categoryClickEvent("4")
        }

        clSpareParts.setOnClickListener {
            categoryClickEvent("6")
        }

        clCourier.setOnClickListener {
            categoryClickEvent("12")
        }

        clGarage.setOnClickListener {
            categoryClickEvent("3")
        }

        clCarAccessories.setOnClickListener {
            categoryClickEvent("7")
        }

        clHireEquipments.setOnClickListener {
            categoryClickEvent("8");
        }

        clTyreServices.setOnClickListener {
            categoryClickEvent("9")
        }

        clBreakDown.setOnClickListener {
            categoryClickEvent("10")
        }

        clRentCar.setOnClickListener {
            categoryClickEvent("11")
        }

        icLanguage.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            homeViewModel.apiGetCountries()
        }

        tvProfile.setOnClickListener {
            val i = Intent(mContext, ProfileActivity::class.java)
            startActivity(i)
        }

        tvLogout.setOnClickListener {

            progressBar.visibility = View.VISIBLE
            val login = Login()
            login.seller_id = Helper().getLoginData(mContext).id
            homeViewModel.logoutAndClearFCM(login)
        }

        tvHelpCenter.setOnClickListener {

            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra(IntentKeyEnum.WEB_VIEW_URL.name, "https://aswack.com/help.php")
            startActivity(intent)

        }

        tvFaq.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra(IntentKeyEnum.WEB_VIEW_URL.name, "https://aswack.com/faq.php")
            startActivity(intent)

        }


        tvAboutUs.setOnClickListener {
            val intent = Intent(mContext, PageDetailActivity::class.java)
            intent.putExtra(IntentKeyEnum.PAGE_NO.name, "1")
            startActivity(intent)
        }

        tvContactUs.setOnClickListener {
            val intent = Intent(mContext, PageDetailActivity::class.java)
            intent.putExtra(IntentKeyEnum.PAGE_NO.name, "2")
            startActivity(intent)
        }


        tvTermsCondition.setOnClickListener {
            val intent = Intent(mContext, PageDetailActivity::class.java)
            intent.putExtra(IntentKeyEnum.PAGE_NO.name, "4")
            startActivity(intent)
        }

        tvPrivacyPolicy.setOnClickListener {
            val intent = Intent(mContext, PageDetailActivity::class.java)
            intent.putExtra(IntentKeyEnum.PAGE_NO.name, "3")
            startActivity(intent)
        }


        tvBooking.setOnClickListener {
            val i = Intent(mContext, BookingListActivity::class.java)
            startActivity(i)
        }



        tvPurchasedPackages.setOnClickListener {
            val i = Intent(mContext, PurchasedPackageListActivity::class.java)
            startActivity(i)
        }


        icNotification.setOnClickListener {
            val i = Intent(mContext, AdminNotificationActivity::class.java)
            startActivity(i)
        }

    }


    private fun blinkText() {
        val animator = ObjectAnimator.ofArgb(
            tvOurServices,
            "textColor",
            Color.BLUE,
            Color.RED,
            Color.GREEN,
            Color.BLACK
        )
        animator.duration = 2500
        animator.repeatCount = 1000
        animator.repeatMode = ValueAnimator.REVERSE
        animator.start()
    }

    override fun onResume() {
        super.onResume()
        blinkText()
    }

    private fun initDrawer() {

        rvOurServices.layoutManager = GridLayoutManager(mContext, 3)
        rvOurServices.setHasFixedSize(true)
        sliderArray.add(R.drawable.ic_slider_1)
        sliderArray.add(R.drawable.ic_slider_2)
        rvSlider.layoutManager = LinearLayoutManager(
            mContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rvSlider.setHasFixedSize(true)

        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvSlider)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        // Handle empty login data gracefully
        val loginData = Helper().getLoginData(mContext)
        val userName = if (loginData.name.isNullOrEmpty()) "User" else loginData.name
        tvUserName.setText(getString(R.string.hello) + " " + userName)
        icMenu.setOnClickListener(View.OnClickListener { drawer.openDrawer(GravityCompat.START) })
        initFab()
    }

    private fun initFab() {
        // Now set all the FABs and all the action name
        // texts as GONE
        fbCall.visibility = View.GONE
        fbChat.visibility = View.GONE
        tvCall.visibility = View.GONE
        tvChat.visibility = View.GONE

        // make the boolean variable as false, as all the
        // action name texts and all the sub FABs are invisible
        isAllFabsVisible = false

        // We will make all the FABs and action name texts
        // visible only when Parent FAB button is clicked So
        // we have to handle the Parent FAB button first, by
        // using setOnClickListener you can see below
        fbAdd.setOnClickListener(
            View.OnClickListener {
                isAllFabsVisible = if (!isAllFabsVisible!!) {

                    // when isAllFabsVisible becomes
                    // true make all the action name
                    // texts and FABs VISIBLE.
                    fbCall.show()
                    fbChat.hide()
                    tvCall.setVisibility(View.VISIBLE)
                    tvChat.setVisibility(View.GONE)
                    fbAdd.setImageDrawable(resources.getDrawable(R.drawable.ic_close))
                    // make the boolean variable true as
                    // we have set the sub FABs
                    // visibility to GONE
                    true
                } else {

                    // when isAllFabsVisible becomes
                    // true make all the action name
                    // texts and FABs GONE.
                    fbCall.hide()
                    fbChat.hide()
                    tvCall.visibility = View.GONE
                    tvChat.visibility = View.GONE

                    // make the boolean variable false
                    // as we have set the sub FABs
                    // visibility to GONE
                    fbAdd.setImageDrawable(resources.getDrawable(R.drawable.ic_round_add_24))
                    false
                }
            })
        fbChat.setOnClickListener(
            View.OnClickListener {
                //                        startActivity(new Intent(mContext, ChatActivity.class));
            })

        // below is the sample action to handle add alarm
        // FAB. Here it shows simple Toast msg The Toast
        // will be shown only when they are visible and only
        // when user clicks on them
        fbCall.setOnClickListener(
            View.OnClickListener {
                callToAdmin("+91 7635932119")
            })

        tvCall.setOnClickListener {
            callToAdmin("+91 7635932119")
        }
    }

//    override fun onOurServiceCLick(pos: Int, from: String) {
//        if (pos == 2) {
//            startActivity(Intent(mContext, DashBoardActivity::class.java))
//        } else {
//            OpenSellingDialogue(pos, from)
//        }
//    }


//    override fun onServiceCLick(pos: Int) {
//        if (dialog != null) {
//            dialog!!.dismiss()
//        }
//
//        //0 = sell car
//        //1 = buy car
//        //2 = Garage
//        //3 = vehical insurance
//        //4 = Emergency srvice
//        //5 = Spare parts
//        //6 = Car accessiories
//        //7 = Delivery service
//        if (pos == 0) {
//             val intent = Intent(mContext, SellVehicleActivity::class.java)
//            intent.putExtra(IntentKeyEnum.CAT_ID.name, "1")
//            startActivity(intent)
//        } else if (pos == 1) {
//            val intent = Intent(mContext, BuyVehicalActivity::class.java)
//            intent.putExtra(IntentKeyEnum.CAT_ID.name, "1")
//            startActivity(intent)
//        }
//    }

    override fun onCatgeorySelected(categories: Categories, posOfCategory: Int) {
        if (posOfCategory == 1) {

            var from = ""
            if (posOfCategory == 0) {
                from = "What are you selling?"
            } else if (posOfCategory == 1) {
                from = "What you want you buy?"
            } else if (posOfCategory == 2) {
                from = "Garage"
            } else if (posOfCategory == 4) {
                from =
                    "24*7 Online Emergency Service"
            } else if (posOfCategory == 3) {
                from = ""

            }


            dialogHelper.openBuySellDialog(
                posOfCategory,
                from,
                homeViewModel.vehicleList,
                object : VehicleCategoryAdapter.ICustomListListener {
                    override fun onCatgeorySelected(categories: Categories, position: Int) {

                        if (posOfCategory == 0) {
                            // TEMPORARY: Bypass company status check and navigate directly
                            dialogHelper.dismissDialog()
                            categories.id?.let {
                                val intent = Intent(mContext, DashBoardActivity::class.java)
                                intent.putExtra(IntentKeyEnum.CAT_ID.name, it)
                                intent.putExtra(IntentKeyEnum.COMPANY_ID.name, "")
                                intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, "")
                                startActivity(intent)
                            }
                            
                            /* ORIGINAL CODE - Commented out for temporary bypass
                            val login = Login()
                            login.seller_id = Helper().getLoginData(mContext).id
                            categories.id?.let {
                                homeViewModel.getCompanyRegisterStatusForCategory(login, it)
                            }
                            */
                        } else if (posOfCategory == 1) {

                            dialogHelper.dismissDialog()

                            val intent = Intent(mContext, BuyVehicalActivity::class.java)
                            intent.putExtra(IntentKeyEnum.CAT_ID.name, categories.id)
                            startActivity(intent)
                        }
                    }

                })


        } else {
            // TEMPORARY: Bypass company status check and navigate directly
            categories.id?.let {
                progressBar.visibility = View.GONE
                val intent = Intent(mContext, DashBoardActivity::class.java)
                intent.putExtra(IntentKeyEnum.CAT_ID.name, it)
                intent.putExtra(IntentKeyEnum.COMPANY_ID.name, "")
                intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, "")
                startActivity(intent)
            }

            /* ORIGINAL CODE - Commented out for temporary bypass
            val login = Login()
            login.seller_id = Helper().getLoginData(mContext).id
            categories.id?.let {
                login.category_id = it
                progressBar.visibility = View.VISIBLE
                homeViewModel.getCompanyRegisterStatusForCategory(login, it)
            }
            */
        }
    }


    private fun setCategoryData(categories: ArrayList<Categories>) {
        Glide.with(this).load(categories[0].image).into(ivCategorySellVehicle)
        tvCategoryNameSellVehicle.text = categories[0].name
        tvSubTextSellVehicle.text = categories[0].sub_text

        Glide.with(this).load(categories[1].image).into(ivCategoryBuyVehicle)
        tvCategoryNameBuyVehicle.text = categories[1].name
        tvSubTextBuyVehicle.text = categories[1].sub_text

        Glide.with(this).load(categories[2].image).into(ivCategoryGarage)
        tvCategoryNameGarage.text = categories[2].name
        tvSubTextGarage.text = categories[2].sub_text

        Glide.with(this).load(categories[3].image).into(ivCategoryInsurance)
        tvCategoryNameInsurance.text = categories[3].name
        tvSubTextInsurance.text = categories[3].sub_text

        Glide.with(this).load(categories[4].image).into(ivCategoryEmergencyServices)
        tvCategoryNameEmergencyServices.text = categories[4].name
        tvSubTextEmergencyServices.text = categories[4].sub_text

        Glide.with(this).load(categories[5].image).into(ivCategorySpareParts)
        tvCategoryNameSpareParts.text = categories[5].name
        tvSubTextSpareParts.text = categories[5].sub_text

        Glide.with(this).load(categories[6].image).into(ivCategoryAccessories)
        tvCategoryNameAccessories.text = categories[6].name
        tvSubTextAccessories.text = categories[6].sub_text

        Glide.with(this).load(categories[7].image).into(ivCategoryHireEquip)
        tvCategoryNameHireEquip.text = categories[7].name
        tvSubTextHireEquip.text = categories[7].sub_text

        Glide.with(this).load(categories[8].image).into(ivCategoryTyreServices)
        tvCategoryNameTyreServices.text = categories[8].name
        tvSubTextTyreServices.text = categories[8].sub_text

        Glide.with(this).load(categories[9].image).into(ivCategoryBreakDown)
        tvCategoryNameBreakDown.text = categories[9].name
        tvSubTextBreakDown.text = categories[9].sub_text

        Glide.with(this).load(categories[10].image).into(ivCategoryRentCar)
        tvCategoryNameRentCar.text = categories[10].name
        tvSubTextRentCar.text = categories[10].sub_text

        Glide.with(this).load(categories[11].image).into(ivCategoryCourier)
        tvCategoryNameCourier.text = categories[11].name
        tvSubTextCourier.text = categories[11].sub_text
    }


    fun categoryClickEvent(categoryId: String) {
        // TEMPORARY: Bypass company status check and navigate directly to screens
        progressBar.visibility = View.GONE
        
        // Navigate directly to appropriate screens based on category ID
        // Use empty strings for company_id and package_id to allow navigation without login
        val intent = when (categoryId) {
            "1" -> {
                // Sell Vehicle
                Intent(mContext, SellVehicleListActivity::class.java).apply {
                    putExtra(IntentKeyEnum.COMPANY_ID.name, "")
                    putExtra(IntentKeyEnum.CAT_ID.name, categoryId)
                    putExtra(IntentKeyEnum.PACKAGE_ID.name, "")
                }
            }
            "3", "5", "10" -> {
                // Garage, Emergency Service, Breakdown
                Intent(mContext, GarageServiceListActivity::class.java).apply {
                    putExtra(IntentKeyEnum.COMPANY_ID.name, "")
                    putExtra(IntentKeyEnum.MAIN_CAT_ID.name, categoryId)
                    putExtra(IntentKeyEnum.PACKAGE_ID.name, "")
                }
            }
            "4", "8", "11" -> {
                // Insurance, Hire Heavy Equipment, Rent a Car
                Intent(mContext, InsuranceProductsListActivity::class.java).apply {
                    putExtra(IntentKeyEnum.COMPANY_ID.name, "")
                    putExtra(IntentKeyEnum.MAIN_CAT_ID.name, categoryId)
                    putExtra(IntentKeyEnum.PACKAGE_ID.name, "")
                }
            }
            "6", "7" -> {
                // Spare Parts, Car Accessories
                Intent(mContext, SparePartsCarAccessoriesListActivity::class.java).apply {
                    putExtra(IntentKeyEnum.COMPANY_ID.name, "")
                    putExtra(IntentKeyEnum.MAIN_CAT_ID.name, categoryId)
                    putExtra(IntentKeyEnum.PACKAGE_ID.name, "")
                }
            }
            "9" -> {
                // Tyre Service
                Intent(mContext, TyreServiceListActivity::class.java).apply {
                    putExtra(IntentKeyEnum.COMPANY_ID.name, "")
                    putExtra(IntentKeyEnum.MAIN_CAT_ID.name, categoryId)
                    putExtra(IntentKeyEnum.PACKAGE_ID.name, "")
                }
            }
            "12" -> {
                // Courier
                Intent(mContext, CourierListActivity::class.java).apply {
                    putExtra(IntentKeyEnum.COMPANY_ID.name, "")
                    putExtra(IntentKeyEnum.MAIN_CAT_ID.name, categoryId)
                    putExtra(IntentKeyEnum.PACKAGE_ID.name, "")
                }
            }
            else -> {
                // Default to DashBoardActivity
                Intent(mContext, DashBoardActivity::class.java).apply {
                    putExtra(IntentKeyEnum.CAT_ID.name, categoryId)
                    putExtra(IntentKeyEnum.COMPANY_ID.name, "")
                    putExtra(IntentKeyEnum.PACKAGE_ID.name, "")
                }
            }
        }
        startActivity(intent)

        /* ORIGINAL CODE - Commented out for temporary bypass
        val login = Login()
        login.seller_id = Helper().getLoginData(mContext).id
        login.category_id = categoryId
        progressBar.visibility = View.VISIBLE
        homeViewModel.getCompanyRegisterStatusForCategory(login, categoryId)
        */
    }

    /**
     * Call the number
     */
    fun callToAdmin(number: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$number")
        startActivity(dialIntent)
    }
}