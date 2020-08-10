package com.example.services.viewmodels.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.services.common.UtilsFunctions
import com.example.services.model.CommonModel
import com.example.services.repositories.home.HomeJobsRepository
import com.example.services.viewmodels.BaseViewModel

class HomeViewModel : BaseViewModel() {
    private val mIsUpdating = MutableLiveData<Boolean>()
    private val isClick = MutableLiveData<String>()
    private var homeRepository = HomeJobsRepository()
    private var categoriesList = MutableLiveData<CategoriesListResponse>()
    private var subServicesList = MutableLiveData<CategoriesListResponse>()
    private var clearCart = MutableLiveData<CommonModel>()
    /*private var jobsHistoryResponse = MutableLiveData<JobsResponse>()
    private var acceptRejectJob = MutableLiveData<CommonModel>()
    private var startCompleteJob = MutableLiveData<CommonModel>()*/

    init {
        if (UtilsFunctions.isNetworkConnectedWithoutToast()) {
            categoriesList = homeRepository.getCategories("")
            subServicesList = homeRepository.getSubServices("")
            clearCart = homeRepository.clearCart("")
        }

    }

    fun getJobs(): LiveData<CategoriesListResponse> {
        return categoriesList
    }

    fun getGetSubServices(): LiveData<CategoriesListResponse> {
        return subServicesList
    }
    fun getClearCartRes(): LiveData<CommonModel> {
        return clearCart
    }

    override fun isLoading(): LiveData<Boolean> {
        return mIsUpdating
    }

    override fun isClick(): LiveData<String> {
        return isClick
    }

    override fun clickListener(v: View) {
        isClick.value = v.resources.getResourceName(v.id).split("/")[1]
    }

    fun getSubServices(mJsonObject: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            subServicesList = homeRepository.getSubServices(mJsonObject)
            mIsUpdating.postValue(true)
        }

    }

    fun getCategories() {
        if (UtilsFunctions.isNetworkConnected()) {
            categoriesList = homeRepository.getCategories("")
            mIsUpdating.postValue(true)
        }

    }

    fun clearCart(s: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            clearCart = homeRepository.clearCart(s)
            mIsUpdating.postValue(true)
        }

    }

}