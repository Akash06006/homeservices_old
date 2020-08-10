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
import com.example.services.databinding.RowRateListingBinding
import com.example.services.model.promocode.PromoCodeListResponse
import com.example.services.model.ratnigreviews.ReviewsListResponse
import com.example.services.views.promocode.PromoCodeActivity
import com.example.services.views.ratingreviews.ReviewsListActivity

class ReviewsListAdapter(
        context: ReviewsListActivity,
        addressList: ArrayList<ReviewsListResponse.Data>?,
        var activity: Context
) :
        RecyclerView.Adapter<ReviewsListAdapter.ViewHolder>() {
    private val mContext: ReviewsListActivity
    private var viewHolder: ViewHolder? = null
    private var addressList: ArrayList<ReviewsListResponse.Data>?

    init {
        this.mContext = context
        this.addressList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_rate_listing,
                parent,
                false
        ) as RowRateListingBinding
        return ViewHolder(binding.root, viewType, binding, mContext, addressList)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        viewHolder = holder
         holder.binding!!.tvServiceName.text = addressList?.get(position)?.user?.firstName + " " + addressList?.get(position)?.user?.lastName
         holder.binding!!.tvServiceDetail.text = addressList?.get(position)?.review

         holder.binding!!.rBar.setRating(addressList?.get(position)?.rating!!.toFloat())//setRating(addressList[position].rating?.toFloat())
         Glide.with(mContext)
                 .load(addressList!![position].user?.image)
                 //.apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                 .placeholder(R.drawable.ic_category)
                 .into(holder.binding.ivServiceImage)


    }

    override fun getItemCount(): Int {

        return addressList!!.count()


    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
    (
            v: View, val viewType: Int, //These are the general elements in the RecyclerView
            val binding: RowRateListingBinding?,
            mContext: ReviewsListActivity,
            addressList: ArrayList<ReviewsListResponse.Data>?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}