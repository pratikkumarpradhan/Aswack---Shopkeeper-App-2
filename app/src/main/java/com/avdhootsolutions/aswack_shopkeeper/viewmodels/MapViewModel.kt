package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.avdhootsolutions.aswack_shopkeeper.models.AddressData
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.google.firebase.firestore.FirebaseFirestore



/**
 * ViewModel for menu list page
 */
class MapViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * live data for item list
     */
    var addedAddressLiveData = MutableLiveData<AddressData>()


    private val helper = Helper()

     init {
     }





}

