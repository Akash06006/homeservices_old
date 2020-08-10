package com.example.services.model.ratnigreviews

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*{
"orderId":"a47f989d-b5fa-49da-914a-46f0cbc4fe3c",
"ratingData":[
 {"serviceId":"5f7e9eda-2f5d-4554-918b-445808007659","rating":"4.5","review":"Very Nice Product"},
 {"serviceId":"","rating":"2","review":"Very Nice Product"},
 {"serviceId":"","rating":"2","review":"Very Nice Product"}
 ]
}*/
class RatingReviewListInput {
    @SerializedName("orderId")
    @Expose
    var orderId: String? = null
    @SerializedName("ratingData")
    @Expose
    var ratingData = ArrayList<RatingData>()
    @SerializedName("empRatingData")
    @Expose
    var empRatingData = ArrayList<EmpRatingData>()


}

class RatingData {
    @SerializedName("serviceId")
    @Expose
    var serviceId: String? = null
    @SerializedName("rating")
    @Expose
    var rating: String? = null
    @SerializedName("review")
    @Expose
    var review: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null
}

class EmpRatingData {
    @SerializedName("empId")
    @Expose
    var empId: String? = null
    @SerializedName("rating")
    @Expose
    var rating: String? = null
    @SerializedName("review")
    @Expose
    var review: String? = null
    /* @SerializedName("name")
     @Expose
     var name: String? = null
     @SerializedName("icon")
     @Expose
     var icon: String? = null*/
}