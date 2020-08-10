package com.example.services.viewmodels.subcategories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.view.View
import com.example.services.common.UtilsFunctions
import com.example.services.model.LoginResponse
import com.example.services.model.subcategories.SubCategoriesRespnse
import com.example.services.repositories.subcategories.SubCategoriesRepository
import com.example.services.viewmodels.BaseViewModel
import com.google.gson.JsonObject

class SubCategoriesViewModel : BaseViewModel() {
    private var data : MutableLiveData<LoginResponse>? = null
    private var subCatList = MutableLiveData<SubCategoriesRespnse>()

    private var subcategoriesRepository = SubCategoriesRepository()
    private val mIsUpdating = MutableLiveData<Boolean>()
    private val btnClick = MutableLiveData<String>()

    init {
        if (UtilsFunctions.isNetworkConnectedWithoutToast()) {
            subCatList = subcategoriesRepository.getSubCategoriesList(null)
        }

    }

    fun getSubcategoriesRes() : LiveData<SubCategoriesRespnse> {
        return subCatList
    }

    override fun isLoading() : LiveData<Boolean> {
        return mIsUpdating
    }

    override fun isClick() : LiveData<String> {
        return btnClick
    }

    override fun clickListener(v : View) {
        btnClick.value = v.resources.getResourceName(v.id).split("/")[1]
    }

    fun getSubcategories(mJsonObject : JsonObject) {
        if (UtilsFunctions.isNetworkConnected()) {
            subCatList = subcategoriesRepository.getSubCategoriesList(mJsonObject)
            mIsUpdating.postValue(true)
        }

    }



}