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
import com.example.services.databinding.ServiceSubcatItemBinding
import com.example.services.model.services.Headers
import com.example.services.model.services.SubCategory
import com.example.services.views.subcategories.ServicesListActivity

class SubCategoriesFilterListAdapter(
        context: ServicesListActivity,
        addressList: ArrayList<Headers>,
        var activity: Context
) :
        RecyclerView.Adapter<SubCategoriesFilterListAdapter.ViewHolder>() {
    private val mContext: ServicesListActivity
    private var viewHolder: ViewHolder? = null
    private var addressList: ArrayList<Headers>

    init {
        this.mContext = context
        this.addressList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.service_subcat_item,
                parent,
                false
        ) as ServiceSubcatItemBinding
        return ViewHolder(binding.root, viewType, binding, mContext, addressList)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        viewHolder = holder
        holder.binding!!.tvCatName.text = addressList[position].name

        if (addressList[position].isSelected.equals("true")) {
            //holder.binding.topLay.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.btn_bg_shape_colored))
            holder.binding.tvCatName.setBackgroundColor(mContext.resources.getColor(R.color.btnBackground))
            holder.binding.tvCatName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(GlobalConstants.COLOR_CODE))/*mContext.getResources().getColorStateList(R.color.colorOrange)*/)

            holder.binding.tvCatName.setTextColor(mContext.resources.getColor(R.color.colorWhite))
        } else {
            // holder.binding.topLay.setBackgroundResource(R.drawable.shape_round_corner)
            holder.binding.tvCatName.setTextColor(mContext.resources.getColor(R.color.colorBlack))
            holder.binding.tvCatName.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorWhite))

            //holder.binding.tvCatName.setBackgroundColor(mContext.resources.getColor(R.color.btnBackgroundWhite))
        }
        holder.binding!!.tvCatName.setOnClickListener {
            mContext.selectSubCat(addressList[position].id, position)
        }
    }

    override fun getItemCount(): Int {
        return addressList.count()
    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
    (
            v: View, val viewType: Int, //These are the general elements in the RecyclerView
            val binding: ServiceSubcatItemBinding?,
            mContext: ServicesListActivity,
            addressList: ArrayList<Headers>?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}