package com.avdhootsolutions.aswack_shopkeeper.activities

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.rilixtech.widget.countrycodepicker.CountryCodePicker
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import com.rilixtech.widget.countrycodepicker.CountryCodePicker.OnCountryChangeListener
import com.rilixtech.widget.countrycodepicker.Country
import android.content.Intent
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.avdhootsolutions.aswack_shopkeeper.activities.VerifyOTPActivity
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.GroupChatViewModel
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.OTPViewModel
import kotlinx.android.synthetic.main.activity_otp.*

class OTPActivity : AppCompatActivity() {
    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var otpViewModel : OTPViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        if(ContextCompat.checkSelfPermission(baseContext, "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {

        }

        mContext = this@OTPActivity
        initView()
        init()

        clickListener()
    }

    private fun clickListener() {

        tvResendOTP.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            tvOtpSubmit.performClick()
        }

        tvOtpSubmit.setOnClickListener{

            if (TextUtils.isEmpty(etMobile.text.toString())) {
                // when mobile number text field is empty
                // displaying a toast message.
                Toast.makeText(
                    this,
                    "Please enter a valid phone number.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // if the text field is not empty we are calling our
                // send OTP method for getting OTP from Firebase.

                    Log.e("country code ", ccp.selectedCountryCode)
                Log.e("full number ", etMobile.text.toString())
                val phone = "+" + ccp.selectedCountryCode + etMobile.text.toString()
                progressBar.visibility = View.VISIBLE
                otpViewModel.sendVerificationCode(phone, this)

            }

        }

        tvSubmit.setOnClickListener{
            // validating if the OTP text field is empty or not.
            if (TextUtils.isEmpty(pinView.getText().toString())) {
                // if the OTP text field is empty display
                // a message to user to enter OTP
                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show();
            } else {
                // if OTP field is not empty calling
                // method to verify the OTP.
                  progressBar.visibility = View.VISIBLE

                otpViewModel.verifyCode(pinView.text.toString())

            }
        }


        llBack.setOnClickListener {

            clOTP.visibility = View.GONE

            val animation = AnimationUtils.loadAnimation(this,R.anim.slide_left)
            clMobile.startAnimation(animation)

            clMobile.visibility = View.VISIBLE

        }


    }

    private fun initView() {
        ccp.setOnCountryChangeListener {
            //Toast.makeText(mContext, "Updated " + selectedCountry.getName(), Toast.LENGTH_SHORT).show();
        }
//        tvOtpSubmit.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(mContext, VerifyOTPActivity::class.java))
//            //startActivity(new Intent(mContext, SignUpActivity.class));
//        })
    }



    private fun init() {
        otpViewModel = ViewModelProvider(this).get(OTPViewModel::class.java)
        otpViewModel.codeLiveData.observe(this, Observer { code ->

            Log.e("codee ", code)
            pinView.setText(code)
        })

        otpViewModel.verificationFailedLiveData.observe(this, Observer { message ->
            progressBar.visibility = View.GONE
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()

        })

        otpViewModel.signInSuccessLiveData.observe(this, Observer { isSuccess ->
            progressBar.visibility = View.GONE
            if (isSuccess){

                val helper = Helper()
                helper.setPhoneNo(etMobile.text!!.toString(), this)

                Toast.makeText(mContext, "OTP is verified ", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SignUpActivity::class.java)
                intent.putExtra(IntentKeyEnum.MOBILE_NUMBER.name, etMobile.text.toString())
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent)
                finish()

            }else{
                Toast.makeText(
                    this,
                    "You have Enter Wrong OTP.",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })

        otpViewModel.codeSentLiveData.observe(this, Observer { isSent ->
            progressBar.visibility = View.GONE
            if (isSent){
                // Upadte UI
                clMobile.visibility = View.GONE
                val animation = AnimationUtils.loadAnimation(this,R.anim.slide_right)
                clOTP.startAnimation(animation)
                tvFptitlethree.setText("To ${ccp.selectedCountryCode} ${etMobile.text.toString()}")
                clOTP.visibility = View.VISIBLE
            }
        })


    }

    


}