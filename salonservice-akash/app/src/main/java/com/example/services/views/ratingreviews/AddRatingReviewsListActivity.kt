package com.example.services.views.ratingreviews

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.services.databinding.ActivityReviewsListBinding
import com.example.services.R
import com.example.services.common.UtilsFunctions
import com.example.services.constants.GlobalConstants
import com.example.services.model.CommonModel
import com.example.services.model.orders.OrdersDetailResponse
import com.example.services.model.ratnigreviews.EmpRatingData
import com.example.services.model.ratnigreviews.RatingData
import com.example.services.model.ratnigreviews.RatingReviewListInput
import com.example.services.utils.BaseActivity
import com.example.services.utils.DialogClass
import com.example.services.utils.DialogssInterface
import com.example.services.viewmodels.ratingreviews.RatingReviewsViewModel
import com.example.services.views.cart.CartListActivity
import com.google.gson.JsonObject
import com.uniongoods.adapters.AddRatingReviewsListAdapter
import com.uniongoods.adapters.CheckoutAddressListAdapter
import com.uniongoods.adapters.ReviewsListAdapter

class AddRatingReviewsListActivity : BaseActivity(), DialogssInterface {
    lateinit var reviewsBinding: ActivityReviewsListBinding
    lateinit var reviewsViewModel: RatingReviewsViewModel
    var reviewsAdapter: AddRatingReviewsListAdapter? = null
    var cartObject = JsonObject()
    var count = 0
    var orderId = ""
    var mLoadMoreViewCheck = true
    lateinit var linearLayoutManager: LinearLayoutManager
    var suborders: ArrayList<OrdersDetailResponse.Suborders>? = null
    val ratingData = RatingReviewListInput()
    private var confirmationDialog: Dialog? = null
    private var mDialogClass = DialogClass()
    // var ratingReviewInput = ArrayList<RatingReviewListInput>()
    override fun getLayoutId(): Int {
        return R.layout.activity_reviews_list
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        confirmationDialog = mDialogClass.setDefaultDialog(
            this,
            this,
            "Rating",
            getString(R.string.warning_rate_cancel)
        )
        confirmationDialog?.show()
    }

    override fun onDialogConfirmAction(mView: View?, mKey: String) {
        when (mKey) {
            "Rating" -> {
                confirmationDialog?.dismiss()
                finish()
            }
        }
    }

    override fun onDialogCancelAction(mView: View?, mKey: String) {
        when (mKey) {

            "Rating" -> confirmationDialog?.dismiss()

        }
    }

