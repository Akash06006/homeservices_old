package com.example.services.repositories.subcategories

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.example.services.R
import com.example.services.api.ApiClient
import com.example.services.api.ApiResponse
import com.example.services.api.ApiService
import com.example.services.application.MyApplication
import com.example.services.common.UtilsFunctions
import com.example.services.model.CommonModel
import com.example.services.model.address.AddressListResponse
import com.example.services.model.address.AddressResponse
import com.example.services.model.subcategories.SubCategoriesRespnse
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import retrofit2.Response

class SubCategoriesRepository {
    private var data : MutableLiveData<SubCategoriesRespnse>? = null
    private val gson = GsonBuilder().serializeNulls().create()

    init {
        data = MutableLiveData()
    }

    fun getSubCategoriesList(jsonObject : JsonObject?) : MutableLiveData<SubCategoriesRespnse> {
        if (jsonObject != null) {
            val mApiService = ApiService<JsonObject>()
            mApiService.get(
                object : ApiResponse<JsonObject> {
                    override fun onResponse(mResponse : Response<JsonObject>) {
                        val loginResponse = if (mResponse.body() != null)
                            gson.fromJson<SubCategoriesRespnse>(
                                "" + mResponse.body(),
                                SubCategoriesRespnse::class.java
                            )
                        else {
                            gson.fromJson<SubCategoriesRespnse>(
                                mResponse.errorBody()!!.charStream(),
                                SubCategoriesRespnse::class.java
                            )
                        }
                        data!!.postValue(loginResponse)
                    }

                    override fun onError(mKey : String) {
                        UtilsFunctions.showToastError(MyApplication.instance.getString(R.string.internal_server_error))
                        data!!.postValue(null)
                    }

                }, ApiClient.getApiInterface().getSubCatList(jsonObject)
            )

        }
        return data!!

    }

}