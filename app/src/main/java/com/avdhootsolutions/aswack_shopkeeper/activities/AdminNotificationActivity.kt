package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.activities.AddNotificationActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.adapters.AdminNotificationListAdapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.NotificationAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.NotificationListViewModel
import kotlinx.android.synthetic.main.activity_notification_list.*
import kotlinx.android.synthetic.main.header_title.*

class AdminNotificationActivity : AppCompatActivity(),
    AdminNotificationListAdapter.ICustomListListener {
    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var notificationListViewModel: NotificationListViewModel

    lateinit var adminNotificationListAdapter: AdminNotificationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)
        mContext = this@AdminNotificationActivity
        initView()

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

    private fun initView() {

        notificationListViewModel = ViewModelProvider(this).get(NotificationListViewModel::class.java)

        progressBar.visibility = View.VISIBLE

        notificationListViewModel.adminNotificationListLiveData.observe(this, Observer { adminNotificationList ->
            progressBar.visibility = View.GONE

            adminNotificationListAdapter = AdminNotificationListAdapter(mContext, this)
            rvNotification.adapter = adminNotificationListAdapter
            adminNotificationListAdapter.setList(adminNotificationList)
        })


        notificationListViewModel.errorMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


        tvTitle.text = getString(R.string.admin_notification)

        rvNotification.layoutManager = LinearLayoutManager(mContext)
        rvNotification.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE
        val login = Login()
        login.seller_id = Helper().getLoginData(mContext).id
        notificationListViewModel.apiGetAdminNotificationList(login)
    }
}