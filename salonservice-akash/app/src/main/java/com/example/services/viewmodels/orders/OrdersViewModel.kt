package com.example.services.viewmodels.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.view.View
import com.example.services.common.UtilsFunctions
import com.example.services.model.CommonModel
import com.example.services.model.LoginResponse
import com.example.services.model.address.AddressResponse
import com.example.services.model.orders.OrdersListResponse
import com.example.services.repositories.orders.OrdersRepository
import com.example.services.viewmodels.BaseViewModel
import com.google.gson.JsonObject

class OrdersViewModel : BaseViewModel() {
    private var data: MutableLiveData<LoginResponse>? = null
    private var addressDetail = MutableLiveData<AddressResponse>()
    private var deleteAddress = MutableLiveData<CommonModel>()
    private var cancelOrder = MutableLiveData<CommonModel>()
    private var completeOrder = MutableLiveData<CommonModel>()
    private var ordersList = MutableLiveData<OrdersListResponse>()
    private var ordersHistoryList = MutableLiveData<OrdersListResponse>()
    private var ordersRepository = OrdersRepository()
    private val mIsUpdating = MutableLiveData<Boolean>()
    private val btnClick = MutableLiveData<String>()

    init {
        if (UtilsFunctions.isNetworkConnectedWithoutToast()) {
            ordersList = ordersRepository.orderList()
            ordersHistoryList = ordersRepository.orderHistoryList()
            cancelOrder = ordersRepository.cancelOrder(null)
            completeOrder = ordersRepository.completeOrder(null)
        }

    }

    fun getOrdersListRes(): LiveData<OrdersListResponse> {
        return ordersList
    }

    fun getOrdersHistoryListRes(): LiveData<OrdersListResponse> {
        return ordersHistoryList
    }

    fun getCancelOrderRes(): LiveData<CommonModel> {
        return cancelOrder
    }

    fun getCompleteOrderRes(): LiveData<CommonModel> {
        return completeOrder
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

    fun cancelOrder(mJsonObject: JsonObject) {
        if (UtilsFunctions.isNetworkConnected()) {
            cancelOrder = ordersRepository.cancelOrder(mJsonObject)
            mIsUpdating.postValue(true)
        }
    }

    fun completeOrder(mJsonObject: JsonObject) {
        if (UtilsFunctions.isNetworkConnected()) {
            cancelOrder = ordersRepository.completeOrder(mJsonObject)
            mIsUpdating.postValue(true)
        }
    }

    fun getOrderList() {
        if (UtilsFunctions.isNetworkConnected()) {
            ordersList = ordersRepository.orderList()
            mIsUpdating.postValue(true)
        }

    }

}