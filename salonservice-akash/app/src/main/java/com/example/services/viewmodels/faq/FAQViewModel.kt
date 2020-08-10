package com.example.services.viewmodels.faq

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.view.View
import com.example.services.common.UtilsFunctions
import com.example.services.model.CommonModel
import com.example.services.model.faq.FAQListResponse
import com.example.services.model.notificaitons.NotificationsListResponse
import com.example.services.repositories.faq.FAQRepository
import com.example.services.viewmodels.BaseViewModel

class FAQViewModel : BaseViewModel() {
    private var clearAllNotifications = MutableLiveData<CommonModel>()
    private var faqList = MutableLiveData<FAQListResponse>()

    private var faqRepository = FAQRepository()
    private val mIsUpdating = MutableLiveData<Boolean>()
    private val btnClick = MutableLiveData<String>()

    init {
        if (UtilsFunctions.isNetworkConnectedWithoutToast()) {
            faqList = faqRepository.getFAQList("")
            //clearAllNotifications = notificationRepository.clearAllNotifications("")
        }
    }


    fun getFAQList(): LiveData<FAQListResponse> {
        return faqList
    }

    override fun isLoading(): LiveData<Boolean> {
        return mIsUpdating
    }

    override fun isClick(): LiveData<String> {
        return btnClick
    }

    override fun clickListener(v: View) {
        btnClick.value = v.resources.getResourceName(v.id).split("/")[1]
    }


    fun getFAQList(userId: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            faqList = faqRepository.getFAQList(userId)
            mIsUpdating.postValue(true)
        }
    }

}