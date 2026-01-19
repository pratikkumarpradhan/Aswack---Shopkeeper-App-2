package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.Paint
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.ProductsImagesAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.Offer
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.OfferDetailViewModel

import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_offer_details_search_vehicles.*


import kotlinx.android.synthetic.main.header_title.*

class SearchVehicleOfferDetailActivity : AppCompatActivity(), ProductsImagesAdapter.ICustomListListener {

    lateinit var offerDetailViewModel: OfferDetailViewModel
    lateinit var mContext: Context

    lateinit var dialogHelper : DialogHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_details_search_vehicles)
        mContext = this@SearchVehicleOfferDetailActivity
        init()
        clickListener()
    }

    private fun init() {
        tvOnlineChat.visibility = View.GONE

        dialogHelper = DialogHelper(mContext)

        offerDetailViewModel = ViewModelProvider(this).get(OfferDetailViewModel::class.java)
        offerDetailViewModel.selectedOffer = Gson().fromJson(intent.getStringExtra(IntentKeyEnum.OFFER_DETAILS.name).toString(), Offer::class.java)

        tvName.text = offerDetailViewModel.selectedOffer.name
        tvType.visibility = View.GONE
        // tvName.text = offerDetailViewModel.offerProductDetail.vehicle_model_name + " (" + offerDetailViewModel.offerProductDetail.vehicle_year_name + ")"
   //     tvType.text = offerDetailViewModel.selectedOffer.vehicle_company_name + ", " + offerDetailViewModel.productList.vehicle_type_name +", "
//        offerDetailViewModel.productList.vehicle_model_name
        tvProductNameValue.text = offerDetailViewModel.selectedOffer.product_name
        tvOfferCodeValue.text = offerDetailViewModel.selectedOffer.code
        tvPriceValue.text = Helper().getCurrencySymbol(mContext) +offerDetailViewModel.selectedOffer.offer_price
        tvStartDateValue.text = offerDetailViewModel.selectedOffer.start_date
        tvOfferEndDateValue.text = offerDetailViewModel.selectedOffer.end_date
        tvOriginalPriceValue.text = Helper().getCurrencySymbol(mContext) +offerDetailViewModel.selectedOffer.original_price

        tvOriginalPriceValue.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            text = offerDetailViewModel.selectedOffer.original_price
        }


        tvDescription.text = offerDetailViewModel.selectedOffer.description


//        val productRequest = ProductRequest()
//        productRequest.vehicle_category_id = offerDetailViewModel.selectedOffer.
//        productRequest.master_category_id = offerDetailViewModel.selectedOffer.seller_company_id
//        productRequest.user_id = Helper().getLoginData(mContext).id
//        productRequest.id = offerDetailViewModel.selectedOffer.product_id
//
//        offerDetailViewModel.apiGetProductDetails(productRequest)
//
//        progressBar.visibility = View.VISIBLE

        tvTitle.text = offerDetailViewModel.selectedOffer.name
        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvProduct.setLayoutManager(layoutManager)
        rvProduct.setHasFixedSize(true)

        var imageList = ArrayList<String>()

        if (!offerDetailViewModel.selectedOffer.image_1.isNullOrEmpty()){
            imageList.add(offerDetailViewModel.selectedOffer.image_1!!)
        }


        if (!offerDetailViewModel.selectedOffer.image_2.isNullOrEmpty()){
            imageList.add(offerDetailViewModel.selectedOffer.image_2!!)
        }


        if (!offerDetailViewModel.selectedOffer.image_3.isNullOrEmpty()){
            imageList.add(offerDetailViewModel.selectedOffer.image_3!!)
        }


        if (!offerDetailViewModel.selectedOffer.image_4.isNullOrEmpty()){
            imageList.add(offerDetailViewModel.selectedOffer.image_4!!)
        }


        if (!offerDetailViewModel.selectedOffer.image_5.isNullOrEmpty()){
            imageList.add(offerDetailViewModel.selectedOffer.image_5!!)
        }

        if (!offerDetailViewModel.selectedOffer.image_6.isNullOrEmpty()){
            imageList.add(offerDetailViewModel.selectedOffer.image_6!!)
        }

        if (!offerDetailViewModel.selectedOffer.image_7.isNullOrEmpty()){
            imageList.add(offerDetailViewModel.selectedOffer.image_7!!)
        }

        val productsImagesAdapter = ProductsImagesAdapter(mContext, this)
        rvProduct.adapter = productsImagesAdapter
        productsImagesAdapter.setList(imageList)
    }


    /**
     * Click events
     */
    private fun clickListener() {

        iv_back.setOnClickListener {
            finish()
        }


        tvOnlineChat.setOnClickListener {
            val intent = Intent(mContext, ChatActivity::class.java)
            intent.putExtra(IntentKeyEnum.OFFER_DETAILS.name, Gson().toJson(offerDetailViewModel.selectedOffer))
            startActivity(intent)
        }
    }

    override fun onImageSelected(imagePath: String) {
        dialogHelper.showFullImageDialog(imagePath)
    }


}