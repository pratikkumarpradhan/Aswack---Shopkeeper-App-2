package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.LoginViewModel
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.OTPViewModel
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.ccp
import kotlinx.android.synthetic.main.activity_login.tvSubmit

class LoginActivity : AppCompatActivity() {

    /**
     * View model
     */
    lateinit var loginViewModel : LoginViewModel

    lateinit var mContext: Context

    /**
     * Firebase token for send notification
     */
    var firebaseToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mContext = this@LoginActivity

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

        tvForgotPassword.setOnClickListener {
            val intent = Intent(mContext, OTPForForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        tvSubmit.setOnClickListener(View.OnClickListener {

            if (etLoginid.text.toString().isNotEmpty() && etPassword.text.toString().isNotEmpty()) {

                progressBar.visibility = View.VISIBLE

                val login = Login()
                login.mobile = etLoginid.text.toString()
                login.password = etPassword.text.toString()
                if (firebaseToken.isNullOrEmpty()){
                    FirebaseMessaging.getInstance().token.addOnCompleteListener {
                        if(it.isComplete){
                            firebaseToken = it.result.toString()
                            Log.e("firebaseToken ", firebaseToken)

                        }
                    }
                }

                login.device_token = firebaseToken

                loginViewModel.apiLogin(login)

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
                Toast.makeText(mContext,
                    resources.getString(R.string.fill_all_details),
                    Toast.LENGTH_SHORT).show()
            }


        })
        llRegister.setOnClickListener(View.OnClickListener {
            startActivity(Intent(mContext,
                OTPActivity::class.java))
        })
    }

    private fun init() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isComplete){
                firebaseToken = it.result.toString()
                Log.e("firebaseToken ", firebaseToken)

            }
        }

        loginViewModel.loginResponseLiveData.observe(this, Observer { loginResponse ->
            Toast.makeText(mContext, resources.getString(R.string.login_successfull), Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
            Helper().setLoginData(loginResponse[0],mContext)
            val i = Intent(mContext, HomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            finish()
        })



        loginViewModel.errorMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })




    }
}