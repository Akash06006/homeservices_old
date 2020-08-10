package com.example.services.views.subcategories

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.services.R
import com.example.services.application.MyApplication
import com.example.services.common.UtilsFunctions
import com.example.services.constants.GlobalConstants
import com.example.services.utils.BaseActivity
import com.example.services.databinding.ActivitySubCategoriesBinding
import com.example.services.model.subcategories.SubCategoriesRespnse
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.viewmodels.subcategories.SubCategoriesViewModel
import com.example.services.views.cart.CartListActivity
import com.google.gson.JsonObject
import com.uniongoods.adapters.SubCategoriesListAdapter

//import com.example.services.model.subcategories.SubCategories

class SubCategoriesActivity : BaseActivity() {
    lateinit var categoriesBinding: ActivitySubCategoriesBinding
    lateinit var categoriesViewModel: SubCategoriesViewModel
    private var subCatsList = ArrayList<com.example.services.model.subcategories.Subcategories>()
    var catId = ""
    var isCart = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_sub_categories
    }
    override fun onResume() {
        super.onResume()
        isCart = SharedPrefClass().getPrefValue(
                MyApplication.instance,
                GlobalConstants.isCartAdded
        ).toString()
        if (isCart.equals("true")) {
            categoriesBinding.commonToolBar.imgRight.visibility = View.VISIBLE
        } else {
            categoriesBinding.commonToolBar.imgRight.visibility = View.GONE
        }
    }
    override fun initViews() {
        categoriesBinding = viewDataBinding as ActivitySubCategoriesBinding
        categoriesViewModel = ViewModelProviders.of(this).get(SubCategoriesViewModel::class.java)
        categoriesBinding.subCategoriesViewModel = categoriesViewModel
        categoriesBinding.commonToolBar.imgRight.visibility = View.GONE
        categoriesBinding.commonToolBar.imgRight.setImageResource(R.drawable.ic_cart)
        categoriesBinding.commonToolBar.imgToolbarText.text =
                resources.getString(R.string.sub_categories)
        catId = intent.extras?.get("catId").toString()
        // initRecyclerView()
        val mJsonObject = JsonObject()
        mJsonObject.addProperty(
                "category_id", catId
        )
        if (UtilsFunctions.isNetworkConnected()) {
            categoriesViewModel.getSubcategories(mJsonObject)
            startProgressDialog()
        }

        categoriesViewModel.getSubcategoriesRes().observe(this,
                Observer<SubCategoriesRespnse> { response ->
                    stopProgressDialog()
                    if (response != null) {
                        val message = response.message
                        when {
                            response.code == 200 -> {
                                subCatsList.addAll(response.body.subcategories)
                                categoriesBinding.title.visibility = View.GONE
                                categoriesBinding.rvSubcategories.visibility = View.VISIBLE
                                categoriesBinding.tvNoRecord.visibility = View.GONE
                                initRecyclerView()
                            }
                            else -> message?.let {
                                UtilsFunctions.showToastError(it)
                                categoriesBinding.title.visibility = View.VISIBLE
                                categoriesBinding.rvSubcategories.visibility = View.GONE
                                categoriesBinding.tvNoRecord.visibility = View.VISIBLE
                            }
                        }

                    }
                })

        categoriesViewModel.isClick().observe(
                this, Observer<String>(function =
        fun(it: String?) {
            when (it) {
                "img_right" -> {
                    val intent = Intent(this, CartListActivity::class.java)
                    startActivity(intent)
                }
            }
        })
        )

    }

    private fun initRecyclerView() {
        val myJobsListAdapter = SubCategoriesListAdapter(this, subCatsList, this)
        val linearLayoutManager = LinearLayoutManager(this)
        // linearLayoutManager.orientation = RecyclerView.VERTICAL
        //categoriesBinding.rvSubcategories.layoutManager = linearLayoutManager
        val gridLayoutManager = GridLayoutManager(this, 2)
        /* gridLayoutManager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {

             override fun getSpanSize(position: Int): Int {
                 return if (position == 16) { // totalRowCount : How many item you want to show
                     2 // the item in position now takes up 4 spans
                 } else 1
             }
         })*/

        categoriesBinding.rvSubcategories.layoutManager = gridLayoutManager
        categoriesBinding.rvSubcategories.setHasFixedSize(true)
        categoriesBinding.rvSubcategories.adapter = myJobsListAdapter
        categoriesBinding.rvSubcategories.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
    }

    fun callServicesActivity(position: Int) {

        val intent = Intent(this, ServicesListActivity::class.java)
        intent.putExtra("catId", catId)
        intent.putExtra("subCatId", subCatsList[position].id)
        startActivity(intent)
    }

}
