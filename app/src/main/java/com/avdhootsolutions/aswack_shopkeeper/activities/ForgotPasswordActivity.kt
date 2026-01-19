package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.ForgotPasswordViewModel
import kotlinx.android.synthetic.main.activity_login.*

class ForgotPasswordActivity : AppCompatActivity() {

    /**
     * View model
     */
    lateinit var forgotPasswordViewModel: ForgotPasswordViewModel

    lateinit var mContext: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        mContext = this@ForgotPasswordActivity

        init()

        clickListener()


    }


    /**
     * Click events
     */
    private fun clickListener() {
        ccp.setOnCountryChangeListener {
            //Toast.makeText(mContext, "Updated " + selectedCountry.getName(), Toast.LENGTH_SHORT).show();
        }
        tvSubmit.setOnClickListener(View.OnClickListener {

            if (etLoginid.text.toString().isNotEmpty() && etPassword.text.toString().isNotEmpty()) {

                progressBar.visibility = View.VISIBLE

                val login = Login()
                login.mobile = etLoginid.text.toString()
                login.password = etPassword.text.toString()

                forgotPasswordViewModel.apiForgotPassword(login)

//                if (etLoginid.text.toString().length == 10) {
//                    Helper().setPhoneNo(etLoginid.text.toString(), mContext)
//                    startActivity(Intent(mContext,
//                        HomeActivity::class.java))
//                } else {
//                    Toast.makeText(mContext,
//                        resources.getString(R.string.enter_ten_characters),
//                        Toast.LENGTH_SHORT).show()
//                }


            } else {
                Toast.makeText(
                    mContext,
                    resources.getString(R.string.fill_all_details),
                    Toast.LENGTH_SHORT
                ).show()
            }


        })
    }

    private fun init() {
        forgotPasswordViewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)

        etLoginid.setText(intent.getStringExtra(IntentKeyEnum.MOBILE_NUMBER.name))
        etLoginid.keyListener = null

        forgotPasswordViewModel.successMessageLiveData.observe(this, Observer { loginResponse ->
            Toast.makeText(
                mContext,
                resources.getString(R.string.password_changed_successfully),
                Toast.LENGTH_LONG
            ).show()

            val login = Login()
            login.mobile = etLoginid.text.toString()
            login.password = etPassword.text.toString()
            forgotPasswordViewModel.apiLogin(login)
        })


        forgotPasswordViewModel.loginResponseLiveData.observe(this, Observer { loginResponse ->
            progressBar.visibility = View.GONE
            Helper().setLoginData(loginResponse[0], mContext)
            val i = Intent(mContext, HomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            finish()
        })

        forgotPasswordViewModel.errorMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })

    }
}