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
import com.example.services.databinding.VendorItemBinding
import com.example.services.viewmodels.home.Banners
import com.example.services.viewmodels.home.Subcat
import com.example.services.views.home.HomeFragment
import kotlin.collections.ArrayList


class DashboardBannersRecyclerAdapter(
    context: HomeFragment,
    addressList: ArrayList<Banners>,
    var activity: Context
) :
    RecyclerView.Adapter<DashboardBannersRecyclerAdapter.ViewHolder>() {
    private val mContext: HomeFragment
    private var viewHolder: ViewHolder? = null
    private var categoriesList: ArrayList<Banners>

    init {
        this.mContext = context
        this.categoriesList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.vendor_item,
            parent,
            false
        ) as VendorItemBinding
        return ViewHolder(binding.root, viewType, binding, mContext, categoriesList)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        viewHolder = holder
        holder.binding!!.tvVendorName.visibility = View.GONE//text = categoriesList[position].name

        Glide.with(mContext)
            .load(categoriesList[position].url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .placeholder(R.drawable.ic_category)
            .into(holder.binding.tvVendorImage)
    }

    override fun getItemCount(): Int {
        return categoriesList.count()
    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
        (
        v: View, val viewType: Int, //These are the general elements in the RecyclerView
        val binding: VendorItemBinding?,
        mContext: HomeFragment,
        addressList: ArrayList<Banners>
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}