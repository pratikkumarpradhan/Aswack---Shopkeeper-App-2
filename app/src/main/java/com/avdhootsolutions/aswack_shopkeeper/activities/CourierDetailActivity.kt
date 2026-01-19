package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.CourierListAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.BookingStatusEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.BookingList
import com.avdhootsolutions.aswack_shopkeeper.models.BookingStatusRequest
import com.avdhootsolutions.aswack_shopkeeper.models.CourierDetails
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.BookingDetailViewModel
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.CourierDetailViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_booking_list.*
import kotlinx.android.synthetic.main.activity_courier_details.*
import kotlinx.android.synthetic.main.activity_courier_details.progressBar
import kotlinx.android.synthetic.main.header_title.*


class CourierDetailActivity : AppCompatActivity(){

    lateinit var courierDetailViewModel: CourierDetailViewModel
    lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courier_details)
        mContext = this@CourierDetailActivity
        init()
        clickListener()
    }

    private fun init() {

        tvChatWithCustomer.visibility = View.GONE

        courierDetailViewModel = ViewModelProvider(this).get(CourierDetailViewModel::class.java)
        courierDetailViewModel.courierDetails = Gson().fromJson(
            intent.getStringExtra(IntentKeyEnum.COURIER_DETAIL.name).toString(),
            CourierDetails::class.java
        )
        tvTitle.text = resources.getString(R.string.courier_detail)

        courierDetailViewModel.courierDetailsLiveData.observe(this, Observer { courierDetails ->
            progressBar.visibility = View.GONE
            setCourierDataInUI()
        })


    }

    /**
     * Set courier data in UI
     */
    private fun setCourierDataInUI() {
        tvItemName.text = courierDetailViewModel.courierDetails.item_name
        tvDescriptionValue.text = courierDetailViewModel.courierDetails.description
        tvWeightValue.text = courierDetailViewModel.courierDetails.weight
        tvDimensionValue.text = courierDetailViewModel.courierDetails.dimensions

       // Set From data
        tvFAddressValue.text = courierDetailViewModel.courierDetails.from_house_no + ", " +
        courierDetailViewModel.courierDetails.from_street_name + " - " +
        courierDetailViewModel.courierDetails.from_pincode
        tvFCountryValue.text = courierDetailViewModel.courierDetails.from_country_name
        tvFStateValue.text = courierDetailViewModel.courierDetails.from_state_name
        tvFCityValue.text = courierDetailViewModel.courierDetails.from_city_name
        tvFPersonNameValue.text = courierDetailViewModel.courierDetails.from_person_name
        tvFMobileValue.text = courierDetailViewModel.courierDetails.from_mobile

        // Set To data
        tvTAddressValue.text = courierDetailViewModel.courierDetails.to_house_no + ", " +
                courierDetailViewModel.courierDetails.to_street_name + " - " +
                courierDetailViewModel.courierDetails.to_pincode
        tvTCountryValue.text = courierDetailViewModel.courierDetails.to_country_name
        tvTStateValue.text = courierDetailViewModel.courierDetails.to_state_name
        tvTCityValue.text = courierDetailViewModel.courierDetails.to_city_name
        tvTPersonNameValue.text = courierDetailViewModel.courierDetails.to_person_name
        tvTMobileValue.text = courierDetailViewModel.courierDetails.to_mobile

    }


    /**
     * Click events
     */
    private fun clickListener() {

        iv_back.setOnClickListener {
            finish()
        }

//        tvChatWithCustomer.setOnClickListener {
//
//            val bookingStatusRequest = BookingStatusRequest()
//            bookingStatusRequest.appointment_id = bookingDetailViewModel.bookingDetails.id
//            bookingStatusRequest.code = bookingDetailViewModel.bookingDetails.appointment_code
//            bookingStatusRequest.status = "2"
//            bookingStatusRequest.user_id = bookingDetailViewModel.bookingDetails.user_id
//            bookingStatusRequest.seller_name = Helper().getLoginData(mContext).name
//
//            progressBar.visibility = View.VISIBLE
//            bookingDetailViewModel.changeBookingStatus(bookingStatusRequest)
//        }
    }


    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE

        val productList = ProductList()
        productList.seller_id = Helper().getLoginData(mContext).id
        productList.seller_company_id = courierDetailViewModel.courierDetails.seller_company_id
        productList.id = courierDetailViewModel.courierDetails.id

        courierDetailViewModel.getCourierInquiryDetails(productList)
    }
}