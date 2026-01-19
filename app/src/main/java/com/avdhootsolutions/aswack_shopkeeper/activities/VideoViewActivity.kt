package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.widget.VideoView
import android.os.Bundle
import android.util.Log
import com.avdhootsolutions.aswack_shopkeeper.R
import kotlinx.android.synthetic.main.activity_video_view.*

class VideoViewActivity : AppCompatActivity() {
    private var mContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)
        mContext = this@VideoViewActivity
        initView()
        //        init();
    }

    private fun initView() {
        val videoUrl = intent.getStringExtra("videoUrl")
        Log.e("videoUrl ", videoUrl!!)
        videoView.setVideoURI(Uri.parse(videoUrl))
        videoView.start()
    }
}