package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.ProductsImagesAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.TransmissionEnum
import com.avdhootsolutions.aswack_shopkeeper.models.Offer
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Utils
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.OfferDetailViewModel
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.SignUpViewModel
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.VehicleDetailViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_offer_details.*
import kotlinx.android.synthetic.main.header_title.*


class OfferDetailActivity : AppCompatActivity(), ProductsImagesAdapter.ICustomListListener {

    lateinit var offerDetailViewModel: OfferDetailViewModel
    lateinit var mContext: Context

    lateinit var dialogHelper: DialogHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_details)
        mContext = this@OfferDetailActivity
        init()
        clickListener()
    }

    private fun init() {
        dialogHelper = DialogHelper(mContext)
        offerDetailViewModel = ViewModelProvider(this).get(OfferDetailViewModel::class.java)
        offerDetailViewModel.selectedOffer = Gson().fromJson(intent.getStringExtra(IntentKeyEnum.OFFER_DETAILS.name).toString(), Offer::class.java)
        offerDetailViewModel.offerProductDetail = Gson().fromJson(intent.getStringExtra(IntentKeyEnum.VEHICLE_DETAILS.name).toString(), SellVehicle::class.java)
        tvTitle.text =offerDetailViewModel.selectedOffer.name + "'s details"
        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvProduct.layoutManager = layoutManager
        rvProduct.setHasFixedSize(true)

        offerDetailViewModel.successMessageOfDeleteLiveData.observe(this,
            androidx.lifecycle.Observer {
                progressBar.visibility = View.GONE
                val intent = Intent(mContext, ThankYouForAddActivity::class.java)
                intent.putExtra(IntentKeyEnum.IS_PRODUCT_DELETED.name, true)
                startActivity(intent)
                finish()
            })

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

        tvName.text = offerDetailViewModel.selectedOffer.name

        if (offerDetailViewModel.selectedOffer.product_name.isNullOrEmpty()){
            tvProductNameValue.text = "NA"
        }else{
            tvProductNameValue.text = offerDetailViewModel.selectedOffer.product_name
        }

       // tvName.text = offerDetailViewModel.offerProductDetail.vehicle_model_name + " (" + offerDetailViewModel.offerProductDetail.vehicle_year_name + ")"
        tvType.text = offerDetailViewModel.offerProductDetail.vehicle_brand_name + ", " + offerDetailViewModel.offerProductDetail.vehicle_type_name +", "
        offerDetailViewModel.offerProductDetail.vehicle_model_name + " (" + offerDetailViewModel.offerProductDetail.vehicle_year_name + ")"
        tvOfferCodeValue.text = offerDetailViewModel.selectedOffer.code
        tvOfferPriceValue.text = Helper().getCurrencySymbol(mContext) + offerDetailViewModel.selectedOffer.offer_price
        tvStartDateValue.text = offerDetailViewModel.selectedOffer.start_date
        tvOfferEndDateValue.text = offerDetailViewModel.selectedOffer.end_date
        tvOriginalPriceValue.text = Helper().getCurrencySymbol(mContext) + offerDetailViewModel.selectedOffer.original_price
        tvCategoryValue.text = offerDetailViewModel.offerProductDetail.vehicle_cat_name


        tvDescription.text = offerDetailViewModel.selectedOffer.description

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
                    productList.id = offerDetailViewModel.selectedOffer.id
                    productList.type = "1"
                    offerDetailViewModel.apiDeleteOffer(productList)
                }

            })
        }
        iv_back.setOnClickListener {
            finish()
        }

        tvEdit.setOnClickListener {
            val intent = Intent(mContext, UpdateOfferActivity::class.java)
            intent.putExtra(IntentKeyEnum.OFFER_DETAILS.name, Gson().toJson(offerDetailViewModel.selectedOffer))
            startActivity(intent)
            finish()
        }

    }

    override fun onImageSelected(imagePath: String) {
        dialogHelper.showFullImageDialog(imagePath)
    }


}