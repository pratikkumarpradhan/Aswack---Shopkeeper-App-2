package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.SliderImageAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.OwnersEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.TransmissionEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.UserTypeEnum
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.avdhootsolutions.aswack_shopkeeper.models.SliderData
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.SearchVehicleDetailViewModel
import com.google.gson.Gson
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_car_search_view.*
import kotlinx.android.synthetic.main.header_title.*


class SearchVehicleDetailActivity : AppCompatActivity(),
    SliderImageAdapter.ICustomListListener {

    lateinit var vehicleDetailViewModel: SearchVehicleDetailViewModel

    var sliderDataArrayList = ArrayList<SliderData>()

    lateinit var mContext: Context

    lateinit var dialogHelper: DialogHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_search_view)
        mContext = this@SearchVehicleDetailActivity
        init()
        clickListener()
    }

    private fun init() {
        dialogHelper = DialogHelper(mContext)
        iv_Like.visibility = View.GONE
        iv_share.visibility = View.GONE
        tvOnlineChat.visibility = View.GONE

        vehicleDetailViewModel = ViewModelProvider(this).get(SearchVehicleDetailViewModel::class.java)

        vehicleDetailViewModel.selectedVehcile =
            Gson().fromJson(intent.getStringExtra(IntentKeyEnum.VEHICLE_DETAILS.name).toString(),
                SellVehicle::class.java)
        tvTitle.text = resources.getString(R.string.vehicle_details)

        tvTitleCar.text = vehicleDetailViewModel.selectedVehcile.title

        var imageList = ArrayList<String>()

        if (!vehicleDetailViewModel.selectedVehcile.image1.isNullOrEmpty()) {
            imageList.add(vehicleDetailViewModel.selectedVehcile.image1!!)
            sliderDataArrayList.add(SliderData(vehicleDetailViewModel.selectedVehcile.image1!!))
        }


        if (!vehicleDetailViewModel.selectedVehcile.image2.isNullOrEmpty()) {
            imageList.add(vehicleDetailViewModel.selectedVehcile.image2!!)
            sliderDataArrayList.add(SliderData(vehicleDetailViewModel.selectedVehcile.image2!!))
        }


        if (!vehicleDetailViewModel.selectedVehcile.image3.isNullOrEmpty()) {
            imageList.add(vehicleDetailViewModel.selectedVehcile.image3!!)
            sliderDataArrayList.add(SliderData(vehicleDetailViewModel.selectedVehcile.image3!!))
        }


        if (!vehicleDetailViewModel.selectedVehcile.image4.isNullOrEmpty()) {
            imageList.add(vehicleDetailViewModel.selectedVehcile.image4!!)
            sliderDataArrayList.add(SliderData(vehicleDetailViewModel.selectedVehcile.image4!!))
        }


        if (!vehicleDetailViewModel.selectedVehcile.image5.isNullOrEmpty()) {
            imageList.add(vehicleDetailViewModel.selectedVehcile.image5!!)
            sliderDataArrayList.add(SliderData(vehicleDetailViewModel.selectedVehcile.image5!!))
        }

        if (!vehicleDetailViewModel.selectedVehcile.image6.isNullOrEmpty()) {
            imageList.add(vehicleDetailViewModel.selectedVehcile.image6!!)
            sliderDataArrayList.add(SliderData(vehicleDetailViewModel.selectedVehcile.image6!!))
        }

        if (!vehicleDetailViewModel.selectedVehcile.image7.isNullOrEmpty()) {
            imageList.add(vehicleDetailViewModel.selectedVehcile.image7!!)
            sliderDataArrayList.add(SliderData(vehicleDetailViewModel.selectedVehcile.image7!!))
        }


        val adapter = SliderImageAdapter(mContext, sliderDataArrayList, this)
        slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR)
        slider.setSliderAdapter(adapter)
        slider.setScrollTimeInSec(3)
        slider.setAutoCycle(true)
        slider.startAutoCycle()


        tvadno.text = "Ad no.: " + vehicleDetailViewModel.selectedVehcile.advertisement_code


        tvCarname.text =
            vehicleDetailViewModel.selectedVehcile.vehicle_model_name + " (" + vehicleDetailViewModel.selectedVehcile.vehicle_year_name + ")"
        tvType.text =
            vehicleDetailViewModel.selectedVehcile.vehicle_brand_name + ", " + vehicleDetailViewModel.selectedVehcile.vehicle_type_name
     //   tvcarsellAdd.text = vehicleDetailViewModel.selectedVehcile.ad
        tvPostingdate.text = vehicleDetailViewModel.selectedVehcile.created_datetime
        tvtypecar.text = vehicleDetailViewModel.selectedVehcile.vehicle_fuel_name
      //  tvMobileNoValue.text = vehicleDetailViewModel.selectedVehcile.contact_number
        tvCarkm.text = vehicleDetailViewModel.selectedVehcile.driven_km + " Km"
        tvfulldescaddreview.text = vehicleDetailViewModel.selectedVehcile.description
        tvMobileNoValue.text = vehicleDetailViewModel.selectedVehcile.contact_number
        tvPrice.text = resources.getString(R.string.price_colun) + " " +Helper().getCurrencySymbol(mContext) + vehicleDetailViewModel.selectedVehcile.price

        // If the user type is user then checkout button will be hide.. Because in user side, he can not add offer
        // If the login seller is the owner of the vehicle, Then checkout button will be hide.
        if (vehicleDetailViewModel.selectedVehcile.user_type == UserTypeEnum.USER.ordinal.toString()){
            tvCheckOutOffers.visibility = View.GONE
        }else if (vehicleDetailViewModel.selectedVehcile.user_type == UserTypeEnum.SELLER.ordinal.toString() &&
            vehicleDetailViewModel.selectedVehcile.user_id == Helper().getLoginData(mContext).id){
            tvCheckOutOffers.visibility = View.GONE
            tvCheckOutProducts.visibility = View.GONE
        }

        when(vehicleDetailViewModel.selectedVehcile.owners){
            OwnersEnum.FIRST_OWNER.ordinal.toString()->{
                tvOwner.text = resources.getString(R.string.first_owner)
            }

            OwnersEnum.SECOND_OWNER.ordinal.toString()->{
                tvOwner.text = resources.getString(R.string.second_owner)
            }

            OwnersEnum.THIRD_OWNER.ordinal.toString()->{
                tvOwner.text = resources.getString(R.string.third_owner)
            }

            OwnersEnum.FOUR_OWNER.ordinal.toString()->{
                tvOwner.text = resources.getString(R.string.fourth_owner)
            }

        }

        tvpostedon.text = "Posted On: " + vehicleDetailViewModel.selectedVehcile.created_datetime

        if (vehicleDetailViewModel.selectedVehcile.transmission == TransmissionEnum.MANUAL.ordinal.toString()) {
            tvVarient.text = resources.getString(R.string.manual)
        } else if (vehicleDetailViewModel.selectedVehcile.transmission == TransmissionEnum.AUTOMATIC.ordinal.toString()) {
            tvVarient.text = resources.getString(R.string.automatic)
        }

      //  tvDescription.text = vehicleDetailViewModel.selectedVehcile.description

    }


    /**
     * Click events
     */
    private fun clickListener() {


        clposteddetails.setOnClickListener {
            if (tvMobileNoValue.text.isNotEmpty()){
                call(tvMobileNoValue.text.toString())
            }
        }

        iv_back.setOnClickListener {
            finish()
        }

        tvOnlineChat.setOnClickListener {

            var productList = ProductList()
            productList.id = vehicleDetailViewModel.selectedVehcile.id
            productList.product_name = vehicleDetailViewModel.selectedVehcile.title

            productList.master_category_id = "1"

            productList.seller_id = vehicleDetailViewModel.selectedVehcile.user_id
            productList.seller_name = vehicleDetailViewModel.selectedVehcile.seller_name

            productList.seller_company_id = vehicleDetailViewModel.selectedVehcile.seller_company_id
            productList.seller_company_name = vehicleDetailViewModel.selectedVehcile.seller_company_name

            val intent = Intent(mContext, ChatActivity::class.java)
            intent.putExtra(IntentKeyEnum.PRODUCT_LIST.name, Gson().toJson(productList))
            startActivity(intent)
        }

        tvCheckOutOffers.setOnClickListener {
            val intent = Intent(mContext, MyOfferListActivity::class.java)
            intent.putExtra(IntentKeyEnum.IS_BUYER_OF_VEHICLE.name, true)
            intent.putExtra(IntentKeyEnum.SELLER_ID.name, vehicleDetailViewModel.selectedVehcile.user_id)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, vehicleDetailViewModel.selectedVehcile.seller_company_id)
            intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, "1")
            startActivity(intent)
        }


        tvCheckOutProducts.setOnClickListener {
            val intent = Intent(mContext, SellVehicleListActivity::class.java)
            intent.putExtra(IntentKeyEnum.IS_BUYER_OF_VEHICLE.name, true)
            intent.putExtra(IntentKeyEnum.SELLER_ID.name, vehicleDetailViewModel.selectedVehcile.user_id)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, vehicleDetailViewModel.selectedVehcile.seller_company_id)
            intent.putExtra(IntentKeyEnum.USER_TYPE.name, vehicleDetailViewModel.selectedVehcile.user_type)
            intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, "1")
            startActivity(intent)
        }

//        iv_Like.setOnClickListener {
//
//            progressBar.visibility = View.VISIBLE
//
//            val wishListRequest = WishListRequest()
//            wishListRequest.user_id = Helper().getLoginData(mContext).id
//            wishListRequest.user_type = UserTypeEnum.USER.ordinal.toString()
//            wishListRequest.master_category_id = "1"
//            wishListRequest.product_id = vehicleDetailViewModel.selectedVehcile.id
//            wishListRequest.operationtype = "insert"
//            vehicleDetailViewModel.apiAddRemoveInWishList(wishListRequest)
//
//        }

    }

    override fun imageSelect(imagePath: String?) {
        imagePath?.let { dialogHelper.showFullImageDialog(it) }
    }



    /**
     * Call the number
     */
    fun call(number: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$number")
        startActivity(dialIntent)
    }


}