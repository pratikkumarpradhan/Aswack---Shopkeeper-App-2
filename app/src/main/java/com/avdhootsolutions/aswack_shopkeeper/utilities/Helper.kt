package com.avdhootsolutions.aswack_shopkeeper.utilities

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.models.Register
import com.google.gson.Gson
import com.squareup.okhttp.OkHttpClient
import kotlinx.android.synthetic.main.activity_sell_vehical.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


class Helper(){

    companion object {
        const val TAG = "Helper"
        const val PACKAGE = "package"
        const val RFQ_CLOSE = "2"
        const val PICK_IMAGE_MULTIPLE = 11;
    }


    val mClient = OkHttpClient()


    fun setPhoneNo(phoneNo : String, context: Context){
        val editor = context.getSharedPreferences("PHONE_NO", AppCompatActivity.MODE_PRIVATE).edit()
        editor.putString("phoneNo", phoneNo)
        editor.apply()

    }

    fun getPhoneNo(context: Context) : String{

        val prefs = context.getSharedPreferences("PHONE_NO", AppCompatActivity.MODE_PRIVATE)
        val phoneNo =
            prefs.getString("phoneNo", "") //"No name defined" is the default value.

        return phoneNo?:""
    }

    /**
     * Set currency Symbol
     * @param context context of the class
     * @param symbol selected symbol
     */
    fun setCurrencySymbol(symbol : String, context: Context){
        val editor = context.getSharedPreferences("CURRENCY_SYMBOL", AppCompatActivity.MODE_PRIVATE).edit()
        editor.putString("CURRENCY_SYMBOL", symbol)
        editor.apply()

    }

    /**
     * Get currency Symbol
     * @param context context of the class
     */
    fun getCurrencySymbol(context: Context) : String{

        val prefs = context.getSharedPreferences("CURRENCY_SYMBOL", AppCompatActivity.MODE_PRIVATE)
        val currencySymbol =
            prefs.getString("CURRENCY_SYMBOL", context.resources.getString(R.string.rupee_symbol)) //"No name defined" is the default value.

        return currencySymbol?:context.resources.getString(R.string.rupee_symbol)
    }


    /**
     * Save registration/Login data into shared preference
     * @param register model class
     * @param context context of the class
     */
    fun setLoginData(register : Register, context: Context){
        val regiterData = Gson().toJson(register)
        val editor = context.getSharedPreferences("REGISTER_DATA", AppCompatActivity.MODE_PRIVATE).edit()
        editor.putString("registerData", regiterData)
        editor.apply()

    }


    /**
     * Get save data of the login
     * @param context context of the class
     */
    fun getLoginData(context: Context) : Register{
        val prefs = context.getSharedPreferences("REGISTER_DATA", AppCompatActivity.MODE_PRIVATE)
        val registerData =
            prefs.getString("registerData", "") //"No name defined" is the default value.


        if (registerData.isNullOrEmpty()){
            return Register()
        }else{
            return Gson().fromJson(registerData, Register::class.java)

        }

    }


    /**
     * Save registration/Login data into shared preference
     * @param register model class
     * @param context context of the class
     */
    fun clearLoginData(context: Context){
        val editor = context.getSharedPreferences("REGISTER_DATA", AppCompatActivity.MODE_PRIVATE).edit()
        editor.putString("registerData", "")
        editor.apply()

    }


    /**
     * Get how many days remaining
     * @param endDate end date
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDaysRemaining(endDate : String) : String{

        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate =  sdf.format(cal.time)

        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDateValue: LocalDate = LocalDate.parse(currentDate, dateFormatter)
        val endDateValue: LocalDate = LocalDate.parse(endDate, dateFormatter)
        var days = ChronoUnit.DAYS.between(startDateValue, endDateValue) + 1

        if (days < 0){
            days = 0
        }

        return days.toString()
    }


    /**
     * Check that start date is greater than en date
     */
    fun isEndDateIsGreaterThanStartDate(d1 : String, d2 : String) : Boolean{
        Log.e("startDate " , d1)
        Log.e("endDate " , d2)
        val dfDate = SimpleDateFormat("yyyy/MM/dd")

        var b = false
        try {
            b = if (dfDate.parse(d1).before(dfDate.parse(d2))) {
                true //If start date is before end date
            } else dfDate.parse(d1).equals(dfDate.parse(d2))
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return b
    }
}