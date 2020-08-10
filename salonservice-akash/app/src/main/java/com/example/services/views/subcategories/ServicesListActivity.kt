package com.example.services.views.subcategories

import android.content.Intent
import android.text.TextUtils
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
import com.example.services.databinding.ActivityServicesBinding
import com.example.services.model.CommonModel
import com.example.services.model.services.Headers
import com.example.services.model.services.Services
import com.example.services.model.services.ServicesListResponse
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.viewmodels.services.ServicesViewModel
import com.example.services.views.cart.CartListActivity
import com.google.gson.JsonObject
import com.uniongoods.adapters.ServicesListAdapter
import com.uniongoods.adapters.SubCategoriesFilterListAdapter

class ServicesListActivity : BaseActivity() {
    lateinit var servicesBinding: ActivityServicesBinding
    lateinit var servicesViewModel: ServicesViewModel
    private var serVicesList = ArrayList<Services>()
    private var subCategoryList = ArrayList<Headers>()
    var selectedPos = 0
    var catId = ""
    var subCatId = ""
    var pos = 0
    var servicesListAdapter: ServicesListAdapter? = null
    var subcatFilterAdapter: SubCategoriesFilterListAdapter? = null
    var isCart = ""
    val serviceObject = JsonObject()
    override fun getLayoutId(): Int {
        return R.layout.activity_services
    }

    override fun onResume() {
        super.onResume()
        isCart = SharedPrefClass().getPrefValue(
                MyApplication.instance,
                GlobalConstants.isCartAdded
        ).toString()
        if (isCart.equals("true")) {
            servicesBinding.commonToolBar.imgRight.visibility = View.VISIBLE
        } else {
            servicesBinding.commonToolBar.imgRight.visibility = View.GONE
        }
    }

