package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.models.OrderRequest
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.OrderListAdapter

import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.OrderListViewModel
import kotlinx.android.synthetic.main.activity_booking_list.*
import kotlinx.android.synthetic.main.activity_package_list.*
import kotlinx.android.synthetic.main.activity_package_list.progressBar

import kotlinx.android.synthetic.main.header_title.*

class OrderListActivity : AppCompatActivity(), OrderListAdapter.ICustomListListener {

    /**
     * View model
     */
    lateinit var orderListViewModel: OrderListViewModel

    lateinit var mContext: Context

    lateinit var orderListAdapter: OrderListAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_list)
        mContext = this@OrderListActivity

        init()

        clickListener()


    }


    /**
     * Click events
     */
    private fun clickListener() {

        iv_back.setOnClickListener {
            finish()
        }
    }

    private fun init() {
        orderListViewModel = ViewModelProvider(this).get(OrderListViewModel::class.java)

        orderListViewModel.companyId = intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()

        orderListViewModel.orderListLiveData.observe(this, androidx.lifecycle.Observer { orderList ->
            progressBar.visibility = View.GONE
            orderListAdapter = OrderListAdapter(mContext, this)
            orderListAdapter.setList(orderList)
            rvBooking.adapter = orderListAdapter
        })



        tvTitle.text = "Order List"

        orderListViewModel.errorMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


        rvBooking.layoutManager = LinearLayoutManager(mContext)
        rvBooking.setHasFixedSize(true)


    }


    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE

        val productList = ProductList()
        productList.seller_id = Helper().getLoginData(mContext).id
        productList.seller_company_id = orderListViewModel.companyId
        orderListViewModel.getOrderList(productList)
    }

    override fun onOrderSelected(orderRequest: OrderRequest, position: Int) {
        val intent = Intent(mContext, OrderDetailsActivity::class.java)
        intent.putExtra(IntentKeyEnum.ORDER_ID.name, orderRequest.id)
        intent.putExtra(IntentKeyEnum.COMPANY_ID.name, orderRequest.seller_company_id)
        startActivity(intent)
    }

}