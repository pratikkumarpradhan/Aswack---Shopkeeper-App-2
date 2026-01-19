package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import android.view.View
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import kotlinx.android.synthetic.main.activity_thank_you.*

class ThankYouForAddActivity : AppCompatActivity() {
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank_you)

        mContext = this@ThankYouForAddActivity

        if (intent.hasExtra(IntentKeyEnum.IS_PRODUCT_UPDATED.name)){
            tvMessage.text = resources.getString(R.string.product_has_been_updated)
        }else if (intent.hasExtra(IntentKeyEnum.IS_PRODUCT_DELETED.name)){
            tvMessage.text = resources.getString(R.string.product_has_been_deleted)
        }else if (intent.hasExtra(IntentKeyEnum.IS_REPLY_ADDED.name)){
            tvMessage.text = resources.getString(R.string.reply_successfully_added)
        }

        btnOk.setOnClickListener(View.OnClickListener {
           finish()
        })
    }
}