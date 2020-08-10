package com.example.services.model.services

data class Services(

        val id: String,
        var name: String,
        var description: String,
        var price: String,
        var icon: String,
        var thumbnail: String,
        var type: String,
        var duration: String,
        var rating: String,
        val favourite: String
        //var cart : String
)

data class Favourite(
        val id: String
)