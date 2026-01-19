package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avdhootsolutions.aswack_shopkeeper.R
import androidx.recyclerview.widget.GridLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.adapters.MyProductsAdapter
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.adapters.VehicleSearchListAdapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.VehicleSellListAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.BuyVehicle
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.SearchCarListViewModel
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.SellCarListViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_products.*
import kotlinx.android.synthetic.main.activity_search_car_list.*
import kotlinx.android.synthetic.main.activity_search_car_list.progressBar
import kotlinx.android.synthetic.main.header_title.*

class SearchVehicleListActivity : AppCompatActivity(), VehicleSellListAdapter.ICustomListListener,
    VehicleSearchListAdapter.ICustomListListener {
    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var searchCarListViewModel: SearchCarListViewModel



    /**
     * Adapter for sold vehicle list
     */
    lateinit var vehicleSearchListAdapter: VehicleSearchListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_car_list)
        mContext = this@SearchVehicleListActivity
        initView()

        clickListener()
    }

    /**
     * All click events
     */
    private fun clickListener() {
        icFilter.setOnClickListener(View.OnClickListener {
            finish()
        })


        etSearchProduct.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {

                searchCarListViewModel.searchableBuyVehicleList = ArrayList<SellVehicle>()
                if (editable?.length!! > 0){
                    for (menuItem in searchCarListViewModel.buyVehicleList){
                        if (menuItem.vehicle_brand_name!!.contains(editable.toString()) ||
                            menuItem.vehicle_model_name!!.contains(editable.toString()) ||
                            menuItem.vehicle_type_name!!.contains(editable.toString()) ||
//                            menuItem.price!!.contains(editable.toString(), true) ||
                            menuItem.vehicle_fuel_name!!.contains(editable.toString()) ||
                            menuItem.vehicle_year_name!!.contains(editable.toString())  ){

                            searchCarListViewModel.searchableBuyVehicleList.add(menuItem)
                        }
                    }
                }else{
                    searchCarListViewModel.searchableBuyVehicleList = searchCarListViewModel.buyVehicleList
                }


//                vehicleSellListAdapter = VehicleSellListAdapter(mContext, this@SearchVehicleListActivity)
//                rvProduct.adapter = vehicleSellListAdapter
//                vehicleSellListAdapter.setList(sellCarListViewModel.searchableVehicleSellList)

            }

        })



    }

    private fun initView() {


        rvCar.setLayoutManager(LinearLayoutManager(mContext))
        rvCar.setHasFixedSize(true)

        searchCarListViewModel = ViewModelProvider(this).get(SearchCarListViewModel::class.java)

        progressBar.visibility = View.VISIBLE

        searchCarListViewModel.buyVehicleListLiveData.observe(this, Observer { buyVehicleList ->
            progressBar.visibility = View.GONE
            vehicleSearchListAdapter = VehicleSearchListAdapter(mContext, this)
            rvCar.adapter = vehicleSearchListAdapter
            vehicleSearchListAdapter.setList(buyVehicleList)

            tvTitleFound.text = buyVehicleList.size.toString() +" vehicles found"
        })



        searchCarListViewModel.errorMessage.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })
        tvTitle.setText(getString(R.string.search_result_of_vehicles))
        iv_back.setOnClickListener(View.OnClickListener { finish() })

    }


    override fun onResume() {
        super.onResume()
        searchCarListViewModel.searchFilter = Gson().fromJson(intent.getStringExtra(IntentKeyEnum.BUY_CAR_DATA.name), BuyVehicle::class.java)
        searchCarListViewModel.getBuyVehicle(searchCarListViewModel.searchFilter)
    }

    override fun onSoldvehicleSelected(sellVehicle: SellVehicle, position: Int) {
        val intent = Intent(mContext, SearchVehicleDetailActivity::class.java)
        intent.putExtra(IntentKeyEnum.VEHICLE_DETAILS.name, Gson().toJson(sellVehicle))
        startActivity(intent)

    }
}