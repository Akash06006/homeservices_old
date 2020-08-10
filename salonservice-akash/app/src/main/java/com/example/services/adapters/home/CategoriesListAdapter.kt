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
import com.example.services.databinding.CategoryItemBinding
import com.example.services.views.home.HomeFragment
import kotlin.collections.ArrayList

class CategoriesListAdapter(
        context : HomeFragment,
        addressList : ArrayList<com.example.services.viewmodels.home.Services>,
        var activity : Context
) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {
    private val mContext : HomeFragment
    private var viewHolder : ViewHolder? = null
    private var categoriesList : ArrayList<com.example.services.viewmodels.home.Services>

    init {
        this.mContext = context
        this.categoriesList = addressList
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent : ViewGroup, viewType : Int) : ViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.category_item,
            parent,
            false
        ) as CategoryItemBinding
        return ViewHolder(binding.root, viewType, binding, mContext, categoriesList)
    }

    override fun onBindViewHolder(@NonNull holder : ViewHolder, position : Int) {
        viewHolder = holder
       holder.binding!!.catHeader.setText(categoriesList[position].name)
       // holder.binding!!.catDes.text(categoriesList[position].)
        Glide.with(mContext)
            .load(categoriesList[position].icon)
            .placeholder(R.drawable.ic_category)
            .into( holder.binding.catImg)
    }

    override fun getItemCount() : Int {
        return categoriesList.count()
    }

    inner class ViewHolder//This constructor would switch what to findViewBy according to the type of viewType
        (
        v : View, val viewType : Int, //These are the general elements in the RecyclerView
        val binding : CategoryItemBinding?,
        mContext : HomeFragment,
        addressList : ArrayList<com.example.services.viewmodels.home.Services>?
    ) : RecyclerView.ViewHolder(v) {
        /*init {
            binding.linAddress.setOnClickListener {
                mContext.deleteAddress(adapterPosition)
            }
        }*/
    }
}