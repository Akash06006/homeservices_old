package com.example.services.model.faq

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FAQListResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("body")
    @Expose
    var data: ArrayList<Data>? = null

    /* "id": "00020606-2945-42ec-819c-a4195b7b2ba1",
            "question": "What is dashboard?",
            "answer": " wdknwacasbc ascbas cambscv ksdbvd svbhdfvb ndsvjkbvdf vbkfdv dfjv kjld ",
            "status": 0,
            "language": "FR"*/
    inner class Data {
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("question")
        @Expose
        var question: String? = null
        @SerializedName("answer")
        @Expose
        var answer: String? = null
        @SerializedName("status")
        @Expose
        var status: String? = null
        @SerializedName("language")
        @Expose
        var language: String? = null
    }
}
