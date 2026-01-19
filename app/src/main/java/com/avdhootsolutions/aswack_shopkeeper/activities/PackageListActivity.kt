package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.CustomPackageListAdapter

import com.avdhootsolutions.aswack_shopkeeper.adapters.RegularPackageListAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.PackageListViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_package_list.*
import kotlinx.android.synthetic.main.activity_package_list.progressBar
import kotlinx.android.synthetic.main.activity_package_list.tvSendCustomRequest
import kotlinx.android.synthetic.main.activity_regular_package_details.*

import kotlinx.android.synthetic.main.header_title.*
import java.io.File
import java.util.ArrayList

class PackageListActivity : AppCompatActivity(), RegularPackageListAdapter.ICustomListListener,
    CustomPackageListAdapter.ICustomListListener {

    /**
     * View model
     */
    private lateinit var packageListViewModel: PackageListViewModel

    lateinit var mContext: Context

    /**
     * Adapter for regular packages
     */
    lateinit var regularPackageListAdapter: RegularPackageListAdapter

    /**
     * Adapter for custom packages
     */
    lateinit var customPackageListAdapter: CustomPackageListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_list)
        mContext = this@PackageListActivity

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


        tvSendCustomRequest.setOnClickListener {

            progressBar.visibility = View.VISIBLE
            packageListViewModel.companyRegister.seller_id = Helper().getLoginData(mContext).id

            packageListViewModel.companyRegister.package_id = ""
            packageListViewModel.companyRegister.custom_request = "1"
            packageListViewModel.companyRegister.offer = ""
            packageListViewModel.companyRegister.post = ""
            packageListViewModel.companyRegister.notification = ""
            packageListViewModel.companyRegister.duration = ""
            if (packageListViewModel.directPackage) {
                packageListViewModel.companyRegister.company_id = packageListViewModel.companyid
                packageListViewModel.apiInsertCustomPackage(packageListViewModel.companyRegister)
            } else {

                //    packageDetailViewModel.apiCompanyRegister(packageDetailViewModel.companyRegister)

                if (packageListViewModel.filePath.isNotEmpty()) {
                    val file = File(packageListViewModel.filePath)
                    packageListViewModel.apiCompanyRegisterWithFile(
                        packageListViewModel.companyRegister,
                        file,
                        mContext)

                } else {
                    packageListViewModel.apiCompanyRegisterWithFile(
                        packageListViewModel.companyRegister,
                        null,
                        mContext)
                }

            }


        }


        tvRegular.setOnClickListener {

            tvRegular.background = resources.getDrawable(R.drawable.bg_orange)
            tvOfferForMe.background = null
            tvOfferForMe.setTextColor(resources.getColor(R.color.text_color))
            tvRegular.setTextColor(resources.getColor(R.color.white))

            rvRegularPackage.visibility = View.VISIBLE
            rvCustomPackage.visibility = View.GONE
        }

        tvOfferForMe.setOnClickListener {
            tvOfferForMe.background = resources.getDrawable(R.drawable.bg_orange)
            tvOfferForMe.setTextColor(resources.getColor(R.color.white))
            tvRegular.background = null
            tvRegular.setTextColor(resources.getColor(R.color.text_color))

            rvCustomPackage.visibility = View.VISIBLE
            rvRegularPackage.visibility = View.GONE

        }

    }

    private fun init() {

        rvRegularPackage.layoutManager = LinearLayoutManager(mContext)
        rvRegularPackage.setHasFixedSize(true)

        rvCustomPackage.layoutManager = LinearLayoutManager(mContext)
        rvCustomPackage.setHasFixedSize(true)

        regularPackageListAdapter = RegularPackageListAdapter(mContext, this)
        customPackageListAdapter = CustomPackageListAdapter(mContext, this)

        packageListViewModel = ViewModelProvider(this).get(PackageListViewModel::class.java)

        packageListViewModel.successCustomPackageRequestMessage.observe(
            this,
            androidx.lifecycle.Observer {
                progressBar.visibility = View.GONE
                val intent = Intent(mContext, ThankYouForRegistrationActivity::class.java)
                if (!it.custom_request_code.isNullOrEmpty()){
                    intent.putExtra(IntentKeyEnum.CUSTOM_REQUEST_CODE.name, it.custom_request_code)
                }
                startActivity(intent)

            })


        packageListViewModel.successCustomPackagePurchasedLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                progressBar.visibility = View.GONE

                val intent = Intent(this, ThankYouForRegistrationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(IntentKeyEnum.PACKAGE_PURCHASED.name, true)
                startActivity(intent)
                finish()

            })



        if (intent.hasExtra(IntentKeyEnum.DIRECT_PACKAGE.name)) {

            val dialogHelper = DialogHelper(mContext)
            dialogHelper.showPaymentPendingDialog()

            packageListViewModel.directPackage = true
            packageListViewModel.companyid =
                intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()
        } else {
            packageListViewModel.companyRegister = Gson().fromJson(
                intent.getStringExtra(IntentKeyEnum.COMPANY_REGISTRATION_DATA.name),
                CompanyRegister::class.java
            )
            packageListViewModel.filePath =
                intent.getStringExtra(IntentKeyEnum.FILE_PATH.name).toString()

        }



        packageListViewModel.regularPackageDataListLiveData.observe(this, Observer { packageList ->
            progressBar.visibility = View.GONE

            regularPackageListAdapter.setList(packageList)
            rvRegularPackage.adapter = regularPackageListAdapter

            if (packageListViewModel.directPackage){
                progressBar.visibility = View.VISIBLE

                val packageRequest = PackageRequest()
                packageRequest.seller_id = Helper().getLoginData(mContext).id
                packageRequest.seller_company_id = packageListViewModel.companyid
                packageListViewModel.getCustomPackageList(packageRequest)

            }
        })

        packageListViewModel.customPackageDataListLiveData.observe(this, Observer { customPackageList ->
            progressBar.visibility = View.GONE

            customPackageListAdapter.setList(customPackageList)
            rvCustomPackage.adapter = customPackageListAdapter
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
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })





    }


    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE

        val packageItem = PackageRequest()
        packageItem.pkdid = ""
        packageListViewModel.getRegularPackageList(packageItem)
    }

    override fun onPackageSelected(packageDataItem: PackageData, position: Int) {

        val intent = Intent(mContext, PackageDetailsActivity::class.java)

        if (packageListViewModel.directPackage) {
            intent.putExtra(IntentKeyEnum.DIRECT_PACKAGE.name, "1")
            intent.putExtra(IntentKeyEnum.COMPANY_ID.name, packageListViewModel.companyid)
        } else {
            intent.putExtra(
                IntentKeyEnum.COMPANY_REGISTRATION_DATA.name,
                Gson().toJson(packageListViewModel.companyRegister)
            )
            intent.putExtra(IntentKeyEnum.FILE_PATH.name, packageListViewModel.filePath)
        }

        intent.putExtra(IntentKeyEnum.PACKAGE_DATA.name, Gson().toJson(packageDataItem))

        startActivity(intent)


    }

    /**
     * When click on buy now button, Call api
     * @param customPackageData model class
     * @param position position of the list
     */
    override fun onCustomPackageBuy(customPackageData: CustomPackageData, position: Int) {

        progressBar.visibility = View.VISIBLE
        val packageRequest = PackageSubscriptionRequest()

        packageRequest.seller_id = Helper().getLoginData(mContext).id
        packageRequest.package_id = customPackageData.id
        packageRequest.company_id = packageListViewModel.companyid
        packageRequest.custom_request = "1"
        packageRequest.offer = customPackageData.offer
        packageRequest.post = customPackageData.post
        packageRequest.notification = customPackageData.notification
        packageRequest.duration = customPackageData.duration
        packageRequest.type = customPackageData.status

        packageListViewModel.apiSelectCustomPackageSubscription(packageRequest)

    }
}