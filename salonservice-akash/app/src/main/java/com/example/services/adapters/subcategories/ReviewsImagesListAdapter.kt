package com.uniongoods.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.services.R
import com.example.services.databinding.ImageItemBinding
import com.example.services.model.services.DateSlotsResponse
import com.example.services.views.ratingreviews.WriteReviewsActivity

class ReviewsImagesListAdapter(
    context: WriteReviewsActivity,
    addressList: ArrayList<String>,
    var activity: Context
) :
    RecyclerView.Adapter<ReviewsImagesListAdapter.ViewHolder>() {
    private val mContext: WriteReviewsActivity
    private var viewHolder: ViewHolder? = null
    private var dateList: ArrayList<String>

    init {
        this.mContext = context
        this.dateList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.image_item,
            parent,
            false
        ) as ImageItemBinding
        return ViewHolder(binding.root, viewType, binding, mContext, dateList)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        viewHolder = holder

        Glide.with(mContext)
            .load(dateList[position])
            .placeholder(R.drawable.ic_add_photo)
            .into(holder.binding!!.imgProduct)
        holder.binding!!.imgCross.setOnClickListener {
            mContext.removeImage(position)
        }
    }

    override fun getItemCount(): Int {
        return dateList.count()
    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
        (
        v: View, val viewType: Int, //These are the general elements in the RecyclerView
        val binding: ImageItemBinding?,
        mContext: WriteReviewsActivity,
        addressList: ArrayList<String>?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}