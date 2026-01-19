package com.avdhootsolutions.aswack_shopkeeper.service

import android.app.Activity
import android.util.Log

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

import java.util.concurrent.TimeUnit


class FirebaseAuthServices() {


    // variable for FirebaseAuth class
    private var mAuth: FirebaseAuth? = null

    private lateinit var firebaseAuthListner : FirebaseAuthListener

    init {
        // below line is for getting instance
        // of our FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();
        mAuth?.useAppLanguage()
    }

    // string for storing our verification ID
    private var verificationId: String? = null

    fun isUserLogin() : String{
        return mAuth?.uid?:""
    }

    fun getUserId() : String {
        return mAuth?.uid?:""
    }

    fun getMobileNo() : String {
        return mAuth?.currentUser?.phoneNumber?:""
    }

    fun setLoginListner(firebaseAuthListner : FirebaseAuthListener){
        this.firebaseAuthListner = firebaseAuthListner

    }

     fun sendVerificationCode(number: String, activity: Activity) {
        // this method is used for getting
        // OTP on user phone number.
        val options = PhoneAuthOptions.newBuilder(mAuth!!)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // callback method is called on Phone auth provider.
    private val   // initializing our callbacks for on
    // verification callback method.
            mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            // below method is used when
            // OTP is sent from Firebase
            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                // when we receive the OTP it
                // contains a unique id which
                // we are storing in our string
                // which we have already created.
                verificationId = s

                firebaseAuthListner.onCodeSent()

            }

            // this method is called when user
            // receive OTP from Firebase.
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                // below line is used for getting OTP code
                // which is sent in phone auth credentials.
                val code = phoneAuthCredential.smsCode

                // checking if the code
                // is null or not.
                if (code != null) {
                    // if the code is not null then
                    // we are setting that code to
                    // our OTP edittext field.

                    firebaseAuthListner.getCode(code)
                    // after setting this code
                    // to OTP edittext field we
                    // are calling our verifycode method.
                    verifyCode(code)
                }
            }

            // this method is called when firebase doesn't
            // sends our OTP code due to any error or issue.
            override fun onVerificationFailed(e: FirebaseException) {
                // displaying error message with firebase exception.
                e.message?.let { firebaseAuthListner.getVerificationFailedMessage(it)

                    Log.e("error found ", it)}

            }
        }



    // below method is use to verify code from Firebase.
     fun verifyCode(code: String) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        val credential = verificationId?.let { PhoneAuthProvider.getCredential(it, code) }

        // after getting credential we are
        // calling sign in method.
        credential?.let { signInWithCredential(it) }
    }


    private fun signInWithCredential(credential: PhoneAuthCredential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // if the code is correct and the task is successful
                    // we are sending our user to new activity.
                    mAuth?.currentUser?.uid?.let { Log.e("userId " , it) }
                    firebaseAuthListner.signInSuccess(true, "")

                } else {
                    // if the code is not correct then we are
                    // displaying an error message to the user.
                    task.exception?.message?.let { firebaseAuthListner.signInSuccess(false, it) }
                }
            }
    }

    interface FirebaseAuthListener {
        fun onCodeSent()
        fun getCode(code: String)
        fun getVerificationFailedMessage(message: String)
        fun signInSuccess(isSuccess: Boolean, message: String)
    }



}
