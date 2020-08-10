package com.example.services.repositories.vendor

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
import com.example.services.model.cart.CartListResponse
import com.example.services.model.fav.FavListResponse
import com.example.services.model.vendor.VendorListResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import retrofit2.Response

class VendorsRepository {
    private var data1: MutableLiveData<VendorListResponse>? = null
    private val gson = GsonBuilder().serializeNulls().create()

    init {
        data1 = MutableLiveData()
    }

    fun vendorList(mJsonObject: String, lat: String, longi: String): MutableLiveData<VendorListResponse> {
        if (!TextUtils.isEmpty(mJsonObject)) {
            val mApiService = ApiService<JsonObject>()
            mApiService.get(
                    object : ApiResponse<JsonObject> {
                        override fun onResponse(mResponse: Response<JsonObject>) {
                            val loginResponse = if (mResponse.body() != null)
                                gson.fromJson<VendorListResponse>(
                                        "" + mResponse.body(),
                                        VendorListResponse::class.java
                                )
                            else {
                                gson.fromJson<VendorListResponse>(
                                        mResponse.errorBody()!!.charStream(),
                                        VendorListResponse::class.java
                                )
                            }
                            data1!!.postValue(loginResponse)
                        }

                        override fun onError(mKey: String) {
                            UtilsFunctions.showToastError(MyApplication.instance.getString(R.string.internal_server_error))
                            data1!!.postValue(null)
                        }

                    }, ApiClient.getApiInterface().vendorList(mJsonObject, lat, longi)
            )

        }
        return data1!!

    }

}