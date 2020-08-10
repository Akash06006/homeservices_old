package com.uniongoods.adapters

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.services.R
import com.example.services.databinding.AddressItemBinding
import com.example.services.model.address.AddressListResponse
import com.example.services.views.address.AddressListActivity

class AddressListAdapter(
    context : AddressListActivity,
    addressList : ArrayList<AddressListResponse.Body>,
    var activity : Context
) :
    RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {
    private val mContext : AddressListActivity
    private var viewHolder : ViewHolder? = null
    private var addressList : ArrayList<AddressListResponse.Body>

    init {
        this.mContext = context
        this.addressList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent : ViewGroup, viewType : Int) : ViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.address_item,
            parent,
            false
        ) as AddressItemBinding
        return ViewHolder(binding.root, viewType, binding, mContext, addressList)
    }

    override fun onBindViewHolder(@NonNull holder : ViewHolder, position : Int) {
        viewHolder = holder
        holder.binding!!.tvAddress.text = addressList[position].addressType
        holder.binding!!.tvAddressDetail.text = addressList[position].addressName
        if (!TextUtils.isEmpty(addressList[position].default) && addressList[position].default.equals(
                "1"
            )
        ) {
            holder.binding.rdDefault.setChecked(true)
            holder.binding.rdDefault.setClickable(false)
        } else {
            holder.binding.rdDefault.setChecked(false)
            holder.binding.rdDefault.setClickable(true)
        }
        if (addressList[position].addressType.equals(mContext.getString(R.string.home))) {
            holder.binding.addresssImg.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_home))
        } else if (addressList[position].addressType.equals(mContext.getString(R.string.work))) {
            holder.binding.addresssImg.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_work))
        } else {
            holder.binding.addresssImg.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_other))
        }
        holder.binding.imgAddress.setOnClickListener {
            mContext.deleteAddress(addressList[position].id)
        }

        holder.binding.rdDefault.setOnClickListener {
            if (!TextUtils.isEmpty(addressList[position].default) && !addressList[position].default.equals(
                    "1"
                )
            ) {
                mContext.makeDefaultAddress(position)
                holder.binding.rdDefault.setChecked(false)
            } else {
                holder.binding.rdDefault.setChecked(true)
            }
        }
    }

    override fun getItemCount() : Int {
        return addressList.count()
    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
        (
        v : View, val viewType : Int, //These are the general elements in the RecyclerView
        val binding : AddressItemBinding?,
        mContext : AddressListActivity,
        addressList : ArrayList<AddressListResponse.Body>?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}