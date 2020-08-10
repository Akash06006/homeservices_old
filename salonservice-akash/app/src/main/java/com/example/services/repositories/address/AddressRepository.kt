package com.example.services.repositories.address

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
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import retrofit2.Response
class AddressRepository {
    private var data : MutableLiveData<AddressResponse>? = null
    private var data2 : MutableLiveData<CommonModel>? = null
    private var data1 : MutableLiveData<AddressListResponse>? = null

    private val gson = GsonBuilder().serializeNulls().create()

    init {
        data = MutableLiveData()
        data1 = MutableLiveData()
        data2 = MutableLiveData()
    }



    fun addAddress(jsonObject : JsonObject?) : MutableLiveData<AddressResponse> {
        if (jsonObject != null) {
            val mApiService = ApiService<JsonObject>()
            mApiService.get(
                object : ApiResponse<JsonObject> {
                    override fun onResponse(mResponse : Response<JsonObject>) {
                        val loginResponse = if (mResponse.body() != null)
                            gson.fromJson<AddressResponse>(
                                "" + mResponse.body(),
                                AddressResponse::class.java
                            )
                        else {
                            gson.fromJson<AddressResponse>(
                                mResponse.errorBody()!!.charStream(),
                                AddressResponse::class.java
                            )
                        }
                        data!!.postValue(loginResponse)
                    }

                    override fun onError(mKey : String) {
                        UtilsFunctions.showToastError(MyApplication.instance.getString(R.string.internal_server_error))
                        data!!.postValue(null)
                    }

                }, ApiClient.getApiInterface().addAddress(jsonObject)
            )

        }
        return data!!

    }
    fun updateAddress(jsonObject : JsonObject?) : MutableLiveData<AddressResponse> {
        if (jsonObject != null) {
            val mApiService = ApiService<JsonObject>()
            mApiService.get(
                object : ApiResponse<JsonObject> {
                    override fun onResponse(mResponse : Response<JsonObject>) {
                        val loginResponse = if (mResponse.body() != null)
                            gson.fromJson<AddressResponse>(
                                "" + mResponse.body(),
                                AddressResponse::class.java
                            )
                        else {
                            gson.fromJson<AddressResponse>(
                                mResponse.errorBody()!!.charStream(),
                                AddressResponse::class.java
                            )
                        }
                        data!!.postValue(loginResponse)
                    }

                    override fun onError(mKey : String) {
                        UtilsFunctions.showToastError(MyApplication.instance.getString(R.string.internal_server_error))
                        data!!.postValue(null)
                    }

                }, ApiClient.getApiInterface().updateAddress(jsonObject)
            )

        }
        return data!!

    }

    fun deleteAddress(jsonObject : String?) : MutableLiveData<CommonModel> {
        if (!TextUtils.isEmpty(jsonObject)) {
            var addAddressObject = JsonObject()
            addAddressObject.addProperty(
                "addressId", jsonObject
            )
            val mApiService = ApiService<JsonObject>()
            mApiService.get(
                object : ApiResponse<JsonObject> {
                    override fun onResponse(mResponse : Response<JsonObject>) {
                        val loginResponse = if (mResponse.body() != null)
                            gson.fromJson<CommonModel>(
                                "" + mResponse.body(),
                                CommonModel::class.java
                            )
                        else {
                            gson.fromJson<CommonModel>(
                                mResponse.errorBody()!!.charStream(),
                                CommonModel::class.java
                            )
                        }
                        data2!!.postValue(loginResponse)
                    }

                    override fun onError(mKey : String) {
                        UtilsFunctions.showToastError(MyApplication.instance.getString(R.string.internal_server_error))
                        data2!!.postValue(null)
                    }

                }, ApiClient.getApiInterface().deleteAddress(jsonObject!!)
            )

        }
        return data2!!

    }

    fun getAddressList() : MutableLiveData<AddressListResponse> {
        //if (jsonObject != null) {
            val mApiService = ApiService<JsonObject>()
            mApiService.get(
                object : ApiResponse<JsonObject> {
                    override fun onResponse(mResponse : Response<JsonObject>) {
                        val loginResponse = if (mResponse.body() != null)
                            gson.fromJson<AddressListResponse>(
                                "" + mResponse.body(),
                                AddressListResponse::class.java
                            )
                        else {
                            gson.fromJson<AddressListResponse>(
                                mResponse.errorBody()!!.charStream(),
                                AddressListResponse::class.java
                            )
                        }
                        data1!!.postValue(loginResponse)
                    }

                    override fun onError(mKey : String) {
                        UtilsFunctions.showToastError(MyApplication.instance.getString(R.string.internal_server_error))
                        data1!!.postValue(null)
                    }

                }, ApiClient.getApiInterface().getAddressList()
            )

        //}
        return data1!!

    }


}