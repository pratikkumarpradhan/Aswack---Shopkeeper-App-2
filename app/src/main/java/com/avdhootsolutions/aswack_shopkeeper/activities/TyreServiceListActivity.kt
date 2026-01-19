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
import com.avdhootsolutions.aswack_shopkeeper.adapters.TyreServiceListAdapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.VehicleCategoryAdapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.VehicleSellListAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.VehIcleForEnum
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.TyreServiceListViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_products.*
import kotlinx.android.synthetic.main.activity_my_products.progressBar
import kotlinx.android.synthetic.main.activity_search_car_list.*
import kotlinx.android.synthetic.main.header_title.*
import kotlinx.android.synthetic.main.search.*
import kotlinx.android.synthetic.main.search.etSearchProduct
import kotlinx.android.synthetic.main.usage_product_offer_notification.*

class TyreServiceListActivity : AppCompatActivity(), TyreServiceListAdapter.ICustomListListener {
    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var tyreServiceListViewModel: TyreServiceListViewModel

    /**
     * Dialog helper class
     */
    lateinit var dialogHelper: DialogHelper


    /**
     * Adapter for sold vehicle list
     */
    lateinit var tyreServiceListAdapter: TyreServiceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_products)
        mContext = this@TyreServiceListActivity
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
                tyreServiceListViewModel.vehicleList,
                object : VehicleCategoryAdapter.ICustomListListener {
                    override fun onCatgeorySelected(categories: Categories, position: Int) {
                        dialogHelper.dismissDialog()

                        val intent = Intent(mContext, AddTyreServiceActivity::class.java)
                        intent.putExtra(IntentKeyEnum.VEHICLE_CATEGORY_ID.name, categories.id)
                        intent.putExtra(IntentKeyEnum.COMPANY_ID.name,
                            tyreServiceListViewModel.companyId)
                        intent.putExtra(IntentKeyEnum.PACKAGE_ID.name,
                            tyreServiceListViewModel.packageId)
                        intent.putExtra(IntentKeyEnum.MAIN_CAT_ID.name,
                            tyreServiceListViewModel.mainCatId)
                        startActivity(intent)
                    }

                })

        })


        etSearchProduct.addTextChangedListener(object : TextWatcher,
            SparePartsListAdapter.ICustomListListener, TyreServiceListAdapter.ICustomListListener {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {

                tyreServiceListViewModel.searchableProductList = ArrayList<ProductList>()
                if (editable?.length!! > 0) {
                    for (menuItem in tyreServiceListViewModel.productList) {
                        if (menuItem.vehicle_company_name!!.contains(editable.toString(), true) ||
                            menuItem.vehicle_model_name!!.contains(editable.toString(), true) ||
                            menuItem.vehicle_type_name!!.contains(editable.toString(), true) ||
                            menuItem.serial_number!!.contains(editable.toString(), true) ||
                            menuItem.product_name!!.contains(editable.toString(), true) ||
                            menuItem.price!!.contains(editable.toString(), true)
                        ) {

                            tyreServiceListViewModel.searchableProductList.add(menuItem)
                        }
                    }
                } else {
                    tyreServiceListViewModel.searchableProductList =
                        tyreServiceListViewModel.productList
                }


                tyreServiceListAdapter =
                    TyreServiceListAdapter(mContext, this)
                rvProduct.adapter = tyreServiceListAdapter
                tyreServiceListAdapter.setList(tyreServiceListViewModel.searchableProductList)

            }

            override fun onSoldvehicleSelected(productList: ProductList, position: Int) {

            }

        })


    }

    private fun initView() {

        rvProduct.layoutManager = GridLayoutManager(mContext, 2)
        rvProduct.setHasFixedSize(true)

        dialogHelper = DialogHelper(mContext)
        tyreServiceListViewModel = ViewModelProvider(this).get(TyreServiceListViewModel::class.java)
        tyreServiceListViewModel.companyId =
            intent.getStringExtra(IntentKeyEnum.COMPANY_ID.name).toString()
        tyreServiceListViewModel.packageId=
            intent.getStringExtra(IntentKeyEnum.PACKAGE_ID.name).toString()
        tyreServiceListViewModel.mainCatId =  intent.getStringExtra(IntentKeyEnum.MAIN_CAT_ID.name).toString()



        tyreServiceListViewModel.productListLiveData.observe(this, Observer { productList ->

            tvProductCount.text =
                productList.size.toString() + " ${resources.getString(R.string.products)}"

            tyreServiceListAdapter = TyreServiceListAdapter(mContext, this)
            rvProduct.adapter = tyreServiceListAdapter
            tyreServiceListAdapter.setList(tyreServiceListViewModel.productList)

            tvTotalAddedValue.text = productList.size.toString()
            val productList = ProductList()
            productList.seller_id = Helper().getLoginData(mContext).id
            productList.seller_company_id = tyreServiceListViewModel.companyId
            productList.package_purchased_id = tyreServiceListViewModel.packageId
            productList.type = "0"
            tyreServiceListViewModel.getUsageData(productList)
        })


        tyreServiceListViewModel.packageUsedLiveData.observe(this, androidx.lifecycle.Observer { packageUsage ->

            progressBar.visibility = View.GONE
            tvRemainingValue.text = packageUsage.package_pending
            packageUsage.end_date?.let { tvDaysRemainingValue.text = Helper().getDaysRemaining(it)
            }

        })

        tyreServiceListViewModel.errorMessage.observe(this, Observer { error ->
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })

        tyreServiceListViewModel.errorInProductListMessage.observe(this, Observer { error ->

            tyreServiceListAdapter = TyreServiceListAdapter(mContext, this)
            rvProduct.adapter = tyreServiceListAdapter
            tyreServiceListAdapter.setList(ArrayList())


            tvTotalAddedValue.text = "0"
            val productList = ProductList()
            productList.seller_id = Helper().getLoginData(mContext).id
            productList.seller_company_id = tyreServiceListViewModel.companyId
            productList.package_purchased_id = tyreServiceListViewModel.packageId
            productList.type = "0"
            tyreServiceListViewModel.getUsageData(productList)
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })

        tyreServiceListViewModel.errorOfUsageMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


        rvProduct.layoutManager = GridLayoutManager(mContext, 2)
        rvProduct.setHasFixedSize(true)
        tvTitle.setText(getString(R.string.tyre_services))
        iv_back.setOnClickListener(View.OnClickListener { finish() })

    }


    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE
        val spareParts = SpareParts()
        spareParts.seller_id = Helper().getLoginData(mContext).id
        spareParts.company_seller_id = tyreServiceListViewModel.companyId
        spareParts.package_purchased_id = tyreServiceListViewModel.packageId
        spareParts.master_category_id = tyreServiceListViewModel.mainCatId
        tyreServiceListViewModel.getTyreServiceList(spareParts)
    }


    override fun onSoldvehicleSelected(productList: ProductList, position: Int) {
        val intent = Intent(mContext, TyreServiceDetailActivity::class.java)
        intent.putExtra(IntentKeyEnum.PRODUCT_LIST.name, Gson().toJson(productList))
        startActivity(intent)

    }
}