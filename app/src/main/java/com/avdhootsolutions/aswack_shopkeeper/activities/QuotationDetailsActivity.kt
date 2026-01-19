package com.avdhootsolutions.aswack_shopkeeper.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import android.content.Context
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.YesNoEnum
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.QuotationDetailViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_quotation_details.*
import kotlinx.android.synthetic.main.header_title.*
import kotlinx.android.synthetic.main.header_title.tvTitle
import kotlinx.android.synthetic.main.row_rating.*
import java.io.*
import java.util.*

class QuotationDetailsActivity : AppCompatActivity(){

    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var quotationDetailViewModel: QuotationDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotation_details)
        mContext = this@QuotationDetailsActivity
        initView()

        clickListener()
    }

    /**
     * Click events
     */
    private fun clickListener() {

    }

    private fun initView() {
        tvTitle.text = getString(R.string.view_quotation)
        iv_back.setOnClickListener(View.OnClickListener { finish() })
        quotationDetailViewModel = ViewModelProvider(this).get(QuotationDetailViewModel::class.java)
        quotationDetailViewModel.productDataForSeller = Gson().fromJson(intent.getStringExtra(IntentKeyEnum.CHAT_PRODUCT_DATA.name).toString(), ProductDataForSeller::class.java)


        quotationDetailViewModel.quotationDetailLiveData.observe(this, androidx.lifecycle.Observer { quotationDetails ->
            progressBar.visibility = View.GONE

            tvUserName.text = quotationDetails.user_name
            tvCompanyNameValue.text = quotationDetails.seller_company_name
            tvCategoryValue.text = quotationDetails.master_category_name


            tvProductName.text = quotationDetails.product_name
            tvOriginalPriceValue.text = Helper().getCurrencySymbol(mContext) +quotationDetails.regular_price
            tvDiscountPriceValue.text = Helper().getCurrencySymbol(mContext) +quotationDetails.discounted_price
            tvTaxValue.text = quotationDetails.tax_extra
            tvQtyValue.text = quotationDetails.qty
            tvGrandTotalValue.text = quotationDetails.grand_total

            tvDescriptionValue.text = quotationDetails.description
            tvTermsValue.text = quotationDetails.terms_conditions
            tvAdditionalDetailsValue.text = quotationDetails.additional_details
            tvDeliveryValue.text = quotationDetails.delivery_terms
            tvQuotationCodeValue.text = quotationDetails.quotation_code

            if (quotationDetails.payment_done == YesNoEnum.YES.ordinal.toString()){
                tvPaymentDoneValue.text = resources.getString(R.string.yes)
            }else{
                tvPaymentDoneValue.text = resources.getString(R.string.no)
            }
        })

        quotationDetailViewModel.errorMessage.observe(this, androidx.lifecycle.Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })
    }


    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE
        val login = Login()
        login.seller_id = Helper().getLoginData(mContext).id
        login.seller_company_id = quotationDetailViewModel.productDataForSeller.companyId
        login.master_category_id = quotationDetailViewModel.productDataForSeller.categoryId
        login.id =  quotationDetailViewModel.productDataForSeller.quotationId

        quotationDetailViewModel.apiGetQuotationDetails(login)

    }




}