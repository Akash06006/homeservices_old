package com.example.services.model.orders

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CreateOrdersResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var data: Body? = null

    inner class Body {
        @SerializedName("orderNo")
        @Expose
        var orderNo: String? = null
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("progressStatus")
        @Expose
        var progressStatus: String? = null
        @SerializedName("trackStatus")
        @Expose
        var trackStatus: String? = null
        @SerializedName("cancellationReason")
        @Expose
        var cancellationReason: String? = null
        @SerializedName("createdAt")
        @Expose
        var createdAt: String? = null
        @SerializedName("updatedAt")
        @Expose
        var updatedAt: String? = null
        @SerializedName("addressId")
        @Expose
        var addressId: String? = null
        @SerializedName("serviceDateTime")
        @Expose
        var serviceDateTime: String? = null
        @SerializedName("orderPrice")
        @Expose
        var orderPrice: String? = null
        @SerializedName("serviceCharges")
        @Expose
        var serviceCharges: String? = null
        @SerializedName("offerPrice")
        @Expose
        var offerPrice: String? = null
        @SerializedName("promoCode")
        @Expose
        var promoCode: String? = null
        @SerializedName("totalOrderPrice")
        @Expose
        var totalOrderPrice: String? = null
        @SerializedName("userId")
        @Expose
        var userId: String? = null
        @SerializedName("companyId")
        @Expose
        var companyId: String? = null


    }


}
