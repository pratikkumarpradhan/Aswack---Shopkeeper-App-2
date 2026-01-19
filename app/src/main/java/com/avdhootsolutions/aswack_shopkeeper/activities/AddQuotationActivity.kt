package com.avdhootsolutions.aswack_shopkeeper.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import android.content.Intent
import android.content.Context
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.AddQuotationViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_quotation_product.*
import kotlinx.android.synthetic.main.header_title.*
import java.io.*
import java.util.*

class AddQuotationActivity : AppCompatActivity(){

    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var addQuotationViewModel: AddQuotationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_quotation_product)
        mContext = this@AddQuotationActivity
        initView()

        clickListener()
    }

    /**
     * Click events
     */
    private fun clickListener() {
        tvSubmit.setOnClickListener {
            if (etProductName.text.toString().isNullOrEmpty() ||
                etProductPrice.text.toString().isNullOrEmpty() ||
                etDescription.text.toString().isNullOrEmpty() ||
                etDelivery.text.toString().isNullOrEmpty() ||
                etGrandTotal.text.toString().isNullOrEmpty() ||
                etTerms.text.toString().isNullOrEmpty()
            ) {
                Toast.makeText(mContext,
                    resources.getString(R.string.fill_all_details),
                    Toast.LENGTH_SHORT).show()
            }  else {
                val addQuotation = QuotationRequest()

                addQuotation.user_id = addQuotationViewModel.productDataForSeller.userId

                if (addQuotationViewModel.productDataForSeller.productId.isEmpty()){
                    addQuotation.product_id = "0"
                    addQuotation.product_name = etProductName.text.toString()
                }else{
                    addQuotation.product_id = addQuotationViewModel.productDataForSeller.productId
                }

                addQuotation.regular_price = etProductPrice.text.toString()
                addQuotation.discounted_price = etDiscountPrice.text.toString()
                addQuotation.grand_total = etGrandTotal.text.toString()
                addQuotation.tax_extra = etExtraTax.text.toString()
                addQuotation.qty = etQuantity.text.toString()
                addQuotation.description = etDescription.text.toString()

                addQuotation.warranty = etWarranty.text.toString()
                addQuotation.additional_details = etAdditionalDetails.text.toString()
                addQuotation.terms_conditions = etTerms.text.toString()
                addQuotation.delivery_terms= etDelivery.text.toString()

                addQuotation.offer_id = addQuotationViewModel.productDataForSeller.offerId
                addQuotation.seller_id = Helper().getLoginData(mContext).id
                addQuotation.seller_company_id = addQuotationViewModel.productDataForSeller.companyId
                addQuotation.master_category_id = addQuotationViewModel.productDataForSeller.categoryId

                progressBar.visibility = View.VISIBLE

                addQuotationViewModel.apiCreateQuotation(addQuotation)

            }
        }
    }

    private fun initView() {
        tvTitle.text = getString(R.string.customer_quotation_format)
        iv_back.setOnClickListener(View.OnClickListener { finish() })
        addQuotationViewModel = ViewModelProvider(this).get(AddQuotationViewModel::class.java)
        addQuotationViewModel.productDataForSeller = Gson().fromJson(intent.getStringExtra(IntentKeyEnum.CHAT_PRODUCT_DATA.name).toString(), ProductDataForSeller::class.java)


        if (addQuotationViewModel.productDataForSeller.productName.isNotEmpty()){
            etProductName.setText(addQuotationViewModel.productDataForSeller.productName)
            etProductName.keyListener = null
        }



        addQuotationViewModel.errorMessage.observe(this, androidx.lifecycle.Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


        addQuotationViewModel.quotationAddedLiveData.observe(this, androidx.lifecycle.Observer { quotationData ->

            progressBar.visibility = View.GONE

            addQuotationViewModel.productDataForSeller.quotationId = quotationData.id?:""
            addQuotationViewModel.productDataForSeller.quotationRequest = addQuotationViewModel.addedQuotationRequest
            val intent = Intent(mContext, ChatListActivity::class.java)
            intent.putExtra(IntentKeyEnum.CHAT_PRODUCT_DATA_WITH_QUOTATION.name, Gson().toJson(addQuotationViewModel.productDataForSeller))
            startActivity(intent)
            finish()
        })




    }




}