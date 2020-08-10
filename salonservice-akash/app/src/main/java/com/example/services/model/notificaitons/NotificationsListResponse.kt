package com.example.services.model.notificaitons

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationsListResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var data: ArrayList<Data>? = null

    /*{
     "code": 200,
     "message": "success",
     "body": [
         {
             "id": "2547c043-85cb-4bfc-9b0a-95b0565f41a2",
             "notificationTitle": "Your order No.- ORDER0087 is Confirmed on 19-May-2020 3:58 pm",
             "notificationDescription": "Your order No.- ORDER0087 is Confirmed on 19-May-2020 3:58 pm",
             "userId": "87af7745-6514-41f5-b849-7255e06170fb",
             "role": 3,
             "readStatus": 0,
             "status": 1,
             "createdAt": "2020-05-19T10:21:00.000Z",
             "updatedAt": "2020-05-19T10:21:00.000Z"
         }]*/
    inner class Data {
        @SerializedName("id")
        @Expose
        var notification_id: String? = null
        @SerializedName("notificationTitle")
        @Expose
        var notification_title: String? = null
        @SerializedName("notificationDescription")
        @Expose
        var notification_description: String? = null
        @SerializedName("userId")
        @Expose
        var userId: String? = null
        @SerializedName("role")
        @Expose
        var role: String? = null
        @SerializedName("readStatus")
        @Expose
        var readStatus: String? = null
        @SerializedName("status")
        @Expose
        var status: String? = null
        @SerializedName("created_at")
        @Expose
        var created_at: String? = null
        @SerializedName("updated_at")
        @Expose
        var updated_at: String? = null
    }
}
