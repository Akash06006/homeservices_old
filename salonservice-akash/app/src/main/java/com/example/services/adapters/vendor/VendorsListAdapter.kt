package com.uniongoods.adapters

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.services.R
import com.example.services.constants.GlobalConstants
import com.example.services.databinding.VendorItemBinding
import com.example.services.model.cart.CartListResponse
import com.example.services.model.fav.FavListResponse
import com.example.services.model.vendor.VendorListResponse
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.views.favorite.FavoriteListActivity
import com.example.services.views.home.DashboardActivity
import com.example.services.views.vendor.VendorsListActivity

class VendorsListAdapter(
    context: VendorsListActivity,
    addressList: ArrayList<VendorListResponse.Body>
    //  var activity: Context
) :
    RecyclerView.Adapter<VendorsListAdapter.ViewHolder>() {
    private val mContext: VendorsListActivity
    private var viewHolder: ViewHolder? = null
    private var addressList: ArrayList<VendorListResponse.Body>

    init {
        this.mContext = context
        this.addressList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.vendor_item,
            parent,
            false
        ) as VendorItemBinding
        return ViewHolder(binding.root, viewType, binding, mContext, addressList)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        viewHolder = holder

        holder.binding!!.tvVendorName.text = addressList[position].companyName
        holder.binding!!.tvAddress.text = addressList[position].address1

        // holder.binding.imgFavourite.setImageResource(R.drawable.ic_delete)
        //holder.binding!!.rBar.setRating(addressList[position].rating?.toFloat())
        Glide.with(mContext)
            .load(addressList[position].logo1)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .placeholder(R.drawable.ic_category)
            .into(holder.binding.tvVendorImage)


        holder.binding!!.tvVendorImage.setOnClickListener {
            if (!TextUtils.isEmpty(addressList[position].cartCompanyId) && !addressList[position].cartCompanyId.equals(
                    addressList[position].id
                )
            ) {
                mContext.clearCartDialog()
            } else {
                /* SharedPrefClass().putObject(
                     mContext,
                     GlobalConstants.COMPANY_ID,
                     addressList[position].id
                 )*/
                GlobalConstants.COMPANY_ID = addressList[position].id.toString()
                val intent = Intent(mContext, DashboardActivity::class.java)
                mContext.startActivity(intent)
            }
            // mContext.callServiceDetail(addressList[position].service?.id!!)
            /*SharedPrefClass().putObject(
                mContext,
                GlobalConstants.COMPANY_ID,
                addressList[position].id

            )*/

        }


    }

    override fun getItemCount(): Int {
        return addressList.count()
    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
        (
        v: View, val viewType: Int, //These are the general elements in the RecyclerView
        val binding: VendorItemBinding?,
        mContext: VendorsListActivity,
        addressList: ArrayList<VendorListResponse.Body>?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}