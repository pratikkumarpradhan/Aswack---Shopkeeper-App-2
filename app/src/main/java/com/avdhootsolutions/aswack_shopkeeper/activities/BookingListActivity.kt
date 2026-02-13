package com.avdhootsolutions.aswack_shopkeeper.activities


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.BookingListAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.BookingList
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.BookingListViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_booking_list.*
import kotlinx.android.synthetic.main.header_title.*

class BookingListActivity : AppCompatActivity(), BookingListAdapter.ICustomListListener {
    lateinit var mContext: Context


    /**
     * View model
     */
    lateinit var bookingListViewModel: BookingListViewModel


    /**
     * Adapter for booking list
     */
    lateinit var bookingListAdapter: BookingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_list)
        mContext = this@BookingListActivity
        initView()

        clickListener()
    }

    /**
     * All click events
     */
    private fun clickListener() {
        iv_back.setOnClickListener({ finish() })
    }

    private fun initView() {

        tvTitle.text = getString(R.string.booking_list)

        rvBooking.layoutManager = LinearLayoutManager(mContext)
        rvBooking.setHasFixedSize(true)

        bookingListViewModel = ViewModelProvider(this).get(BookingListViewModel::class.java)

        bookingListViewModel.bookingListLiveData.observe(this, Observer { bookingList ->
            progressBar.visibility = View.GONE
            bookingListAdapter = BookingListAdapter(mContext, this)
            rvBooking.adapter = bookingListAdapter
            bookingListAdapter.setList(bookingList)

        })


        bookingListViewModel.errorMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


    }


    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE

        val loginData = Helper().getLoginData(mContext)
        val login = Login()
        // Use id if available, otherwise use mobile number or seller_id
        login.seller_id = loginData.id ?: loginData.seller_id ?: loginData.mobile
        
        // Only make API call if seller_id is not null or empty
        if (!login.seller_id.isNullOrEmpty()) {
            bookingListViewModel.getBookingList(login)
        } else {
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, "Please login again", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onBookingSelected(bookingList: BookingList, position: Int) {
        val intent = Intent(mContext, BookingDetailActivity::class.java)
        intent.putExtra(IntentKeyEnum.BOOKING_DETAIL.name, Gson().toJson(bookingList))
        startActivity(intent)
    }
}