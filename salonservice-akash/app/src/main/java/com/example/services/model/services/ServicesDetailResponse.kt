package com.example.services.model.services

import com.example.services.model.LoginResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ServicesDetailResponse {
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
        @SerializedName("icon")
        @Expose
        var icon: String? = null
        @SerializedName("thumbnail")
        @Expose
        var thumbnail: String? = null
        @SerializedName("includedServices")
        @Expose
        var includedServices: String? = null
        @SerializedName("excludedServices")
        @Expose
        var excludedServices: String? = null
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
        @SerializedName("cart")
        @Expose
        var cart: String? = null
        @SerializedName("favourite")
        @Expose
        var favourite: String? = null
        @SerializedName("rating")
        @Expose
        var rating: String? = null
        /* @SerializedName("category")
         @Expose
         var category: String? = null*/


        /*val icon : String,
        val thumbnail : String,
        val includedServices : List<String>,
        val excludedServices : List<String>,
        val id : String,
        val name : String,
        val description : String,
        val price : Int,
        val type : String,
        val duration : Int,
        val turnaroundTime : String,
        val rating : Int,
        val category : String*/
    }
}