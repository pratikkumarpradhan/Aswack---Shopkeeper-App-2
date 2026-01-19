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
import com.avdhootsolutions.aswack_shopkeeper.adapters.*
import com.avdhootsolutions.aswack_shopkeeper.enums.GenderEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.models.City
import com.avdhootsolutions.aswack_shopkeeper.models.Register
import com.avdhootsolutions.aswack_shopkeeper.models.States
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.SignUpViewModel
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*

class SignUpActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var picker: DatePickerDialog

    /**
     * View model
     */
    lateinit var signUpViewModel: SignUpViewModel

    /**
     * Firebase token for send notification
     */
    var firebaseToken = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mContext = this@SignUpActivity
        initView()

        clickListener()
    }

    private fun clickListener() {


        tvDateValue.setOnClickListener(View.OnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val year = cldr[Calendar.YEAR]
            picker = DatePickerDialog(mContext!!,R.style.MyTimePickerDialogTheme,
                { view, year, monthOfYear, dayOfMonth -> tvDateValue.setText(year.toString() + "/" + (monthOfYear + 1) + "/" + dayOfMonth.toString()) },
                year,
                month,
                day)
            picker!!.show()
        })
        ivBack.setOnClickListener(View.OnClickListener { finish() })

        tvSubmit.setOnClickListener(View.OnClickListener {

            if (etfullname.text.toString().isNullOrEmpty() ||
                etmobileno.text.toString().isNullOrEmpty() ||
                etemail.text.toString().isNullOrEmpty() ||
                etpincode.text.toString().isNullOrEmpty() ||
                etPassword.text.toString().isNullOrEmpty() ||
                etConfirmPassword.text.toString().isNullOrEmpty()
            ) {
                Toast.makeText(mContext,
                    resources.getString(R.string.fill_all_details),
                    Toast.LENGTH_SHORT).show()
            } else if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
                Toast.makeText(mContext,
                    resources.getString(R.string.password_not_matched),
                    Toast.LENGTH_SHORT).show()
            } else if (!rbMale.isChecked && !rbFemale.isChecked) {
                Toast.makeText(mContext,
                    resources.getString(R.string.select_gender),
                    Toast.LENGTH_SHORT).show()
            } else {

                progressBar.visibility = View.VISIBLE

                val register = Register()
                register.name = etfullname.text.toString()
                register.email = etemail.text.toString()
                register.mobile = etmobileno.text.toString()
                register.password = etPassword.text.toString()
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
                    signUpViewModel.countriesLiveData.value?.get(spCountry.selectedItemPosition)?.id
                register.state =
                    signUpViewModel.statesLiveData.value?.get(spState.selectedItemPosition)?.id
                register.city =
                    signUpViewModel.citiesLiveData.value?.get(spCity.selectedItemPosition)?.id

                if (firebaseToken.isNullOrEmpty()){
                    FirebaseMessaging.getInstance().token.addOnCompleteListener {
                        if(it.isComplete){
                            firebaseToken = it.result.toString()
                            Log.e("firebaseToken ", firebaseToken)

                        }
                    }
                }

                register.device_token = firebaseToken

                signUpViewModel.apiRegister(register)

            }
        })
        llAlreadyRegister.setOnClickListener(View.OnClickListener {
            startActivity(Intent(mContext,
                LoginActivity::class.java))
        })


    }

    private fun initView() {

        etmobileno.setText(intent.getStringExtra(IntentKeyEnum.MOBILE_NUMBER.name))
        etmobileno.keyListener = null

        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isComplete){
                firebaseToken = it.result.toString()
                Log.e("firebaseToken ", firebaseToken)

            }
        }

        signUpViewModel.apiGetCountries()

        signUpViewModel.countriesLiveData.observe(this, androidx.lifecycle.Observer { countryList ->
            val adapter = Country1Adapter(this,
              countryList)

            spCountry.adapter = adapter

            spCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long,
                ) {

                    Log.e("positionSelected " , position.toString() + " jhuj")

                    val stateData = Register()
                    stateData.country = signUpViewModel.countriesLiveData.value?.get(position)?.id
                    signUpViewModel.apiGetStateList(stateData)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        })

        signUpViewModel.statesLiveData.observe(this, androidx.lifecycle.Observer { stateList ->
            val adapter = StatesAdapter(this,
                 stateList)

            spState.adapter = adapter


            spState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long,
                ) {

                    val cityData = Register()
                    cityData.state = signUpViewModel.statesLiveData.value?.get(position)?.id
                    signUpViewModel.apiGetCityList(cityData)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        })

        signUpViewModel.citiesLiveData.observe(this, androidx.lifecycle.Observer { cityList ->
            val adapter = CitiesAdapter(this,
              cityList)

            spCity.adapter = adapter
        })


        signUpViewModel.registerResponseLiveData.observe(this,
            androidx.lifecycle.Observer { registerResponseData ->
                progressBar.visibility = View.GONE
                Toast.makeText(mContext,
                    resources.getString(R.string.registration_successfull),
                    Toast.LENGTH_SHORT).show()
                Helper().setLoginData(registerResponseData[0], mContext)
                val i = Intent(mContext, HomeActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()
            })

        signUpViewModel.errorMessage.observe(this, androidx.lifecycle.Observer { error ->

            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        })



    }
}