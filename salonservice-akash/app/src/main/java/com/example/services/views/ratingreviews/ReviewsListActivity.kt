package com.example.services.views.ratingreviews

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.services.databinding.ActivityReviewsListBinding
import com.example.services.R
import com.example.services.common.UtilsFunctions
import com.example.services.model.ratnigreviews.ReviewsListResponse
import com.example.services.utils.BaseActivity
import com.example.services.viewmodels.ratingreviews.RatingReviewsViewModel
import com.google.gson.JsonObject
import com.uniongoods.adapters.PromoCodeListAdapter
import com.uniongoods.adapters.ReviewsListAdapter

class ReviewsListActivity : BaseActivity() {
    lateinit var reviewsBinding: ActivityReviewsListBinding
    lateinit var reviewsViewModel: RatingReviewsViewModel
    var reviewsAdapter: ReviewsListAdapter? = null
    var cartObject = JsonObject()
    var count = 0
    var serviceId = ""
    var mPastVisibleItems = 0
    var mVisibleItemCount = 0
    var mTotalItemCount = 0
    var mLoadMoreViewCheck = true
    lateinit var linearLayoutManager: LinearLayoutManager
    var reivewsList = ArrayList<ReviewsListResponse.Data>()
    override fun getLayoutId(): Int {
        return R.layout.activity_reviews_list
    }

    override fun initViews() {

        reviewsBinding = viewDataBinding as ActivityReviewsListBinding
        reviewsViewModel = ViewModelProviders.of(this).get(RatingReviewsViewModel::class.java)
        linearLayoutManager = LinearLayoutManager(this)
        reviewsBinding.commonToolBar.imgRight.visibility = View.GONE
        reviewsBinding.commonToolBar.imgRight.setImageResource(R.drawable.ic_cart)
        reviewsBinding.commonToolBar.imgToolbarText.text =
                resources.getString(R.string.reviews)
        reviewsBinding.reviewsViewModel = reviewsViewModel
        serviceId = intent.extras?.get("serviceId").toString()

        reviewsAdapter = ReviewsListAdapter(this, reivewsList, this)
        if (UtilsFunctions.isNetworkConnected()) {
            reviewsViewModel.getReviewsList(serviceId, count.toString())
            startProgressDialog()
        }
        initRecyclerView()
        //addRecords()
        UtilsFunctions.hideKeyBoard(reviewsBinding.tvNoRecord)
        reviewsViewModel.getReviewsRes().observe(this,
                Observer<ReviewsListResponse> { response ->
                    stopProgressDialog()
                    if (response != null) {
                        mLoadMoreViewCheck = true
                        val message = response.message
                        when {
                            response.code == 200 -> {
                                reivewsList.addAll(response.body!!.data!!)
                                reviewsBinding.rvReviews.visibility = View.VISIBLE
                                reviewsBinding.tvNoRecord.visibility = View.GONE
                                reviewsBinding.title.visibility = View.GONE
                                //initRecyclerView()
                                reviewsAdapter?.notifyDataSetChanged()
                            }
                            else -> message?.let {
                                UtilsFunctions.showToastError(it)
                                //reviewsBinding.rvReviews.visibility = View.GONE
                                //reviewsBinding.tvNoRecord.visibility = View.VISIBLE
                            }
                        }

                    }
                })


    }


    fun loadMoreData() {
        count++
        if (UtilsFunctions.isNetworkConnected()) {
            reviewsViewModel.getReviewsList(serviceId, count.toString())
            startProgressDialog()
        }
    }

    private fun initRecyclerView() {
        reviewsBinding.rvReviews.setHasFixedSize(true)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        reviewsBinding.rvReviews.layoutManager = linearLayoutManager
        reviewsBinding.rvReviews.adapter = reviewsAdapter
        reviewsBinding.rvReviews.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                /*val totalItemCount = recyclerView.layoutManager!!.itemCount
                if (totalItemCount == lastVisibleItemPosition + 1) {

                }*/
                if (dy > 0) //check for scroll down
                {
                    mVisibleItemCount = linearLayoutManager.childCount
                    mTotalItemCount = linearLayoutManager.itemCount
                    mPastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()
                    if (mLoadMoreViewCheck) {
                        if ((mVisibleItemCount + mPastVisibleItems) >= mTotalItemCount) {
                            mLoadMoreViewCheck = false
                            count++
                            //reviewsViewModel.getReviewsList(serviceId, count.toString())
                            addRecords()
                        }
                    }
                }
            }
        })
    }

    private fun addRecords() {
        for (i in 0..9) {
            var item = ReviewsListResponse()
            reivewsList.add(item.Data())
        }
        reviewsAdapter?.notifyDataSetChanged()
    }

    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()

}
