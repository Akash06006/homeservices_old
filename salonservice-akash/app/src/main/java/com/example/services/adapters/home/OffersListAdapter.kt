package com.uniongoods.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.services.R
import com.example.services.databinding.CategoryItemBinding
import com.example.services.views.home.HomeFragment
import kotlinx.android.synthetic.main.trending_service_item.view.*
import kotlin.collections.ArrayList

class OffersListAdapter(
        context : HomeFragment,
        addressList : ArrayList<com.example.services.viewmodels.home.Banners>,
        var activity : Context
) : PagerAdapter() {
    private var inflater : LayoutInflater? = null
    // private val images = arrayOf(R.drawable.anton, R.drawable.frankjpg, R.drawable.redcharlie, R.drawable.westboundary)
    private val mContext : HomeFragment
    private var viewHolder : CategoriesGridListAdapter.ItemHolder? = null
    private var offersList : ArrayList<com.example.services.viewmodels.home.Banners>

    init {
        this.mContext = context
        this.offersList = addressList
    }

    override fun isViewFromObject(view : View, `object` : Any) : Boolean {
        return view === `object`
    }

    override fun getCount() : Int {
        return offersList.size
    }

    override fun instantiateItem(container : ViewGroup, position : Int) : Any {
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater!!.inflate(R.layout.trending_service_item, null)
        // view.imageView_slide.setImageResource(images[position])
        view.tv_service_name!!.text = offersList[position].name
        Glide.with(mContext)
            .load(offersList[position].icon)
           // .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .placeholder(R.drawable.ic_category)
            .into(view.img_service!!)
        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container : ViewGroup, position : Int, `object` : Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }

}