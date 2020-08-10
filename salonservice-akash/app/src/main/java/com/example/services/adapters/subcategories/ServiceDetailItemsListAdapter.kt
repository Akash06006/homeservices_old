package com.uniongoods.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.services.R
import com.example.services.databinding.ServiceDetailItemBinding
import com.example.services.model.DetailModel
import com.example.services.model.services.DateSlotsResponse
import com.example.services.views.cart.CheckoutAddressActivity
import com.example.services.views.subcategories.ServiceDetailActivity
import com.google.firebase.database.collection.LLRBNode

class ServiceDetailItemsListAdapter(
        context: ServiceDetailActivity,
        addressList: ArrayList<DetailModel>,
        var activity: Context
) :
        RecyclerView.Adapter<ServiceDetailItemsListAdapter.ViewHolder>() {
    private val mContext: ServiceDetailActivity
    private var viewHolder: ViewHolder? = null
    private var dateList: ArrayList<DetailModel>

    init {
        this.mContext = context
        this.dateList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.service_detail_item,
                parent,
                false
        ) as ServiceDetailItemBinding
        return ViewHolder(binding.root, viewType, binding, mContext, dateList)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        viewHolder = holder
        holder.binding!!.tvItemTitle.text = dateList[position].title
        holder.binding!!.tvItemName.text = dateList[position].value
        if (dateList[position].title.equals("Excluded Services")) {
            holder.binding!!.tvItemName.setTextColor(Color.RED)
        }

    }

    override fun getItemCount(): Int {
        return dateList.count()
    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
    (
            v: View, val viewType: Int, //These are the general elements in the RecyclerView
            val binding: ServiceDetailItemBinding?,
            mContext: ServiceDetailActivity,
            addressList: ArrayList<DetailModel>?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}