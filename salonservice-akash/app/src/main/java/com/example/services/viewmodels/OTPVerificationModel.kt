package com.example.services.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.text.TextUtils
import android.view.View
import com.example.services.R
import com.example.services.application.MyApplication
import com.example.services.common.UtilsFunctions
import com.example.services.constants.GlobalConstants
import com.example.services.model.CommonModel
import com.example.services.model.LoginResponse
import com.example.services.repositories.LoginRepository
import com.example.services.sharedpreference.SharedPrefClass
import com.google.gson.JsonObject
import org.json.JSONObject

class OTPVerificationModel : BaseViewModel() {
    override fun isLoading() : LiveData<Boolean> {
    return mIsUpdating   }

    override fun isClick() : LiveData<String> {
        return btnClick     }

    override fun clickListener(v : View) {
        btnClick.value=v.resources.getResourceName(v.id).split("/")[1]
    }

    private val mIsUpdating = MutableLiveData<Boolean>()
    private val btnClick = MutableLiveData<String>()




    val loading : LiveData<Boolean>
        get() = mIsUpdating






}