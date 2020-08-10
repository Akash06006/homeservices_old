package com.example.services.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("code")
    @Expose
    var code : Int? = null
    @SerializedName("message")
    @Expose
    var message : String? = null
    @SerializedName("body")
    @Expose
    var data : Body? = null

    inner class Body {

        @SerializedName("id")
        @Expose
        var id : String? = null
        @SerializedName("isAddressAdded")
        @Expose
        var isAddressAdded : String? = null

        @SerializedName("firstName")
        @Expose
        var firstName : String? = null
        @SerializedName("lastName")
        @Expose
        var lastName : String? = null
        @SerializedName("email")
        @Expose
        var email : String? = null
        @SerializedName("phoneNumber")
        @Expose
        var phoneNumber : String? = null
        @SerializedName("countryCode")
        @Expose
        var countryCode : String? = null
        @SerializedName("password")
        @Expose
        var password : String? = null
        @SerializedName("image")
        @Expose
        var image : String? = null
        @SerializedName("deviceToken")
        @Expose
        var deviceToken : String? = null
        @SerializedName("userType")
        @Expose
        var userType : String? = null
        @SerializedName("sessionToken")
        @Expose
        var sessionToken : String? = null
        @SerializedName("moduleType")
        @Expose
        var moduleType : String? = null
        @SerializedName("platform")
        @Expose
        var platform : String? = null
        @SerializedName("status")
        @Expose
        var status : Int? = null
        @SerializedName("createdAt")
        @Expose
        var createdAt : String? = null
        @SerializedName("updatedAt")
        @Expose
        var updatedAt : String? = null
        /*@SerializedName("user_id")
        @Expose
        var userId : Int? = null
        @SerializedName("user_type")
        @Expose
        var userType : String? = null
        @SerializedName("profile_image")
        @Expose
        var profile_image : String? = null
        @SerializedName("sessionToken")
        @Expose
        var session_token : String? = null
        @SerializedName("status")
        @Expose
        var status : String? = null
        @SerializedName("availablity")
        @Expose
        var availablity : String? = null

        @SerializedName("countryCode")
        @Expose
        var countryCode : String? = null
        @SerializedName("email")
        @Expose
        var email : String? = null
        @SerializedName("password")
        @Expose
        var password : String? = null
        @SerializedName("phoneNumber")
        @Expose
        var phoneNumber : String? = null
        @SerializedName("first_name")
        @Expose
        var firstName : String? = null
        @SerializedName("last_name")
        @Expose
        var lastName : String? = null
        @SerializedName("gender")
        @Expose
        var gender : String? = null
        @SerializedName("address")
        @Expose
        var address : String? = null
        @SerializedName("profile_pic")
        @Expose
        var profilePic : String? = null
        @SerializedName("device_type")
        @Expose
        var deviceType : String? = null
        @SerializedName("device_id")
        @Expose
        var deviceId : String? = null
        @SerializedName("jwt_token")
        @Expose
        var jwtToken : String? = null
        @SerializedName("notify_id")
        @Expose
        var notifyId : String? = null
        @SerializedName("created_at")
        @Expose
        var createdAt : String? = null
        @SerializedName("updated_at")
        @Expose
        var updatedAt : String? = null*/

    }
}
