package com.example.services.views.cart

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.services.R
import com.example.services.application.MyApplication
import com.example.services.common.UtilsFunctions
import com.example.services.constants.GlobalConstants
import com.example.services.utils.BaseActivity
import com.example.services.viewmodels.cart.CartViewModel
import com.example.services.databinding.ActivityCheckoutAddressBinding
import com.example.services.model.CommonModel
import com.example.services.model.address.AddressListResponse
import com.example.services.model.cart.CartListResponse
import com.example.services.model.orders.CreateOrdersResponse
import com.example.services.model.services.DateSlotsResponse
import com.example.services.model.services.TimeSlotsResponse
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.utils.DialogClass
import com.example.services.utils.DialogssInterface
import com.example.services.utils.Utils
import com.example.services.viewmodels.address.AddressViewModel
import com.example.services.viewmodels.promocode.PromoCodeViewModel
import com.example.services.viewmodels.services.ServicesViewModel
import com.example.services.views.address.AddAddressActivity
import com.example.services.views.home.DashboardActivity
import com.example.services.views.home.LandingMainActivity
import com.example.services.views.payment.PaymentActivity
import com.example.services.views.promocode.PromoCodeActivity
import com.google.gson.JsonObject
import com.uniongoods.adapters.CartListAdapter
import com.uniongoods.adapters.CheckoutAddressListAdapter
import com.uniongoods.adapters.DateListAdapter
import com.uniongoods.adapters.TimeSlotsListAdapter
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class CheckoutAddressActivity : BaseActivity(), DialogssInterface {
    lateinit var cartBinding: ActivityCheckoutAddressBinding
    lateinit var cartViewModel: CartViewModel
    var cartList = ArrayList<CartListResponse.Data>()
    var addressListAdapter: CheckoutAddressListAdapter? = null
    private var confirmationDialog: Dialog? = null
    private var mDialogClass = DialogClass()
    var timeSlotsAdapter: TimeSlotsListAdapter? = null
    var dateSlotsAdapter: DateListAdapter? = null
    var slotsList = ArrayList<TimeSlotsResponse.Slots>()
    var dateList = ArrayList<DateSlotsResponse.Body>()
    var pos = 0
    var selectedDate = ""
    var date = ""
    var selectedTime = ""
    var quantityCount = 0
    var selectedAddressType = "1"
    var couponCodeId = ""
    var price = 0
    var payableAmount = ""
    var orderNo = ""
    var orderId = ""
    lateinit var addressViewModel: AddressViewModel
    private var addressesList = ArrayList<AddressListResponse.Body>()
    var addressId = ""
    // var addressType = ""
    lateinit var servicesViewModel: ServicesViewModel
    lateinit var promcodeViewModel: PromoCodeViewModel
    private val SECOND_ACTIVITY_REQUEST_CODE = 0
    override fun getLayoutId(): Int {
        return R.layout.activity_checkout_address
    }

    override fun onResume() {
        super.onResume()
        if (cartBinding.tvChange.getText().toString().equals(getString(R.string.add_address))) {
            addressViewModel.addressList()
        }
    }

    override fun initViews() {
        cartBinding = viewDataBinding as ActivityCheckoutAddressBinding
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        addressViewModel = ViewModelProviders.of(this).get(AddressViewModel::class.java)
        servicesViewModel = ViewModelProviders.of(this).get(ServicesViewModel::class.java)
        promcodeViewModel = ViewModelProviders.of(this).get(PromoCodeViewModel::class.java)
        cartBinding.commonToolBar.imgRight.visibility = View.GONE
        cartBinding.commonToolBar.imgRight.setImageResource(R.drawable.ic_cart)
        cartBinding.commonToolBar.imgToolbarText.text =
            resources.getString(R.string.checkout)
        cartBinding.cartViewModel = cartViewModel
        val userId = SharedPrefClass()!!.getPrefValue(
            MyApplication.instance,
            GlobalConstants.USERID
        ).toString()
        if (UtilsFunctions.isNetworkConnected()) {
            startProgressDialog()
            //cartViewModel.getcartList(userId)
        }
        cartBinding.tvChange.setBackgroundTintList(
            ColorStateList.valueOf(
                Color.parseColor(
                    GlobalConstants.COLOR_CODE
                )
            )/*mContext.getResources().getColorStateList(R.color.colorOrange)*/
        )
        cartBinding.btnCheckout.setBackgroundTintList(
            ColorStateList.valueOf(
                Color.parseColor(
                    GlobalConstants.COLOR_CODE
                )
            )/*mContext.getResources().getColorStateList(R.color.colorOrange)*/
        )

        for (i in 0..4) {
            val item = DateSlotsResponse()
            dateList.add(item.Body())
            dateList[i].date = Utils(this).getDateLocal(
                "EEE MMM dd HH:mm:ss zzzz yyyy",
                getDaysAgo(i).toString(),
                "MM-dd-YYYY"
            )
            //dateList[i].date = getDaysAgo(i).toString()
            dateList[i].selected = "false"
        }

        cartBinding.rvDate.visibility = View.VISIBLE
        cartBinding.tvDateRecord.visibility = View.GONE
        // cartBinding.btnSubmit.visibility = View.VISIBLE
        initDateRecyclerView()
        /*EEE MMM dd HH:mm:ss zzzz yyyy*/
        cartViewModel.getCartListRes().observe(this,
            Observer<CartListResponse> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            cartList.addAll(response.body!!.data!!)
                            payableAmount = response.body?.sum.toString()
                            cartBinding.tvTotalItems.setText(cartList.size.toString())
                            cartBinding.tvOfferPrice.setText(GlobalConstants.Currency + " " + response.body?.sum)
                            cartBinding.tvPromo.setText(getString(R.string.apply_coupon))
                            cartBinding.rlRealPrice.visibility = View.GONE
                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)

                        }
                    }
                }
            })


        servicesViewModel.getTimeSlotsRes().observe(this,
            Observer<TimeSlotsResponse> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            //if (quantityCount != 0) {
                            selectedTime = ""
                            slotsList.clear()
                            slotsList.addAll(response.data!!.slots!!)
                            cartBinding.rvSlots.visibility = View.VISIBLE
                            cartBinding.tvNoRecord.visibility = View.GONE
                            cartBinding.tvTimeSlots.visibility = View.VISIBLE
                            cartBinding.title.visibility = View.GONE
                            initRecyclerView()
                            //}

                        }
                        else -> {
                            message?.let {
                                UtilsFunctions.showToastError(it)
                                cartBinding.tvNoRecord.setText(message)
                            }
                            selectedTime = ""
                            // cartBinding.btnSubmit.visibility = View.GONE
                            cartBinding.rvSlots.visibility = View.GONE
                            cartBinding.tvTimeSlots.visibility = View.GONE
                            cartBinding.tvNoRecord.visibility = View.VISIBLE
                            cartBinding.title.visibility = View.VISIBLE
                        }
                    }

                }
            })

        cartViewModel.getUpdatePayemntStatusRes().observe(this,
            Observer<CommonModel> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            SharedPrefClass().putObject(
                                this,
                                GlobalConstants.SelectedAddressType,
                                "null"
                            )
                            //showToastSuccess(message)
                            showPaymentSuccessDialog()
                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                        }
                    }
                }
            })
        cartViewModel.getOrderPlaceRes().observe(this,
            Observer<CreateOrdersResponse> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            SharedPrefClass().putObject(
                                this,
                                GlobalConstants.SelectedAddressType,
                                "null"
                            )
                            //showToastSuccess(message)
                            // showPaymentSuccessDialog()
                            orderId = response.data?.id!!
                            orderNo = response.data?.orderNo!!
                            val intent = Intent(this, PaymentActivity::class.java)
                            intent.putExtra("amount", payableAmount)
                            intent.putExtra("currency", GlobalConstants.Currency)
                            intent.putExtra("totalItems", cartList.size.toString())
                            startActivityForResult(intent, 200)

                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                        }
                    }
                }
            })

        addressViewModel.getAddressList().observe(this,
            Observer<AddressListResponse> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            addressesList.addAll(response.data!!)
                            var default = "false"
                            cartBinding.tvChange.setText(getString(R.string.change))
                            for (item in addressesList) {
                                if (item.default.equals("1")) {
                                    default = "true"
                                    addressId = item.id.toString()
                                    cartBinding.tvAddress.text = item.addressType
                                    cartBinding.tvAddressDetail.text = item.addressName

                                    if (item.addressType.equals(getString(R.string.home))) {
                                        cartBinding.addresssImg.setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.ic_home
                                            )
                                        )
                                    } else if (item.addressType.equals(getString(R.string.work))) {
                                        cartBinding.addresssImg.setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.ic_work
                                            )
                                        )
                                    } else {
                                        cartBinding.addresssImg.setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.ic_other
                                            )
                                        )
                                    }
                                }
                            }
                            if (default.equals("false")) {
                                if (addressesList.size > 0) {
                                    setAddressData(0)
                                }
                            }
                            cartBinding.addressItem.visibility = View.VISIBLE
                        }
                        else -> {
                            /*message?.let {
                                UtilsFunctions.showToastError(it)
                            }*/
                            cartBinding.tvChange.setText(getString(R.string.add_address))
                            cartBinding.addressItem.visibility = View.GONE
                        }
                    }

                }
            })

        promcodeViewModel.getRemovePromoRes().observe(this,
            Observer<CommonModel> { response ->
                stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            couponCodeId = ""
                            cartList.clear()
                            cartViewModel.getCartList()
                        }
                        else -> message?.let {
                            UtilsFunctions.showToastError(it)
                        }
                    }

                }
            })

        cartViewModel.isClick().observe(
            this, Observer<String>(function =
            fun(it: String?) {
                when (it) {
                    "tvPromo" -> {
                        if (cartBinding.tvPromo.getText().toString().equals(getString(R.string.apply_coupon))) {
                            val intent = Intent(this, PromoCodeActivity::class.java)
                            startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
                        } else {
                            confirmationDialog = mDialogClass.setDefaultDialog(
                                this,
                                this,
                                "Remove Coupon",
                                getString(R.string.warning_remove_coupon)
                            )
                            confirmationDialog?.show()

                        }
                    }
                    "tvChange" -> {
                        if (cartBinding.tvChange.getText().toString().equals(getString(R.string.add_address))) {
                            val intent = Intent(this, AddAddressActivity::class.java)
                            startActivity(intent)
                        } else {
                            addressDialog()
                        }

                    }
                    "btnCheckout" -> {

                        if (TextUtils.isEmpty(selectedDate)) {
                            showToastError(resources.getString(R.string.select_date_msg))
                        } else if (TextUtils.isEmpty(selectedTime)) {
                            showToastError(resources.getString(R.string.select_time_msg))
                        } else if (TextUtils.isEmpty(addressId)) {
                            showToastError("Please Select Address")
                        } else {


                            val addressObject = JsonObject()
                            addressObject.addProperty(
                                "addressId", addressId
                            )
                            addressObject.addProperty(
                                "serviceDateTime", selectedDate + " " + selectedTime
                            )
                            addressObject.addProperty(
                                "orderPrice", payableAmount
                            )
                            addressObject.addProperty(
                                "serviceCharges", "0"
                            )
                            addressObject.addProperty(
                                "promoCode", couponCodeId
                            )
                            cartViewModel.orderPlace(addressObject)


                        }
/*{

 "addressId" :"d936a494-61ba-4ae4-9f29-83af67826c8b",
 "serviceDateTime":"2020-03-30 14:00:00",
 "orderPrice":"600",
 "serviceCharges":"",
 "promoCode" :"PRO1234567"
}*/
                    }
                }
            })
        )
    }


    override fun onDialogConfirmAction(mView: View?, mKey: String) {
        when (mKey) {
            "Remove Cart" -> {
                confirmationDialog?.dismiss()
                if (UtilsFunctions.isNetworkConnected()) {

                }

            }
            "Remove Coupon" -> {
                confirmationDialog?.dismiss()
                if (UtilsFunctions.isNetworkConnected()) {
                    startProgressDialog()
                    promcodeViewModel.removePromoCode(couponCodeId)
                }

            }

        }
    }

    fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, daysAgo)

        return calendar.time
    }


    override fun onDialogCancelAction(mView: View?, mKey: String) {
        when (mKey) {
            "Remove Cart" -> confirmationDialog?.dismiss()
            "Remove Coupon" -> confirmationDialog?.dismiss()
        }
    }

    fun addressDialog() {
        confirmationDialog = Dialog(this, R.style.transparent_dialog_borderless)
        confirmationDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(this),
                R.layout.address_list_dialog,
                null,
                false
            )

        confirmationDialog?.setContentView(binding.root)
        confirmationDialog?.setCancelable(false)

        confirmationDialog?.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        confirmationDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val rvAddress = confirmationDialog?.findViewById<RecyclerView>(R.id.rvAddRess)
        val cancel = confirmationDialog?.findViewById<Button>(R.id.cancel)

        addressListAdapter = CheckoutAddressListAdapter(this, addressesList, this)
        val linearLayoutManager = LinearLayoutManager(this)
        //  val gridLayoutManager = GridLayoutManager(this, 2)
        //cartBinding.rvSubcategories.layoutManager = gridLayoutManager
        //  cartBinding.rvSubcategories.setHasFixedSize(true)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rvAddress?.layoutManager = linearLayoutManager
        rvAddress?.adapter = addressListAdapter
        rvAddress?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })

        cancel?.setOnClickListener {
            confirmationDialog?.dismiss()
        }
        confirmationDialog?.show()
    }

    fun selectAddress(position: Int) {
        confirmationDialog?.dismiss()
        setAddressData(position)
    }

    private fun setAddressData(pos: Int) {
        addressId = addressesList[pos].id.toString()
        cartBinding.tvAddress.text = addressesList[pos].addressType
        cartBinding.tvAddressDetail.text = addressesList[pos].addressName

        if (addressesList[pos].addressType.equals(getString(R.string.home))) {
            cartBinding.addresssImg.setImageDrawable(resources.getDrawable(R.drawable.ic_home))
        } else if (addressesList[pos].addressType.equals(getString(R.string.work))) {
            cartBinding.addresssImg.setImageDrawable(resources.getDrawable(R.drawable.ic_work))
        } else {
            cartBinding.addresssImg.setImageDrawable(resources.getDrawable(R.drawable.ic_other))
        }
    }

    private fun initRecyclerView() {
        timeSlotsAdapter = TimeSlotsListAdapter(this, slotsList, this)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        cartBinding.rvSlots.layoutManager = linearLayoutManager
        cartBinding.rvSlots.adapter = timeSlotsAdapter
        cartBinding.rvSlots.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
    }

    private fun initDateRecyclerView() {
        // cartBinding.rvDate.visibility=View.GONE
        dateSlotsAdapter = DateListAdapter(this, dateList, this)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        cartBinding.rvDate.layoutManager = linearLayoutManager
        cartBinding.rvDate.adapter = dateSlotsAdapter
        cartBinding.rvDate.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
    }

    fun selectTimeSlot(position: Int) {
        var i = 0
        for (item in slotsList) {

            slotsList[i].selected = "false"
            i++
        }
        slotsList[position].selected = "true"
        //selectedTime = slotsList[position].timing!!
        selectedTime = slotsList[position].slot!!
        timeSlotsAdapter?.notifyDataSetChanged()

    }

    fun selectDatelot(position: Int) {

        var i = 0
        for (item in dateList) {

            dateList[i].selected = "false"
            i++
        }
        dateList[position].selected = "true"
        dateSlotsAdapter?.notifyDataSetChanged()
        selectedDate = dateList[position].date!!
        callGetTimeSlotsApi()
    }

    private fun callGetTimeSlotsApi() {
        /*price = quantityCount * priceAmount.toInt()
        serviceDetailBinding.tvTotalPrice.setText(currency + price.toString())*/
        if (!TextUtils.isEmpty(selectedDate)) {
            var cartObject = JsonObject()
            /*cartObject.addProperty(
                    "serviceId", cartList[0].id
            )*/
            cartObject.addProperty(
                "date", selectedDate
            )
            servicesViewModel.getTimeSlot(selectedDate)
        }
    }

    private fun showPaymentSuccessDialog() {
        confirmationDialog = Dialog(this, R.style.transparent_dialog)
        confirmationDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(this),
                R.layout.order_place_success_dialog,
                null,
                false
            )

        confirmationDialog?.setContentView(binding.root)
        confirmationDialog?.setCancelable(false)

        confirmationDialog?.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        confirmationDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val cancel = confirmationDialog?.findViewById<Button>(R.id.btnDone)
        cancel?.setOnClickListener {
            confirmationDialog?.dismiss()
            SharedPrefClass().putObject(
                this,
                GlobalConstants.isCartAdded,
                "false"
            )
            val intent = Intent(this, LandingMainActivity::class.java)
            intent.putExtra("from", "splash")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        confirmationDialog?.show()
    }

    // This method is called when the second activity finishes
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check that it is the SecondActivity with an OK result
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
                //if (resultCode == Activity.RESULT_OK) {
                // cartList.clear()
                // cartViewModel.getCartList()

                val response = data?.getStringExtra("promoCodeData")
                val obj = JSONObject(response)
                val dis = obj.getString("discount")
                payableAmount = obj.getString("payableAmount")
                val totalAmount = obj.getString("totalAmount")
                couponCodeId = obj.getString("code")
                // couponCode = response.coupanDetails?.coupanCode.toString()
                cartBinding.tvPromo.setText(dis + "% " + "Discount coupon applied. Remove?")
                cartBinding.rlRealPrice.visibility = View.VISIBLE
                cartBinding.tvOfferPrice.setText(GlobalConstants.Currency + " " + payableAmount)
                cartBinding.tvRealPrice.setText(GlobalConstants.Currency + " " + totalAmount)

                val str = cartBinding.tvPromo.getText().toString()
                var span = str.split(".")
                val rr = span[1]
                val styledString = SpannableStringBuilder(cartBinding.tvPromo.getText().toString())
                var length = cartBinding.tvPromo.getText().toString().length
                val startPos = length - 7
                styledString.setSpan(StyleSpan(Typeface.BOLD), startPos, styledString.length, 0)
                styledString.setSpan(
                    ForegroundColorSpan(Color.RED),
                    startPos,
                    styledString.length,
                    0
                )
                cartBinding.tvPromo.setText(/*span[0] + ". " +*/ styledString)


                //}
            } else if (requestCode == 200) {
                val message = data?.getStringExtra("status")
                if (message.equals("success")) {
                    // showToastSuccess("Hit Api")/*
                    // {
                    // "transactionId" :"TRANS12345",
                    // "paymentMode":"Net Banking",
                    // "status":"2",
                    // "orderId" : "aed5935d-c603-4f1d-9235-8affccb953a2",
                    // "amount" :399
                    //}*/
                    val transactionId = data?.getStringExtra("id")
                    val addressObject = JsonObject()
                    addressObject.addProperty(
                        "transactionId", transactionId
                    )
                    addressObject.addProperty(
                        "paymentMode", "Net Banking"
                    )
                    addressObject.addProperty(
                        "status", "1"
                    )
                    addressObject.addProperty(
                        "orderId", orderId
                    )
                    addressObject.addProperty(
                        "amount", payableAmount
                    )
                    Log.d("addressObject", addressObject.toString())
                    cartViewModel.updatePaymentStatus(addressObject)
                    //   showPaymentSuccessDialog()
                } else if (message.equals("Payment Cancelled")) {
                    showToastError("Payment Cancelled")
                } else {
                    showToastError("Something went wrong.")
                }
            }
        }
    }
}


