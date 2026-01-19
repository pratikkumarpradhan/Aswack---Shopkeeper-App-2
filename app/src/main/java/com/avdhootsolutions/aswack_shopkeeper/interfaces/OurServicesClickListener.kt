package com.avdhootsolutions.aswack_shopkeeper.interfaces

import com.avdhootsolutions.aswack_shopkeeper.apiCallBacks.LoginCallBack
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OurServicesClickListener {
    fun onOurServiceCLick(pos: Int, from: String)



}