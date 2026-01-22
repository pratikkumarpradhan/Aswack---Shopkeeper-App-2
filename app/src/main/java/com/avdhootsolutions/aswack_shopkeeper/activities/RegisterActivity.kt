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
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    lateinit var mContext: Context
    var firebaseToken = ""
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mContext = this@RegisterActivity

        init()
        clickListener()
    }

    private fun init() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isComplete) {
                firebaseToken = it.result.toString()
                Log.e("firebaseToken", firebaseToken)
            }
        }
    }

    private fun clickListener() {
        tvSubmit.setOnClickListener {
            if (validateFields()) {
                progressBar.visibility = View.VISIBLE
                registerUser()
            }
        }
    }

    private fun validateFields(): Boolean {
        if (etFullName.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter Full Name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (etEmail.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter Email Address", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString().trim()).matches()) {
            Toast.makeText(mContext, "Please enter valid Email Address", Toast.LENGTH_SHORT).show()
            return false
        }
        if (etMobile.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter Mobile Number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (etPassword.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter Password", Toast.LENGTH_SHORT).show()
            return false
        }
        if (etPassword.text.toString().trim().length < 6) {
            Toast.makeText(mContext, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun registerUser() {
        // Check if mobile number already exists
        db.collection("dealerUsers")
            .whereEqualTo("mobile", etMobile.text.toString().trim())
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Mobile number doesn't exist, proceed with registration
                    val dealerData = hashMapOf(
                        "name" to etFullName.text.toString().trim(),
                        "email" to etEmail.text.toString().trim(),
                        "mobile" to etMobile.text.toString().trim(),
                        "password" to etPassword.text.toString().trim(),
                        "deviceToken" to firebaseToken,
                        "createdAt" to Timestamp.now()
                    )

                    // Use mobile number as document ID for easy lookup
                    db.collection("dealerUsers")
                        .document(etMobile.text.toString().trim())
                        .set(dealerData)
                        .addOnSuccessListener {
                            Log.e("RegisterActivity", "User registered successfully")
                            
                            // Save user data locally
                            val register = Register()
                            register.name = etFullName.text.toString().trim()
                            register.email = etEmail.text.toString().trim()
                            register.mobile = etMobile.text.toString().trim()
                            register.password = etPassword.text.toString().trim()
                            register.device_token = firebaseToken
                            register.id = etMobile.text.toString().trim()
                            Helper().setLoginData(register, mContext)

                            Toast.makeText(mContext, "Registration successful", Toast.LENGTH_SHORT).show()
                            progressBar.visibility = View.GONE

                            // Navigate to Home screen
                            val i = Intent(mContext, HomeActivity::class.java)
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(i)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Log.e("RegisterActivity", "Error registering user", e)
                            progressBar.visibility = View.GONE
                            Toast.makeText(mContext, "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Mobile number already exists
                    progressBar.visibility = View.GONE
                    Toast.makeText(mContext, "Mobile number already registered", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("RegisterActivity", "Error checking mobile number", e)
                progressBar.visibility = View.GONE
                Toast.makeText(mContext, "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
