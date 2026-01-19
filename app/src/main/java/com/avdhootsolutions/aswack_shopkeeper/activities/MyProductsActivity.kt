package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import androidx.recyclerview.widget.GridLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.adapters.MyProductsAdapter
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.avdhootsolutions.aswack_shopkeeper.activities.AddProductActivity
import kotlinx.android.synthetic.main.activity_my_products.*
import kotlinx.android.synthetic.main.header_title.*

class MyProductsActivity : AppCompatActivity() {
    lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_products)
        mContext = this@MyProductsActivity
        initView()
    }

    private fun initView() {

        rvProduct.setLayoutManager(GridLayoutManager(mContext, 2))
        rvProduct.setHasFixedSize(true)
        rvProduct.setAdapter(MyProductsAdapter(mContext))
        tvTitle.setText(getString(R.string.products))
        iv_back.setOnClickListener(View.OnClickListener { finish() })
        tvAddProduct.setOnClickListener(View.OnClickListener {
            startActivity(Intent(mContext,
                AddProductActivity::class.java))
        })
    }
}