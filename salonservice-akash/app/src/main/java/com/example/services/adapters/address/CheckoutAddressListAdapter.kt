package com.uniongoods.adapters

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.services.R
import com.example.services.databinding.AddressItemBinding
import com.example.services.model.address.AddressListResponse
import com.example.services.utils.DialogssInterface
import com.example.services.views.address.AddressListActivity
import com.example.services.views.cart.CheckoutAddressActivity

class CheckoutAddressListAdapter(
        context: CheckoutAddressActivity,
        addressList: ArrayList<AddressListResponse.Body>,
        var activity: Context
) :
        RecyclerView.Adapter<CheckoutAddressListAdapter.ViewHolder>() {
    private val mContext: CheckoutAddressActivity
    private var viewHolder: ViewHolder? = null
    private var addressList: ArrayList<AddressListResponse.Body>

    init {
        this.mContext = context
        this.addressList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.address_item,
                parent,
                false
        ) as AddressItemBinding
        return ViewHolder(binding.root, viewType, binding, mContext, addressList)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        viewHolder = holder
        holder.binding!!.imgAddress.visibility = View.GONE
        holder.binding!!.rdDefault.visibility = View.GONE
        holder.binding!!.tvAddress.text = addressList[position].addressType
        holder.binding!!.tvAddressDetail.text = addressList[position].addressName
        if (addressList[position].addressType.equals(mContext.getString(R.string.home))) {
            holder.binding.addresssImg.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_home))
        } else if (addressList[position].addressType.equals(mContext.getString(R.string.work))) {
            holder.binding.addresssImg.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_work))
        } else {
            holder.binding.addresssImg.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_other))
        }
        holder.binding.addressItem.setOnClickListener {
            mContext.selectAddress(position)
        }

    }

    override fun getItemCount(): Int {
        return addressList.count()
    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
    (
            v: View, val viewType: Int, //These are the general elements in the RecyclerView
            val binding: AddressItemBinding?,
            mContext: CheckoutAddressActivity,
            addressList: ArrayList<AddressListResponse.Body>?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }


}