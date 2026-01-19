package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avdhootsolutions.aswack_shopkeeper.service.FirebaseAuthServices


class OTPViewModel : ViewModel(),
    FirebaseAuthServices.FirebaseAuthListener {


    var firebaseAuthServices = FirebaseAuthServices()

    var codeLiveData = MutableLiveData<String>()
    var verificationFailedLiveData = MutableLiveData<String>()
    var signInSuccessLiveData = MutableLiveData<Boolean>()
    var codeSentLiveData = MutableLiveData<Boolean>()

    init {
        firebaseAuthServices.setLoginListner(this)
    }

    fun sendVerificationCode(phoneNo : String, activity: Activity){
        firebaseAuthServices.sendVerificationCode(phoneNo, activity)
    }

    fun verifyCode(code: String){
        firebaseAuthServices.verifyCode(code)
    }



    override fun onCodeSent() {
        codeSentLiveData.value = true
    }

    override fun getCode(code: String) {
        codeLiveData.value = code
    }

    override fun getVerificationFailedMessage(message: String) {
         verificationFailedLiveData.value = message
    }

    override fun signInSuccess(isSuccess: Boolean, message: String) {
           signInSuccessLiveData.value = isSuccess
    }


}