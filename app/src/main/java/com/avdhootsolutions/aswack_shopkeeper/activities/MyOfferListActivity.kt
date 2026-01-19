package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.adapters.OffersAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.Offer
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.OfferListViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_offer.*
import kotlinx.android.synthetic.main.header_title.*
import kotlinx.android.synthetic.main.search.*
import kotlinx.android.synthetic.main.usage_product_offer_notification.*


class MyOfferListActivity : AppCompatActivity(), OffersAdapter.ICustomListListener {

    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var offerListViewModel: OfferListViewModel


    /**
     * Adapter of the offer list
     */
    lateinit var offersAdapter: OffersAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_offer)
        mContext = this@MyOfferListActivity
        initView()
    }

    private fun initView() {

        offerListViewModel = ViewModelProvider(this).get(OfferListViewModel::class.java)
        offerListViewModel.mainCatId =
            intent.getStringExtra(IntentKeyEnum.MAIN_CAT_ID.name).toString()
        offerListViewModel.companyId =
            intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()
        offerListViewModel.packageId =
            intent.getStringExtra(IntentKeyEnum.PACKAGE_ID.name).toString()

        if (intent.hasExtra(IntentKeyEnum.IS_BUYER_OF_VEHICLE.name)) {
            offerListViewModel.isBuyerOfVehicle = true
            tvProductList.visibility = View.GONE
            tvProductCount.visibility = View.GONE
            tvAddOffers.visibility = View.GONE
            clUsage.visibility = View.GONE

        }



        if (intent.hasExtra(IntentKeyEnum.IS_BUYER_OF_VEHICLE.name)) {
            offerListViewModel.sellerId =
                intent.getStringExtra(IntentKeyEnum.SELLER_ID.name).toString()
        }

        offerListViewModel.offerListLiveData.observe(this, Observer { offerList ->
            offersAdapter = OffersAdapter(mContext, this)
            offersAdapter.setList(offerList)
            rvOffer.adapter = offersAdapter

            tvProductCount.text = offerList.size.toString() + " Offers"

            tvTotalAddedValue.text = offerList.size.toString()

            // If it is the buyer of the vehicle then usage api should not call
            if (!offerListViewModel.isBuyerOfVehicle) {
                val productList = ProductList()
                productList.seller_id = Helper().getLoginData(mContext).id
                productList.seller_company_id = offerListViewModel.companyId
                productList.package_purchased_id = offerListViewModel.packageId
                productList.type = "1"
                offerListViewModel.getUsageData(productList)
            } else {
                progressBar.visibility = View.GONE
            }


        })


        offerListViewModel.packageUsedLiveData.observe(
            this,
            androidx.lifecycle.Observer { packageUsage ->

                progressBar.visibility = View.GONE
                tvRemainingValue.text = packageUsage.package_pending
                packageUsage.end_date?.let {

                    if (packageUsage.end_date!!.isNotEmpty()) {
                        tvDaysRemainingValue.text = Helper().getDaysRemaining(it)
                    }
                }
            })



        offerListViewModel.errorMessage.observe(this, Observer { error ->
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })

        offerListViewModel.errorInOfferListMessage.observe(this, Observer { error ->
            tvTotalAddedValue.text = "0"

            offersAdapter = OffersAdapter(mContext, this)
            offersAdapter.setList(ArrayList())
            rvOffer.adapter = offersAdapter


            val productList = ProductList()
            productList.seller_id = Helper().getLoginData(mContext).id
            productList.seller_company_id = offerListViewModel.companyId
            productList.package_purchased_id = offerListViewModel.packageId
            productList.type = "1"
            offerListViewModel.getUsageData(productList)
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


        offerListViewModel.errorMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


        rvOffer.setLayoutManager(LinearLayoutManager(mContext))
        rvOffer.setHasFixedSize(true)
        tvTitle.setText(getString(R.string.offers))
        iv_back.setOnClickListener(View.OnClickListener { finish() })
        tvAddOffers.setOnClickListener(View.OnClickListener {

            if (offerListViewModel.mainCatId == "1") {
                val intent = Intent(mContext, AddOfferActivity::class.java)
                intent.putExtra(IntentKeyEnum.COMPANY_ID.name, offerListViewModel.companyId)
                intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, offerListViewModel.mainCatId)
                intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, offerListViewModel.packageId)
                startActivity(intent)
            } else {
                val intent = Intent(mContext, AddOfferForProductsActivity::class.java)
                intent.putExtra(IntentKeyEnum.COMPANY_ID.name, offerListViewModel.companyId)
                intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name, offerListViewModel.mainCatId)
                intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, offerListViewModel.packageId)
                startActivity(intent)
            }


        })


        etSearchProduct.addTextChangedListener(object : TextWatcher,
            OffersAdapter.ICustomListListener {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {

                offerListViewModel.searchableOfferList = ArrayList<Offer>()
                if (editable?.length!! > 0) {
                    for (menuItem in offerListViewModel.offerList) {
                        if (menuItem.code!!.contains(editable.toString(), true) ||
                            menuItem.name!!.contains(editable.toString(), true) ||
                            menuItem.offer_price!!.contains(editable.toString(), true) ||
                            menuItem.original_price!!.contains(editable.toString(), true)
                        ) {

                            offerListViewModel.searchableOfferList.add(menuItem)
                        }
                    }
                } else {
                    offerListViewModel.searchableOfferList = offerListViewModel.offerList
                }


                offersAdapter = OffersAdapter(mContext, this)
                rvOffer.adapter = offersAdapter
                offersAdapter.setList(offerListViewModel.searchableOfferList)

            }

            override fun onOfferSelected(offer: Offer, position: Int) {

                Log.e("selected offer ", offer.seller_category_id!!)

                if (offerListViewModel.isBuyerOfVehicle) {
                    val intent = Intent(mContext, SearchVehicleOfferDetailActivity::class.java)
                    intent.putExtra(IntentKeyEnum.OFFER_DETAILS.name, Gson().toJson(offer))
                    startActivity(intent)
                } else {
                    if (offer.seller_category_id == "1") {
                        val intent = Intent(mContext, OfferDetailActivity::class.java)
                        intent.putExtra(IntentKeyEnum.OFFER_DETAILS.name, Gson().toJson(offer))

                        var productDetail = SellVehicle()
                        for (i in offerListViewModel.vehicleSellList.indices) {
                            if (offerListViewModel.vehicleSellList[i].id == offer.product_id) {

                                productDetail = offerListViewModel.vehicleSellList[i]
                                break
                            }
                        }

                        intent.putExtra(
                            IntentKeyEnum.VEHICLE_DETAILS.name,
                            Gson().toJson(productDetail)
                        )
                        startActivity(intent)
                    } else {
                        val intent1 = Intent(mContext, OfferDetailForProductsActivity::class.java)
                        intent1.putExtra(IntentKeyEnum.OFFER_DETAILS.name, Gson().toJson(offer))

                        var productDetail1 = ProductList()
                        for (i in offerListViewModel.productList.indices) {
                            if (offerListViewModel.productList[i].id == offer.product_id) {

                                productDetail1 = offerListViewModel.productList[i]
                                break

                                Log.e("hello cat other", offerListViewModel.productList[i].id!!)
                            }
                        }

                        intent1.putExtra(
                            IntentKeyEnum.PRODUCT_LIST.name,
                            Gson().toJson(productDetail1)
                        )
                        startActivity(intent1)
                    }
                }
            }

        })

    }

    override fun onOfferSelected(offer: Offer, position: Int) {

        Log.e("selected offer ", offer.seller_category_id!!)

        if (offerListViewModel.isBuyerOfVehicle) {
            val intent = Intent(mContext, SearchVehicleOfferDetailActivity::class.java)
            intent.putExtra(IntentKeyEnum.OFFER_DETAILS.name, Gson().toJson(offer))
            startActivity(intent)
        } else {
            if (offer.seller_category_id == "1") {
                val intent = Intent(mContext, OfferDetailActivity::class.java)
                intent.putExtra(IntentKeyEnum.OFFER_DETAILS.name, Gson().toJson(offer))

                var productDetail = SellVehicle()
                for (i in offerListViewModel.vehicleSellList.indices) {
                    if (offerListViewModel.vehicleSellList[i].id == offer.product_id) {

                        productDetail = offerListViewModel.vehicleSellList[i]
                        break
                    }
                }

                intent.putExtra(IntentKeyEnum.VEHICLE_DETAILS.name, Gson().toJson(productDetail))
                startActivity(intent)
            } else {
                val intent1 = Intent(mContext, OfferDetailForProductsActivity::class.java)
                intent1.putExtra(IntentKeyEnum.OFFER_DETAILS.name, Gson().toJson(offer))

                var productDetail1 = ProductList()
                for (i in offerListViewModel.productList.indices) {
                    if (offerListViewModel.productList[i].id == offer.product_id) {

                        productDetail1 = offerListViewModel.productList[i]
                        break

                        Log.e("hello cat other", offerListViewModel.productList[i].id!!)
                    }
                }

                intent1.putExtra(IntentKeyEnum.PRODUCT_LIST.name, Gson().toJson(productDetail1))
                startActivity(intent1)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE

        /**
         * If the user is buyer of the vehicle,
         * Then we have to call selected vehicle's seller offer
         */
        if (offerListViewModel.isBuyerOfVehicle) {
            val offer = Offer()
            offer.seller_id = offerListViewModel.sellerId
            offer.seller_company_id = offerListViewModel.companyId
            offer.seller_category_id = offerListViewModel.mainCatId
            offerListViewModel.getOfferList(offer, mContext)
        } else {
            val offer = Offer()
            offer.seller_id = Helper().getLoginData(mContext).id
            offer.seller_company_id = offerListViewModel.companyId
            offer.seller_category_id = offerListViewModel.mainCatId
            offerListViewModel.getOfferList(offer, mContext)
        }


    }
}