    override fun initViews() {

        reviewsBinding = viewDataBinding as ActivityReviewsListBinding
        reviewsViewModel = ViewModelProviders.of(this).get(RatingReviewsViewModel::class.java)
        linearLayoutManager = LinearLayoutManager(this)
        reviewsBinding.commonToolBar.imgRight.visibility = View.GONE
        reviewsBinding.commonToolBar.imgRight.setImageResource(R.drawable.ic_cart)
        reviewsBinding.commonToolBar.imgToolbarText.text =
            resources.getString(R.string.rating)
        reviewsBinding.reviewsViewModel = reviewsViewModel
        orderId = intent.extras?.get("orderId").toString()
        //ratingReviewInput[0].orderId = orderId
        reviewsBinding.btnSubmit.visibility = View.INVISIBLE
        reviewsBinding.btnSubmit.setBackgroundTintList(
            ColorStateList.valueOf(
                Color.parseColor(
                    GlobalConstants.COLOR_CODE
                )
            )/*mContext.getResources().getColorStateList(R.color.colorOrange)*/
        )
        if (UtilsFunctions.isNetworkConnected()) {
            reviewsViewModel.orderDetail(orderId)
            startProgressDialog()
        }
        // initRecyclerView()

        UtilsFunctions.hideKeyBoard(reviewsBinding.tvNoRecord)
        reviewsViewModel.getOrderDetail().observe(this,
            Observer<OrdersDetailResponse> { response ->
                stopProgressDialog()
                if (response != null) {
                    mLoadMoreViewCheck = true
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            //ratingReviewInput
                            // var ratingReviewInput = RatingReviewListInput("orderId", null)

                            ratingData.orderId = response.data?.id
                            suborders = response.data?.suborders
                            if (suborders?.size!! > 0) {
                                for (item in suborders!!) {
                                    val rating = RatingData()
                                    rating.rating = "0"
                                    rating.review = ""
                                    rating.name = item.service?.name
                                    rating.icon = item.service?.icon
                                    rating.serviceId = item.service?.id
                                    ratingData.ratingData?.add(rating)
                                }
                                val emp = response.data?.assignedEmployees
                                val rating = RatingData()
                                rating.rating = "0"
                                rating.review = ""
                                rating.name =
                                    emp!![0].employee?.firstName + " " + emp[0]?.employee?.lastName
                                rating.icon = emp[0]?.employee?.image
                                rating.serviceId = emp[0]?.employee?.id
                                ratingData.ratingData.add(rating)
                                initRecyclerView()
                                reviewsBinding.rvReviews.visibility = View.VISIBLE
                                reviewsBinding.tvNoRecord.visibility = View.GONE
                                reviewsBinding.title.visibility = View.GONE
                                reviewsAdapter?.notifyDataSetChanged()
                            }
                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                            //reviewsBinding.rvReviews.visibility = View.GONE
                            //reviewsBinding.tvNoRecord.visibility = View.VISIBLE
                        }
                    }

                }
            })

        reviewsViewModel.getRatingRes().observe(this,
            Observer<CommonModel> { response ->
                stopProgressDialog()
                if (response != null) {
                    mLoadMoreViewCheck = true
                    val message = response.message
                    when {
                        response.code == 200 -> {

                            showToastSuccess(message)
                            finish()
                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                            //reviewsBinding.rvReviews.visibility = View.GONE
                            //reviewsBinding.tvNoRecord.visibility = View.VISIBLE
                        }
                    }

                }
            })


        reviewsViewModel.isClick().observe(
            this, Observer<String>(function =
            fun(it: String?) {
                when (it) {
                    "btnSubmit" -> {
                        if (UtilsFunctions.isNetworkConnected()) {
                            startProgressDialog()

                            //  ratingData.ratingData?.add(rating)
                            if (ratingData.ratingData.size > 0) {
                                val last = ratingData.ratingData.size - 1
                                val empRating = EmpRatingData()
                                empRating.rating = ratingData.ratingData[last].rating
                                empRating.review = ratingData.ratingData[last].review
                                empRating.empId = ratingData.ratingData[last].serviceId
                                ratingData.empRatingData.add(empRating)
                                ratingData.ratingData.removeAt(last)
                            }
                            reviewsViewModel.addRatings(ratingData)
                        }

                    }
                }
            })
        )
    }

    fun visibleSubmit() {
        reviewsBinding.btnSubmit.visibility = View.VISIBLE
    }

    private fun initRecyclerView() {
        reviewsAdapter = AddRatingReviewsListAdapter(this, ratingData, this)
        reviewsBinding.rvReviews.setHasFixedSize(true)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        reviewsBinding.rvReviews.layoutManager = linearLayoutManager
        reviewsBinding.rvReviews.adapter = reviewsAdapter
        reviewsBinding.rvReviews.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun addRating(position: Int) {
        confirmationDialog = Dialog(this, R.style.transparent_dialog_borderless)
        confirmationDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(this),
                R.layout.add_rating_dialog,
                null,
                false
            )

        confirmationDialog?.setContentView(binding.root)
        confirmationDialog?.setCancelable(true)

        confirmationDialog?.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        confirmationDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val serviceImage = confirmationDialog?.findViewById<ImageView>(R.id.iv_service_image)
        val serviceName = confirmationDialog?.findViewById<TextView>(R.id.tv_service_name)
        val ratingBar = confirmationDialog?.findViewById<RatingBar>(R.id.rb_ratings)
        val etReview = confirmationDialog?.findViewById<EditText>(R.id.et_review)
        val btnSubmit = confirmationDialog?.findViewById<Button>(R.id.btnSubmit)

        etReview!!.setText(ratingData.ratingData.get(position).review)
        ratingBar!!.setRating(ratingData.ratingData.get(position).rating!!.toFloat())
        serviceName?.setText(ratingData.ratingData.get(position).name)
        Glide.with(this)
            .load(ratingData.ratingData.get(position).icon)
            //.apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .placeholder(R.drawable.ic_category)
            .into(serviceImage!!)
        btnSubmit?.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(GlobalConstants.COLOR_CODE))/*mContext.getResources().getColorStateList(R.color.colorOrange)*/)

        btnSubmit?.setOnClickListener {
            ratingData.ratingData.get(position).review = etReview!!.getText().toString().trim()
            ratingData.ratingData.get(position).rating = ratingBar!!.rating.toString()
            reviewsBinding.btnSubmit.visibility = View.INVISIBLE
            reviewsAdapter?.notifyDataSetChanged()
            confirmationDialog?.dismiss()
        }

        confirmationDialog?.show()
    }

}
