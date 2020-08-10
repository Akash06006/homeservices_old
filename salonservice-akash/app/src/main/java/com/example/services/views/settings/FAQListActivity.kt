package com.example.services.views.settings

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.services.R
import com.example.services.application.MyApplication
import com.example.services.common.UtilsFunctions
import com.example.services.constants.GlobalConstants
import com.example.services.databinding.ActivityFaqListBinding
import com.example.services.model.faq.FAQListResponse
import com.example.services.model.notificaitons.NotificationsListResponse
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.utils.BaseActivity
import com.example.services.viewmodels.faq.FAQViewModel
import com.uniongoods.adapters.FAQListAdapter

class FAQListActivity : BaseActivity() {
    lateinit var faqListBinding: ActivityFaqListBinding
    lateinit var faqViewModel: FAQViewModel
    private var notificationList = ArrayList<FAQListResponse.Data>()
    var userId = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_faq_list
    }

    override fun initViews() {
        faqListBinding = viewDataBinding as ActivityFaqListBinding
        faqViewModel = ViewModelProviders.of(this).get(FAQViewModel::class.java)
        //notificationsViewModel.getFAQList()
        faqListBinding.faqViewModel = faqViewModel
        faqListBinding.commonToolBar.imgToolbarText.text =
            resources.getString(R.string.faq)
        // faqListBinding.btnClear.visibility = View.GONE
        userId = SharedPrefClass()!!.getPrefValue(
            MyApplication.instance,
            GlobalConstants.USERID
        ).toString()
        if (UtilsFunctions.isNetworkConnected()) {
            faqViewModel.getFAQList(userId)
            startProgressDialog()
        }

        faqViewModel.getFAQList().observe(this,
            Observer<FAQListResponse> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            if (response.data != null && response.data!!.size > 0) {
                                notificationList.addAll(response.data!!)
                                faqListBinding.rvNotification.visibility = View.VISIBLE
                                faqListBinding.tvNoRecord.visibility = View.GONE
                                faqListBinding.title.visibility = View.GONE
                                initRecyclerView()
                            } else {
                                message?.let {
                                    UtilsFunctions.showToastError(message)
                                }
                                faqListBinding.rvNotification.visibility = View.GONE
                                faqListBinding.tvNoRecord.visibility = View.VISIBLE
                                faqListBinding.title.visibility = View.VISIBLE
                                // faqListBinding.btnClear.visibility = View.GONE
                            }
                        }
                        /* response.code == 204 -> {
                             FirebaseFunctions.sendOTP("signup", mJsonObject, this)
                         }*/
                        else -> message?.let {
                            UtilsFunctions.showToastError(message)
                            faqListBinding.rvNotification.visibility = View.GONE
                            faqListBinding.tvNoRecord.visibility = View.VISIBLE
                            faqListBinding.title.visibility = View.VISIBLE
                            // faqListBinding.btnClear.visibility = View.GONE
                        }
                    }

                }
            })

    }

    private fun initRecyclerView() {
        var faqListAdapter =
            FAQListAdapter(
                this@FAQListActivity,
                notificationList,
                this@FAQListActivity
            )
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        faqListBinding.rvNotification.layoutManager = linearLayoutManager
        faqListBinding.rvNotification.adapter = faqListAdapter
        faqListBinding.rvNotification.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
    }

}