    override fun initViews() {
        // setTheme(R.style.ThemeSalon)
        servicesBinding = viewDataBinding as ActivityServicesBinding
        servicesViewModel = ViewModelProviders.of(this).get(ServicesViewModel::class.java)

        servicesBinding.commonToolBar.imgRight.setImageResource(R.drawable.ic_cart)
        servicesBinding.commonToolBar.imgToolbarText.text =
                resources.getString(R.string.services)
        servicesBinding.servicesViewModel = servicesViewModel
        catId = intent.extras?.get("catId").toString()
        subCatId = intent.extras?.get("subCatId").toString()
        //initRecyclerView()
        //subcat.name="All"
        serviceObject.addProperty(
                "category", catId
        )
        serviceObject.addProperty(
                "subcategory", "0"
        )
        if (UtilsFunctions.isNetworkConnected()) {
            servicesViewModel.getServices(catId)
            startProgressDialog()
        }

        val subcat = Headers("0", "", catId, "All", "All", "true")
        subCategoryList.add(subcat)
        //  initSubCatRecyclerView()
        servicesViewModel.serviceListRes().observe(this,
                Observer<ServicesListResponse> { response ->
                    stopProgressDialog()
                    if (response != null) {
                        val message = response.message
                        when {
                            response.code == 200 -> {
                                serVicesList.clear()
                                var isCheck = "false"
                                if (subCategoryList.size == 1) {
                                    subCategoryList.addAll(response.body.headers)
                                    for (item in subCategoryList) {
                                        item.isSelected = "false"
                                    }
                                    subCategoryList[0].isSelected = "true"
                                }
                                if (subCategoryList.size == 1) {
                                    servicesBinding.rvSubcategories.visibility = View.GONE
                                } else {
                                    servicesBinding.rvSubcategories.visibility = View.VISIBLE
                                    initSubCatRecyclerView()
                                }
                                /* for (item in subCategoryList) {
                                     if (item.subCategorySelect == "true") {
                                         isCheck = "true"
                                     }
                                 }*/
                                /*if (isCheck == "false") {
                                    subCategoryList[0].subCategorySelect = "true"
                                }*/
                                serVicesList.addAll(response.body.services)
                                if (response.body.services.size > 0) {
                                    servicesBinding.title.visibility = View.GONE
                                    servicesBinding.rvServices.visibility = View.VISIBLE
                                    servicesBinding.tvNoRecord.visibility = View.GONE
                                } else {
                                    servicesBinding.title.visibility = View.VISIBLE
                                    servicesBinding.rvServices.visibility = View.GONE
                                    servicesBinding.tvNoRecord.visibility = View.VISIBLE
                                }
                                initRecyclerView()


                            }
                            else -> message?.let {
                                UtilsFunctions.showToastError(it)
                                servicesBinding.title.visibility = View.VISIBLE
                                servicesBinding.rvServices.visibility = View.GONE
                                servicesBinding.tvNoRecord.visibility = View.VISIBLE
                            }
                        }

                    }
                })

        servicesViewModel.addRemoveCartRes().observe(this,
                Observer<CommonModel> { response ->
                    stopProgressDialog()
                    if (response != null) {
                        val message = response.message
                        when {
                            response.code == 200 -> {
                                servicesViewModel.getServices(catId)
                            }
                            else -> message?.let {
                                UtilsFunctions.showToastError(it)
                            }
                        }

                    }
                })

        servicesViewModel.addRemovefavRes().observe(this,
                Observer<CommonModel> { response ->
                    stopProgressDialog()
                    if (response != null) {
                        val message = response.message
                        when {
                            response.code == 200 -> {
                                /*if (serVicesList[pos].favourite.equals("false")) {
                                    serVicesList[pos].favourite = "true"
                                } else {
                                    serVicesList[pos].favourite = "false"
                                }
                                servicesListAdapter?.notifyDataSetChanged()*/
                                servicesViewModel.getServices(catId)
                            }
                            else -> message?.let {
                                UtilsFunctions.showToastError(it)
                            }
                        }

                    }
                })


        servicesViewModel.isClick().observe(
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
        servicesListAdapter = ServicesListAdapter(this, serVicesList, this)
        val gridLayoutManager = GridLayoutManager(this, 1)
        servicesBinding.rvServices.layoutManager = gridLayoutManager
        servicesBinding.rvServices.setHasFixedSize(true)
        servicesBinding.rvServices.adapter = servicesListAdapter
        servicesBinding.rvServices.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
    }

    private fun initSubCatRecyclerView() {
        subcatFilterAdapter = SubCategoriesFilterListAdapter(this, subCategoryList, this)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        servicesBinding.rvSubcategories.layoutManager = linearLayoutManager
        servicesBinding.rvSubcategories.adapter = subcatFilterAdapter
        servicesBinding.rvSubcategories.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
        servicesBinding.rvSubcategories.smoothScrollToPosition(selectedPos)

    }


    fun addRemoveToCart(position: Int, addRemove: String) {
        var isCart = "false"
        var cartObject = JsonObject()
        cartObject.addProperty(
                "serviceId", serVicesList[position].id
        )

        if (addRemove.equals(getString(R.string.add))) {
            isCart = "true"
        } else {
            isCart = "false"
        }
        cartObject.addProperty(
                "status", isCart
        )
        if (UtilsFunctions.isNetworkConnected()) {
            servicesViewModel.addCart(cartObject)
            startProgressDialog()
        }
    }

    fun addRemovefav(position: Int, favId: String) {
        pos = position
        var favObject = JsonObject()
        if (TextUtils.isEmpty(favId)) {
            favObject.addProperty(
                    "serviceId", serVicesList[position].id
            )
            if (UtilsFunctions.isNetworkConnected()) {
                servicesViewModel.addFav(favObject)
                startProgressDialog()
            }
        } else {
            if (UtilsFunctions.isNetworkConnected()) {
                servicesViewModel.removeFav(favId)
                startProgressDialog()
            }
        }
    }

    fun selectSubCat(id: String, position: Int) {
        selectedPos == position

        catId = id
        for (item in subCategoryList) {
            item.isSelected = "false"
        }
        subCategoryList[position].isSelected = "true"

        serviceObject.addProperty(
                "category", catId
        )
        serviceObject.addProperty(
                "subcategory", id
        )

        subcatFilterAdapter?.notifyDataSetChanged()
        if (UtilsFunctions.isNetworkConnected()) {
            servicesViewModel.getServices(id)
            startProgressDialog()
        }

    }

    fun callServiceDetail(serviceId: String) {
        val intent = Intent(this, ServiceDetailActivity::class.java)
        intent.putExtra("serviceId", serviceId)
        startActivity(intent)
    }

}
