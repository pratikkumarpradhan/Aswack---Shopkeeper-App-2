package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import android.content.Intent
import android.view.View
import com.avdhootsolutions.aswack_shopkeeper.activities.HomeActivity
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import kotlinx.android.synthetic.main.activity_thank_you_registration.*


class ThankYouForRegistrationActivity : AppCompatActivity() {
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank_you_registration)

        mContext = this@ThankYouForRegistrationActivity


        if (intent.hasExtra(IntentKeyEnum.PACKAGE_PURCHASED.name)){
            tvSmsgtitlethree.text = "Your Payment  has been successfully completed.\n" +
                    "Now you can start using your package with our Application."

            tvSmsgtitleone.text = "Thank you for your purchase with ASWACK."
        }

        if (intent.hasExtra(IntentKeyEnum.CUSTOM_REQUEST_CODE.name)){
            tvSmsgtitleFive.visibility = View.VISIBLE
            tvSmsgtitleFive.text = "Your custom package request id is "+intent.getStringExtra(IntentKeyEnum.CUSTOM_REQUEST_CODE.name)
        }

//        else if (intent.hasExtra(IntentKeyEnum.PACKAGE_PURCHASED.name)){
//            tvSmsgtitlethree.text = resources.getString(R.string.your_package_purchased)
//            tvSmsgtitlefour.visibility = View.GONE
//        }

        btnSubmit.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
            finish()
        })
    }
}