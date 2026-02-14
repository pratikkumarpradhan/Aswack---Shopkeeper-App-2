package com.avdhootsolutions.aswack_shopkeeper.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Utils


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startTimer()
    }

    fun startTimer() {
        Handler().postDelayed({
            if (!Utils.isNetworkAvailable(this@SplashActivity)) {
                val builder = AlertDialog.Builder(this@SplashActivity)
                builder.setMessage("Internet Connection Required")
                    .setCancelable(false)
                    .setPositiveButton("Retry") { dialog, which ->
                        val intent = intent
                        startActivity(intent)
                        finish()
                    }
                val alert = builder.create()
                alert.show()
            } else {
                // Check if user is already logged in
                            if (Helper().getLoginData(this).mobile.isNullOrEmpty()){
                                val i = Intent(applicationContext, LoginActivity::class.java)
                                startActivity(i)
                                finish()
                            }else{
                                val i = Intent(applicationContext, HomeActivity::class.java)
                                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(i)
                                finish()
                            }
            }
        }, 1000)
    }
}