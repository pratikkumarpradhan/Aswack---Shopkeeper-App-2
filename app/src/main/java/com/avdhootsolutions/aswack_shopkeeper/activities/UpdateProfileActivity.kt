package com.avdhootsolutions.aswack_shopkeeper.activities

import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.CitiesAdapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.Country1Adapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.StatesAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.GenderEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.models.Register
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.ProfileViewModel
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*

class UpdateProfileActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var picker: DatePickerDialog

    /**
     * View model
     */
    lateinit var profileViewModel: ProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        mContext = this@UpdateProfileActivity
        initView()

        clickListener()
    }

    private fun clickListener() {


        tvDateValue.setOnClickListener(View.OnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val year = cldr[Calendar.YEAR]
            picker = DatePickerDialog(
                mContext!!, R.style.MyTimePickerDialogTheme,
                { view, year, monthOfYear, dayOfMonth -> tvDateValue.setText(year.toString() + "/" + (monthOfYear + 1) + "/" + dayOfMonth.toString()) },
                year,
                month,
                day
            )
            picker!!.show()
        })
        ivBack.setOnClickListener(View.OnClickListener { finish() })

        tvSubmit.setOnClickListener(View.OnClickListener {

            if (etfullname.text.toString().isNullOrEmpty() ||
                etmobileno.text.toString().isNullOrEmpty() ||
                etemail.text.toString().isNullOrEmpty() ||
                etpincode.text.toString().isNullOrEmpty()
            ) {
                Toast.makeText(
                    mContext,
                    resources.getString(R.string.fill_all_details),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
                Toast.makeText(
                    mContext,
                    resources.getString(R.string.password_not_matched),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!rbMale.isChecked && !rbFemale.isChecked) {
                Toast.makeText(
                    mContext,
                    resources.getString(R.string.select_gender),
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                progressBar.visibility = View.VISIBLE

                val register = Register()
                register.seller_id = Helper().getLoginData(mContext).id
                register.name = etfullname.text.toString()
                register.email = etemail.text.toString()
                register.mobile = etmobileno.text.toString()
                register.pincode = etpincode.text.toString()
                register.houseno = ethouseno.text.toString()
                register.streetno = etstreetname.text.toString()
                register.pincode = etpincode.text.toString()
                register.dob = tvDateValue.text.toString()
                if (rbMale.isChecked) {
                    register.gender = GenderEnum.MALE.ordinal.toString()
                } else if (rbFemale.isChecked) {
                    register.gender = GenderEnum.FEMALE.ordinal.toString()
                }

                register.country =
                    profileViewModel.countriesLiveData.value?.get(spCountry.selectedItemPosition)?.id
                register.state =
                    profileViewModel.statesLiveData.value?.get(spState.selectedItemPosition)?.id
                register.city =
                    profileViewModel.citiesLiveData.value?.get(spCity.selectedItemPosition)?.id

                profileViewModel.apiUpdateProfile(register)

            }
        })

    }

    private fun initView() {

        tvAppName.text = resources.getString(R.string.update_profile)
//        etmobileno.keyListener = null
//        etstreetname.keyListener = null
//        ethouseno.keyListener = null
//        etfullname.keyListener = null
//        etemail.keyListener = null
//        etpincode.keyListener = null
        ivEdit.visibility = View.GONE
        etPassword.visibility = View.GONE
        etConfirmPassword.visibility = View.GONE

        etmobileno.setText(intent.getStringExtra(IntentKeyEnum.MOBILE_NUMBER.name))
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)


        profileViewModel.countriesLiveData.observe(
            this,
            androidx.lifecycle.Observer { countryList ->
                val adapter = Country1Adapter(
                    this,
                    countryList
                )

                spCountry.adapter = adapter

                spCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long,
                    ) {

                        Log.e("positionSelected ", position.toString() + " jhuj")

                        val stateData = Register()
                        stateData.country =
                            profileViewModel.countriesLiveData.value?.get(position)?.id
                        profileViewModel.apiGetStateList(stateData)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            })

        profileViewModel.statesLiveData.observe(this, androidx.lifecycle.Observer { stateList ->
            val adapter = StatesAdapter(
                this,
                stateList
            )

            spState.adapter = adapter


            spState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long,
                ) {

                    val cityData = Register()
                    cityData.state = profileViewModel.statesLiveData.value?.get(position)?.id
                    profileViewModel.apiGetCityList(cityData)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        })

        profileViewModel.citiesLiveData.observe(this, androidx.lifecycle.Observer { cityList ->
            val adapter = CitiesAdapter(
                this,
                cityList
            )

            spCity.adapter = adapter


            val login = Login()
            login.seller_id = Helper().getLoginData(mContext).id

            profileViewModel.apiGetProfileDetails(login)

        })

        profileViewModel.successMessageLiveData.observe(
            this,
            androidx.lifecycle.Observer { isScuccess ->
                progressBar.visibility = View.GONE
                if (isScuccess) {
                    Toast.makeText(
                        mContext,
                        resources.getString(R.string.profile_updated_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()

                }
            })


        profileViewModel.profileResponseLiveData.observe(this,
            androidx.lifecycle.Observer { profileData ->
                progressBar.visibility = View.GONE

                etemail.setText(profileData[0].email)
                etmobileno.setText(profileData[0].mobile)
                etfullname.setText(profileData[0].name)
                etpincode.setText(profileData[0].pincode)
                tvDateValue.setText(profileData[0].dob)
                etstreetname.setText(profileData[0].streetno)
                ethouseno.setText(profileData[0].houseno)

                if (profileData[0].gender == GenderEnum.MALE.ordinal.toString()) {
                    rbMale.isChecked = true
                } else if (profileData[0].gender == GenderEnum.FEMALE.ordinal.toString()) {
                    rbFemale.isChecked = true
                }

                profileViewModel.statesLiveData.value?.let { stateList ->
                    for (i in stateList.indices) {
                        if (stateList[i].id == profileData[0].state) {
                            spState.setSelection(i)
                        }
                    }
                }


                profileViewModel.citiesLiveData.value?.let { cityList ->
                    for (i in cityList.indices) {
                        if (cityList[i].id == profileData[0].city) {
                            spCity.setSelection(i)
                        }
                    }
                }


            })

        profileViewModel.errorMessage.observe(this, androidx.lifecycle.Observer { error ->

            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })


    }

    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE
        profileViewModel.apiGetCountries()
    }
}