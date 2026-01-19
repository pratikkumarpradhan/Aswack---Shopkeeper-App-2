package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.NotificationSendData
import com.avdhootsolutions.aswack_shopkeeper.models.RFQList
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.RFQDetailViewModel
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.RFQListViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_rfqdetail.*
import kotlinx.android.synthetic.main.header_title.*

class RFQDetailActivity : AppCompatActivity() {
    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var rfqDetailViewModel: RFQDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rfqdetail)
        mContext = this@RFQDetailActivity
        initView()

        clickListeners()


    }

    private fun clickListeners() {
        iv_back.setOnClickListener(View.OnClickListener { finish() })

        tvChatWithCustomer.setOnClickListener(View.OnClickListener {

            val notificationSendData = NotificationSendData()
            notificationSendData.rfq_id = rfqDetailViewModel.rfqDetails.id
            notificationSendData.seller_id = rfqDetailViewModel.rfqDetails.seller_id
            notificationSendData.company_id = rfqDetailViewModel.rfqDetails.seller_company_id

            rfqDetailViewModel.sendNotificationToUserForRFQResponse(notificationSendData)

            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(IntentKeyEnum.RFQ_DATA.name, Gson().toJson(rfqDetailViewModel.rfqDetails))
            startActivity(intent)
        })

        tvAddQuotation.setOnClickListener {
            val intent = Intent(mContext, AddQuotationActivity::class.java)
            intent.putExtra(IntentKeyEnum.CHAT_PRODUCT_DATA.name, Gson().toJson(rfqDetailViewModel.rfqDetails.productDataForSeller))
            startActivity(intent)
            finish()
        }
        tvViewQuotation.setOnClickListener {
            val intent = Intent(mContext, QuotationDetailsActivity::class.java)
            intent.putExtra(IntentKeyEnum.CHAT_PRODUCT_DATA.name, Gson().toJson(rfqDetailViewModel.rfqDetails.productDataForSeller))
            startActivity(intent)
        }
    }

    private fun initView() {
        tvTitle.text = getString(R.string.requirement)


        rfqDetailViewModel = ViewModelProvider(this).get(RFQDetailViewModel::class.java)
        rfqDetailViewModel.rfqDetails = Gson().fromJson(intent.getStringExtra(IntentKeyEnum.RFQ_DATA.name).toString(), RFQList::class.java)


        if (rfqDetailViewModel.rfqDetails.isChatInitiated){
            tvCanAddQuotationAfterChat.visibility = View.GONE

            if (rfqDetailViewModel.rfqDetails.productDataForSeller.quotationId.isNullOrEmpty()){
                tvAddQuotation.visibility = View.VISIBLE
                tvViewQuotation.visibility = View.GONE
            }else{
                tvAddQuotation.visibility = View.GONE
                tvViewQuotation.visibility = View.VISIBLE
            }

        }else{
            tvCanAddQuotationAfterChat.visibility = View.VISIBLE
            tvAddQuotation.visibility = View.GONE
            tvViewQuotation.visibility = View.GONE
        }
        
        tvCustomerNameValue.text = rfqDetailViewModel.rfqDetails.user_name
        tvInquiryValue.text = rfqDetailViewModel.rfqDetails.rfq_code
        tvProductNameValue.text = rfqDetailViewModel.rfqDetails.product_name

        if (rfqDetailViewModel.rfqDetails.serial_number.isNullOrEmpty()){
            tvSerialNo.visibility = View.GONE
            tvSerialNoValue.visibility = View.GONE
        }else{
            tvSerialNoValue.text = rfqDetailViewModel.rfqDetails.serial_number
        }

        tvQtyValue.text = rfqDetailViewModel.rfqDetails.qty
        tvModelNameValue.text = rfqDetailViewModel.rfqDetails.vehicle_model_name
        tvDesInfo.text = rfqDetailViewModel.rfqDetails.description
        tvPostedOn.text = rfqDetailViewModel.rfqDetails.created_datetime
        tvBrandTypeValue.text = rfqDetailViewModel.rfqDetails.vehicle_brand_name + ", " +
                rfqDetailViewModel.rfqDetails.vehicle_type_name

        if (rfqDetailViewModel.rfqDetails.product_condition == "0"){
            tvConditionValue.text = "Used"
        }else if (rfqDetailViewModel.rfqDetails.product_condition == "1"){
            tvConditionValue.text = "New"
        }


        tvVehicleYearValue.text = rfqDetailViewModel.rfqDetails.vehicle_year_name
        tvFuelValue.text = rfqDetailViewModel.rfqDetails.vehicle_fuel_name
        tvPostedOn.text = rfqDetailViewModel.rfqDetails.created_datetime

        if (rfqDetailViewModel.rfqDetails.status == Helper.RFQ_CLOSE){
            tvChatWithCustomer.visibility = View.GONE
            tvStatusValue.text = resources.getString(R.string.closed_rfq)
            tvStatusValue.setTextColor(Color.parseColor("#dc2c1f"))
        }else{
            tvStatusValue.text = resources.getString(R.string.active)
            tvStatusValue.setTextColor(Color.parseColor("#008000"))
        }


    }
}