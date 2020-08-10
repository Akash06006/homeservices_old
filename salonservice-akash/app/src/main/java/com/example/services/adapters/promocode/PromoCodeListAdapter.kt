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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.services.R
import com.example.services.constants.GlobalConstants
import com.example.services.databinding.PromoCodeItemBinding
import com.example.services.model.promocode.PromoCodeListResponse
import com.example.services.views.promocode.PromoCodeActivity
import java.util.*
import kotlin.collections.ArrayList

class PromoCodeListAdapter(
        context: PromoCodeActivity,
        addressList: ArrayList<PromoCodeListResponse.Body>,
        var activity: Context
) :
        RecyclerView.Adapter<PromoCodeListAdapter.ViewHolder>() {
    private val mContext: PromoCodeActivity
    private var viewHolder: ViewHolder? = null
    private var addressList: ArrayList<PromoCodeListResponse.Body>

    init {
        this.mContext = context
        this.addressList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.promo_code_item,
                parent,
                false
        ) as PromoCodeItemBinding
        return ViewHolder(binding.root, viewType, binding, mContext, addressList)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        viewHolder = holder
        holder.binding!!.tvPromo.text = addressList[position].name
        holder.binding!!.tvPromoCode.text = addressList[position].code
        holder.binding!!.tvDiscount.text = addressList[position].discount + "%"

        val rnd = Random();
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        if (color.equals(mContext.resources.getColor(R.color.colorBlack))) {
            //mContext.baseActivity.showToastError("black")
            holder.binding!!.tvDiscount.setTextColor(Color.WHITE)
            holder.binding!!.tvDiscount.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorWhite))

            holder.binding!!.tvDiscount.setTextColor(Color.WHITE)
        } else {
            // mContext.baseActivity.showToastError("other")
            holder.binding!!.tvDiscount.setBackgroundTintList(ColorStateList.valueOf(color)/*mContext.getResources().getColorStateList(R.color.colorOrange)*/)

        }
        //holder.binding!!.tvDiscount.setBackgroundColor(color)

        holder.binding!!.tvPromoDesc.setText(
                addressList[position].description
        )

        //holder.binding!!.rBar.setRating(addressList[position].rating?.toFloat())
        Glide.with(mContext)
                .load(addressList[position].icon)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                .placeholder(R.drawable.ic_category)
                .into(holder.binding.imgPromo)

        holder.binding!!.btnApply.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(GlobalConstants.COLOR_CODE))/*mContext.getResources().getColorStateList(R.color.colorOrange)*/)

        holder.binding!!.btnApply.setOnClickListener {
            mContext.callApplyCouponApi(addressList[position].code!!)
        }


    }

    override fun getItemCount(): Int {
        return addressList.count()
    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
    (
            v: View, val viewType: Int, //These are the general elements in the RecyclerView
            val binding: PromoCodeItemBinding?,
            mContext: PromoCodeActivity,
            addressList: ArrayList<PromoCodeListResponse.Body>?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}