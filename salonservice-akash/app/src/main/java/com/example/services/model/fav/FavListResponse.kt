package com.example.services.model.fav

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FavListResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: ArrayList<Body>? = null


    inner class Body {
        @SerializedName("service")
        @Expose
        var service: Service? = null
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("serviceId")
        @Expose
        var serviceId: String? = null
        @SerializedName("userId")
        @Expose
        var userId: String? = null
        @SerializedName("orderTotalPrice")
        @Expose
        var price: String? = null
        @SerializedName("quantity")
        @Expose
        var quantity: String? = null
        @SerializedName("created_at")
        @Expose
        var created_at: String? = null
        @SerializedName("updated_at")
        @Expose
        var updated_at: String? = null
        @SerializedName("companyId")
        @Expose
        var companyId: String? = null
        @SerializedName("cartCategoryType")
        @Expose
        var cartCategoryType: String? = null
        @SerializedName("cartCategoryCompany")
        @Expose
        var cartCategoryCompany: String? = null
    }

    inner class Service {
        @SerializedName("icon")
        @Expose
        var icon: String? = null
        @SerializedName("thumbnail")
        @Expose
        var thumbnail: String? = null
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("description")
        @Expose
        var description: String? = null
        @SerializedName("price")
        @Expose
        var price: String? = null
        @SerializedName("type")
        @Expose
        var type: String? = null
        @SerializedName("duration")
        @Expose
        var duration: String? = null
        @SerializedName("turnaroundTime")
        @Expose
        var turnaroundTime: String? = null
        @SerializedName("includedServices")
        @Expose
        var includedServices: String? = null
        @SerializedName("excludedServices")
        @Expose
        var excludedServices: String? = null
        @SerializedName("createdAt")
        @Expose
        var createdAt: String? = null
        @SerializedName("status")
        @Expose
        var status: String? = null


    }
}
