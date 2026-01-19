package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.models.ProductDataForSeller
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.ChatProductListAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.ChatListViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_chat.iv_back
import kotlinx.android.synthetic.main.activity_chat_list.*
import kotlinx.android.synthetic.main.activity_chat_list.rvChat
import kotlinx.android.synthetic.main.header_title.*

class ChatListActivity : AppCompatActivity(), ChatProductListAdapter.ICustomListListener {
    lateinit var mContext: Context

    lateinit var chatListViewModel: ChatListViewModel

    lateinit var chatProductListAdapter : ChatProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        mContext = this@ChatListActivity

        init()

        clickListner()
    }

    private fun clickListner() {

        rvChat.setOnClickListener {
            startActivity(Intent(mContext,
                ChatActivity::class.java))
        }

        iv_back.setOnClickListener(View.OnClickListener { finish() })
    }

    private fun init() {

        tvTitle.setText(getString(R.string.chat_list))

        val mLayoutManager = LinearLayoutManager(mContext)
        //        mLayoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(mLayoutManager)
        rvChat.setHasFixedSize(true)
        rvChat.setNestedScrollingEnabled(true)
        chatListViewModel = ViewModelProvider(this).get(ChatListViewModel::class.java)
        chatListViewModel.sellerId = Helper().getLoginData(mContext).id!!

        if (intent.hasExtra(IntentKeyEnum.CHAT_PRODUCT_DATA_WITH_QUOTATION.name)){
            val productDataForSellerForUpdate = Gson().fromJson(intent.getStringExtra(IntentKeyEnum.CHAT_PRODUCT_DATA_WITH_QUOTATION.name), ProductDataForSeller::class.java)

            progressbar.visibility = View.VISIBLE
            chatListViewModel.companyId = productDataForSellerForUpdate.companyId
            chatListViewModel.addQuotationDetailsInSellerAndUser(productDataForSellerForUpdate)

        }else{
            chatListViewModel.companyId = intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()
        }



        Helper().getLoginData(mContext).id?.let { chatListViewModel.getSellerData(it) }

        chatListViewModel.productDataListLiveData.observe(this) { productList ->
            progressbar.visibility = View.GONE

            chatProductListAdapter = ChatProductListAdapter(this, this)
            rvChat.adapter = chatProductListAdapter
            productList?.let {
                chatProductListAdapter.setList(productList)
            }?: kotlin.run {
                chatProductListAdapter.setList(ArrayList())
            }

        }
    }

    override fun onItemSelected(productDataForSeller: ProductDataForSeller) {

        val intent = Intent(mContext, ChatActivity::class.java)
        intent.putExtra(IntentKeyEnum.CHAT_PRODUCT_DATA.name, Gson().toJson(productDataForSeller))
        startActivity(intent)
    }

    override fun onAddQuotationSelected(productDataForSeller: ProductDataForSeller) {

        if (productDataForSeller.quotationId.isNullOrEmpty()){
            val intent = Intent(mContext, AddQuotationActivity::class.java)
            intent.putExtra(IntentKeyEnum.CHAT_PRODUCT_DATA.name, Gson().toJson(productDataForSeller))
            startActivity(intent)
        }else{
            val intent = Intent(mContext, QuotationDetailsActivity::class.java)
            intent.putExtra(IntentKeyEnum.CHAT_PRODUCT_DATA.name, Gson().toJson(productDataForSeller))
            startActivity(intent)
        }


    }
}