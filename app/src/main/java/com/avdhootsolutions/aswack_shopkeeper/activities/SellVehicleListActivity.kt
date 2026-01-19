package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import androidx.recyclerview.widget.GridLayoutManager
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.adapters.VehicleCategoryAdapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.VehicleSellListAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.UserTypeEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.VehIcleForEnum
import com.avdhootsolutions.aswack_shopkeeper.models.Categories
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.SellCarListViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_products.*
import kotlinx.android.synthetic.main.header_title.*
import kotlinx.android.synthetic.main.search.*
import kotlinx.android.synthetic.main.usage_product_offer_notification.*

class SellVehicleListActivity : AppCompatActivity(), VehicleSellListAdapter.ICustomListListener {
    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var sellCarListViewModel : SellCarListViewModel

    /**
     * Dialog helper class
     */
    lateinit var dialogHelper: DialogHelper


    /**
     * Adapter for sold vehicle list
     */
    lateinit var vehicleSellListAdapter: VehicleSellListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_products)
        mContext = this@SellVehicleListActivity
        initView()

        clickListener()
    }

    /**
     * All click events
     */
    private fun clickListener() {

        iv_back.setOnClickListener { finish() }

        tvAddProduct.setOnClickListener(View.OnClickListener {
            dialogHelper.openBuySellDialog(VehIcleForEnum.SELL.ordinal, "What are you selling?", sellCarListViewModel.vehicleList, object : VehicleCategoryAdapter.ICustomListListener{
                override fun onCatgeorySelected(categories: Categories, position: Int) {
                    dialogHelper.dismissDialog()

                    val intent = Intent(mContext, AddVehicleForSellActivity::class.java)
                    intent.putExtra(IntentKeyEnum.VEHICLE_CATEGORY_ID.name, categories.id)
                    intent.putExtra(IntentKeyEnum.COMPANY_ID.name, sellCarListViewModel.companyId)
                    intent.putExtra(IntentKeyEnum.PACKAGE_ID.name, sellCarListViewModel.packageId)
                    startActivity(intent)
                }

            } )

        })


        etSearchProduct.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {

                sellCarListViewModel.searchableVehicleSellList = ArrayList<SellVehicle>()
                if (editable?.length!! > 0){
                    for (menuItem in sellCarListViewModel.vehicleSellList){
                        if (menuItem.vehicle_brand_name!!.contains(editable.toString(), true) ||
                            menuItem.vehicle_model_name!!.contains(editable.toString(), true) ||
                            menuItem.vehicle_type_name!!.contains(editable.toString(), true) ||
                            menuItem.vehicle_fuel_name!!.contains(editable.toString(), true) ||
                            menuItem.vehicle_year_name!!.contains(editable.toString(), true) ||
                            menuItem.price!!.contains(editable.toString(), true) ||
                            menuItem.driven_km!!.contains(editable.toString(), true) ){
                            sellCarListViewModel.searchableVehicleSellList.add(menuItem)
                        }
                    }
                }else{
                    sellCarListViewModel.searchableVehicleSellList = sellCarListViewModel.vehicleSellList
                }


                vehicleSellListAdapter = VehicleSellListAdapter(mContext, this@SellVehicleListActivity)
                rvProduct.adapter = vehicleSellListAdapter
                vehicleSellListAdapter.setList(sellCarListViewModel.searchableVehicleSellList)

            }

        })



    }

    private fun initView() {
        rvProduct.layoutManager = GridLayoutManager(mContext, 2)
        rvProduct.setHasFixedSize(true)
        tvTitle.text = getString(R.string.vehicles)


        dialogHelper = DialogHelper(mContext)
        sellCarListViewModel = ViewModelProvider(this).get(SellCarListViewModel::class.java)
        sellCarListViewModel.companyId = intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()
        sellCarListViewModel.packageId= intent.getStringExtra(IntentKeyEnum.PACKAGE_ID.name).toString()

        if (intent.hasExtra(IntentKeyEnum.IS_BUYER_OF_VEHICLE.name)){
            sellCarListViewModel.isBuyerOfVehicle = true
            sellCarListViewModel.sellerIdOfVehicle = intent.getStringExtra(IntentKeyEnum.SELLER_ID.name).toString()
            sellCarListViewModel.userType = intent.getStringExtra(IntentKeyEnum.USER_TYPE.name).toString()

            tvProductList.visibility = View.GONE
            tvProductCount.visibility = View.GONE
            tvAddProduct.visibility = View.GONE
            clUsage.visibility = View.GONE

        }



        sellCarListViewModel.vehicleSellListLiveData.observe(this, Observer { vehicleSellList ->

            tvProductCount.text = vehicleSellList.size.toString() + " ${resources.getString(R.string.products)}"

            vehicleSellListAdapter = VehicleSellListAdapter(mContext, this)
            rvProduct.adapter = vehicleSellListAdapter
            vehicleSellListAdapter.setList(vehicleSellList)


            if (sellCarListViewModel.isBuyerOfVehicle){
                progressBar.visibility = View.GONE
            }else{
                tvTotalAddedValue.text = vehicleSellList.size.toString()
                val productList = ProductList()
                productList.seller_id = Helper().getLoginData(mContext).id
                productList.seller_company_id = sellCarListViewModel.companyId
                productList.package_purchased_id = sellCarListViewModel.packageId
                productList.type = "0"
                sellCarListViewModel.getUsageData(productList)
            }


        })


        sellCarListViewModel.packageUsedLiveData.observe(this, androidx.lifecycle.Observer { packageUsage ->

            progressBar.visibility = View.GONE
            tvRemainingValue.text = packageUsage.package_pending
            packageUsage.end_date?.let { tvDaysRemainingValue.text = Helper().getDaysRemaining(it)
            }

        })


        sellCarListViewModel.errorMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })

        sellCarListViewModel.errorInProductListMessage.observe(this, Observer { error ->
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()

            vehicleSellListAdapter = VehicleSellListAdapter(mContext, this)
            rvProduct.adapter = vehicleSellListAdapter
            vehicleSellListAdapter.setList(ArrayList())

            if (sellCarListViewModel.isBuyerOfVehicle){
                progressBar.visibility = View.GONE
            }else{
                tvTotalAddedValue.text = "0"
                val productList = ProductList()
                productList.seller_id = Helper().getLoginData(mContext).id
                productList.seller_company_id = sellCarListViewModel.companyId
                productList.package_purchased_id = sellCarListViewModel.packageId
                productList.type = "0"
                sellCarListViewModel.getUsageData(productList)
            }


        })





    }


    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.VISIBLE
        val login = Login()

        if (sellCarListViewModel.isBuyerOfVehicle){

            if (sellCarListViewModel.userType == UserTypeEnum.SELLER.ordinal.toString()){
                // If the vehicle owner is from seller app
                login.seller_id = sellCarListViewModel.sellerIdOfVehicle
                sellCarListViewModel.getSellList(login)
            }else{
                // If the vehicle owner is from user app
                login.user_id = sellCarListViewModel.sellerIdOfVehicle
                login.user_type = sellCarListViewModel.userType
                sellCarListViewModel.getSellListFromUserApp(login)
            }

        }else{
            login.seller_id = Helper().getLoginData(mContext).id
            sellCarListViewModel.getSellList(login)
        }


    }

    override fun onSoldvehicleSelected(sellVehicle: SellVehicle, position: Int) {

        val intent = Intent(mContext, SellVehicleDetailActivity::class.java)

        if (sellCarListViewModel.isBuyerOfVehicle){
            intent.putExtra(IntentKeyEnum.IS_BUYER_OF_VEHICLE.name, true)
        }
        intent.putExtra(IntentKeyEnum.VEHICLE_DETAILS.name, Gson().toJson(sellVehicle))
        startActivity(intent)

    }
}