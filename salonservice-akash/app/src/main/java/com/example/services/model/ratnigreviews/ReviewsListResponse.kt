package com.example.services.model.ratnigreviews

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewsListResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var body: Body? = null


    inner class Body {

        @SerializedName("avgRating")
        @Expose
        var avgRating: String? = null
        @SerializedName("data")
        @Expose
        var data: ArrayList<Data>? = null
    }

    inner class Data {
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("rating")
        @Expose
        var rating: String? = null
        @SerializedName("review")
        @Expose
        var review: String? = null
        @SerializedName("user")
        @Expose
        var user: User? = null


    }

    inner class User {
        @SerializedName("image")
        @Expose
        var image: String? = null
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("firstName")
        @Expose
        var firstName: String? = null
        @SerializedName("lastName")
        @Expose
        var lastName: String? = null
    }
}
