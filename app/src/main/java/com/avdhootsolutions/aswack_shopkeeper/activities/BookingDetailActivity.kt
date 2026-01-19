package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.enums.BookingStatusEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.BookingList
import com.avdhootsolutions.aswack_shopkeeper.models.BookingStatusRequest
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.BookingDetailViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_booking_details.*
import kotlinx.android.synthetic.main.header_title.*


class BookingDetailActivity : AppCompatActivity(){

    lateinit var bookingDetailViewModel: BookingDetailViewModel
    lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)
        mContext = this@BookingDetailActivity
        init()
        clickListener()
    }

    private fun init() {

        bookingDetailViewModel = ViewModelProvider(this).get(BookingDetailViewModel::class.java)
        bookingDetailViewModel.bookingDetails = Gson().fromJson(
            intent.getStringExtra(IntentKeyEnum.BOOKING_DETAIL.name).toString(),
            BookingList::class.java
        )
        tvTitle.text = resources.getString(R.string.booking_detail)

        setBookingDataInUI()

        bookingDetailViewModel.successMessageLiveData.observe(this,
            androidx.lifecycle.Observer { isScuccess ->
                progressBar.visibility = View.GONE
                if (isScuccess) {
                    Toast.makeText(
                        mContext,
                        resources.getString(R.string.booking_status_changed),
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()

                }
            })

    }

    /**
     * Set booking data in UI
     */
    private fun setBookingDataInUI() {
        tvBookingCodeValue.text = bookingDetailViewModel.bookingDetails.appointment_code
        tvCustomerName.text = bookingDetailViewModel.bookingDetails.user_name
        tvModelNameValue.text = bookingDetailViewModel.bookingDetails.vehicle_model_name
        tvBrandNameValue.text = bookingDetailViewModel.bookingDetails.vehicle_company_name
        tvTypeNameValue.text = bookingDetailViewModel.bookingDetails.vehicle_type_name
        tvVehicleNumValue.text = bookingDetailViewModel.bookingDetails.vehicle_number
        tvMobile.text = bookingDetailViewModel.bookingDetails.user_mobile
        tvAppoDateValue.text = bookingDetailViewModel.bookingDetails.appointment_date
        tvAppoTimeValue.text = bookingDetailViewModel.bookingDetails.appointment_time

        when (bookingDetailViewModel.bookingDetails.status) {
            BookingStatusEnum.PENDING.ordinal.toString() -> {
                tvBookingStatusValue.text = resources.getString(R.string.pending)
            }
            BookingStatusEnum.ACCEPTED.ordinal.toString() -> {
                tvBookingStatusValue.text = resources.getString(R.string.accepted)
                tvAccept.visibility = View.GONE
                tvReject.visibility = View.GONE
            }
            BookingStatusEnum.REJECT.ordinal.toString() -> {
                tvBookingStatusValue.text = resources.getString(R.string.rejected)
                tvAccept.visibility = View.GONE
                tvReject.visibility = View.GONE
            }
        }
    }


    /**
     * Click events
     */
    private fun clickListener() {

        iv_back.setOnClickListener {
            finish()
        }

        tvAccept.setOnClickListener {

            val bookingStatusRequest = BookingStatusRequest()
            bookingStatusRequest.appointment_id = bookingDetailViewModel.bookingDetails.id
            bookingStatusRequest.code = bookingDetailViewModel.bookingDetails.appointment_code
            bookingStatusRequest.status = "2"
            bookingStatusRequest.user_id = bookingDetailViewModel.bookingDetails.user_id
            bookingStatusRequest.seller_name = Helper().getLoginData(mContext).name

            progressBar.visibility = View.VISIBLE
            bookingDetailViewModel.changeBookingStatus(bookingStatusRequest)
        }

        tvReject.setOnClickListener {

            tvSend.visibility = View.VISIBLE
            etRejectReason.visibility = View.VISIBLE

        }

        tvSend.setOnClickListener {
            val bookingStatusRequest = BookingStatusRequest()
            bookingStatusRequest.appointment_id = bookingDetailViewModel.bookingDetails.id
            bookingStatusRequest.code = bookingDetailViewModel.bookingDetails.appointment_code
            bookingStatusRequest.status = "3"
            bookingStatusRequest.rejected_reason = etRejectReason.text.toString()
            bookingStatusRequest.user_id = bookingDetailViewModel.bookingDetails.user_id
            bookingStatusRequest.seller_name = Helper().getLoginData(mContext).name

            progressBar.visibility = View.VISIBLE
            bookingDetailViewModel.changeBookingStatus(bookingStatusRequest)
        }

    }
}