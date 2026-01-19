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
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.PurchasedPackageListAdapter

import com.avdhootsolutions.aswack_shopkeeper.adapters.RegularPackageListAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.models.PackageData
import com.avdhootsolutions.aswack_shopkeeper.models.PurchasedPackageData
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.PurchasePackageListViewModel
import kotlinx.android.synthetic.main.activity_package_list.*
import kotlinx.android.synthetic.main.activity_package_list.progressBar
import kotlinx.android.synthetic.main.activity_purchased_package_list.*

import kotlinx.android.synthetic.main.header_title.*

class PurchasedPackageListActivity : AppCompatActivity(),
    PurchasedPackageListAdapter.ICustomListListener {

    /**
     * View model
     */
    lateinit var purchasePackageListViewModel: PurchasePackageListViewModel

    lateinit var mContext: Context

    /**
     * Adapter for our services
     */
    lateinit var packageListAdapter: PurchasedPackageListAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchased_package_list)
        mContext = this@PurchasedPackageListActivity

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
        rvPackage.layoutManager = LinearLayoutManager(mContext)
        rvPackage.setHasFixedSize(true)
        tvTitle.text = resources.getString(R.string.my_packages)

        purchasePackageListViewModel = ViewModelProvider(this).get(PurchasePackageListViewModel::class.java)

      //  purchasePackageListViewModel.companyid = intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()

        purchasePackageListViewModel.regularPackageDataListLiveData.observe(this, Observer { packageList ->
            progressBar.visibility = View.GONE
            packageListAdapter = PurchasedPackageListAdapter(mContext, this)
            packageListAdapter.setList(packageList)
            rvPackage.adapter = packageListAdapter
        })



        purchasePackageListViewModel.errorMessage.observe(this, Observer { error ->
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })





    }


    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE

        val login = Login()
        login.seller_id = Helper().getLoginData(mContext).id
       // login.seller_company_id = purchasePackageListViewModel.companyid
        purchasePackageListViewModel.getPurchasedPackageList(login)
    }

    override fun onPackageSelected(packageDataItem: PurchasedPackageData, position: Int) {

    }
}