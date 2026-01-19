package com.avdhootsolutions.aswack_shopkeeper.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.YesNoEnum
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.OrderDetailViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_order_detail.*

import kotlinx.android.synthetic.main.header_title.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailsActivity : AppCompatActivity(){

    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var orderDetailViewModel: OrderDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        mContext = this@OrderDetailsActivity
        initView()

        clickListener()
    }

    /**
     * Click events
     */
    private fun clickListener() {

    }

    private fun initView() {
        tvTitle.text = getString(R.string.order_detail)
        iv_back.setOnClickListener(View.OnClickListener { finish() })
        orderDetailViewModel = ViewModelProvider(this).get(OrderDetailViewModel::class.java)
        orderDetailViewModel.orderId = intent.getStringExtra(IntentKeyEnum.ORDER_ID.name).toString()
        orderDetailViewModel.companyId = intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()

        orderDetailViewModel.orderDetailLiveData.observe(this, androidx.lifecycle.Observer { orderDetails ->

            val login = Login()
            login.seller_id = Helper().getLoginData(mContext).id
            login.seller_company_id = orderDetails.seller_company_id
            login.id =  orderDetails.quotation_id

            orderDetailViewModel.apiGetQuotationDetails(login)

            tvName.text = orderDetails.order_code
           // tvCompanyName.text = orderDetails.seller_company_name

            tvCompanyName.visibility = View.GONE

            tvInvoiceCodeValue.text = orderDetails.invoice_code
            tvDate.text = orderDetails.created_datetime
            tvPaidAmountValue.text = orderDetails.paid_amount

        })


        orderDetailViewModel.quotationDetailLiveData.observe(this, androidx.lifecycle.Observer { quotationDetails ->
            progressBar.visibility = View.GONE

            tvUserName.text = quotationDetails.user_name
            tvCategoryValue.text = quotationDetails.master_category_name

            tvProductName.text = quotationDetails.product_name

            tvOriginalPriceValue.text = Helper().getCurrencySymbol(mContext) + quotationDetails.regular_price
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


    }

        override fun onResume() {
            super.onResume()

            progressBar.visibility = View.VISIBLE

            val productList = ProductList()
            productList.seller_id = Helper().getLoginData(mContext).id
            productList.seller_company_id = orderDetailViewModel.companyId
            productList.id = orderDetailViewModel.orderId
            orderDetailViewModel.getOrderDetails(productList)
        }


    }

