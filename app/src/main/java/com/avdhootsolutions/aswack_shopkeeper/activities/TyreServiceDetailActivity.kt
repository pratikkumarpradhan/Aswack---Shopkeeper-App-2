package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.ProductsImagesAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.TransmissionEnum
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Utils
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.SignUpViewModel
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.SparePartsCarAccesoriesDetailViewModel
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.TyreServiceDetailViewModel
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.VehicleDetailViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_spare_parts_details.*
import kotlinx.android.synthetic.main.activity_tyre_service_details.*
import kotlinx.android.synthetic.main.activity_tyre_service_details.rvProduct
import kotlinx.android.synthetic.main.activity_tyre_service_details.tvCategoryValue
import kotlinx.android.synthetic.main.activity_tyre_service_details.tvConditionValue
import kotlinx.android.synthetic.main.activity_tyre_service_details.tvDescription
import kotlinx.android.synthetic.main.activity_tyre_service_details.tvEdit
import kotlinx.android.synthetic.main.activity_tyre_service_details.tvName
import kotlinx.android.synthetic.main.activity_tyre_service_details.tvPostedOnValue
import kotlinx.android.synthetic.main.activity_tyre_service_details.tvPriceValue
import kotlinx.android.synthetic.main.activity_tyre_service_details.tvProductCodeValue
import kotlinx.android.synthetic.main.activity_tyre_service_details.tvSerialNumberValue
import kotlinx.android.synthetic.main.activity_tyre_service_details.tvType
import kotlinx.android.synthetic.main.header_title.*


class TyreServiceDetailActivity : AppCompatActivity(), ProductsImagesAdapter.ICustomListListener {

    lateinit var tyreServiceDetailViewModel: TyreServiceDetailViewModel
    lateinit var mContext: Context

    lateinit var dialogHelper: DialogHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tyre_service_details)
        mContext = this@TyreServiceDetailActivity
        init()
        clickListener()
    }

    private fun init() {

        dialogHelper = DialogHelper(mContext)
        tyreServiceDetailViewModel =
            ViewModelProvider(this).get(TyreServiceDetailViewModel::class.java)
        tyreServiceDetailViewModel.productList =
            Gson().fromJson(intent.getStringExtra(IntentKeyEnum.PRODUCT_LIST.name).toString(),
                ProductList::class.java)
        tvTitle.text = tyreServiceDetailViewModel.productList.product_name + "'s details"

        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvProduct.setLayoutManager(layoutManager)
        rvProduct.setHasFixedSize(true)


        var imageList = ArrayList<String>()

        if (!tyreServiceDetailViewModel.productList.image1.isNullOrEmpty()) {
            imageList.add(tyreServiceDetailViewModel.productList.image1!!)
        }


        if (!tyreServiceDetailViewModel.productList.image2.isNullOrEmpty()) {
            imageList.add(tyreServiceDetailViewModel.productList.image2!!)
        }


        if (!tyreServiceDetailViewModel.productList.image3.isNullOrEmpty()) {
            imageList.add(tyreServiceDetailViewModel.productList.image3!!)
        }


        if (!tyreServiceDetailViewModel.productList.image4.isNullOrEmpty()) {
            imageList.add(tyreServiceDetailViewModel.productList.image4!!)
        }


        if (!tyreServiceDetailViewModel.productList.image5.isNullOrEmpty()) {
            imageList.add(tyreServiceDetailViewModel.productList.image5!!)
        }

        if (!tyreServiceDetailViewModel.productList.image6.isNullOrEmpty()) {
            imageList.add(tyreServiceDetailViewModel.productList.image6!!)
        }

        if (!tyreServiceDetailViewModel.productList.image7.isNullOrEmpty()) {
            imageList.add(tyreServiceDetailViewModel.productList.image7!!)
        }

        val productsImagesAdapter = ProductsImagesAdapter(mContext, this)
        rvProduct.adapter = productsImagesAdapter
        productsImagesAdapter.setList(imageList)

        tvName.text = tyreServiceDetailViewModel.productList.vehicle_model_name
        tvType.text =
            tyreServiceDetailViewModel.productList.vehicle_company_name + ", " + tyreServiceDetailViewModel.productList.vehicle_type_name
        tvPriceValue.text = Helper().getCurrencySymbol(mContext) +tyreServiceDetailViewModel.productList.price
        tvProductCodeValue.text = tyreServiceDetailViewModel.productList.product_code
        tvPostedOnValue.text = tyreServiceDetailViewModel.productList.created_datetime
        tvSerialNumberValue.text = tyreServiceDetailViewModel.productList.serial_number

        tvTyreWidthValue.text = tyreServiceDetailViewModel.productList.tyre_width
        tvRimDiameterValue.text = tyreServiceDetailViewModel.productList.rim_diameter
        tvAspectRatioValue.text = tyreServiceDetailViewModel.productList.aspect_ratio
        tvLoadIndexValue.text = tyreServiceDetailViewModel.productList.load_index
        tvSpeedIndexValue.text = tyreServiceDetailViewModel.productList.speed_index
        tvTyreSizeValue.text = tyreServiceDetailViewModel.productList.tyre_size

        tvCategoryValue.text = tyreServiceDetailViewModel.productList.vehicle_cat_name
        tvProductNameValue.text = tyreServiceDetailViewModel.productList.product_name

        if (tyreServiceDetailViewModel.productList.product_condition == "0") {
            tvConditionValue.text = "Old"
        } else if (tyreServiceDetailViewModel.productList.product_condition == "1") {
            tvConditionValue.text = "New"
        }

        if (tyreServiceDetailViewModel.productList.is_tubeless == "0") {
            tvIsTubeLessValue.text = "Yes"
        } else if (tyreServiceDetailViewModel.productList.product_condition == "1") {
            tvIsTubeLessValue.text = "No"
        }

        tvDescription.text = tyreServiceDetailViewModel.productList.description

    }


    /**
     * Click events
     */
    private fun clickListener() {

        iv_back.setOnClickListener {
            finish()
        }

        tvEdit.setOnClickListener {
            val intent = Intent(mContext, UpdateTyreServicesActivity::class.java)
            intent.putExtra(IntentKeyEnum.PRODUCT_LIST.name, Gson().toJson(tyreServiceDetailViewModel.productList))
            startActivity(intent)
            finish()
        }
    }

    override fun onImageSelected(imagePath: String) {
        dialogHelper.showFullImageDialog(imagePath)
    }

}