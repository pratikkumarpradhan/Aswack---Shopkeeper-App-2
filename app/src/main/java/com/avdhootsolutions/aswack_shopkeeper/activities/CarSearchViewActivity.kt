package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.avdhootsolutions.aswack_shopkeeper.models.SliderData
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.SliderImageAdapter
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_car_search_view.*
import kotlinx.android.synthetic.main.header_title.*
import java.util.ArrayList

class CarSearchViewActivity : AppCompatActivity(), SliderImageAdapter.ICustomListListener {
    lateinit var mContext: Context
    var sliderDataArrayList = ArrayList<SliderData>()

    lateinit var dialogHelper: DialogHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_search_view)
        mContext = this@CarSearchViewActivity
        initView()
    }

    private fun initView() {
        dialogHelper = DialogHelper(mContext)
//        tvCompanyProfile = findViewById(R.id.tvCompanyProfile);
//        tvOnlineChat = findViewById(R.id.tvOnlineChat);

        tvTitle.setText("Ad No: 897678")
        iv_back.setOnClickListener(View.OnClickListener { finish() })
        /*tvCompanyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, GarageDetailViewActivity.class));
            }
        });*

        /tvSubmit.setOnClickListener(View.OnClickListener {
         */
            //    startActivity(new Intent(mContext, GarageDetailViewActivity.class));
       // })
        /* tvOnlineChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ChatActivity.class));
            }
        });*/sliderDataArrayList.add(SliderData("https://i.picsum.photos/id/1/200/300.jpg?hmac=jH5bDkLr6Tgy3oAg5khKCHeunZMHq0ehBZr6vGifPLY"))
        sliderDataArrayList.add(SliderData("https://i.picsum.photos/id/1/200/300.jpg?hmac=jH5bDkLr6Tgy3oAg5khKCHeunZMHq0ehBZr6vGifPLY"))
        sliderDataArrayList.add(SliderData("https://i.picsum.photos/id/1/200/300.jpg?hmac=jH5bDkLr6Tgy3oAg5khKCHeunZMHq0ehBZr6vGifPLY"))
        val adapter = SliderImageAdapter(mContext, sliderDataArrayList, this)
        slider.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        slider.setSliderAdapter(adapter)
        slider.scrollTimeInSec = 3
        slider.isAutoCycle = true
        slider.startAutoCycle()
    }

    override fun imageSelect(imagePath: String?) {
        imagePath?.let { dialogHelper.showFullImageDialog(it) }
    }
}