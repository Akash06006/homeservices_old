package com.uniongoods.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.services.R
import com.example.services.databinding.CategoryItemBinding
import com.example.services.views.home.HomeFragment
import com.example.services.views.subcategories.ServiceDetailActivity
import kotlinx.android.synthetic.main.trending_service_item.view.*
import kotlin.collections.ArrayList

class TrendingServiceListAdapter(
        context: HomeFragment,
        addressList: ArrayList<com.example.services.viewmodels.home.Trending>,
        var activity: Context
) : PagerAdapter() {
    private var inflater: LayoutInflater? = null
    // private val images = arrayOf(R.drawable.anton, R.drawable.frankjpg, R.drawable.redcharlie, R.drawable.westboundary)
    private val mContext: HomeFragment
    private var viewHolder: CategoriesGridListAdapter.ItemHolder? = null
    private var categoriesList: ArrayList<com.example.services.viewmodels.home.Trending>

    init {
        this.mContext = context
        this.categoriesList = addressList
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return categoriesList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater!!.inflate(R.layout.trending_service_item, null)
        // view.imageView_slide.setImageResource(images[position])
        view.tv_service_name!!.text = categoriesList[position].name
        Glide.with(mContext)
                .load(categoriesList[position].icon)
                // .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                .placeholder(R.drawable.ic_category)
                .into(view.img_service!!)

        view.trending_item.setOnClickListener {
            val intent = Intent(activity, ServiceDetailActivity::class.java)
            intent.putExtra("serviceId", categoriesList[position].id)
            mContext.startActivity(intent)
        }
        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }

}