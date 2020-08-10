package com.uniongoods.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.services.R
import com.example.services.databinding.OrderServiceItemBinding
import com.example.services.model.orders.OrdersListResponse
import com.example.services.utils.BaseActivity

class OrderServicesListAdapter(
        context: BaseActivity,
        addressList: ArrayList<OrdersListResponse.Suborders>?,
        var activity: Context
) :
        RecyclerView.Adapter<OrderServicesListAdapter.ViewHolder>() {
    private val mContext: BaseActivity
    private var viewHolder: ViewHolder? = null
    private var addressList: ArrayList<OrdersListResponse.Suborders>?

    init {
        this.mContext = context
        this.addressList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.order_service_item,
                parent,
                false
        ) as OrderServiceItemBinding
        return ViewHolder(binding.root, viewType, binding, mContext, addressList)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        viewHolder = holder
        holder.binding!!.tvServiceName.text = addressList!![position].service?.name
        holder.binding!!.tvQuantity.setText(mContext.resources.getString(R.string.quantity) + ": " + addressList!![position].quantity)
        // holder.binding!!.tvTime.setText(": " + addressList!![position].Timing)
        // holder.binding!!.tvDate.setText(": " + addressList!![position].created_at)

        Glide.with(mContext)
                .load(addressList!![position].service?.icon)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                .placeholder(R.drawable.ic_category)
                .into(holder.binding!!.imgService!!)

    }


    override fun getItemCount(): Int {
        return addressList!!.count()
    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
    (
            v: View, val viewType: Int, //These are the general elements in the RecyclerView
            val binding: OrderServiceItemBinding?,
            mContext: BaseActivity,
            addressList: ArrayList<OrdersListResponse.Suborders>?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}