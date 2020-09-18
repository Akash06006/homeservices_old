package com.example.services.views.orders

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.services.R
import com.example.services.application.MyApplication
import com.example.services.common.UtilsFunctions
import com.example.services.common.UtilsFunctions.showToastSuccess
import com.example.services.constants.GlobalConstants
import com.example.services.utils.BaseActivity
import com.example.services.databinding.ActivityOrderListBinding
import com.example.services.model.CommonModel
import com.example.services.model.orders.OrdersListResponse
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.utils.BaseFragment
import com.example.services.utils.DialogClass
import com.example.services.utils.DialogssInterface
import com.example.services.viewmodels.orders.OrdersViewModel
import com.example.services.views.ratingreviews.AddRatingReviewsListActivity
import com.google.gson.JsonObject
import com.uniongoods.adapters.OrderListAdapter
import com.uniongoods.adapters.OrderListFragmentAdapter

class OrdersListFragment : BaseFragment(), DialogssInterface {
    lateinit var orderBinding: ActivityOrderListBinding
    lateinit var ordersViewModel: OrdersViewModel
    var orderList = ArrayList<OrdersListResponse.Body>()
    var orderListAdapter: OrderListFragmentAdapter? = null
    private var confirmationDialog: Dialog? = null
    private var mDialogClass = DialogClass()
    var cancelOrderObject = JsonObject()
    var completeOrderObject = JsonObject()
    var pos = 0
    var orderId = ""
    var addressType = ""
    private val SECOND_ACTIVITY_REQUEST_CODE = 0

    override fun getLayoutResId(): Int {
        return R.layout.activity_order_list
    }


    override fun initView() {
        orderBinding = viewDataBinding as ActivityOrderListBinding
        ordersViewModel = ViewModelProviders.of(this).get(OrdersViewModel::class.java)
        orderBinding.commonToolBar.imgRight.visibility = View.GONE
        orderBinding.commonToolBar.topToolbar.visibility = View.GONE

        // orderBinding.commonToolBar.visibility = View.GONE

        orderBinding.commonToolBar.imgRight.setImageResource(R.drawable.ic_cart)
        orderBinding.commonToolBar.imgToolbarText.text =
            resources.getString(R.string.orders)
        orderBinding.cartViewModel = ordersViewModel
        val userId = SharedPrefClass()!!.getPrefValue(
            MyApplication.instance,
            GlobalConstants.USERID
        ).toString()
        if (UtilsFunctions.isNetworkConnected()) {
            baseActivity.startProgressDialog()
        }
        addressType = SharedPrefClass().getPrefValue(
            MyApplication.instance,
            GlobalConstants.SelectedAddressType
        ).toString()

        ordersViewModel.getOrdersListRes().observe(this,
            Observer<OrdersListResponse> { response ->
                baseActivity.stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            orderList.clear()
                            orderList.addAll(response.data!!)
                            if (orderList.size > 0) {
                                orderBinding.rvOrder.visibility = View.VISIBLE
                                orderBinding.title.visibility = View.GONE
                                orderBinding.tvNoRecord.visibility = View.GONE
                                initRecyclerView()
                            } else {
                                orderBinding.rvOrder.visibility = View.GONE
                                orderBinding.title.visibility = View.VISIBLE
                                orderBinding.tvNoRecord.visibility = View.VISIBLE
                            }

                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                            orderBinding.rvOrder.visibility = View.GONE
                            orderBinding.tvNoRecord.visibility = View.VISIBLE
                            orderBinding.title.visibility = View.VISIBLE

                        }
                    }

                }
            })
        ordersViewModel.getCancelOrderRes().observe(this,
            Observer<CommonModel> { response ->
                baseActivity.stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            baseActivity.startProgressDialog()
                            orderList.clear()
                            message?.let { showToastSuccess(it) }
                            ordersViewModel.getOrderList()
                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                        }
                    }

                }
            })

        ordersViewModel.getCompleteOrderRes().observe(this,
            Observer<CommonModel> { response ->
                baseActivity.stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            baseActivity.startProgressDialog()
                            //orderList.clear()
                            // ordersViewModel.getOrderList()
                            callRatingReviewsActivity(orderId)
                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                        }
                    }

                }
            })

        ordersViewModel.isClick().observe(
            this, Observer<String>(function =
            fun(it: String?) {
                when (it) {
                    /* "tvPromo" -> {

                     }*/
                }
            })
        )
    }

    override fun onResume() {
        super.onResume()
        if (UtilsFunctions.isNetworkConnected()) {
            // startProgressDialog()
            orderList.clear()
            ordersViewModel.getOrderList()
        }
    }

