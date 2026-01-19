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
import com.avdhootsolutions.aswack_shopkeeper.adapters.CourierListAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.BookingList
import com.avdhootsolutions.aswack_shopkeeper.models.CourierDetails
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.BookingListViewModel
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.CourierListViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_booking_list.*
import kotlinx.android.synthetic.main.header_title.*

class CourierListActivity : AppCompatActivity(), CourierListAdapter.ICustomListListener {
    lateinit var mContext: Context


    /**
     * View model
     */
    lateinit var courierListViewModel: CourierListViewModel


    /**
     * Adapter for courier list
     */
    lateinit var courierListAdapter: CourierListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_list)
        mContext = this@CourierListActivity
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

        tvTitle.text = getString(R.string.courier_list)

        rvBooking.layoutManager = LinearLayoutManager(mContext)
        rvBooking.setHasFixedSize(true)

        courierListViewModel = ViewModelProvider(this).get(CourierListViewModel::class.java)

        courierListViewModel.companyId = intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()

        courierListViewModel.courierListLiveData.observe(this, Observer { courierList ->
            progressBar.visibility = View.GONE
            courierListAdapter = CourierListAdapter(mContext, this)
            rvBooking.adapter = courierListAdapter
            courierListAdapter.setList(courierList)

        })


        courierListViewModel.errorMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


    }


    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE

        val productList = ProductList()
        productList.seller_id = Helper().getLoginData(mContext).id
        productList.seller_company_id = courierListViewModel.companyId

        courierListViewModel.getCourierInquiryList(productList)
    }


    override fun onCourierSelected(courierDetails: CourierDetails, position: Int) {
        val intent = Intent(mContext, CourierDetailActivity::class.java)
        intent.putExtra(IntentKeyEnum.COURIER_DETAIL.name, Gson().toJson(courierDetails))
        startActivity(intent)
    }
}