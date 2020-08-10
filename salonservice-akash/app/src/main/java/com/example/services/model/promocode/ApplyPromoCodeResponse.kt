package com.example.services.model.promocode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApplyPromoCodeResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var data: Body? = null

    /*{
        "code": 200,
        "message": "Coupan Applied Successfully",
        "body": {
            "totalAmount": 2400,
            "discountPrice": 480,
            "payableAmount": 1920,
            "coupanId": "25cbf58b-46ba-4ba2-b25d-8f8f653e9f11",
            "coupanCode": "PROMO123",
            "coupanDiscount": "20"
        }
    }*/
    inner class Body {
        @SerializedName("totalAmount")
        @Expose
        var totalAmount: String? = null
        @SerializedName("discountPrice")
        @Expose
        var discountPrice: String? = null
        @SerializedName("payableAmount")
        @Expose
        var payableAmount: String? = null
        @SerializedName("discount")
        @Expose
        var discount: String? = null
        @SerializedName("coupanDiscount")
        @Expose
        var coupanDiscount: String? = null
        @SerializedName("coupanId")
        @Expose
        var coupanId: String? = null
        @SerializedName("coupanCode")
        @Expose
        var coupanCode: String? = null
    }
}
