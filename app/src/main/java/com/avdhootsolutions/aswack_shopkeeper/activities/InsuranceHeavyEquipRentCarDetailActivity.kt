package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.ProductsImagesAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.SparePartsCarAccesoriesDetailViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_garage_details.*
import kotlinx.android.synthetic.main.activity_spare_parts_details.*
import kotlinx.android.synthetic.main.activity_spare_parts_details.progressBar
import kotlinx.android.synthetic.main.activity_spare_parts_details.rvProduct
import kotlinx.android.synthetic.main.activity_spare_parts_details.tvCategoryValue
import kotlinx.android.synthetic.main.activity_spare_parts_details.tvDelete
import kotlinx.android.synthetic.main.activity_spare_parts_details.tvDescription
import kotlinx.android.synthetic.main.activity_spare_parts_details.tvEdit
import kotlinx.android.synthetic.main.activity_spare_parts_details.tvName
import kotlinx.android.synthetic.main.activity_spare_parts_details.tvPostedOnValue
import kotlinx.android.synthetic.main.activity_spare_parts_details.tvPriceValue
import kotlinx.android.synthetic.main.activity_spare_parts_details.tvProductCodeValue
import kotlinx.android.synthetic.main.activity_spare_parts_details.tvSerialNumber
import kotlinx.android.synthetic.main.activity_spare_parts_details.tvSerialNumberValue
import kotlinx.android.synthetic.main.activity_spare_parts_details.tvType
import kotlinx.android.synthetic.main.header_title.*


class InsuranceHeavyEquipRentCarDetailActivity : AppCompatActivity(), ProductsImagesAdapter.ICustomListListener {

    lateinit var sparePartsCarAccesoriesDetailViewModel: SparePartsCarAccesoriesDetailViewModel
    lateinit var mContext: Context

    lateinit var dialogHelper: DialogHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spare_parts_details)
        mContext = this@InsuranceHeavyEquipRentCarDetailActivity
        init()
        clickListener()
    }

    private fun init() {
        dialogHelper = DialogHelper(mContext)
        sparePartsCarAccesoriesDetailViewModel = ViewModelProvider(this).get(SparePartsCarAccesoriesDetailViewModel::class.java)
        sparePartsCarAccesoriesDetailViewModel.productList = Gson().fromJson(intent.getStringExtra(IntentKeyEnum.PRODUCT_LIST.name).toString(), ProductList::class.java)
        tvTitle.text = sparePartsCarAccesoriesDetailViewModel.productList.product_name + "'s details"

        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            rvProduct.setLayoutManager(layoutManager)
        rvProduct.setHasFixedSize(true)

        sparePartsCarAccesoriesDetailViewModel.successMessageOfDeleteLiveData.observe(this,
            androidx.lifecycle.Observer {
                progressBar.visibility = View.GONE
                val intent = Intent(mContext, ThankYouForAddActivity::class.java)
                intent.putExtra(IntentKeyEnum.IS_PRODUCT_DELETED.name, true)
                startActivity(intent)
                finish()
            })



        var imageList = ArrayList<String>()

        if (!sparePartsCarAccesoriesDetailViewModel.productList.image1.isNullOrEmpty()){
            imageList.add(sparePartsCarAccesoriesDetailViewModel.productList.image1!!)
        }


        if (!sparePartsCarAccesoriesDetailViewModel.productList.image2.isNullOrEmpty()){
            imageList.add(sparePartsCarAccesoriesDetailViewModel.productList.image2!!)
        }


        if (!sparePartsCarAccesoriesDetailViewModel.productList.image3.isNullOrEmpty()){
            imageList.add(sparePartsCarAccesoriesDetailViewModel.productList.image3!!)
        }


        if (!sparePartsCarAccesoriesDetailViewModel.productList.image4.isNullOrEmpty()){
            imageList.add(sparePartsCarAccesoriesDetailViewModel.productList.image4!!)
        }


        if (!sparePartsCarAccesoriesDetailViewModel.productList.image5.isNullOrEmpty()){
            imageList.add(sparePartsCarAccesoriesDetailViewModel.productList.image5!!)
        }

        if (!sparePartsCarAccesoriesDetailViewModel.productList.image6.isNullOrEmpty()){
            imageList.add(sparePartsCarAccesoriesDetailViewModel.productList.image6!!)
        }

        if (!sparePartsCarAccesoriesDetailViewModel.productList.image7.isNullOrEmpty()){
            imageList.add(sparePartsCarAccesoriesDetailViewModel.productList.image7!!)
        }

        val productsImagesAdapter = ProductsImagesAdapter(mContext, this)
        rvProduct.adapter = productsImagesAdapter
        productsImagesAdapter.setList(imageList)

        tvName.text = sparePartsCarAccesoriesDetailViewModel.productList.product_name
        tvModelValue.text= sparePartsCarAccesoriesDetailViewModel.productList.vehicle_model_name
        tvType.text = sparePartsCarAccesoriesDetailViewModel.productList.vehicle_company_name + ", " + sparePartsCarAccesoriesDetailViewModel.productList.vehicle_type_name
        tvPriceValue.text = Helper().getCurrencySymbol(mContext) + sparePartsCarAccesoriesDetailViewModel.productList.price
        tvProductCodeValue.text = sparePartsCarAccesoriesDetailViewModel.productList.product_code
        tvPostedOnValue.text = sparePartsCarAccesoriesDetailViewModel.productList.created_datetime
        tvSerialNumberValue.text = sparePartsCarAccesoriesDetailViewModel.productList.serial_number

        if (sparePartsCarAccesoriesDetailViewModel.productList.vehicle_year_name!!.isNotEmpty()){
            tvYearValue.visibility = View.VISIBLE
            tvYear.visibility = View.VISIBLE
            tvYearValue.text = sparePartsCarAccesoriesDetailViewModel.productList.vehicle_year_name
        }


        if (sparePartsCarAccesoriesDetailViewModel.productList.serial_number!!.isEmpty()){
            tvSerialNumber.visibility = View.GONE
            tvSerialNumberValue.visibility = View.GONE
        }


        tvCondition.visibility = View.GONE
        tvConditionValue.visibility = View.GONE

        tvCategoryValue.text = sparePartsCarAccesoriesDetailViewModel.productList.vehicle_cat_name

        if (sparePartsCarAccesoriesDetailViewModel.productList.product_condition == "0"){
            tvConditionValue.text = "Old"
        }else if(sparePartsCarAccesoriesDetailViewModel.productList.product_condition == "1"){
            tvConditionValue.text = "New"
        }

        tvDescription.text = sparePartsCarAccesoriesDetailViewModel.productList.description

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
                    productList.id = sparePartsCarAccesoriesDetailViewModel.productList.id
                    productList.type = "0"
                    sparePartsCarAccesoriesDetailViewModel.apiDeleteProduct(productList)
                }

            })
        }


        iv_back.setOnClickListener {
            finish()
        }

        tvEdit.setOnClickListener {
            val intent = Intent(mContext, UpdateInsuranceHeavyEquipRentCarActivity::class.java)
            intent.putExtra(IntentKeyEnum.PRODUCT_LIST.name, Gson().toJson(sparePartsCarAccesoriesDetailViewModel.productList))
            startActivity(intent)
            finish()
        }

    }

    override fun onImageSelected(imagePath: String) {
        dialogHelper.showFullImageDialog(imagePath)
    }


}