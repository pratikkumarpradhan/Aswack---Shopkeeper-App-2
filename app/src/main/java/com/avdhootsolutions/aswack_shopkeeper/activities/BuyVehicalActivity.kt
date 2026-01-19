package com.avdhootsolutions.aswack_shopkeeper.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.*
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.TransmissionEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.UserTypeEnum
import com.avdhootsolutions.aswack_shopkeeper.models.BuyVehicle
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.BuyVehicleViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_buy_vehical.*
import kotlinx.android.synthetic.main.header_title.*

class BuyVehicalActivity : AppCompatActivity(), BrandsCheckBoxAdapter.ICustomListListener,
    VehicleTypeCheckBoxAdapter.ICustomListListener,
    VehicleModelCheckBoxAdapter.ICustomListListener, FuelCheckBoxAdapter.ICustomListListener,
    YearCheckBoxAdapter.ICustomListListener {
    lateinit var mContext: Context

    /**
     * View model
     */
    lateinit var buyVehicleViewModel: BuyVehicleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_vehical)
        mContext = this@BuyVehicalActivity
        initView()

        clickListener()
    }


    /**
     * All Click events
     */
    private fun clickListener() {

        iv_back.setOnClickListener { finish() }

        rlSearch.setOnClickListener {

            val buyVehicle = BuyVehicle()
            buyVehicle.user_id = Helper().getLoginData(mContext).id
            buyVehicle.category = buyVehicleViewModel.categoryId
            if (rbAutomatic.isChecked) {
                buyVehicle.transmission = TransmissionEnum.AUTOMATIC.ordinal.toString()
            } else if (rbManual.isChecked) {
                buyVehicle.transmission = TransmissionEnum.MANUAL.ordinal.toString()
            }
            buyVehicle.user_type = UserTypeEnum.SELLER.ordinal.toString()

            for (i in buyVehicleViewModel.brandsTypeModelList.indices) {
                if (buyVehicleViewModel.brandsTypeModelList[i].isChecked) {
                    buyVehicleViewModel.brandsTypeModelList[i].vehicle_brand_id?.let { it1 ->
                        buyVehicle.vehicle_brand.add(it1)
                    }
                }
            }

            for (i in buyVehicleViewModel.typeList.indices) {
                if (buyVehicleViewModel.typeList[i].isChecked) {
                    buyVehicleViewModel.typeList[i].vehicle_type_id?.let { it1 ->
                        buyVehicle.vehicle_type.add(it1)
                    }
                }
            }

            for (i in buyVehicleViewModel.modelList.indices) {
                if (buyVehicleViewModel.modelList[i].isChecked) {
                    buyVehicleViewModel.modelList[i].vehicle_model_id?.let { it1 ->
                        buyVehicle.vehicle_model.add(it1)
                    }
                }
            }

            for (i in buyVehicleViewModel.fuelList.indices) {
                if (buyVehicleViewModel.fuelList[i].isChecked) {
                    buyVehicleViewModel.fuelList[i].id?.let { it1 ->
                        buyVehicle.vehicle_fuel.add(it1)
                    }
                }
            }

            for (i in buyVehicleViewModel.yearList.indices) {
                if (buyVehicleViewModel.yearList[i].isChecked) {
                    buyVehicleViewModel.yearList[i].id?.let { it1 ->
                        buyVehicle.vehicle_year.add(it1)
                    }
                }
            }


            val intent = Intent(mContext, SearchVehicleListActivity::class.java)
            intent.putExtra(IntentKeyEnum.BUY_CAR_DATA.name, Gson().toJson(buyVehicle))
            startActivity(intent)


        }
    }

    private fun initView() {

        tvTitle.text = resources.getString(R.string.search_vehicles)

        rvBrand.layoutManager = GridLayoutManager(mContext, 2)
        rvBrand.setHasFixedSize(true)

        rvType.layoutManager = GridLayoutManager(mContext, 2)
        rvType.setHasFixedSize(true)

        rvModel.layoutManager = GridLayoutManager(mContext, 2)
        rvModel.setHasFixedSize(true)

        rvYear.layoutManager = GridLayoutManager(mContext, 2)
        rvYear.setHasFixedSize(true)

        rvFuel.layoutManager = GridLayoutManager(mContext, 2)
        rvFuel.setHasFixedSize(true)



        buyVehicleViewModel = ViewModelProvider(this).get(BuyVehicleViewModel::class.java)

        buyVehicleViewModel.categoryId = intent.getStringExtra(IntentKeyEnum.CAT_ID.name).toString()

        progressBar.visibility = View.VISIBLE
        val sellVehicle = SellVehicle()
        sellVehicle.category = buyVehicleViewModel.categoryId
        buyVehicleViewModel.apiGetBrandsTypesModels(sellVehicle)

        buyVehicleViewModel.brandTypeModelsLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                progressBar.visibility = View.GONE
                val brandAdapter = BrandsCheckBoxAdapter(mContext, this)
                rvBrand.adapter = brandAdapter
                brandAdapter.setList(buyVehicleViewModel.brandsTypeModelList)
            })


        buyVehicleViewModel.fuelListLiveData.observe(this, androidx.lifecycle.Observer {
            val adapter = FuelCheckBoxAdapter(
                this,
                this
            )
            progressBar.visibility = View.GONE
            rvFuel.adapter = adapter
            adapter.setList(buyVehicleViewModel.fuelList)

        })

        buyVehicleViewModel.yearListLiveData.observe(this, androidx.lifecycle.Observer {
            val adapter = YearCheckBoxAdapter(
                this,
                this
            )

            rvYear.adapter = adapter
            adapter.setList(buyVehicleViewModel.yearList)
        })


        buyVehicleViewModel.errorMessage.observe(this, androidx.lifecycle.Observer { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


    }

    override fun onBrandSelected() {
        val typesAdapter = VehicleTypeCheckBoxAdapter(mContext, this)
        rvType.adapter = typesAdapter
        typesAdapter.setList(buyVehicleViewModel.getAllTytpes())
    }

    override fun onTypesSelected() {
        val modelsAdapter = VehicleModelCheckBoxAdapter(mContext, this)
        rvModel.adapter = modelsAdapter
        modelsAdapter.setList(buyVehicleViewModel.getAllModels())
    }

    override fun onModelSelected() {
    }

    override fun onFuelSelected() {

    }

    override fun onYearSelected() {

    }
}