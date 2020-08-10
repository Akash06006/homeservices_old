package com.example.services.viewmodels.vendor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.view.View
import com.example.services.common.UtilsFunctions
import com.example.services.model.CommonModel
import com.example.services.model.LoginResponse
import com.example.services.model.address.AddressResponse
import com.example.services.model.cart.CartListResponse
import com.example.services.model.fav.FavListResponse
import com.example.services.model.vendor.VendorListResponse
import com.example.services.repositories.favorite.FavoriteRepository
import com.example.services.repositories.vendor.VendorsRepository
import com.example.services.viewmodels.BaseViewModel

class VendorsViewModel : BaseViewModel() {
    private var data: MutableLiveData<LoginResponse>? = null
    private var addressDetail = MutableLiveData<AddressResponse>()
    private var deleteAddress = MutableLiveData<CommonModel>()

    private var favList = MutableLiveData<VendorListResponse>()
    private var vendorRepository = VendorsRepository()
    private val mIsUpdating = MutableLiveData<Boolean>()
    private val btnClick = MutableLiveData<String>()

    init {
        if (UtilsFunctions.isNetworkConnectedWithoutToast()) {
            favList = vendorRepository.vendorList("", "", "")
        }

    }

    fun getVendorListRes(): LiveData<VendorListResponse> {
        return favList
    }


    override fun isLoading(): LiveData<Boolean> {
        return mIsUpdating
    }

    override fun isClick(): LiveData<String> {
        return btnClick
    }

    override fun clickListener(v: View) {
        btnClick.value = v.resources.getResourceName(v.id).split("/")[1]
    }

    /* fun addRemoveCart(mJsonObject : JsonObject) {
         if (UtilsFunctions.isNetworkConnected()) {
             addressDetail = cartRepository.addAddress(mJsonObject)
             mIsUpdating.postValue(true)
         }
     }*/

    fun getVendorList(id: String, lat: String, longi: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            favList = vendorRepository.vendorList(id, lat, longi)
            mIsUpdating.postValue(true)
        }

    }

}