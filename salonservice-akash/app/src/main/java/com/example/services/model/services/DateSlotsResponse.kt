package com.example.services.model.services

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DateSlotsResponse {
    @SerializedName("code")
    @Expose
    var code : Int? = null
    @SerializedName("message")
    @Expose
    var message : String? = null
    @SerializedName("data")
    @Expose
    var data : ArrayList<Body>? = null

    inner class Body {

        @SerializedName("date")
        @Expose
        var date : String? = null
        @SerializedName("status")
        @Expose
        var status : String? = null
        @SerializedName("selected")
        @Expose
        var selected : String? = null


    }
}
