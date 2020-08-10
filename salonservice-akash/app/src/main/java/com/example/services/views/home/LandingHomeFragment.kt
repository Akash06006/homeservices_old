package com.example.services.views.home

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.services.R
import com.example.services.common.UtilsFunctions
import com.example.services.common.UtilsFunctions.showToastError
import com.example.services.constants.GlobalConstants
import com.example.services.databinding.FragmentHomeLandingBinding
import com.example.services.model.CommonModel
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.utils.BaseFragment
import com.example.services.utils.DialogClass
import com.example.services.utils.DialogssInterface
import com.example.services.viewmodels.home.CategoriesListResponse
import com.example.services.viewmodels.home.HomeViewModel
import com.example.services.viewmodels.home.Services
import com.example.services.views.vendor.VendorsListActivity
import com.github.nkzawa.global.Global
import com.google.gson.JsonObject
import com.uniongoods.adapters.*

class
LandingHomeFragment : BaseFragment(), DialogssInterface {
    private var categoriesList = ArrayList<Services>()
    lateinit var homeViewModel: HomeViewModel
    private lateinit var fragmentHomeBinding: FragmentHomeLandingBinding
    private var offersList =
        ArrayList<com.example.services.viewmodels.home.Banners>()
    private var confirmationDialog: Dialog? = null
    private var mDialogClass = DialogClass()
    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_landing
    }

    override fun onResume() {
        super.onResume()
        if (UtilsFunctions.isNetworkConnected()) {
            // baseActivity.startProgressDialog()
            homeViewModel.getCategories()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initView() {
        var cartCategoryTypeId = ""
        fragmentHomeBinding = viewDataBinding as FragmentHomeLandingBinding
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        fragmentHomeBinding.homeViewModel = homeViewModel
        categoriesList?.clear()
        val mJsonObject = JsonObject()
        mJsonObject.addProperty(
            "acceptStatus", "1"
        )
        if (UtilsFunctions.isNetworkConnected()) {
            baseActivity.startProgressDialog()
        }
        homeViewModel.getJobs().observe(this,
            Observer<CategoriesListResponse> { response ->
                baseActivity.stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            GlobalConstants.Currency = response.body.currency
                            GlobalConstants.CART_CATEGORY_TYPE = response.body.cartCategoryType
                            cartCategoryTypeId = response.body.cartCategoryType
                            GlobalConstants.ABOUT_US = response.body.aboutUsLink
                            GlobalConstants.TERMS_CONDITION = response.body.termsLink
                            GlobalConstants.COMPANY_ID = response.body.cartCategoryCompany
                            GlobalConstants.PRIVACY_POLICY = response.body.privacyLink
                            if (TextUtils.isEmpty(cartCategoryTypeId)) {
                                SharedPrefClass().putObject(
                                    activity!!,
                                    GlobalConstants.isCartAdded,
                                    "false"
                                )
                                (activity as LandingMainActivity).onResumedForFragment()
                            } else {
                                SharedPrefClass().putObject(
                                    activity!!,
                                    GlobalConstants.isCartAdded,
                                    "true"
                                )
                                (activity as LandingMainActivity).onResumedForFragment()
                            }
                            categoriesList.clear()
                            offersList.clear()
                            categoriesList?.addAll(response.body.services)
                            fragmentHomeBinding.gvServices.visibility = View.VISIBLE
                            initRecyclerView()
                            offersList.addAll(response.body.banners)
                            if (offersList.size > 0) {
                                offerListViewPager()
                                fragmentHomeBinding.offersViewpager.visibility = View.VISIBLE
                            } else {
                                fragmentHomeBinding.offersViewpager.visibility = View.GONE
                            }
                        }
                        else -> message?.let {
                            showToastError(it)
                            fragmentHomeBinding.gvServices.visibility = View.GONE
                        }
                    }
                }
            })

        homeViewModel.getClearCartRes().observe(this,
            Observer<CommonModel> { response ->
                baseActivity.stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            cartCategoryTypeId = ""

                            SharedPrefClass().putObject(
                                activity!!,
                                GlobalConstants.isCartAdded,
                                "false"
                            )
                            (activity as LandingMainActivity).onResumedForFragment()


                        }
                        else -> message?.let {
                            showToastError(it)
                            fragmentHomeBinding.gvServices.visibility = View.GONE
                        }
                    }
                }
            })
        fragmentHomeBinding.gvServices.onItemClickListener =
            AdapterView.OnItemClickListener { parent, v, position, id ->

                if (!TextUtils.isEmpty(cartCategoryTypeId) && !cartCategoryTypeId.contains(
                        categoriesList[position].id
                    )
                ) {
                    // showToastError("Clear Cart message")
                    showClearCartDialog()
                } else {
                    GlobalConstants.COLOR_CODE =
                        "#" + categoriesList[position].colorCode.toString().trim()
                    val intent = Intent(activity!!, VendorsListActivity::class.java)
                    intent.putExtra("catId", categoriesList[position].id)
                    intent.putExtra("name", categoriesList[position].name)
                    GlobalConstants.CATEGORY_SELECTED = categoriesList[position].id
                    GlobalConstants.CATEGORY_SELECTED_NAME = categoriesList[position].name
                    startActivity(intent)
                }
                // if (categoriesList[position].isService.equals("false")) {

                // }
            }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun showClearCartDialog() {
        confirmationDialog = mDialogClass.setDefaultDialog(
            activity!!,
            this,
            "Clear Cart",
            getString(R.string.warning_clear_cart)
        )
        confirmationDialog?.show()
    }

    private fun initRecyclerView() {
        val adapter = LandingHomeCategoriesGridListAdapter(
            this@LandingHomeFragment,
            categoriesList,
            activity!!
        )
        fragmentHomeBinding.gvServices.adapter = adapter
    }

    private fun offerListViewPager() {
        val adapter = LandingHomeOffersListAdapter(this@LandingHomeFragment, offersList, activity!!)
        fragmentHomeBinding.offersViewpager.adapter = adapter

    }

    override fun onDialogConfirmAction(mView: View?, mKey: String) {
        when (mKey) {
            "Clear Cart" -> {
                confirmationDialog?.dismiss()
                if (UtilsFunctions.isNetworkConnected()) {
                    /* servicesViewModel.removeCart(pos)
                     startProgressDialog()*/
                    homeViewModel.clearCart("clear")
                }

            }

        }
    }

    override fun onDialogCancelAction(mView: View?, mKey: String) {
        when (mKey) {
            "Clear Cart" -> confirmationDialog?.dismiss()
        }
    }

}