package com.example.services.model.orders

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrdersDetailResponse {
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

        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("serviceDateTime")
        @Expose
        var serviceDateTime: String? = null
        @SerializedName("orderPrice")
        @Expose
        var orderPrice: String? = null
        @SerializedName("promoCode")
        @Expose
        var promoCode: String? = null
        @SerializedName("offerPrice")
        @Expose
        var offerPrice: String? = null
        @SerializedName("serviceCharges")
        @Expose
        var serviceCharges: String? = null
        @SerializedName("totalOrderPrice")
        @Expose
        var totalOrderPrice: String? = null
        @SerializedName("addressId")
        @Expose
        var addressId: String? = null
        @SerializedName("companyId")
        @Expose
        var companyId: String? = null
        @SerializedName("userId")
        @Expose
        var userId: String? = null
        @SerializedName("progressStatus")
        @Expose
        var progressStatus: String? = null
        @SerializedName("trackStatus")
        @Expose
        var trackStatus: String? = null

        @SerializedName("trackingLatitude")
        @Expose
        var trackingLatitude: String? = null
        @SerializedName("trackingLongitude")
        @Expose
        var trackingLongitude: String? = null
        @SerializedName("cancellationReason")
        @Expose
        var cancellationReason: String? = null
        @SerializedName("createdAt")
        @Expose
        var createdAt: String? = null
        @SerializedName("updatedAt")
        @Expose
        var updatedAt: String? = null
        @SerializedName("address")
        @Expose
        var address: Address? = null
        @SerializedName("suborders")
        @Expose
        var suborders: ArrayList<Suborders>? = null
        @SerializedName("assignedEmployees")
        @Expose
        var assignedEmployees: ArrayList<AssignedEmployees>? = null

    }

    inner class Suborders {
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("serviceId")
        @Expose
        var serviceId: String? = null
        @SerializedName("quantity")
        @Expose
        var quantity: String? = null
        @SerializedName("service")
        @Expose
        var service: Service? = null


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

    }

    inner class AssignedEmployees {
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("jobStatus")
        @Expose
        var jobStatus: String? = null
        @SerializedName("employee")
        @Expose
        var employee: Employee? = null

    }

    inner class Employee {
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
        @SerializedName("countryCode")
        @Expose
        var countryCode: String? = null
        @SerializedName("phoneNumber")
        @Expose
        var phoneNumber: String? = null
    }

    inner class Address {
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("addressName")
        @Expose
        var addressName: String? = null
        @SerializedName("addressType")
        @Expose
        var addressType: String? = null
        @SerializedName("houseNo")
        @Expose
        var houseNo: String? = null
        @SerializedName("latitude")
        @Expose
        var latitude: String? = null
        @SerializedName("longitude")
        @Expose
        var longitude: String? = null
        @SerializedName("town")
        @Expose
        var town: String? = null
        @SerializedName("landmark")
        @Expose
        var landmark: String? = null
        @SerializedName("city")
        @Expose
        var city: String? = null
    }
}
