package com.example.services.views.promocode

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.services.databinding.ActivityPromoCodeBinding
import com.example.services.R
import com.example.services.application.MyApplication
import com.example.services.common.UtilsFunctions
import com.example.services.constants.GlobalConstants
import com.example.services.model.CommonModel
import com.example.services.model.cart.CartListResponse
import com.example.services.model.promocode.ApplyPromoCodeResponse
import com.example.services.model.promocode.PromoCodeListResponse
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.utils.BaseActivity
import com.example.services.viewmodels.favorite.FavoriteViewModel
import com.example.services.viewmodels.promocode.PromoCodeViewModel
import com.example.services.viewmodels.services.ServicesViewModel
import com.google.gson.JsonObject
import com.uniongoods.adapters.FavoriteListAdapter
import com.uniongoods.adapters.PromoCodeListAdapter

class PromoCodeActivity : BaseActivity() {
    lateinit var promoCodeBinding: ActivityPromoCodeBinding
    lateinit var promcodeViewModel: PromoCodeViewModel
    var promocodeListAdapter: PromoCodeListAdapter? = null
    var cartObject = JsonObject()
    var pos = 0
    var promoList = ArrayList<PromoCodeListResponse.Body>()
    override fun getLayoutId(): Int {
        return R.layout.activity_promo_code
    }

    override fun initViews() {

        promoCodeBinding = viewDataBinding as ActivityPromoCodeBinding
        promcodeViewModel = ViewModelProviders.of(this).get(PromoCodeViewModel::class.java)

        promoCodeBinding.commonToolBar.imgRight.visibility = View.GONE
        promoCodeBinding.commonToolBar.imgRight.setImageResource(R.drawable.ic_cart)
        promoCodeBinding.commonToolBar.imgToolbarText.text =
                resources.getString(R.string.promo_code)
        promoCodeBinding.promcodeViewModel = promcodeViewModel
        val userId = SharedPrefClass()!!.getPrefValue(
                MyApplication.instance,
                GlobalConstants.USERID
        ).toString()
        if (UtilsFunctions.isNetworkConnected()) {
            startProgressDialog()
            //cartViewModel.getcartList(userId)
        }
        //initRecyclerView()
        promoCodeBinding.btnApplyPromo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(GlobalConstants.COLOR_CODE))/*mContext.getResources().getColorStateList(R.color.colorOrange)*/)

        UtilsFunctions.hideKeyBoard(promoCodeBinding.tvNoRecord)
        promcodeViewModel.getPromoListRes().observe(this,
                Observer<PromoCodeListResponse> { response ->
                    stopProgressDialog()
                    if (response != null) {
                        val message = response.message
                        when {
                            response.code == 200 -> {
                                promoList.addAll(response.data!!)
                                promoCodeBinding.rvPromo.visibility = View.VISIBLE
                                promoCodeBinding.title.visibility = View.GONE
                                promoCodeBinding.tvNoRecord.visibility = View.GONE
                                initRecyclerView()
                            }
                            else -> message?.let {
                                UtilsFunctions.showToastError(it)
                                promoCodeBinding.rvPromo.visibility = View.GONE
                                promoCodeBinding.title.visibility = View.VISIBLE
                                promoCodeBinding.tvNoRecord.visibility = View.VISIBLE
                            }
                        }

                    }
                })

        promcodeViewModel.getApplyPromoRes().observe(this,
                Observer<ApplyPromoCodeResponse> { response ->
                    stopProgressDialog()
                    if (response != null) {
                        val message = response.message
                        when {
                            response.code == 200 -> {
                                showToastSuccess(message)
                                val mJsonObject = JsonObject()
                                mJsonObject.addProperty("discount", response.data?.coupanDiscount)
                                mJsonObject.addProperty("payableAmount", response.data?.payableAmount)
                                mJsonObject.addProperty("totalAmount", response.data?.totalAmount)
                                mJsonObject.addProperty("couponId", response.data?.coupanId)
                                mJsonObject.addProperty("code", response.data?.coupanCode)
                                val intent = Intent()
                                intent.putExtra("promoCodeData", mJsonObject.toString())
                                setResult(Activity.RESULT_OK, intent)
                                finish()
                            }
                            else -> message?.let {
                                UtilsFunctions.showToastError(it)

                            }
                        }

                    }
                })
        promcodeViewModel.isClick().observe(
                this, Observer<String>(function =
        fun(it: String?) {
            when (it) {
                "btnApplyPromo" -> {
                    if (TextUtils.isEmpty(promoCodeBinding.etCouponCode.getText().toString())) {
                        showToastError(getString(R.string.enter_promocode_msg))
                    } else {
                        callApplyCouponApi(promoCodeBinding.etCouponCode.getText().toString())
                    }
                }
            }
        })
        )

    }

    private fun initRecyclerView() {
        promocodeListAdapter = PromoCodeListAdapter(this, promoList, this)
        val linearLayoutManager = LinearLayoutManager(this)
        //val gridLayoutManager = GridLayoutManager(this, 2)
        //promoCodeBinding.rvFavorite.layoutManager = gridLayoutManager
        promoCodeBinding.rvPromo.setHasFixedSize(true)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        promoCodeBinding.rvPromo.layoutManager = linearLayoutManager
        promoCodeBinding.rvPromo.adapter = promocodeListAdapter
        promoCodeBinding.rvPromo.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
    }

    fun callApplyCouponApi(code: String) {
        if (UtilsFunctions.isNetworkConnected()) {
            startProgressDialog()
            promcodeViewModel.applyPromoCode(code)
        }

    }


}
