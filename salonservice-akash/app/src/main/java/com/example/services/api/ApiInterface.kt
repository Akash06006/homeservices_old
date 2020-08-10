package com.example.services.api

import com.example.services.model.ratnigreviews.RatingReviewListInput
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.HashMap

interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("mobile/auth/login")
    fun callLogin(@Body jsonObject: JsonObject): Call<JsonObject>

    /*@POST("login/")
    fun callLogin(@Body jsonObject : JsonObject) : Call<JsonObject>*/
    @Multipart
    @POST("register/")
    fun finishRegistartion(
        @PartMap mHashMap: HashMap<String,
                RequestBody>, @Part image: MultipartBody.Part?
    ): Call<JsonObject>

    @Multipart
    @POST("mobile/profile/updateprofile")
    fun callUpdateProfile(
        @PartMap mHashMap: HashMap<String,
                RequestBody>, @Part image: MultipartBody.Part?
    ): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST("checkPhoneNumber/")
    fun checkPhoneExistence(@Body jsonObject: JsonObject): Call<JsonObject>

    @POST("mobile/auth/logout")
    fun callLogout(@Body mJsonObject: JsonObject): Call<JsonObject>

    @POST("resetpassword/")
    fun resetPassword(@Body mJsonObject: JsonObject): Call<JsonObject>

    //@POST("resetpassword/")
    //fun getProfile(@Body mJsonObject : JsonObject) : Call<JsonObject>
    @POST("users/changepassword/")
    fun chnagePassword(@Body mJsonObject: JsonObject): Call<JsonObject>

    @GET("mobile/profile/getprofile")
    fun getProfile(): Call<JsonObject>

    @GET("driver/vehicle/latLongList")
    fun getVehicleList(): Call<JsonObject>

    @GET("service/driver/getServiceList")
    fun getServicesList(@Query("status") status: String): Call<JsonObject>

    @GET("fuel/driver/getFuelList")
    fun getFuelEntryList(): Call<JsonObject>

    @GET("mobile/notification")
    fun getNotificationList(): Call<JsonObject>
    @GET("mobile/getFaq")
    fun getFAQList(@Query("limit") limit: String,@Query("page") page: String): Call<JsonObject>


    @GET("mobile/notification")
    fun getNotificationList(@Path("id") id: String): Call<JsonObject>

    @DELETE("mobile/notification/clearAll")
    fun clearAllNotification(): Call<JsonObject>

    @DELETE("mobile/address/delete")
    fun deleteAddress(@Query("addressId") addressId: String): Call<JsonObject>

    @GET("vendor/getVendorList")
    fun getVendorList(): Call<JsonObject>

    @GET("mobile/services/getParentCategories")
    fun getCategories(): Call<JsonObject>

    @DELETE("mobile/cart/clear")
    fun clearCart(): Call<JsonObject>

    @GET("mobile/services/getSubcat/{id}")
    fun getSubServices(@Path("id") id: String): Call<JsonObject>

    @GET("mobile/services/getServices/{id}")
    fun getServices(@Path("id") id: String): Call<JsonObject>

    @POST("job/driver/changeJobStatus")
    fun startCompleteJob(@Body mJsonObject: JsonObject): Call<JsonObject>


    @POST("mobile/address/add")
    fun addAddress(@Body mJsonObject: JsonObject): Call<JsonObject>

    @PUT("mobile/address/update")
    fun updateAddress(@Body mJsonObject: JsonObject): Call<JsonObject>

    @GET("mobile/address/list")
    fun getAddressList(): Call<JsonObject>

    @GET("mobile/cart/list")
    fun cartList(/*@Path("id") id : String*/): Call<JsonObject>

    @GET("mobile/favourite/list")
    fun favList(/*@Path("id") id : String*/): Call<JsonObject>

    @GET("mobile/rating/serviceRatings")
    fun ratingRaviewsList(
        @Query("serviceId") serviceId: String, @Query("page") page: String, @Query(
            "limit"
        ) limit: String
    ): Call<JsonObject>

    @GET("mobile/services/getCompanies")
    fun vendorList(@Query("categoryId") serviceId: String, @Query("latitude") page: String, @Query("longitude") limit: String): Call<JsonObject>


    @GET("mobile/orders/detail/{id}")
    fun orderDetail(@Path("id") id: String): Call<JsonObject>

    @POST("mobile/rating/addRating")
    fun addRatings(@Body mJsonObject: RatingReviewListInput): Call<JsonObject>

    //    {id}
//service_id
    @Multipart
    @POST("fuel/addFuel")
    fun callAddFuelEntry(
        @PartMap mHashMap: HashMap<String,
                RequestBody>, @Part image: MultipartBody.Part?
    ): Call<JsonObject>

    @Multipart
    @POST("service/updateServiceEntry")
    fun callUpdateService(
        @PartMap mHashMap: HashMap<String,
                RequestBody>, @Part image: MultipartBody.Part?
    ): Call<JsonObject>

    @POST("mobile/services/getsubcategories")
    fun getSubCatList(@Body mJsonObject: JsonObject): Call<JsonObject>


    @POST("mobile/cart/add")
    fun addCart(@Body mJsonObject: JsonObject): Call<JsonObject>

    @DELETE("mobile/cart/remove")
    fun removeCart(@Query("cartId") cartId: String): Call<JsonObject>

    @POST("mobile/favourite/add")
    fun addFav(@Body mJsonObject: JsonObject): Call<JsonObject>

    @DELETE("mobile/favourite/remove")
    fun removeFav(@Query("favId") favId: String): Call<JsonObject>

    @GET("mobile/schedule/getSchedule")
    fun getTimeSlots(@Query("serviceDate") serviceDate: String): Call<JsonObject>

    @GET("mobile/services/getDates")
    fun getDateSlots(): Call<JsonObject>

    @GET("mobile/coupan/getPromoList")
    fun getPromoList(): Call<JsonObject>

    @POST("mobile/coupan/applyCoupan")
    fun applyCoupon(@Body mJsonObject: JsonObject): Call<JsonObject>

    @POST("mobile/coupan/removeCoupan")
    fun removeCoupon(@Body mJsonObject: JsonObject): Call<JsonObject>

    @POST("mobile/orders/create")
    fun ordePlace(@Body mJsonObject: JsonObject): Call<JsonObject>

    @POST("mobile/orders/paymentStatus")
    fun updatePaymentSuccess(@Body mJsonObject: JsonObject): Call<JsonObject>


    @POST("mobile/orders/cancel")
    fun cancelOrder(@Body mJsonObject: JsonObject): Call<JsonObject>

    @POST("mobile/orders/status")
    fun completeOrder(@Body mJsonObject: JsonObject): Call<JsonObject>

    @GET("mobile/services/detail")
    fun getServiceDetail(@Query("serviceId") addressId: String): Call<JsonObject>

    @GET("mobile/orders/list")
    fun orderList(@Query("progressStatus") progressStatus: String): Call<JsonObject>

    @GET("mobile/orders/list")
    fun orderHistroyList(@Query("progressStatus") progressStatus: String): Call<JsonObject>

}