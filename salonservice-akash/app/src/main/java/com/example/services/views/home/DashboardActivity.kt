package com.example.services.views.home

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.services.R
import com.example.services.application.MyApplication
import com.example.services.constants.GlobalConstants
import com.example.services.databinding.ActivityDashboardBinding
import com.example.services.model.CommonModel
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.socket.TrackingActivity
import com.example.services.utils.BaseActivity
import com.example.services.utils.DialogClass
import com.example.services.utils.DialogssInterface
import com.example.services.views.cart.CartListActivity
import com.example.services.views.promocode.PromoCodeActivity

class DashboardActivity : BaseActivity(),
    DialogssInterface {
    var activityDashboardBinding: ActivityDashboardBinding? = null
    private var confirmationDialog: Dialog? = null
    private var ratingDialog: Dialog? = null
    private var mDialogClass = DialogClass()
    private var dashboardViewModel: DashboardViewModel? = null
    var fragment: Fragment? = null
    var isCart = ""
    //    companion object {
//        @get:Synchronized
//        lateinit var toolBarText : TextView
//        lateinit var toolBarImage : ImageView
//        var removedFrag : String = ""
//
//    }
    override fun getLayoutId(): Int {
        return R.layout.activity_dashboard
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initViews() {


        activityDashboardBinding = viewDataBinding as ActivityDashboardBinding
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        activityDashboardBinding!!.dashboardViewModel = dashboardViewModel
        // val catId = intent.extras?.get("catId").toString()Que
        // val name = intent.extras?.get("name").toString()
        activityDashboardBinding!!.commonToolBar.imgRight.visibility = View.GONE
        activityDashboardBinding!!.commonToolBar.imgToolbarText.setText(GlobalConstants.CATEGORY_SELECTED_NAME)//name/*resources.getString(R.string.home)*/)
        activityDashboardBinding!!.commonToolBar.imgRight.setImageResource(R.drawable.ic_cart)

        fragment = HomeFragment()
        callFragments(fragment, supportFragmentManager, false, "send_data", "")
        //img_right
        dashboardViewModel!!.isClick().observe(
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


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setHeadings() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
        when (fragment) {
            is HomeFragment -> {
                /* activityDashboardBinding!!.commonToolBar.imgToolbarText.text =
                     getString(R.string.home)
                 getString(R.string.calendar)*/

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        setHeadings()
        isCart = SharedPrefClass().getPrefValue(
            MyApplication.instance,
            GlobalConstants.isCartAdded
        ).toString()
        if (isCart.equals("true")) {
            activityDashboardBinding!!.commonToolBar.imgRight.visibility = View.VISIBLE
        } else {
            activityDashboardBinding!!.commonToolBar.imgRight.visibility = View.GONE
        }


        // ic_profile


    }

    override fun onDialogConfirmAction(mView: View?, mKey: String) {
        when (mKey) {
            "logout" -> {
                confirmationDialog?.dismiss()
                //dashboardViewModel!!.callLogoutApi()
                // dashboardViewModel!!.callLogoutApi()

            }
//            "rating" -> {
//                ratingDialog?.dismiss()
//            }
        }
    }

    override fun onDialogCancelAction(mView: View?, mKey: String) {
        when (mKey) {

            "rating" -> ratingDialog?.dismiss()
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}