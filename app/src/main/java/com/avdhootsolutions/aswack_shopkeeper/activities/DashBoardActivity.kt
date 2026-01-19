package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.DashBoardViewModel
import kotlinx.android.synthetic.main.activity_dash_board.*
import kotlinx.android.synthetic.main.activity_dash_board.fbAdd

class DashBoardActivity : AppCompatActivity() {

    lateinit var mContext: Context

    var isAllFabsVisible: Boolean = false


    /**
     * View model
     */
    lateinit var dashBoardViewModel: DashBoardViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        mContext = this@DashBoardActivity

        initView()
        clickListener()
    }

    /**
     * click events
     */
    private fun clickListener() {
        fbAdd.setOnClickListener(
            View.OnClickListener {
                isAllFabsVisible = if (!isAllFabsVisible!!) {

                    // when isAllFabsVisible becomes
                    // true make all the action name
                    // texts and FABs VISIBLE.
                    fbProfile.show()
                    fbProfile.visibility = View.VISIBLE
                    tvProfile.visibility = View.VISIBLE

                    fbAdmin.show()
                    fbAdmin.visibility = View.VISIBLE
                    tvAdmin.visibility = View.VISIBLE

                    fbAdd.setImageDrawable(resources.getDrawable(R.drawable.ic_close))
                    // make the boolean variable true as
                    // we have set the sub FABs
                    // visibility to GONE
                    true
                } else {

                    // when isAllFabsVisible becomes
                    // true make all the action name
                    // texts and FABs GONE.
                    fbProfile.hide()
                    fbProfile.visibility = View.GONE
                    tvProfile.visibility = View.GONE

                    fbAdmin.hide()
                    fbAdmin.visibility = View.GONE
                    tvAdmin.visibility = View.GONE
                    // make the boolean variable false
                    // as we have set the sub FABs
                    // visibility to GONE
                    fbAdd.setImageDrawable(resources.getDrawable(R.drawable.ic_round_add_24))
                    false
                }
            })

        fbProfile.setOnClickListener {
            val intent = Intent(mContext, CompanyProfileActivity::class.java)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
            intent.putExtra(IntentKeyEnum.CAT_ID.name, dashBoardViewModel.categoryId)
            startActivity(intent)
        }

        fbAdmin.setOnClickListener {
           DialogHelper(mContext).openContactUsDialogue()
        }


        ivMyProduct.setOnClickListener(View.OnClickListener {
            if (dashBoardViewModel.categoryId == "1"){

                val intent = Intent(mContext, SellVehicleListActivity::class.java)
                intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
                intent.putExtra(IntentKeyEnum.CAT_ID.name, dashBoardViewModel.categoryId)
                intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, dashBoardViewModel.packageId)
                startActivity(intent)

            }else if (dashBoardViewModel.categoryId == "4" || dashBoardViewModel.categoryId == "8" || dashBoardViewModel.categoryId == "11"){

                val intent = Intent(mContext, InsuranceProductsListActivity::class.java)
                intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
                intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
                intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, dashBoardViewModel.packageId)
                startActivity(intent)

            }else if (dashBoardViewModel.categoryId == "6" || dashBoardViewModel.categoryId == "7"){

                val intent = Intent(mContext, SparePartsCarAccessoriesListActivity::class.java)
                intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
                intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
                intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, dashBoardViewModel.packageId)
                startActivity(intent)

            }else if (dashBoardViewModel.categoryId == "9" ){

                val intent = Intent(mContext, TyreServiceListActivity::class.java)
                intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
                intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
                intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, dashBoardViewModel.packageId)
                startActivity(intent)

            }else if (dashBoardViewModel.categoryId == "3" || dashBoardViewModel.categoryId == "5" || dashBoardViewModel.categoryId == "10" ){

                val intent = Intent(mContext, GarageServiceListActivity::class.java)
                intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
                intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
                intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, dashBoardViewModel.packageId)
                startActivity(intent)

            }else if (dashBoardViewModel.categoryId == "12"){

                val intent = Intent(mContext, CourierListActivity::class.java)
                intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
                intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
                intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, dashBoardViewModel.packageId)
                startActivity(intent)

            }

                else{
//                startActivity(Intent(mContext,
//                    MyProductsActivity::class.java))
            }

        })
        tvMyProduct.setOnClickListener(View.OnClickListener {
            if (dashBoardViewModel.categoryId == "1"){

                val intent = Intent(mContext, SellVehicleListActivity::class.java)
                intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
                intent.putExtra(IntentKeyEnum.CAT_ID.name, dashBoardViewModel.categoryId)
                intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, dashBoardViewModel.packageId)
                startActivity(intent)

            }else if (dashBoardViewModel.categoryId == "6" || dashBoardViewModel.categoryId == "7"){

                val intent = Intent(mContext, SparePartsCarAccessoriesListActivity::class.java)
                intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
                intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
                intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, dashBoardViewModel.packageId)
                startActivity(intent)

            }else if (dashBoardViewModel.categoryId == "9" ){

                val intent = Intent(mContext, TyreServiceListActivity::class.java)
                intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
                intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
                intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, dashBoardViewModel.packageId)
                startActivity(intent)

            }else if (dashBoardViewModel.categoryId == "3" || dashBoardViewModel.categoryId == "5" || dashBoardViewModel.categoryId == "10" ){

                val intent = Intent(mContext, GarageServiceListActivity::class.java)
                intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
                intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
                intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, dashBoardViewModel.packageId)
                startActivity(intent)

            }

            else{
                startActivity(Intent(mContext,
                    MyProductsActivity::class.java))
            }
        })
        ivMyOffer.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, MyOfferListActivity::class.java)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
            intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
            intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, dashBoardViewModel.packageId)
            startActivity(intent)
        })
        tvMyOffer.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, MyOfferListActivity::class.java)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
            intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
            intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, dashBoardViewModel.packageId)
            startActivity(intent)
        })
        ivRFQ.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, RFQListActivity::class.java)
            intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
            startActivity(intent)
        })
        tvRFQ.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, RFQListActivity::class.java)
            intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
            startActivity(intent)
        })
        ivGeneratingNotification.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, SellerNotificationListActivity::class.java)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
            intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
            intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, dashBoardViewModel.packageId)
            startActivity(intent)
        })
        tvGeneratingNotification.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, SellerNotificationListActivity::class.java)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
            intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
            intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, dashBoardViewModel.packageId)
            startActivity(intent)
        })
        ivMyRating.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, RatingAndReviewActivity::class.java)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
            intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
            startActivity(intent)
        })
        tvMyRating.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, RatingAndReviewActivity::class.java)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
            intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
            startActivity(intent)
        })
        ivCustomiseRequirement.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, SellerWisePackageListActivity::class.java)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
            intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
            startActivity(intent)
        })
        tvCustomiseRequirement.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, SellerWisePackageListActivity::class.java)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
            intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, dashBoardViewModel.categoryId)
            startActivity(intent)
        })

        ivCustomerChat.setOnClickListener {

            val intent = Intent(mContext, ChatListActivity::class.java)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
            startActivity(intent)
        }

        tvCustomerChat.setOnClickListener {    val intent = Intent(mContext, ChatListActivity::class.java)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
            startActivity(intent) }

        ivMyOrders.setOnClickListener {
            val intent = Intent(mContext, OrderListActivity::class.java)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
            startActivity(intent)
        }

        tvMyOrders.setOnClickListener {
            val intent = Intent(mContext, OrderListActivity::class.java)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, dashBoardViewModel.companyId)
            startActivity(intent)
        }
    }

    private fun initView() {
        dashBoardViewModel = ViewModelProvider(this).get(DashBoardViewModel::class.java)

        tvWelcomeName.text = resources.getString(R.string.welcome) + " " + Helper().getLoginData(mContext).name

        dashBoardViewModel.categoryId = intent.getStringExtra(IntentKeyEnum.CAT_ID.name).toString()
        dashBoardViewModel.companyId = intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()
        dashBoardViewModel.packageId = intent.getStringExtra(IntentKeyEnum.PACKAGE_ID.name).toString()

        if (intent.hasExtra("NOTIFICATION_FROM_CHAT")){

        }

        if (dashBoardViewModel.categoryId == "12"){
//            ivMyProduct.visibility = View.GONE
            tvMyProduct.text = resources.getString(R.string.inquiries)
        }

        when(dashBoardViewModel.categoryId){
            "1" ->{
                tvAppName.text = "Sell Vehicle"
            }

            "3" ->{
                tvAppName.text = "Garage"
            }

            "4" ->{
                tvAppName.text = "Vehicle Insurance"
            }

            "5" ->{
                tvAppName.text = "Emergency Service's"
            }

            "6" ->{
                tvAppName.text = "Spare Parts"
            }
            "7" ->{
                tvAppName.text = "Car Accessories"
            }

            "8" ->{
                tvAppName.text = "Hire Heavy Equipment"
            }

            "9" ->{
                tvAppName.text = "Tyre Services"
            }

            "10" ->{
                tvAppName.text = "Break Down"
            }

            "11" ->{
                tvAppName.text = "Rent a Car"
            }

            "12" ->{
                tvAppName.text = "Courier"
            }


        }

        dashBoardViewModel.errorMessage.observe(this, androidx.lifecycle.Observer { error ->

            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })

        dashBoardViewModel.successMessageLiveData.observe(this, androidx.lifecycle.Observer { isSuccess ->
            if (isSuccess){
                Toast.makeText(mContext, resources.getString(R.string.offer_created), Toast.LENGTH_SHORT).show()
                finish()
            }
        })



    }
}