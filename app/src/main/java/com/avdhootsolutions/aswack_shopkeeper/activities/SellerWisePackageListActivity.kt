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

import com.avdhootsolutions.aswack_shopkeeper.adapters.RegularPackageListAdapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.SellerWisePackageListAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.UserTypeEnum
import com.avdhootsolutions.aswack_shopkeeper.models.CompanyRegister
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.models.PackageData
import com.avdhootsolutions.aswack_shopkeeper.models.PackageRequest
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.PackageListViewModel
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.SellerWisePackageListViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_purchased_package_list.*
import kotlinx.android.synthetic.main.header_title.*
import java.io.File
import java.util.ArrayList

class SellerWisePackageListActivity : AppCompatActivity(),
    SellerWisePackageListAdapter.ICustomListListener {

    /**
     * View model
     */
    lateinit var packageListViewModel: SellerWisePackageListViewModel

    lateinit var mContext: Context

    /**
     * Adapter for our services
     */
    lateinit var packageListAdapter: SellerWisePackageListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchased_package_list)
        mContext = this@SellerWisePackageListActivity

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


//        tvSendCustomRequest.setOnClickListener {
//
//            progressBar.visibility = View.VISIBLE
//
//            packageListViewModel.companyRegister.seller_id = Helper().getLoginData(mContext).id
//
//            packageListViewModel.companyRegister.package_id = ""
//            packageListViewModel.companyRegister.custom_request = "1"
//            packageListViewModel.companyRegister.offer = ""
//            packageListViewModel.companyRegister.post = ""
//            packageListViewModel.companyRegister.notification = ""
//            packageListViewModel.companyRegister.duration = ""
//            if (packageListViewModel.directPackage){
//                packageListViewModel.companyRegister.company_id = packageListViewModel.companyid
//                packageListViewModel.apiSelectPackage(packageListViewModel.companyRegister)
//            }else{
//
//
//                //    packageDetailViewModel.apiCompanyRegister(packageDetailViewModel.companyRegister)
//
//                val file = File(packageListViewModel.filePath)
//
//                packageListViewModel.apiCompanyRegisterWithFile(packageListViewModel.companyRegister, file, mContext)
//            }
//
//
//
//        }


//        tvRegular.setOnClickListener {
//
//            tvRegular.background = resources.getDrawable(R.drawable.bg_orange)
//            tvOfferForMe.background = null
//            tvOfferForMe.setTextColor(resources.getColor(R.color.text_color))
//            tvRegular.setTextColor(resources.getColor(R.color.white))
//
//            packageListViewModel.regularPackageDataListLiveData.value?.let { it1 ->
//                packageListAdapter.setList(it1)
//            }
//
//        }

//        tvOfferForMe.setOnClickListener {
//            tvOfferForMe.background = resources.getDrawable(R.drawable.bg_orange)
//            tvOfferForMe.setTextColor(resources.getColor(R.color.white))
//            tvRegular.background = null
//            tvRegular.setTextColor(resources.getColor(R.color.text_color))
//
//            packageListAdapter.setList(ArrayList<PackageData>())
//
//        }

    }

    private fun init() {
        packageListAdapter = SellerWisePackageListAdapter(mContext, this)
        packageListViewModel = ViewModelProvider(this).get(SellerWisePackageListViewModel::class.java)
        packageListViewModel.companyid = intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()

        packageListViewModel.successPackageMessage.observe(this, androidx.lifecycle.Observer { success ->

            Toast.makeText(mContext, "Thank you for purchasing", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
            finish()

        })


        packageListViewModel.regularPackageDataListLiveData.observe(this, Observer { packageList ->
            progressBar.visibility = View.GONE

            packageListAdapter.setList(packageList)
            rvPackage.adapter = packageListAdapter
        })

        tvTitle.text = "View Packages"
        packageListViewModel.successMessage.observe(this, androidx.lifecycle.Observer { success ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, success, Toast.LENGTH_SHORT).show()

            val intent = Intent(this, ThankYouForRegistrationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
            finish()

        })


        packageListViewModel.errorMessage.observe(this, Observer { error ->

            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


        rvPackage.layoutManager = LinearLayoutManager(mContext)
        rvPackage.setHasFixedSize(true)


    }


    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE

        val packageRequest = PackageRequest()
        packageRequest.type = UserTypeEnum.SELLER.ordinal.toString()
        packageRequest.seller_id = Helper().getLoginData(mContext).id
        packageRequest.seller_company_id = packageListViewModel.companyid
        packageListViewModel.getPackageList(packageRequest)
    }

    override fun onPackageSelected(packageDataItem: PackageData, position: Int) {

        val intent = Intent(mContext, SellerWisePackageDetailsActivity::class.java)
        intent.putExtra(IntentKeyEnum.DIRECT_PACKAGE.name, "1")
        intent.putExtra(IntentKeyEnum.COMPANY_ID.name, packageListViewModel.companyid)
        intent.putExtra(IntentKeyEnum.PACKAGE_DATA.name, Gson().toJson(packageDataItem))

        startActivity(intent)

//        packageListViewModel.companyRegister.package_id = packageItem.pkd_id
//        packageListViewModel.companyRegister.offer = packageItem.offer
//        packageListViewModel.companyRegister.post = packageItem.post
//        packageListViewModel.companyRegister.notification = packageItem.notification
//        packageListViewModel.companyRegister.duration = packageItem.duration[0]
//        packageListViewModel.apiCompanyRegister(packageListViewModel.companyRegister)
    }

    override fun onStatusSelected(packageDataItem: PackageData, position: Int) {


    }
}