/*
    override fun initViews() {
        orderBinding = viewDataBinding as ActivityOrderListBinding
        ordersViewModel = ViewModelProviders.of(this).get(OrdersViewModel::class.java)
        orderBinding.commonToolBar.imgRight.visibility = View.GONE
        orderBinding.commonToolBar.imgRight.setImageResource(R.drawable.ic_cart)
        orderBinding.commonToolBar.imgToolbarText.text =
            resources.getString(R.string.orders)
        orderBinding.cartViewModel = ordersViewModel
        val userId = SharedPrefClass()!!.getPrefValue(
            MyApplication.instance,
            GlobalConstants.USERID
        ).toString()
        if (UtilsFunctions.isNetworkConnected()) {
            startProgressDialog()
        }
        addressType = SharedPrefClass().getPrefValue(
            MyApplication.instance,
            GlobalConstants.SelectedAddressType
        ).toString()

        ordersViewModel.getOrdersListRes().observe(this,
            Observer<OrdersListResponse> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            orderList.clear()
                            orderList.addAll(response.data!!)
                            if (orderList.size > 0) {
                                orderBinding.rvOrder.visibility = View.VISIBLE
                                orderBinding.title.visibility = View.GONE
                                orderBinding.tvNoRecord.visibility = View.GONE
                                initRecyclerView()
                            } else {
                                orderBinding.rvOrder.visibility = View.GONE
                                orderBinding.title.visibility = View.VISIBLE
                                orderBinding.tvNoRecord.visibility = View.VISIBLE
                            }

                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                            orderBinding.rvOrder.visibility = View.GONE
                            orderBinding.tvNoRecord.visibility = View.VISIBLE
                            orderBinding.title.visibility = View.VISIBLE

                        }
                    }

                }
            })
        ordersViewModel.getCancelOrderRes().observe(this,
            Observer<CommonModel> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            startProgressDialog()
                            orderList.clear()
                            showToastSuccess(message)
                            ordersViewModel.getOrderList()
                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                        }
                    }

                }
            })

        ordersViewModel.getCompleteOrderRes().observe(this,
            Observer<CommonModel> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            startProgressDialog()
                            //orderList.clear()
                            // ordersViewModel.getOrderList()
                            callRatingReviewsActivity(orderId)
                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                        }
                    }

                }
            })

        ordersViewModel.isClick().observe(
            this, Observer<String>(function =
            fun(it: String?) {
                when (it) {
                    */
/* "tvPromo" -> {

                     }*//*

                }
            })
        )
    }
*/


    private fun initRecyclerView() {
        orderListAdapter = OrderListFragmentAdapter(baseActivity, orderList, this)
        val linearLayoutManager = LinearLayoutManager(activity!!)
        //  val gridLayoutManager = GridLayoutManager(this, 2)
        //cartBinding.rvSubcategories.layoutManager = gridLayoutManager
        //  cartBinding.rvSubcategories.setHasFixedSize(true)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        orderBinding.rvOrder.layoutManager = linearLayoutManager
        orderBinding.rvOrder.adapter = orderListAdapter
        orderBinding.rvOrder.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun callRatingReviewsActivity(orderID: String) {
        orderId = orderID
        confirmationDialog = mDialogClass.setDefaultDialog(
            activity!!,
            this,
            "Rating",
            getString(R.string.warning_rate_order)
        )
        confirmationDialog?.show()
    }

    override fun onDialogConfirmAction(mView: View?, mKey: String) {
        when (mKey) {
            "Cancel Order" -> {
                confirmationDialog?.dismiss()
                if (UtilsFunctions.isNetworkConnected()) {
                    ordersViewModel.cancelOrder(cancelOrderObject)
                }

            }
            "Rating" -> {
                confirmationDialog?.dismiss()
                if (UtilsFunctions.isNetworkConnected()) {
                    val intent =
                        Intent(activity, AddRatingReviewsListActivity::class.java)
                    intent.putExtra("orderId", orderId)
                    startActivity(intent)
                }

            }
            "Complete Order" -> {
                confirmationDialog?.dismiss()
                if (UtilsFunctions.isNetworkConnected()) {
                    ordersViewModel.completeOrder(completeOrderObject)
                }
            }

        }
    }

    override fun onDialogCancelAction(mView: View?, mKey: String) {
        when (mKey) {
            "Cancel Order" -> confirmationDialog?.dismiss()
            "Rating" -> {
                confirmationDialog?.dismiss()
                orderList.clear()
                ordersViewModel.getOrderList()
            }
            "Complete Order" -> confirmationDialog?.dismiss()

        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun cancelOrder(position: Int) {
        cancelOrderObject.addProperty(
            "orderId", orderList[position].id
        )
        cancelOrderObject.addProperty(
            "cancellationReason", "Others"
        )

        confirmationDialog = mDialogClass.setDefaultDialog(
            activity!!,
            this,
            "Cancel Order",
            getString(R.string.warning_cancel_order)
        )
        confirmationDialog?.show()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun completeOrder(position: Int) {
        orderId = orderList[position].id.toString()
        completeOrderObject.addProperty(
            "id", orderList[position].id
        )
        completeOrderObject.addProperty(
            "status", "5"
        )

        confirmationDialog = mDialogClass.setDefaultDialog(
            activity!!,
            this,
            "Complete Order",
            getString(R.string.warning_complete_order)
        )
        confirmationDialog?.show()

    }
}
