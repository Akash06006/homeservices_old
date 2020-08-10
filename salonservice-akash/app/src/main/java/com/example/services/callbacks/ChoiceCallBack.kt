package com.example.services.callbacks

interface ChoiceCallBack {
    fun photoFromCamera(mKey:String)
    fun photoFromGallery(mKey:String)
    fun videoFromCamera(mKey:String)
    fun videoFromGallery(mKey:String)
}