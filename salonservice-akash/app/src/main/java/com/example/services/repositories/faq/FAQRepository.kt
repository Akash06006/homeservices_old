package com.example.services.repositories.faq

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.example.services.R
import com.example.services.api.ApiClient
import com.example.services.api.ApiResponse
import com.example.services.api.ApiService
import com.example.services.application.MyApplication
import com.example.services.common.UtilsFunctions
import com.example.services.model.CommonModel
import com.example.services.model.faq.FAQListResponse
import com.example.services.model.notificaitons.NotificationsListResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import retrofit2.Response

class FAQRepository {
    private var data: MutableLiveData<FAQListResponse>? = null
    private var data1: MutableLiveData<CommonModel>? = null
    private val gson = GsonBuilder().serializeNulls().create()

    init {
        data = MutableLiveData()
        data1 = MutableLiveData()
    }

    fun getFAQList(userId: String): MutableLiveData<FAQListResponse> {
        if (!TextUtils.isEmpty(userId)) {
            val mApiService = ApiService<JsonObject>()
            mApiService.get(
                object : ApiResponse<JsonObject> {
                    override fun onResponse(mResponse: Response<JsonObject>) {
                        val loginResponse = if (mResponse.body() != null)
                            gson.fromJson<FAQListResponse>(
                                "" + mResponse.body(),
                                FAQListResponse::class.java
                            )
                        else {
                            gson.fromJson<FAQListResponse>(
                                mResponse.errorBody()!!.charStream(),
                                FAQListResponse::class.java
                            )
                        }
                        data!!.postValue(loginResponse)
                    }

                    override fun onError(mKey: String) {
                        UtilsFunctions.showToastError(MyApplication.instance.getString(R.string.internal_server_error))
                        data!!.postValue(null)
                    }

                }, ApiClient.getApiInterface().getFAQList("100000", "1")
            )
        }
        return data!!
    }


}