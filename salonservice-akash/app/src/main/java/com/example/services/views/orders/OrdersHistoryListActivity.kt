package com.example.services.views.orders

import android.app.Dialog
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.services.R
import com.example.services.application.MyApplication
import com.example.services.common.UtilsFunctions
import com.example.services.constants.GlobalConstants
import com.example.services.utils.BaseActivity
import com.example.services.databinding.ActivityOrderListBinding
import com.example.services.model.CommonModel
import com.example.services.model.orders.OrdersListResponse
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.utils.DialogClass
import com.example.services.utils.DialogssInterface
import com.example.services.viewmodels.orders.OrdersViewModel
import com.google.gson.JsonObject
import com.uniongoods.adapters.OrderListAdapter

class OrdersHistoryListActivity : BaseActivity() {
    lateinit var orderBinding: ActivityOrderListBinding
    lateinit var ordersViewModel: OrdersViewModel
    var orderList = ArrayList<OrdersListResponse.Body>()
    var orderListAdapter: OrderListAdapter? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_order_list
    }

    override fun initViews() {
        orderBinding = viewDataBinding as ActivityOrderListBinding
        ordersViewModel = ViewModelProviders.of(this).get(OrdersViewModel::class.java)
        orderBinding.commonToolBar.imgRight.visibility = View.GONE
        orderBinding.commonToolBar.imgRight.setImageResource(R.drawable.ic_cart)
        orderBinding.commonToolBar.imgToolbarText.text =
            resources.getString(R.string.orders_history)
        orderBinding.cartViewModel = ordersViewModel
        if (UtilsFunctions.isNetworkConnected()) {
            startProgressDialog()
            orderList.clear()
            // ordersViewModel.getOrderList()
        }
        ordersViewModel.getOrdersHistoryListRes().observe(this,
            Observer<OrdersListResponse> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            orderList.addAll(response.data!!)
                            if (orderList.size > 0) {
                                orderBinding.title.visibility = View.GONE
                                orderBinding.rvOrder.visibility = View.VISIBLE
                                orderBinding.tvNoRecord.visibility = View.GONE
                                initRecyclerView()
                            } else {
                                UtilsFunctions.showToastError(getString(R.string.no_record_found))
                                orderBinding.rvOrder.visibility = View.GONE
                                orderBinding.title.visibility = View.VISIBLE
                                orderBinding.tvNoRecord.visibility = View.VISIBLE
                            }

                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                            orderBinding.title.visibility = View.VISIBLE
                            orderBinding.rvOrder.visibility = View.GONE
                            orderBinding.tvNoRecord.visibility = View.VISIBLE

                        }
                    }

                }
            })

    }

    private fun initRecyclerView() {
        orderListAdapter = OrderListAdapter(this, orderList, null)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        orderBinding.rvOrder.layoutManager = linearLayoutManager
        orderBinding.rvOrder.adapter = orderListAdapter
        orderBinding.rvOrder.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
    }
}
