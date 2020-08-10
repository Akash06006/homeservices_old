package com.uniongoods.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.services.R
import com.example.services.constants.GlobalConstants
import com.example.services.databinding.ServicesItemBinding
import com.example.services.model.services.Services
import com.example.services.views.subcategories.ServicesListActivity

class ServicesListAdapter(
    context: ServicesListActivity,
    addressList: ArrayList<Services>,
    var activity: Context
) :
    RecyclerView.Adapter<ServicesListAdapter.ViewHolder>() {
    private val mContext: ServicesListActivity
    private var viewHolder: ViewHolder? = null
    private var addressList: ArrayList<Services>

    init {
        this.mContext = context
        this.addressList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.services_item,
            parent,
            false
        ) as ServicesItemBinding
        return ViewHolder(binding.root, viewType, binding, mContext, addressList)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        viewHolder = holder
        holder.binding!!.tvCatName.text = addressList[position].name
        holder.binding!!.tvOfferPrice.text =
            GlobalConstants.Currency + " " + addressList[position].price.toString()
        holder.binding!!.tvDuration.setText(mContext.resources.getString(R.string.duration) + ": " + addressList[position].duration)
        // holder.binding!!.rBar.setRating(addressList[position].rating.toFloat())
        Glide.with(mContext)
            .load(addressList[position].icon)
            // .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .placeholder(R.drawable.ic_category)
            .into(holder.binding.imgCat)

        if (TextUtils.isEmpty(addressList[position].favourite)) {
            holder.binding.imgFavourite.setImageResource(R.drawable.ic_unfavorite)
        } else {
            holder.binding.imgFavourite.setImageResource(R.drawable.ic_favorite)
        }
        //is button ka krna h

        holder.binding!!.tvAdd.setBackgroundTintList(
            ColorStateList.valueOf(
                Color.parseColor(
                    GlobalConstants.COLOR_CODE
                )
            )/*mContext.getResources().getColorStateList(R.color.colorOrange)*/
        )

        //img_cat
        /* holder.binding!!.tvAdd.setOnClickListener {
             mContext.addRemoveToCart(position, holder.binding!!.tvAdd.getText().toString())
         }*/
        //img_cat
        holder.binding!!.serviceItem.setOnClickListener {
            mContext.callServiceDetail(addressList[position].id)
        }
        holder.binding!!.imgFavourite.setOnClickListener {
            mContext.addRemovefav(position, addressList[position].favourite)

        }
    }

    override fun getItemCount(): Int {
        return addressList.count()
    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
        (
        v: View, val viewType: Int, //These are the general elements in the RecyclerView
        val binding: ServicesItemBinding?,
        mContext: ServicesListActivity,
        addressList: ArrayList<Services>?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}