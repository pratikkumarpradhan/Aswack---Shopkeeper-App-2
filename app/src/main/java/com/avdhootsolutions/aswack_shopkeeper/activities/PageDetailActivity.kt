package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Handler
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.PageDetailsViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_page_details.*
import kotlinx.android.synthetic.main.header_title.*

class PageDetailActivity : AppCompatActivity() {

    lateinit var pageDetailsViewModel: PageDetailsViewModel
    lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_details)
        mContext = this@PageDetailActivity
        init()
        clickListener()
    }

    private fun init() {

        pageDetailsViewModel = ViewModelProvider(this).get(PageDetailsViewModel::class.java)
        pageDetailsViewModel.pageNo = intent.getStringExtra(IntentKeyEnum.PAGE_NO.name).toString()

        when(pageDetailsViewModel.pageNo){
            "1" ->{
                tvTitle.text = resources.getString(R.string.about_us)
            }

            "2" ->{
                tvTitle.text = resources.getString(R.string.contact_us)
            }

            "3" ->{
                tvTitle.text = resources.getString(R.string.privacy_policy)
            }

            "4" ->{
                tvTitle.text = resources.getString(R.string.terms_condition)
            }

            "5" ->{
                tvTitle.text = resources.getString(R.string.help)
            }

        }
        pageDetailsViewModel.errorMessage.observe(this, androidx.lifecycle.Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })

        pageDetailsViewModel.pageDetailsLiveData.observe(this, androidx.lifecycle.Observer { pageDetail ->
            progressBar.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvPageDetails.text = Html.fromHtml(pageDetail.description, Html.FROM_HTML_MODE_COMPACT);
            } else {
                tvPageDetails.text = Html.fromHtml(pageDetail.description);
            }
        })



    }


    /**
     * Click events
     */
    private fun clickListener() {

        iv_back.setOnClickListener {
            finish()
        }

    }


    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE

        val login = Login()
        login.id = pageDetailsViewModel.pageNo

        pageDetailsViewModel.getPageDetails(login)
    }

}