package com.uniongoods.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
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
import com.example.services.databinding.RowAddRateListingBinding
import com.example.services.model.promocode.PromoCodeListResponse
import com.example.services.model.ratnigreviews.RatingReviewListInput
import com.example.services.model.ratnigreviews.ReviewsListResponse
import com.example.services.views.promocode.PromoCodeActivity
import com.example.services.views.ratingreviews.AddRatingReviewsListActivity

class AddRatingReviewsListAdapter(
    context: AddRatingReviewsListActivity,
    addressList: RatingReviewListInput?,
    var activity: Context
) :
    RecyclerView.Adapter<AddRatingReviewsListAdapter.ViewHolder>() {
    private val mContext: AddRatingReviewsListActivity
    private var viewHolder: ViewHolder? = null
    private var addressList: RatingReviewListInput?

    init {
        this.mContext = context
        this.addressList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_add_rate_listing,
            parent,
            false
        ) as RowAddRateListingBinding
        return ViewHolder(binding.root, viewType, binding, mContext, addressList)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        viewHolder = holder
        holder.binding!!.tvServiceName.text = addressList?.ratingData?.get(position)?.name
        holder.binding!!.tvServiceDetail.text = addressList?.ratingData?.get(position)?.review

        if (addressList?.ratingData?.get(position)?.rating!!.toFloat() > 0) {
            mContext.visibleSubmit()
        }
        holder.binding!!.rBar.setRating(addressList?.ratingData?.get(position)?.rating!!.toFloat())//setRating(addressList[position].rating?.toFloat())
        Glide.with(mContext)
            .load(addressList?.ratingData?.get(position)?.icon)
            //.apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .placeholder(R.drawable.ic_category)
            .into(holder.binding.ivServiceImage)
        holder.binding!!.tvAddRating.setBackgroundTintList(
            ColorStateList.valueOf(
                Color.parseColor(
                    GlobalConstants.COLOR_CODE
                )
            )/*mContext.getResources().getColorStateList(R.color.colorOrange)*/
        )


        if (position + 1 == addressList?.ratingData?.size) {
            holder.binding!!.tvAddRating.setText(mContext.resources.getString(R.string.rate_service_agent))
        }
        holder.binding!!.tvAddRating.setOnClickListener {
            mContext.addRating(position/*, holder.binding!!.imgCart.getText().toString()*/)
        }
    }

    override fun getItemCount(): Int {
        if (addressList?.ratingData?.size!! > 0) {
            return addressList?.ratingData!!.count()
        } else {
            return 0
        }


    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
        (
        v: View, val viewType: Int, //These are the general elements in the RecyclerView
        val binding: RowAddRateListingBinding?,
        mContext: AddRatingReviewsListActivity,
        addressList: RatingReviewListInput?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}