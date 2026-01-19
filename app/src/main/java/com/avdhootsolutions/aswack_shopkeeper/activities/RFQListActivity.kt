package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.adapters.ChatProductListAdapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.RFQListAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.NotificationSendData
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.models.RFQList
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.ChatListViewModel
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.RFQListViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_rfqactivity.*
import kotlinx.android.synthetic.main.header_title.*

class RFQListActivity : AppCompatActivity(), RFQListAdapter.ICustomListListener {
    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var rfqListViewModel: RFQListViewModel

    lateinit var chatListViewModel: ChatListViewModel

    lateinit var rfqListAdapter: RFQListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rfqactivity)
        mContext = this@RFQListActivity
        initView()

        clickListner()
    }

    /**
     * Click events
     */
    private fun clickListner() {
        iv_back.setOnClickListener(View.OnClickListener { finish() })
    }

    private fun initView() {

        rvRFQ.layoutManager = LinearLayoutManager(mContext)
        rvRFQ.setHasFixedSize(true)
        tvTitle.text = getString(R.string.customer_requirement)

        rfqListViewModel = ViewModelProvider(this).get(RFQListViewModel::class.java)

        chatListViewModel = ViewModelProvider(this).get(ChatListViewModel::class.java)
        chatListViewModel.sellerId = Helper().getLoginData(mContext).id!!

        rfqListViewModel.mainCatId =
            intent.getStringExtra(IntentKeyEnum.MAIN_CAT_ID.name).toString()
        rfqListViewModel.companyId = intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()
        chatListViewModel.companyId = rfqListViewModel.companyId

        rfqListViewModel.rfqListLiveData.observe(this, androidx.lifecycle.Observer { rfqList ->
            rfqListAdapter = RFQListAdapter(mContext, this)
            rfqListAdapter.setList(rfqList)
            rvRFQ.adapter = rfqListAdapter

            Helper().getLoginData(mContext).id?.let { chatListViewModel.getSellerData(it) }
        })


        rfqListViewModel.errorMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


        chatListViewModel.productDataListLiveData.observe(this) { productList ->
            progressBar.visibility = View.GONE
            for (i in productList.indices){

                for (j in rfqListViewModel.rfqList.indices){
                    if (productList[i].rfqCode.equals(rfqListViewModel.rfqList[j].rfq_code)){
                        rfqListViewModel.rfqList[j].isChatInitiated = true
                        rfqListViewModel.rfqList[j].productDataForSeller = productList[i]
                    }
                }
            }


            rfqListAdapter.setList(rfqListViewModel.rfqList)
        }

    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.VISIBLE
        val productList = ProductList()
        productList.seller_id = Helper().getLoginData(mContext).id
        productList.master_category_id = rfqListViewModel.mainCatId
        rfqListViewModel.getRFQList(productList)
    }

    override fun onRFQSelected(rfqList: RFQList, position: Int) {

        rfqList.seller_company_id = rfqListViewModel.companyId
        rfqList.seller_id = Helper().getLoginData(mContext).id
        rfqList.seller_name = Helper().getLoginData(mContext).name

        val intent = Intent(this, RFQDetailActivity::class.java)
        intent.putExtra(IntentKeyEnum.RFQ_DATA.name, Gson().toJson(rfqList))
        startActivity(intent)
        finish()
    }

    override fun onRFQChatSelected(rfqList: RFQList, position: Int) {
        rfqList.seller_company_id = rfqListViewModel.companyId
        rfqList.seller_id = Helper().getLoginData(mContext).id
        rfqList.seller_name = Helper().getLoginData(mContext).name
        val notificationSendData = NotificationSendData()
        notificationSendData.rfq_id = rfqList.id
        notificationSendData.seller_id = rfqList.seller_id
        notificationSendData.company_id = rfqList.seller_company_id

        rfqListViewModel.sendNotificationToUserForRFQResponse(notificationSendData)

        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra(IntentKeyEnum.RFQ_DATA.name, Gson().toJson(rfqList))
        startActivity(intent)
    }

    override fun onAddQuotationSelected(rfqList: RFQList, position: Int) {
        val intent = Intent(mContext, AddQuotationActivity::class.java)
        intent.putExtra(IntentKeyEnum.CHAT_PRODUCT_DATA.name, Gson().toJson(rfqList.productDataForSeller))
        startActivity(intent)
        finish()
    }

    override fun onViewQuotationSelected(rfqList: RFQList, position: Int) {
        val intent = Intent(mContext, QuotationDetailsActivity::class.java)
        intent.putExtra(IntentKeyEnum.CHAT_PRODUCT_DATA.name, Gson().toJson(rfqList.productDataForSeller))
        startActivity(intent)
    }

}