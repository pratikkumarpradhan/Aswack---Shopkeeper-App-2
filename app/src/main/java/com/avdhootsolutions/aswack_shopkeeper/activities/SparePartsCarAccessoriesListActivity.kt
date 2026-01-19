package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import androidx.recyclerview.widget.GridLayoutManager
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.adapters.SparePartsListAdapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.VehicleCategoryAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.VehIcleForEnum
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.SparePartsListViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_products.*
import kotlinx.android.synthetic.main.activity_my_products.progressBar
import kotlinx.android.synthetic.main.activity_my_products.tvProductCount
import kotlinx.android.synthetic.main.header_title.*
import kotlinx.android.synthetic.main.search.*
import kotlinx.android.synthetic.main.usage_product_offer_notification.*

class SparePartsCarAccessoriesListActivity : AppCompatActivity(),
    SparePartsListAdapter.ICustomListListener {
    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var sparePartsListViewModel: SparePartsListViewModel

    /**
     * Dialog helper class
     */
    lateinit var dialogHelper: DialogHelper


    /**
     * Adapter for sold vehicle list
     */
    lateinit var sparePartsListAdapter: SparePartsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_products)
        mContext = this@SparePartsCarAccessoriesListActivity
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
                sparePartsListViewModel.vehicleList,
                object : VehicleCategoryAdapter.ICustomListListener {
                    override fun onCatgeorySelected(categories: Categories, position: Int) {
                        dialogHelper.dismissDialog()

                        val intent = Intent(mContext, AddSparePartsCarAccesoriesActivity::class.java)
                        intent.putExtra(IntentKeyEnum.VEHICLE_CATEGORY_ID.name, categories.id)
                        intent.putExtra(IntentKeyEnum.COMPANY_ID.name,
                            sparePartsListViewModel.companyId)
                        intent.putExtra(IntentKeyEnum.PACKAGE_ID.name,
                            sparePartsListViewModel.packageId)
                        intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name,
                            sparePartsListViewModel.mainCatId)
                        startActivity(intent)
                    }

                })

        })


        etSearchProduct.addTextChangedListener(object : TextWatcher,
            SparePartsListAdapter.ICustomListListener {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {

                sparePartsListViewModel.searchableProductList = ArrayList<ProductList>()
                if (editable?.length!! > 0) {
                    for (menuItem in sparePartsListViewModel.productList) {
                        if (menuItem.vehicle_company_name!!.contains(editable.toString(), true) ||
                            menuItem.vehicle_model_name!!.contains(editable.toString(), true) ||
                            menuItem.vehicle_type_name!!.contains(editable.toString(), true) ||
                            menuItem.product_name!!.contains(editable.toString(), true) ||
                            menuItem.product_code!!.contains(editable.toString(), true) ||
                            menuItem.serial_number!!.contains(editable.toString(), true) ||
                            menuItem.price!!.contains(editable.toString(), true)
                        ) {

                            sparePartsListViewModel.searchableProductList.add(menuItem)
                        }
                    }
                } else {
                    sparePartsListViewModel.searchableProductList =
                        sparePartsListViewModel.productList
                }


                sparePartsListAdapter =
                    SparePartsListAdapter(mContext, this)
                rvProduct.adapter = sparePartsListAdapter
                sparePartsListAdapter.setList(sparePartsListViewModel.searchableProductList)

            }

            override fun onSoldvehicleSelected(productList: ProductList, position: Int) {

            }

        })


    }

    private fun initView() {

        rvProduct.layoutManager = GridLayoutManager(mContext, 2)
        rvProduct.setHasFixedSize(true)

        dialogHelper = DialogHelper(mContext)
        sparePartsListViewModel = ViewModelProvider(this).get(SparePartsListViewModel::class.java)
        sparePartsListViewModel.companyId =
            intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()
        sparePartsListViewModel.packageId=
            intent.getStringExtra(IntentKeyEnum.PACKAGE_ID.name).toString()
        sparePartsListViewModel.mainCatId =  intent.getStringExtra(IntentKeyEnum.MAIN_CAT_ID.name).toString()



        if (sparePartsListViewModel.mainCatId == "6"){
            tvTitle.text = resources.getString(R.string.spare_parts)
        }else if (sparePartsListViewModel.mainCatId == "7"){
            tvTitle.text = resources.getString(R.string.car_accessories)
        }

        sparePartsListViewModel.productListLiveData.observe(this, Observer { productList ->
            tvProductCount.text =
                productList.size.toString() + " ${resources.getString(R.string.products)}"

            sparePartsListAdapter = SparePartsListAdapter(mContext, this)
            rvProduct.adapter = sparePartsListAdapter
            sparePartsListAdapter.setList(sparePartsListViewModel.productList)

            tvTotalAddedValue.text = productList.size.toString()
            val productList = ProductList()
            productList.seller_id = Helper().getLoginData(mContext).id
            productList.seller_company_id = sparePartsListViewModel.companyId
            productList.package_purchased_id = sparePartsListViewModel.packageId
            productList.type = "0"
            sparePartsListViewModel.getUsageData(productList)
        })


        sparePartsListViewModel.packageUsedLiveData.observe(this, androidx.lifecycle.Observer { packageUsage ->

            progressBar.visibility = View.GONE
            tvRemainingValue.text = packageUsage.package_pending
            packageUsage.end_date?.let { tvDaysRemainingValue.text = Helper().getDaysRemaining(it)
            }

        })

        sparePartsListViewModel.errorMessage.observe(this, Observer { error ->
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })

        sparePartsListViewModel.errorInProductListMessage.observe(this, Observer { error ->

            sparePartsListAdapter = SparePartsListAdapter(mContext, this)
            rvProduct.adapter = sparePartsListAdapter
            sparePartsListAdapter.setList(ArrayList())

            tvTotalAddedValue.text = "0"
            val productList = ProductList()
            productList.seller_id = Helper().getLoginData(mContext).id
            productList.seller_company_id = sparePartsListViewModel.companyId
            productList.package_purchased_id = sparePartsListViewModel.packageId
            productList.type = "0"
            sparePartsListViewModel.getUsageData(productList)
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })




        sparePartsListViewModel.errorOfUsageMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })

        rvProduct.layoutManager = GridLayoutManager(mContext, 2)
        rvProduct.setHasFixedSize(true)

        iv_back.setOnClickListener(View.OnClickListener { finish() })

    }


    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.VISIBLE
        val spareParts = SpareParts()
        spareParts.seller_id = Helper().getLoginData(mContext).id
        spareParts.company_seller_id = sparePartsListViewModel.companyId
        spareParts.package_purchased_id = sparePartsListViewModel.packageId
        spareParts.master_category_id = sparePartsListViewModel.mainCatId
        sparePartsListViewModel.getSparePartsOrCarAssessorsList(spareParts)
    }

    override fun onSoldvehicleSelected(productList: ProductList, position: Int) {
        val intent = Intent(mContext, SparePartsCarAccesoriesDetailActivity::class.java)
        intent.putExtra(IntentKeyEnum.PRODUCT_LIST.name, Gson().toJson(productList))
        startActivity(intent)

    }
}