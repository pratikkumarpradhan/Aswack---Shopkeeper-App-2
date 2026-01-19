package com.avdhootsolutions.aswack_shopkeeper.activities

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.DurationAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.CompanyRegister
import com.avdhootsolutions.aswack_shopkeeper.models.PackageData
import com.avdhootsolutions.aswack_shopkeeper.models.PackageRequest
import com.avdhootsolutions.aswack_shopkeeper.models.PackageSubscriptionRequest
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.PackageDetailViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_regular_package_details.*
import kotlinx.android.synthetic.main.header_title.*
import java.io.File

import com.bumptech.glide.Glide
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject
import java.lang.Exception


class SellerWisePackageDetailsActivity : AppCompatActivity(), PaymentResultWithDataListener,
    ExternalWalletListener, DialogInterface.OnClickListener {

    /**
     * View model
     */
    lateinit var packageDetailViewModel: PackageDetailViewModel

    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regular_package_details)
        mContext = this@SellerWisePackageDetailsActivity
        init()

        clickListener()

    }

    private fun clickListener() {


        iv_back.setOnClickListener {
            finish()
        }

        tvBuyNow.setOnClickListener {

            startPayment()



        }

        tvSendCustomRequest.setOnClickListener {

            progressBar.visibility = View.VISIBLE

            packageDetailViewModel.companyRegister.seller_id = Helper().getLoginData(mContext).id
            packageDetailViewModel.companyRegister.package_id = ""
            packageDetailViewModel.companyRegister.custom_request = "1"
            packageDetailViewModel.companyRegister.offer = ""
            packageDetailViewModel.companyRegister.post = ""
            packageDetailViewModel.companyRegister.notification = ""
            packageDetailViewModel.companyRegister.duration = ""
            if (packageDetailViewModel.directPackage) {
                packageDetailViewModel.companyRegister.company_id = packageDetailViewModel.companyId
                packageDetailViewModel.apiSelectPackage(packageDetailViewModel.companyRegister)
            } else {


                //    packageDetailViewModel.apiCompanyRegister(packageDetailViewModel.companyRegister)

                val file = File(packageDetailViewModel.filePath)

                packageDetailViewModel.apiCompanyRegisterWithFile(
                    packageDetailViewModel.companyRegister,
                    file,
                    mContext
                )
            }


        }

    }


    /**
     * Initialize components
     */
    fun init() {
        packageDetailViewModel = ViewModelProvider(this).get(PackageDetailViewModel::class.java)
        packageDetailViewModel.selectedPackage = Gson().fromJson(
            intent.getStringExtra(IntentKeyEnum.PACKAGE_DATA.name),
            PackageData::class.java
        )

        if (intent.hasExtra(IntentKeyEnum.DIRECT_PACKAGE.name)) {
            packageDetailViewModel.directPackage = true
            packageDetailViewModel.companyId =
                intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()

            packageDetailViewModel.selectedPackage.status?.let {
                tvBuyNow.text = packageDetailViewModel.selectedPackage.status

                cvCustomSendRequest.visibility = View.GONE

            } ?: kotlin.run {
                tvBuyNow.text = resources.getString(R.string.buy_now)
            }


        } else {
            packageDetailViewModel.companyRegister = Gson().fromJson(
                intent.getStringExtra(IntentKeyEnum.COMPANY_REGISTRATION_DATA.name),
                CompanyRegister::class.java
            )
            packageDetailViewModel.filePath =
                intent.getStringExtra(IntentKeyEnum.FILE_PATH.name).toString()

        }


        progressBar.visibility = View.VISIBLE

        val packageItem = PackageRequest()
        packageItem.pkdid = packageDetailViewModel.selectedPackage.id
        packageItem.duration = packageDetailViewModel.selectedPackage.duration[0]
        packageDetailViewModel.selectedDuration = 0
        packageItem.type = packageDetailViewModel.selectedPackage.typeid
        packageItem.seller_id = Helper().getLoginData(mContext).id
        packageItem.seller_company_id = packageDetailViewModel.companyId

        packageDetailViewModel.getPackageDetail(packageItem)

        packageDetailViewModel.packageDetailLiveData.observe(this, Observer { packageDetail ->
            progressBar.visibility = View.GONE
            Glide.with(mContext).load(packageDetail.image).placeholder(R.drawable.placeholder)
                .into(ivpackage)

            packageDetailViewModel.selectedPackage.id = packageDetail.id!!
            tvPnameValue.text = packageDetail.type
            tvPkgTitlevalue.text = packageDetail.title
            tvtaglinevalue.text = packageDetail.description
            tvPacpriceValue.text = Helper().getCurrencySymbol(mContext) + packageDetail.price

            tvTitle.text = packageDetail.title

            val privillage = packageDetail.post + " ${resources.getString(R.string.posts)}," +
                    " ${packageDetail.offer} ${resources.getString(R.string.offers)}" +
                    ", ${packageDetail.notification} ${resources.getString(R.string.notification)}"

            tvprivilegevalue.text = privillage
            tvdescvalue.text = packageDetail.description

            val durationAdapter = DurationAdapter(mContext, packageDetail.duration)
            spDuration.adapter = durationAdapter

            spDuration.setSelection(packageDetailViewModel.selectedDuration)

            spDuration.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long,
                ) {

                    Log.e("positionSelected ", position.toString() + " jhuj")

                    if (position != packageDetailViewModel.selectedDuration) {
                        packageDetailViewModel.selectedDuration = position
                        progressBar.visibility = View.VISIBLE

                        val packageItem = PackageRequest()
                        packageItem.pkdid = ""
                        packageItem.duration = packageDetail.duration[position]
                        packageItem.type = packageDetail.typeid
                        packageItem.seller_id = Helper().getLoginData(mContext).id
                        packageItem.seller_company_id = packageDetailViewModel.companyId

                        packageDetailViewModel.getPackageDetail(packageItem)
                    }


                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }


        })


        packageDetailViewModel.successRegisterLiveData.observe(
            this,
            androidx.lifecycle.Observer { companyRegister ->

                progressBar.visibility = View.GONE

                Toast.makeText(mContext, "Request has been sent", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, ThankYouForRegistrationActivity::class.java)
                if (!companyRegister.custom_request_code.isNullOrEmpty()) {
                    intent.putExtra(
                        IntentKeyEnum.CUSTOM_REQUEST_CODE.name,
                        companyRegister.custom_request_code
                    )
                }

                intent.putExtra(
                    IntentKeyEnum.PACKAGE_PURCHASED.name,
                    ""
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent)
                finish()

            })


        packageDetailViewModel.successPackagePurchasedLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                val intent = Intent(this, ThankYouForRegistrationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(IntentKeyEnum.PACKAGE_PURCHASED.name, true)
                startActivity(intent)
                finish()

            })


        packageDetailViewModel.successCustomPackageRequestMessage.observe(
            this,
            androidx.lifecycle.Observer {
                progressBar.visibility = View.GONE
                val intent = Intent(mContext, ThankYouForRegistrationActivity::class.java)
                if (!it.custom_request_code.isNullOrEmpty()) {
                    intent.putExtra(IntentKeyEnum.CUSTOM_REQUEST_CODE.name, it.custom_request_code)
                }
                startActivity(intent)

            })


        packageDetailViewModel.errorMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })
    }


    private fun startPayment() {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()
        co.setKeyID("rzp_test_kxuu8CSoxDWptj")

        // TPW2LIJHrSJXr1AhIHOGvgTa   --- Secret key

        try {
            var options = JSONObject()
            options.put("name",packageDetailViewModel.selectedPackage.title)
            options.put("description",packageDetailViewModel.selectedPackage.description)
            //You can omit the image option to fetch the image from dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency","INR")
            options.put("amount",packageDetailViewModel.selectedPackage.price)
            options.put("send_sms_hash",true);

            val prefill = JSONObject()
            prefill.put("email",Helper().getLoginData(mContext).email)
            prefill.put("contact",Helper().getLoginData(mContext).mobile)

            options.put("prefill",prefill)


            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(paymentId: String?, p1: PaymentData?) {
        try{
            progressBar.visibility = View.VISIBLE

            val packageRequest = PackageSubscriptionRequest()
            packageRequest.payment_id = paymentId
            packageRequest.seller_id = Helper().getLoginData(mContext).id
            packageRequest.package_id = packageDetailViewModel.selectedPackage.id
            packageRequest.company_id = packageDetailViewModel.companyId
            packageRequest.custom_request = ""
            packageRequest.offer = packageDetailViewModel.selectedPackage.offer
            packageRequest.post = packageDetailViewModel.selectedPackage.post
            packageRequest.notification = packageDetailViewModel.selectedPackage.notification
            packageRequest.duration =
                packageDetailViewModel.selectedPackage.duration[spDuration.selectedItemPosition]
            packageRequest.type = packageDetailViewModel.selectedPackage.status

            packageDetailViewModel.apiSelectPackageSubscription(packageRequest)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        try {

            Toast.makeText(mContext, "Payment Failed : Payment Data: ${p2?.data}", Toast.LENGTH_SHORT).show()

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {
        try{

            Toast.makeText(mContext, "External wallet was selected : Payment Data: ${p1?.data}", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
    }

}