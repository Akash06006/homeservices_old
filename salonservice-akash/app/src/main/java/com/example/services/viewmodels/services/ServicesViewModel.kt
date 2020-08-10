package com.example.services.viewmodels.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.view.View
import com.example.services.common.UtilsFunctions
import com.example.services.model.CommonModel
import com.example.services.model.LoginResponse
import com.example.services.model.services.DateSlotsResponse
import com.example.services.model.services.ServicesDetailResponse
import com.example.services.model.services.ServicesListResponse
import com.example.services.model.services.TimeSlotsResponse
import com.example.services.repositories.services.ServicesRepository
import com.example.services.viewmodels.BaseViewModel
import com.google.gson.JsonObject

class ServicesViewModel : BaseViewModel() {
    private var data: MutableLiveData<LoginResponse>? = null
    private var servicesList = MutableLiveData<ServicesListResponse>()
    private var servicesDetail = MutableLiveData<ServicesDetailResponse>()
    private var carRes = MutableLiveData<CommonModel>()
    private var favRes = MutableLiveData<CommonModel>()
    private var timeSlotsList = MutableLiveData<TimeSlotsResponse>()
    private var dateSlots = MutableLiveData<DateSlotsResponse>()
    private var servicesRepository = ServicesRepository()
    private val mIsUpdating = MutableLiveData<Boolean>()
    private val btnClick = MutableLiveData<String>()

    init {
        if (UtilsFunctions.isNetworkConnectedWithoutToast()) {
            servicesList = servicesRepository.getServicesList("")
            servicesDetail = servicesRepository.getServiceDetail("")
            carRes = servicesRepository.addCart(null)
            favRes = servicesRepository.addFav(null)
            favRes = servicesRepository.removeFav("")
            timeSlotsList = servicesRepository.getTimeSlots("")
            // dateSlots = servicesRepository.getDateSlots()
        }

    }

    fun addRemoveCartRes(): LiveData<CommonModel> {
        return carRes
    }

    fun addRemovefavRes(): LiveData<CommonModel> {
        return favRes
    }

    fun getTimeSlotsRes(): LiveData<TimeSlotsResponse> {
        return timeSlotsList
    }

    /* fun getDateSlotsRes(): LiveData<DateSlotsResponse> {
         return dateSlots
     }*/

    fun getServiceDetailRes(): LiveData<ServicesDetailResponse> {
        return servicesDetail
    }

    fun serviceListRes(): LiveData<ServicesListResponse> {
        return servicesList
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

    fun getServices(mJsonObject: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            servicesList = servicesRepository.getServicesList(mJsonObject)
            mIsUpdating.postValue(true)
        }
    }

    fun addCart(mJsonObject: JsonObject) {
        if (UtilsFunctions.isNetworkConnected()) {
            carRes = servicesRepository.addCart(mJsonObject)
            mIsUpdating.postValue(true)
        }
    }

    fun removeCart(mJsonObject: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            carRes = servicesRepository.removeCart(mJsonObject)
            mIsUpdating.postValue(true)
        }
    }

    fun addFav(mJsonObject: JsonObject) {
        if (UtilsFunctions.isNetworkConnected()) {
            favRes = servicesRepository.addFav(mJsonObject)
            mIsUpdating.postValue(true)
        }
    }

    fun removeFav(mJsonObject: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            favRes = servicesRepository.removeFav(mJsonObject)
            mIsUpdating.postValue(true)
        }
    }

    fun getServiceDetail(mJsonObject: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            servicesDetail = servicesRepository.getServiceDetail(mJsonObject)
            mIsUpdating.postValue(true)
        }
    }

    fun getTimeSlot(mJsonObject: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            timeSlotsList = servicesRepository.getTimeSlots(mJsonObject)
            mIsUpdating.postValue(true)
        }
    }

    /*fun getDateSlots() {
        if (UtilsFunctions.isNetworkConnected()) {
            dateSlots = servicesRepository.getDateSlots()
            mIsUpdating.postValue(true)
        }
    }*/

}