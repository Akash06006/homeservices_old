package com.uniongoods.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.services.R
import com.example.services.constants.GlobalConstants
import com.example.services.databinding.TimeItemBinding
import com.example.services.model.services.TimeSlotsResponse
import com.example.services.views.cart.CheckoutAddressActivity

class TimeSlotsListAdapter(
    context: CheckoutAddressActivity,
    addressList: ArrayList<TimeSlotsResponse.Slots>,
    var activity: Context
) :
    RecyclerView.Adapter<TimeSlotsListAdapter.ViewHolder>() {
    private val mContext: CheckoutAddressActivity
    private var viewHolder: ViewHolder? = null
    private var addressList: ArrayList<TimeSlotsResponse.Slots>

    init {
        this.mContext = context
        this.addressList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.time_item,
            parent,
            false
        ) as TimeItemBinding
        return ViewHolder(binding.root, viewType, binding, mContext, addressList)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        viewHolder = holder
        holder.binding!!.tvCatName.text = addressList[position].slot
        if (addressList[position].status.equals("1")) {
            if (!TextUtils.isEmpty(addressList[position].selected) && addressList[position].selected.equals(
                    "true"
                )
            ) {
                // holder.binding.topLay.setBackgroundResource(R.drawable.btn_bg_shape_colored)
                //holder.binding.tvCatName.setBackgroundColor(mContext.resources.getColor(R.color.btnBackground))
                holder.binding.tvCatName.setBackgroundTintList(
                    ColorStateList.valueOf(
                        Color.parseColor(
                            GlobalConstants.COLOR_CODE
                        )
                    )/*mContext.getResources().getColorStateList(R.color.colorOrange)*/
                )
                holder.binding.tvCatName.setTextColor(mContext.resources.getColor(R.color.colorWhite))
            } else {
                // holder.binding.topLay.setBackgroundResource(R.drawable.shape_round_corner)
                holder.binding.tvCatName.setTextColor(mContext.resources.getColor(R.color.colorBlack))
                holder.binding.tvCatName.setBackgroundColor(mContext.resources.getColor(R.color.btnBackgroundWhite))
                holder.binding.tvCatName.setBackgroundTintList(
                    mContext.getResources().getColorStateList(
                        R.color.colorWhite
                    )
                )

            }
        } else if (addressList[position].status.equals("0")) {
            holder.binding.tvCatName.isEnabled = false
            holder.binding.tvCatName.setTextColor(mContext.resources.getColor(R.color.colorBlack))
            holder.binding.tvCatName.setBackgroundColor(mContext.resources.getColor(R.color.btnBackgroundWhite))
            holder.binding.tvCatName.setBackgroundTintList(
                mContext.getResources().getColorStateList(
                    R.color.colorGrey
                )
            )
        } else {
            holder.binding.tvCatName.isEnabled = false
            holder.binding.tvCatName.setTextColor(mContext.resources.getColor(R.color.colorWhite))
            holder.binding.tvCatName.setBackgroundColor(mContext.resources.getColor(R.color.btnBackgroundWhite))
            holder.binding.tvCatName.setBackgroundTintList(
                mContext.getResources().getColorStateList(
                    R.color.colorRed
                )
            )
        }

        holder.binding!!.tvCatName.setOnClickListener {
            mContext.selectTimeSlot(position)
        }
    }

    override fun getItemCount(): Int {
        return addressList.count()
    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
        (
        v: View, val viewType: Int, //These are the general elements in the RecyclerView
        val binding: TimeItemBinding?,
        mContext: CheckoutAddressActivity,
        addressList: ArrayList<TimeSlotsResponse.Slots>?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}