package com.example.services.viewmodels.home


data class Body(
    val banners: List<Banners>,
    val services: List<Services>,
    val subcat: List<Subcat>,
    val offers: List<Offers>,
    val trending: List<Trending>,
    val cartCategoryType: String,
    val currency: String,
    val cartCategoryCompany: String,
    val aboutUsLink: String,
    val termsLink: String,
    val privacyLink: String

)