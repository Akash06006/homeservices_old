package com.example.services.viewmodels.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.view.View
import com.example.services.common.UtilsFunctions
import com.example.services.model.CommonModel
import com.example.services.model.LoginResponse
import com.example.services.model.address.AddressResponse
import com.example.services.model.cart.CartListResponse
import com.example.services.model.fav.FavListResponse
import com.example.services.repositories.favorite.FavoriteRepository
import com.example.services.viewmodels.BaseViewModel

class FavoriteViewModel : BaseViewModel() {
    private var data: MutableLiveData<LoginResponse>? = null
    private var addressDetail = MutableLiveData<AddressResponse>()
    private var deleteAddress = MutableLiveData<CommonModel>()

    private var favList = MutableLiveData<FavListResponse>()
    private var favoriteRepository = FavoriteRepository()
    private val mIsUpdating = MutableLiveData<Boolean>()
    private val btnClick = MutableLiveData<String>()

    init {
        if (UtilsFunctions.isNetworkConnectedWithoutToast()) {
            favList = favoriteRepository.favoriteList()
        }

    }

    fun getFavListRes(): LiveData<FavListResponse> {
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

    fun getFavList() {
        if (UtilsFunctions.isNetworkConnected()) {
            favList = favoriteRepository.favoriteList()
            mIsUpdating.postValue(true)
        }

    }

}