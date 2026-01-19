package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.avdhootsolutions.aswack_shopkeeper.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.adapters.SearchCarAdapter
import kotlinx.android.synthetic.main.activity_search_car_list.*
import kotlinx.android.synthetic.main.header_title.*
import kotlinx.android.synthetic.main.header_title.tvTitle

class SearchCarListActivity : AppCompatActivity() {
    lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_car_list)
        mContext = this@SearchCarListActivity
        initView()
    }

    private fun initView() {
        tvTitle.setText(getString(R.string.search_result))
        iv_back.setOnClickListener(View.OnClickListener { finish() })
        rvCar.setLayoutManager(LinearLayoutManager(mContext))
        rvCar.setHasFixedSize(true)
        rvCar.setAdapter(SearchCarAdapter(mContext))
    }
}