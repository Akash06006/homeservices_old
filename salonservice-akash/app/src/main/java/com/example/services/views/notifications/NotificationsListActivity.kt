package com.example.services.views.notifications

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.services.R
import com.example.services.application.MyApplication
import com.example.services.common.UtilsFunctions
import com.example.services.constants.GlobalConstants
import com.example.services.databinding.ActivityNotificationsListBinding
import com.example.services.model.CommonModel
import com.example.services.model.notificaitons.NotificationsListResponse
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.utils.BaseActivity
import com.example.services.viewmodels.notifications.NotificationsViewModel
import com.uniongoods.adapters.NotificationsListAdapter

class NotificationsListActivity : BaseActivity() {
    lateinit var notificationsListBinding: ActivityNotificationsListBinding
    lateinit var notificationsViewModel: NotificationsViewModel
    private var notificationList = ArrayList<NotificationsListResponse.Data>()
    var userId = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_notifications_list
    }

    override fun initViews() {
        notificationsListBinding = viewDataBinding as ActivityNotificationsListBinding
        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        notificationsViewModel.getNotificationList()
        notificationsListBinding.notificationViewModel = notificationsViewModel
        notificationsListBinding.commonToolBar.imgToolbarText.text =
            resources.getString(R.string.notifications)
        userId = SharedPrefClass()!!.getPrefValue(
            MyApplication.instance,
            GlobalConstants.USERID
        ).toString()
        if (UtilsFunctions.isNetworkConnected()) {
            notificationsViewModel.getNotificationsList(userId)
            startProgressDialog()
        }

        notificationsViewModel.getNotificationList().observe(this,
            Observer<NotificationsListResponse> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            if (response.data != null && response.data!!.size > 0) {
                                notificationList.addAll(response.data!!)
                                notificationsListBinding.title.visibility = View.GONE
                                notificationsListBinding.title.visibility = View.GONE
                                notificationsListBinding.rvNotification.visibility = View.VISIBLE
                                notificationsListBinding.tvNoRecord.visibility = View.GONE
                                initRecyclerView()
                            } else {
                                message?.let {
                                    UtilsFunctions.showToastError(message)
                                }
                                notificationsListBinding.rvNotification.visibility = View.GONE
                                notificationsListBinding.title.visibility = View.VISIBLE
                                notificationsListBinding.tvNoRecord.visibility = View.VISIBLE
                                notificationsListBinding.title.visibility = View.VISIBLE
                                notificationsListBinding.btnClear.visibility = View.GONE
                            }
                        }
                        /* response.code == 204 -> {
                             FirebaseFunctions.sendOTP("signup", mJsonObject, this)
                         }*/
                        else -> message?.let {
                            UtilsFunctions.showToastError(message)
                            notificationsListBinding.rvNotification.visibility = View.GONE
                            notificationsListBinding.tvNoRecord.visibility = View.VISIBLE
                            notificationsListBinding.title.visibility = View.VISIBLE
                            notificationsListBinding.btnClear.visibility = View.GONE
                        }
                    }

                }
            })

        notificationsViewModel.clearAll().observe(this,
            Observer<CommonModel> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            UtilsFunctions.showToastSuccess(message!!)
                            notificationList.clear()
                            notificationsListBinding.rvNotification.visibility = View.GONE
                            notificationsListBinding.title.visibility = View.VISIBLE
                            notificationsListBinding.tvNoRecord.visibility = View.VISIBLE
                            notificationsListBinding.btnClear.visibility = View.GONE

                        }
                        /* response.code == 204 -> {
                             FirebaseFunctions.sendOTP("signup", mJsonObject, this)
                         }*/
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                        }
                    }

                }
            })

        notificationsViewModel.isClick().observe(
            this, Observer<String>(function =
            fun(it: String?) {
                when (it) {
                    "btn_clear" -> {
                        notificationsViewModel.clearAllNotification(userId)
                    }
                }
            })
        )

    }

    private fun initRecyclerView() {
        var notificationsListAdapter =
            NotificationsListAdapter(
                this@NotificationsListActivity,
                notificationList,
                this@NotificationsListActivity
            )
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        notificationsListBinding.rvNotification.layoutManager = linearLayoutManager
        notificationsListBinding.rvNotification.adapter = notificationsListAdapter
        notificationsListBinding.rvNotification.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
    }

}
