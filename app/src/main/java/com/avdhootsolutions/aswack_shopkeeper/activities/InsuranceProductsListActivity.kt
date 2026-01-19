package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import androidx.recyclerview.widget.GridLayoutManager
import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.adapters.*
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.VehIcleForEnum
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.InsuranceProductListViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_products.*
import kotlinx.android.synthetic.main.header_title.*
import kotlinx.android.synthetic.main.search.*
import kotlinx.android.synthetic.main.usage_product_offer_notification.*

class InsuranceProductsListActivity : AppCompatActivity(), InsuranceProductListAdapter.ICustomListListener {
    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var insuranceProductListViewModel: InsuranceProductListViewModel

    /**
     * Dialog helper class
     */
    lateinit var dialogHelper: DialogHelper


    /**
     * Adapter for sold vehicle list
     */
    lateinit var insuranceProductListAdapter: InsuranceProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_products)
        mContext = this@InsuranceProductsListActivity
        initView()

        clickListener()
    }

    /**
     * All click events
     */
    private fun clickListener() {
        tvAddProduct.setOnClickListener(View.OnClickListener {
            dialogHelper.openBuySellDialog(VehIcleForEnum.SELL.ordinal,
                "What are you selling for?",
                insuranceProductListViewModel.vehicleList,
                object : VehicleCategoryAdapter.ICustomListListener {
                    override fun onCatgeorySelected(categories: Categories, position: Int) {
                        dialogHelper.dismissDialog()

                        val intent = Intent(mContext, AddInsuranceHeavyEquipRentCarActivity::class.java)
                        intent.putExtra(IntentKeyEnum.VEHICLE_CATEGORY_ID.name, categories.id)
                        intent.putExtra(IntentKeyEnum.COMPANY_ID.name,
                            insuranceProductListViewModel.companyId)
                        intent.putExtra(IntentKeyEnum.PACKAGE_ID.name,
                            insuranceProductListViewModel.packageId)
                        intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name,
                            insuranceProductListViewModel.mainCatId)
                        intent.putExtra(IntentKeyEnum.VEHICLE_CATEGORY_ID.name,
                            categories.id)
                        startActivity(intent)
                    }

                })

        })


        etSearchProduct.addTextChangedListener(object : TextWatcher,
            SparePartsListAdapter.ICustomListListener,
            InsuranceProductListAdapter.ICustomListListener {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {

                insuranceProductListViewModel.searchableProductList = ArrayList<ProductList>()
                if (editable?.length!! > 0) {
                    for (menuItem in insuranceProductListViewModel.productList) {
                        if (menuItem.vehicle_company_name!!.contains(editable.toString(), true) ||
                            menuItem.vehicle_model_name!!.contains(editable.toString(), true) ||
                            menuItem.vehicle_type_name!!.contains(editable.toString(), true) ||
                            menuItem.product_name!!.contains(editable.toString(), true) ||
                            menuItem.serial_number!!.contains(editable.toString(), true) ||
                            menuItem.price!!.contains(editable.toString(), true)
                        ) {

                            insuranceProductListViewModel.searchableProductList.add(menuItem)
                        }
                    }
                } else {
                    insuranceProductListViewModel.searchableProductList =
                        insuranceProductListViewModel.productList
                }


                insuranceProductListAdapter =
                    InsuranceProductListAdapter(mContext, this)
                rvProduct.adapter = insuranceProductListAdapter
                insuranceProductListAdapter.setList(insuranceProductListViewModel.searchableProductList)

            }

            override fun onSoldvehicleSelected(productList: ProductList, position: Int) {

            }

        })


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView() {

        rvProduct.layoutManager = GridLayoutManager(mContext, 2)
        rvProduct.setHasFixedSize(true)

        dialogHelper = DialogHelper(mContext)
        insuranceProductListViewModel = ViewModelProvider(this).get(InsuranceProductListViewModel::class.java)
        insuranceProductListViewModel.companyId =
            intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()
        insuranceProductListViewModel.packageId=
            intent.getStringExtra(IntentKeyEnum.PACKAGE_ID.name).toString()
        insuranceProductListViewModel.mainCatId =  intent.getStringExtra(IntentKeyEnum.MAIN_CAT_ID.name).toString()

        insuranceProductListViewModel.getVehicles()

        insuranceProductListViewModel.productListLiveData.observe(this, Observer { productList ->

            tvProductCount.text =
                productList.size.toString() + " ${resources.getString(R.string.products)}"

            insuranceProductListAdapter = InsuranceProductListAdapter(mContext, this)
            rvProduct.adapter = insuranceProductListAdapter
            insuranceProductListAdapter.setList(insuranceProductListViewModel.productList)

            tvTotalAddedValue.text = productList.size.toString()
            val productList = ProductList()
            productList.seller_id = Helper().getLoginData(mContext).id
            productList.seller_company_id = insuranceProductListViewModel.companyId
            productList.package_purchased_id = insuranceProductListViewModel.packageId
            productList.type = "0"
            insuranceProductListViewModel.getUsageData(productList)
        })


        insuranceProductListViewModel.packageUsedLiveData.observe(this, androidx.lifecycle.Observer { packageUsage ->

            progressBar.visibility = View.GONE
            tvRemainingValue.text = packageUsage.package_pending
            packageUsage.end_date?.let { tvDaysRemainingValue.text = Helper().getDaysRemaining(it)
            }

        })



        insuranceProductListViewModel.errorMessage.observe(this, Observer { error ->
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


        insuranceProductListViewModel.errorInProductListMessage.observe(this, Observer { error ->

            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()

            insuranceProductListAdapter = InsuranceProductListAdapter(mContext, this)
            rvProduct.adapter = insuranceProductListAdapter
            insuranceProductListAdapter.setList(ArrayList())

            tvTotalAddedValue.text = "0"
            val productList = ProductList()
            productList.seller_id = Helper().getLoginData(mContext).id
            productList.seller_company_id = insuranceProductListViewModel.companyId
            productList.package_purchased_id = insuranceProductListViewModel.packageId
            productList.type = "0"
            insuranceProductListViewModel.getUsageData(productList)
        })


        insuranceProductListViewModel.errorOfUsageMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })



        rvProduct.layoutManager = GridLayoutManager(mContext, 2)
        rvProduct.setHasFixedSize(true)
        tvTitle.setText(getString(R.string.products))
        iv_back.setOnClickListener(View.OnClickListener { finish() })

    }


    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.VISIBLE
        val spareParts = SpareParts()
        spareParts.seller_id = Helper().getLoginData(mContext).id
        spareParts.company_seller_id = insuranceProductListViewModel.companyId
        spareParts.package_purchased_id = insuranceProductListViewModel.packageId
        spareParts.master_category_id = insuranceProductListViewModel.mainCatId
        insuranceProductListViewModel.getInsuranceProductList(spareParts)
    }

    override fun onSoldvehicleSelected(productList: ProductList, position: Int) {
        val intent = Intent(mContext, InsuranceHeavyEquipRentCarDetailActivity::class.java)
        intent.putExtra(IntentKeyEnum.PRODUCT_LIST.name, Gson().toJson(productList))
        startActivity(intent)

    }
}