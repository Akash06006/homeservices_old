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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.services.R
import com.example.services.constants.GlobalConstants
import com.example.services.databinding.ServicesItemBinding
import com.example.services.model.cart.CartListResponse
import com.example.services.model.fav.FavListResponse
import com.example.services.views.favorite.FavoriteListActivity

class FavoriteListAdapter(
    context: FavoriteListActivity,
    addressList: ArrayList<FavListResponse.Body>,
    var activity: Context
) :
    RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {
    private val mContext: FavoriteListActivity
    private var viewHolder: ViewHolder? = null
    private var addressList: ArrayList<FavListResponse.Body>

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

        holder.binding!!.tvCatName.text = addressList[position].service?.name
        holder.binding!!.tvOfferPrice.text =
            GlobalConstants.Currency + " " + addressList[position].service?.price.toString()
        holder.binding!!.tvDuration.setText(mContext.resources.getString(R.string.duration) + ": " + addressList[position].service?.duration)
        holder.binding!!.tvAdd.setBackgroundTintList(
            ColorStateList.valueOf(
                Color.parseColor(
                    GlobalConstants.COLOR_CODE
                )
            )/*mContext.getResources().getColorStateList(R.color.colorOrange)*/
        )

        holder.binding.imgFavourite.setImageResource(R.drawable.ic_favorite)
        //holder.binding!!.rBar.setRating(addressList[position].rating?.toFloat())
        Glide.with(mContext)
            .load(addressList[position].service?.icon)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .placeholder(R.drawable.ic_category)
            .into(holder.binding.imgCat)

        holder.binding!!.cardView.setOnClickListener {
            if (!TextUtils.isEmpty(addressList[position].cartCategoryType.toString()) && !addressList[position].cartCategoryCompany.equals(
                    addressList[position].companyId.toString()
                )
            ) {
                mContext.clearCartDialog()
            } else {
                mContext.callServiceDetail(addressList[position].service?.id!!)
            }

        }

        holder.binding!!.imgFavourite.setOnClickListener {
            mContext.addRemoveToCart(position/*, holder.binding!!.imgCart.getText().toString()*/)
        }

    }

    override fun getItemCount(): Int {
        return addressList.count()
    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
        (
        v: View, val viewType: Int, //These are the general elements in the RecyclerView
        val binding: ServicesItemBinding?,
        mContext: FavoriteListActivity,
        addressList: ArrayList<FavListResponse.Body>?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}