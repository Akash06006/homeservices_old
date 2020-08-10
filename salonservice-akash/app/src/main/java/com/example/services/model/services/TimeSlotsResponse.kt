package com.example.services.model.services

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TimeSlotsResponse {
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

        @SerializedName("slots")
        @Expose
        var slots: ArrayList<Slots>? = null

        /*@SerializedName("slots") val slots: List<Slots>,
                @SerializedName("leaves") val leaves: List<String>,
                @SerializedName("id") val id: String,
                @SerializedName("fromDate") val fromDate: String,
                @SerializedName("toDate") val toDate: String,
                @SerializedName("startTime") val startTime: String,
                @SerializedName("endTime") val endTime: String,
                @SerializedName("companyId") val companyId: String,
                @SerializedName("status") val status: Int,
                @SerializedName("createdAt") val createdAt: Int*/


        /* @SerializedName("id")
                 @Expose
                 var id : String? = null
                 @SerializedName("timing")
                 @Expose
                 var timing : String? = null
                 @SerializedName("selected")
                 @Expose
                 var selected : String? = null*/


    }

    inner class Slots {
        @SerializedName("slot")
        @Expose
        var slot: String? = null
        @SerializedName("bookings")
        @Expose
        var bookings: String? = null
        @SerializedName("selected")
        @Expose
        var selected: String? = null
        @SerializedName("status")
        @Expose
        var status: String? = null


    }
}
