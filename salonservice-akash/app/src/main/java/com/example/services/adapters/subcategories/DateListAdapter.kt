package com.uniongoods.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.services.R
import com.example.services.constants.GlobalConstants
import com.example.services.databinding.TimeItemBinding
import com.example.services.model.services.DateSlotsResponse
import com.example.services.views.cart.CheckoutAddressActivity
import com.example.services.views.subcategories.ServiceDetailActivity

class DateListAdapter(
        context: CheckoutAddressActivity,
        addressList: ArrayList<DateSlotsResponse.Body>,
        var activity: Context
) :
        RecyclerView.Adapter<DateListAdapter.ViewHolder>() {
    private val mContext: CheckoutAddressActivity
    private var viewHolder: ViewHolder? = null
    private var dateList: ArrayList<DateSlotsResponse.Body>

    init {
        this.mContext = context
        this.dateList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.time_item,
                parent,
                false
        ) as TimeItemBinding
        return ViewHolder(binding.root, viewType, binding, mContext, dateList)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        viewHolder = holder
        holder.binding!!.tvCatName.text = dateList[position].date


        if (dateList[position].selected.equals("true")) {
            // holder.binding.topLay.setBackgroundResource(R.drawable.btn_bg_shape_colored)
            // holder.binding.tvCatName.setBackgroundColor(mContext.resources.getColor(R.color.btnBackground))
            holder.binding.tvCatName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(GlobalConstants.COLOR_CODE))/*mContext.getResources().getColorStateList(R.color.colorOrange)*/)

            holder.binding.tvCatName.setTextColor(mContext.resources.getColor(R.color.colorWhite))
        } else {
            // holder.binding.topLay.setBackgroundResource(R.drawable.shape_round_corner)
            holder.binding.tvCatName.setTextColor(mContext.resources.getColor(R.color.colorBlack))
            holder.binding.tvCatName.setBackgroundColor(mContext.resources.getColor(R.color.btnBackgroundWhite))
            holder.binding.tvCatName.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorWhite))

        }

        /*  if(dateList[position].status.equals("Open")){
              holder.binding.tvCatName.isEnabled=true
              //  holder.binding.mainLayout.setBackgroundColor(mContext.resources.getColor(R.color.colorSuccess))
          }else{
              holder.binding.tvCatName.isEnabled=false
              holder.binding.tvCatName.setBackgroundColor(mContext.resources.getColor(R.color.colorGrey))
          }*/
        holder.binding!!.tvCatName.setOnClickListener {
            mContext.selectDatelot(position)
        }
    }

    override fun getItemCount(): Int {
        return dateList.count()
    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
    (
            v: View, val viewType: Int, //These are the general elements in the RecyclerView
            val binding: TimeItemBinding?,
            mContext: CheckoutAddressActivity,
            addressList: ArrayList<DateSlotsResponse.Body>?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}