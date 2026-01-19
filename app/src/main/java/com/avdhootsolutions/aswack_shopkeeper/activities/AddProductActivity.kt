package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.avdhootsolutions.aswack_shopkeeper.R
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.header_title.*
import kotlinx.android.synthetic.main.header_title.tvTitle

class AddProductActivity : AppCompatActivity() {
    lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        mContext = this@AddProductActivity
        initView()
    }

    private fun initView() {

        tvTitle.text = getString(R.string.add_product)
        iv_back.setOnClickListener(View.OnClickListener { finish() })
//        tvAddProduct.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(mContext,
//                AddReviewProductActivity::class.java))
//        })
        val adapter: ArrayAdapter<*> =
            ArrayAdapter.createFromResource(mContext!!, R.array.company_name, R.layout.spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spCompanyName.setAdapter(adapter)
        val adapterType: ArrayAdapter<*> =
            ArrayAdapter.createFromResource(mContext!!, R.array.model_type, R.layout.spinner_item)
        adapterType.setDropDownViewResource(R.layout.spinner_item)
        spModelType.setAdapter(adapterType)
        val adapterModel: ArrayAdapter<*> =
            ArrayAdapter.createFromResource(mContext!!, R.array.model_name, R.layout.spinner_item)
        adapterModel.setDropDownViewResource(R.layout.spinner_item)
        spModelName.setAdapter(adapterModel)
        val adapterYear: ArrayAdapter<*> =
            ArrayAdapter.createFromResource(mContext!!, R.array.product_name, R.layout.spinner_item)
        adapterYear.setDropDownViewResource(R.layout.spinner_item)
        spProductName.setAdapter(adapterYear)
    }
}