package com.uniongoods.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.services.R
import com.example.services.databinding.NotificationItemBinding
import com.example.services.model.notificaitons.NotificationsListResponse
import com.example.services.views.notifications.NotificationsListActivity

class NotificationsListAdapter(
    context: NotificationsListActivity,
    addressList: ArrayList<NotificationsListResponse.Data>,
    var activity: Context
) :
    RecyclerView.Adapter<NotificationsListAdapter.ViewHolder>() {
    private val mContext: NotificationsListActivity
    private var viewHolder: ViewHolder? = null
    private var jobsList: ArrayList<NotificationsListResponse.Data>

    init {
        this.mContext = context
        this.jobsList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.notification_item,
            parent,
            false
        ) as NotificationItemBinding
        return ViewHolder(binding.root, viewType, binding, mContext, jobsList)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        viewHolder = holder
        /* if (jobsList[position].status!!.equals("5")) {
             holder.binding!!.tvNotificationTitle.text =
                 mContext.resources.getString(R.string.completed)
             holder.binding!!.tvNotificationTitle.setTextColor(mContext.resources.getColor(R.color.success))
         } else if (jobsList[position].status!!.equals("1")) {
             holder.binding!!.tvNotificationTitle.text =
                 mContext.resources.getString(R.string.confirmed)
             holder.binding!!.tvNotificationTitle.setTextColor(mContext.resources.getColor(R.color.orange))
         } else if (jobsList[position].status!!.equals("2")) {
             holder.binding!!.tvNotificationTitle.text =
                 mContext.resources.getString(R.string.cancelled)
             holder.binding!!.tvNotificationTitle.setTextColor(mContext.resources.getColor(R.color.red))
         }*/
        holder.binding!!.tvNotificationTitle.text = jobsList[position].notification_title
        holder.binding!!.tvNotificationDescription.text =
            jobsList[position].notification_description

    }

    override fun getItemCount(): Int {
        return jobsList.count()
    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
        (
        v: View, val viewType: Int, //These are the general elements in the RecyclerView
        val binding: NotificationItemBinding?,
        mContext: NotificationsListActivity,
        addressList: ArrayList<NotificationsListResponse.Data>?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}