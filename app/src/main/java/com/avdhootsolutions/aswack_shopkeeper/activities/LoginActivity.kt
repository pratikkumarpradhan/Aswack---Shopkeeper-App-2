package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.models.Register
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.ccp
import kotlinx.android.synthetic.main.activity_login.tvSubmit

class LoginActivity : AppCompatActivity() {

    lateinit var mContext: Context

    /**
     * Firebase token for send notification
     */
    var firebaseToken = ""
    private val db = FirebaseFirestore.getInstance()

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
                authenticateUser()
            } else {
                Toast.makeText(mContext,
                    resources.getString(R.string.fill_all_details),
                    Toast.LENGTH_SHORT).show()
            }
        })
        
        tvRegisterText.setOnClickListener {
            val intent = Intent(mContext, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun init() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isComplete){
                firebaseToken = it.result.toString()
                Log.e("firebaseToken ", firebaseToken)
            }
            }
        }

    private fun authenticateUser() {
        val mobileNumber = etLoginid.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Get FCM token if not already available
        if (firebaseToken.isNullOrEmpty()) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (it.isComplete) {
                    firebaseToken = it.result.toString()
                    Log.e("firebaseToken", firebaseToken)
                    performLogin(mobileNumber, password)
                }
            }
        } else {
            performLogin(mobileNumber, password)
        }
    }

    private fun performLogin(mobileNumber: String, password: String) {
        // Query Firebase for user with matching mobile and password
        db.collection("dealerUsers")
            .whereEqualTo("mobile", mobileNumber)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Invalid credentials
                    progressBar.visibility = View.GONE
                    Toast.makeText(mContext, "Invalid mobile number or password", Toast.LENGTH_SHORT).show()
                } else {
                    // Login successful
                    val document = documents.documents[0]
                    val dealerData = document.data

                    // Update device token
                    document.reference.update("deviceToken", firebaseToken)
                        .addOnSuccessListener {
                            Log.e("LoginActivity", "Device token updated")
                        }

                    // Save user data locally
                    val register = Register()
                    register.name = dealerData?.get("name") as? String ?: ""
                    register.email = dealerData?.get("email") as? String ?: ""
                    register.mobile = dealerData?.get("mobile") as? String ?: ""
                    register.password = dealerData?.get("password") as? String ?: ""
                    register.device_token = firebaseToken
                    register.id = document.id

                    Helper().setLoginData(register, mContext)

            Toast.makeText(mContext, resources.getString(R.string.login_successfull), Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE

                    // Navigate to Home screen
            val i = Intent(mContext, HomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            finish()
                }
            }
            .addOnFailureListener { e ->
                Log.e("LoginActivity", "Error authenticating user", e)
            progressBar.visibility = View.GONE
                Toast.makeText(mContext, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}