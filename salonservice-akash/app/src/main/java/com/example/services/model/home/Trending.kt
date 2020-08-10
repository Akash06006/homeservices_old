package com.example.services.viewmodels.home

data class Trending(

    val icon : String,
    val thumbnail : String,
    val id : String,
    val name : String,
    val description : String,
    val categoryId : String,
    val category : Category
)