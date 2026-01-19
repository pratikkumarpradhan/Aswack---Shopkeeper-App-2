package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.ProductsImagesAdapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.SliderImageAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.OwnersEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.TransmissionEnum
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.avdhootsolutions.aswack_shopkeeper.models.SliderData
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.SearchVehicleDetailViewModel
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.VehicleDetailViewModel
import com.google.gson.Gson
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_sell_vehicle_details.*
import kotlinx.android.synthetic.main.header_title.*
import kotlinx.android.synthetic.main.header_title.tvTitle


class SellVehicleDetailActivity : AppCompatActivity(),
    SliderImageAdapter.ICustomListListener {

    lateinit var vehicleDetailViewModel: SearchVehicleDetailViewModel

    var sliderDataArrayList = ArrayList<SliderData>()
    lateinit var dialogHelper: DialogHelper
    lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_vehicle_details)
        mContext = this@SellVehicleDetailActivity
        init()
        clickListener()
    }

    private fun init() {
        dialogHelper = DialogHelper(mContext)
        iv_Like.visibility = View.GONE
        iv_share.visibility = View.GONE

        vehicleDetailViewModel = ViewModelProvider(this).get(SearchVehicleDetailViewModel::class.java)

        if (intent.hasExtra(IntentKeyEnum.IS_BUYER_OF_VEHICLE.name)){
            rlReview.visibility = View.GONE
        }

        vehicleDetailViewModel.selectedVehcile =
            Gson().fromJson(intent.getStringExtra(IntentKeyEnum.VEHICLE_DETAILS.name).toString(),
                SellVehicle::class.java)
        tvTitle.text = resources.getString(R.string.vehicle_details)

        tvTitleCar.text = vehicleDetailViewModel.selectedVehcile.title

        vehicleDetailViewModel.successMessageOfDeleteLiveData.observe(this,
            androidx.lifecycle.Observer {
                progressBar.visibility = View.GONE
                val intent = Intent(mContext, ThankYouForAddActivity::class.java)
                intent.putExtra(IntentKeyEnum.IS_PRODUCT_DELETED.name, true)
                startActivity(intent)
                finish()
            })


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
        slider.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        slider.setSliderAdapter(adapter)
        slider.scrollTimeInSec = 3
        slider.isAutoCycle = true
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

        tvDelete.setOnClickListener {
            dialogHelper.showDeleteProductDialog(object : DialogHelper.DeleteDialogListener{
                override fun isDeleteItem() {
                    progressBar.visibility = View.VISIBLE

                    val productList = ProductList()
                    productList.id = vehicleDetailViewModel.selectedVehcile.id
                    productList.type = "2"
                    vehicleDetailViewModel.apiDeleteProduct(productList)
                }

            })
        }

        iv_back.setOnClickListener {
            finish()
        }

        tvEdit.setOnClickListener {
            val intent = Intent(mContext, UpdateSellVehicleActivity::class.java)
            intent.putExtra(IntentKeyEnum.VEHICLE_DETAILS.name, Gson().toJson(vehicleDetailViewModel.selectedVehcile))
            startActivity(intent)
            finish()
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

        imagePath?.let {
            dialogHelper.showFullImageDialog(it)
        }

    }


}