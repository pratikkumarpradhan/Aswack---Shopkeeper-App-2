package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.adapters.SellerNotificationListAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.NotificationListViewModel
import kotlinx.android.synthetic.main.activity_my_offer.*
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.activity_notification.progressBar
import kotlinx.android.synthetic.main.header_title.*
import kotlinx.android.synthetic.main.usage_product_offer_notification.*

class SellerNotificationListActivity : AppCompatActivity(),
    SellerNotificationListAdapter.ICustomListListener {
    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var notificationListViewModel: NotificationListViewModel

    /**
     * Adapter
     */
    lateinit var sellerNotificationListAdapter : SellerNotificationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        mContext = this@SellerNotificationListActivity
        initView()

        clickListener()
    }

    private fun clickListener() {

        tvAddNotification.setOnClickListener {
            val intent = Intent(mContext, AddNotificationActivity::class.java)
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, notificationListViewModel.companyId)
            intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, notificationListViewModel.mainCatId)
            intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, notificationListViewModel.packageId)
            startActivity(intent)
        }

        iv_back.setOnClickListener {
            finish()
        }

    }

    private fun initView() {

        notificationListViewModel = ViewModelProvider(this).get(NotificationListViewModel::class.java)

        notificationListViewModel.companyId = intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()
        notificationListViewModel.packageId = intent.getStringExtra(IntentKeyEnum.PACKAGE_ID.name).toString()
        notificationListViewModel.mainCatId = intent.getStringExtra(IntentKeyEnum.MAIN_CAT_ID.name).toString()
        progressBar.visibility = View.VISIBLE

        notificationListViewModel.sellerNotificationListLiveData.observe(this, Observer { notificationList ->
            sellerNotificationListAdapter = SellerNotificationListAdapter(mContext, this)
            rvNotification.adapter = sellerNotificationListAdapter
            sellerNotificationListAdapter.setList(notificationList)
            tvTotalAddedValue.text = notificationList.size.toString()

            val productList = ProductList()
            productList.seller_id = Helper().getLoginData(mContext).id
            productList.seller_company_id = notificationListViewModel.companyId
            productList.package_purchased_id = notificationListViewModel.packageId
            productList.type = "2"
            notificationListViewModel.getUsageData(productList)

        })


        notificationListViewModel.packageUsedLiveData.observe(this, androidx.lifecycle.Observer { packageUsage ->

            progressBar.visibility = View.GONE
            tvRemainingValue.text = packageUsage.package_pending
            packageUsage.end_date?.let { tvDaysRemainingValue.text = Helper().getDaysRemaining(it)
            }

        })




        notificationListViewModel.errorMessage.observe(this, Observer { error ->
            tvTotalAddedValue.text = "0"

            val productList = ProductList()
            productList.seller_id = Helper().getLoginData(mContext).id
            productList.seller_company_id = notificationListViewModel.companyId
            productList.package_purchased_id = notificationListViewModel.packageId
            productList.type = "2"
            notificationListViewModel.getUsageData(productList)
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


        notificationListViewModel.errorOfUsageMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


        tvTitle.setText(getString(R.string.notification))
        rvNotification.layoutManager = LinearLayoutManager(mContext)
        rvNotification.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE
        val login = Login()
        login.seller_id = Helper().getLoginData(mContext).id
        login.seller_company_id = notificationListViewModel.companyId
        login.package_purchased_id= notificationListViewModel.packageId
        login.master_category_id = notificationListViewModel.mainCatId

        notificationListViewModel.apiGetNotificationList(login)
    }
}