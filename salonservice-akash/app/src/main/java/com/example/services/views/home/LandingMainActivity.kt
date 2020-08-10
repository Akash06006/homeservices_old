package com.example.services.views.home

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
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
import com.example.services.common.UtilsFunctions
import com.example.services.constants.GlobalConstants
import com.example.services.databinding.ActivityLandingBinding
import com.example.services.model.CommonModel
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.utils.BaseActivity
import com.example.services.utils.DialogClass
import com.example.services.utils.DialogssInterface
import com.example.services.views.address.AddressListActivity
import com.example.services.views.authentication.LoginActivity
import com.example.services.views.cart.CartListActivity
import com.example.services.views.favorite.FavoriteListActivity
import com.example.services.views.notifications.NotificationsListActivity
import com.example.services.views.orders.OrdersHistoryListActivity
import com.example.services.views.orders.OrdersListActivity
import com.example.services.views.profile.ProfileActivity
import com.example.services.views.ratingreviews.AddRatingReviewsListActivity
import com.example.services.views.settings.MyAccountsActivity
import com.example.services.views.settings.WebViewActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout

class LandingMainActivity : BaseActivity(),
    DialogssInterface {
    var activityLandingBinding: ActivityLandingBinding? = null
    private var navigationView: NavigationView? = null
    private var drawer: DrawerLayout? = null
    private var confirmationDialog: Dialog? = null
    private var ratingDialog: Dialog? = null
    private var mDialogClass = DialogClass()
    private var dashboardViewModel: DashboardViewModel? = null
    private var removedFrag: String = ""
    var fragment: Fragment? = null
    var isCart = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_landing
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initViews() {
        activityLandingBinding = viewDataBinding as ActivityLandingBinding
        navigationView = activityLandingBinding!!.navView
        navigationView!!.alpha = 0.9f
        drawer = activityLandingBinding!!.drawerLayout
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        activityLandingBinding!!.dashboardViewModel = dashboardViewModel
        // toolBarText = activityDashboardBinding!!.toolbarCommon.imgToolbarText
        // toolBarImage = activityDashboardBinding!!.toolbarCommon.imgRight
        /** Show Rating Dialog here**/
        // checkForRating(0)
        /*****************/
        activityLandingBinding!!.toolbarCommon.toolbar.setImageResource(R.drawable.ic_menu)
        activityLandingBinding!!.toolbarCommon.imgRight.visibility = View.GONE
        activityLandingBinding!!.toolbarCommon.imgToolbarText.setText(resources.getString(R.string.home))
        activityLandingBinding!!.toolbarCommon.imgRight.setImageResource(R.drawable.ic_cart)
//        activityDashboardBinding!!.toolbarCommon.rlTop.setBackgroundColor(resources.getColor(R.color.orange_transparent))


        var id = GlobalConstants.ORDERID
        val from = intent.extras?.get("from").toString()
        if (from.equals("notification")) {
            val status = intent.extras?.get("status").toString()
            if (status.equals("5")) {
                ratingDialog = mDialogClass.setDefaultDialog(
                    this,
                    this,
                    "Rating",
                    getString(R.string.warning_rate_order)
                )
                ratingDialog?.show()
            }
        }/* else {

        }*/
        val image = SharedPrefClass().getPrefValue(
            MyApplication.instance.applicationContext,
            GlobalConstants.USER_IAMGE
        )
        // ic_profile
        Glide.with(this)
            .load(image)
            .placeholder(R.drawable.ic_person)
            .into(activityLandingBinding!!.icProfile)
        val name = SharedPrefClass().getPrefValue(
            MyApplication.instance.applicationContext,
            getString(R.string.first_name)
        )
        if (TextUtils.isEmpty(name.toString()) || name.toString().equals("null")) {
            activityLandingBinding!!.tvName.text = getString(R.string.create_profile)
        } else {
            activityLandingBinding!!.tvName.text = name.toString()
        }

        fragment = LandingHomeFragment()
        callFragments(fragment, supportFragmentManager, false, "send_data", "")
        dashboardViewModel!!.isClick().observe(
            this, Observer<String>(function =
            fun(it: String?) {
                when (it) {
                    "tv_nav_fav" -> {
                        val intent = Intent(this, FavoriteListActivity::class.java)
                        startActivity(intent)
                    }
                    "tv_nav_notification" -> {
                        val intent = Intent(this, NotificationsListActivity::class.java)
                        startActivity(intent)
                    }
                    "tv_nav_address" -> {
                        val intent = Intent(this, AddressListActivity::class.java)
                        startActivity(intent)
                    }
                    "tv_nav_cart" -> {
                        val intent = Intent(this, CartListActivity::class.java)
                        startActivity(intent)
                    }
                    "img_right" -> {
                        val intent = Intent(this, CartListActivity::class.java)
                        startActivity(intent)
                    }
                    "tv_nav_home" -> {
                        activityLandingBinding!!.toolbarCommon.imgRight.visibility = View.VISIBLE
                        /* activityLandingBinding!!.toolbarCommon.imgRight.setImageDrawable(
                             getDrawable(R.drawable.ic_notifications)
                         )*/
                        val fragment = LandingHomeFragment()
                        // activityDashboardBinding!!.toolbarCommon.imgToolbarText.setText("")
                        /*   activityDashboardBinding!!.toolbarCommon.imgToolbarText.text =
                               getString(R.string.home)*/
                        activityLandingBinding!!.drawerLayout.closeDrawers()
                        this.callFragments(
                            fragment,
                            supportFragmentManager,
                            false,
                            "send_data",
                            ""
                        )
                        activityLandingBinding!!.tablayout.getTabAt(0)?.select()
                        activityLandingBinding!!.drawerLayout.closeDrawers()

                    }
                    "tv_nav_order" -> {
                        val intent = Intent(this, OrdersListActivity::class.java)
                        startActivity(intent)
                    }
                    "tv_nav_order_history" -> {
                        val intent = Intent(this, OrdersHistoryListActivity::class.java)
                        startActivity(intent)
                    }

                    "tv_nav_about" -> {
                        /* val intent = Intent(this, OrdersHistoryListActivity::class.java)
                         startActivity(intent)*/
                        //showToastSuccess("Coming Soon")
                        val intent1 = Intent(this, WebViewActivity::class.java)
                        intent1.putExtra("title", getString(R.string.about_us))
                        startActivity(intent1)
                    }
                    "tv_nav_chat" -> {
                        /* val intent = Intent(this, OrdersHistoryListActivity::class.java)
                         startActivity(intent)*/
                        showToastSuccess("Coming Soon")
                    }

                    "ic_profile" -> {
                        val intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                    }
                    "tv_nav_setting" -> {
                        val intent = Intent(this, MyAccountsActivity::class.java)
                        startActivity(intent)
                    }
                    "img_nav_cancel" -> {
                        activityLandingBinding!!.drawerLayout.closeDrawers()
                    }
                    "tv_nav_logout" -> {
                        confirmationDialog = mDialogClass.setDefaultDialog(
                            this,
                            this,
                            "logout",
                            "Do you really want to logout?"
                        )
                        confirmationDialog?.show()

                    }
                    "toolbar" -> {
                        val image = SharedPrefClass().getPrefValue(
                            MyApplication.instance.applicationContext,
                            GlobalConstants.USER_IAMGE
                        )
                        // ic_profile
                        Glide.with(this)
                            .load(image)
                            .placeholder(R.drawable.ic_person)
                            .into(activityLandingBinding!!.icProfile)

                        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
                            drawer!!.closeDrawer(Gravity.LEFT) //CLOSE Nav Drawer!
                        } else {
                            drawer!!.openDrawer(Gravity.LEFT)
                        }
                        val fragmentType =
                            supportFragmentManager.findFragmentById(R.id.frame_layout)
                        /*when (fragmentType) {
                            is HomeFragment -> {
                                activityDashboardBinding!!.toolbarCommon.imgRight.visibility =
                                    View.GONE
                            }
                        }*/
                    }
                }
            })
        )

        dashboardViewModel!!.getLogoutReposne.observe(this,
            Observer<CommonModel> { logoutResponse ->
                this.stopProgressDialog()

                if (logoutResponse != null) {
                    val message = logoutResponse.message

                    if (logoutResponse.code == 200) {
                        SharedPrefClass().putObject(
                            this,
                            "isLogin",
                            false
                        )
                        SharedPrefClass().putObject(
                            this,
                            GlobalConstants.SelectedAddressType,
                            "null"
                        )
                        SharedPrefClass().putObject(
                            this,
                            GlobalConstants.USER_IAMGE,
                            "null"
                        )
                        showToastSuccess(getString(R.string.logout_msg))
                        val intent1 = Intent(this, LoginActivity::class.java)
                        startActivity(intent1)
                        finish()

                    } else {
                        showToastError(message)
                    }
                }
            })

        dashboardViewModel!!.isLoading().observe(this, Observer<Boolean> { aBoolean ->
            if (aBoolean!!) {
                this.startProgressDialog()
            } else {
                this.stopProgressDialog()
            }
        })

        activityLandingBinding!!.tablayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                var fragment: Fragment? = null
                //   activityDashboardBinding!!.toolbarCommon.imgRight.visibility = View.GONE
                when (tab!!.position) {
                    /* 0 -> fragment = HomeFragment()
                     1 -> fragment = JobRequestsFragment()*/
                }
                callFragments(fragment, supportFragmentManager, false, "send_data", "")

                Handler().postDelayed({
                    setHeadings()
                }, 300)

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //var fragment : Fragment? = null
                //Not In use
            }
        })

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setHeadings() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
        when (fragment) {
            is HomeFragment -> {
                /* activityDashboardBinding!!.toolbarCommon.imgToolbarText.text =
                     getString(R.string.home)
                 getString(R.string.calendar)*/

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun onResumedForFragment() {
        onResume()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        setHeadings()
        /*  if (GlobalConstants.selectedCheckedFragment == 100) {
              activityDashboardBinding!!.tablayout.getTabAt(GlobalConstants.selectedFragment)!!.select()
              GlobalConstants.selectedCheckedFragment = 0
          }*/
        activityLandingBinding!!.topBannerLayout.setBackgroundColor(
            (Color.parseColor(
                GlobalConstants.COLOR_CODE
            ))
        )
        activityLandingBinding!!.topBannerLayout.setBackgroundTintList(
            ColorStateList.valueOf(
                Color.parseColor(
                    GlobalConstants.COLOR_CODE
                )
            )/*mContext.getResources().getColorStateList(R.color.colorOrange)*/
        )
        isCart = SharedPrefClass().getPrefValue(
            MyApplication.instance,
            GlobalConstants.isCartAdded
        ).toString()
        if (isCart.equals("true")) {
            activityLandingBinding!!.toolbarCommon.imgRight.visibility = View.VISIBLE
        } else {
            activityLandingBinding!!.toolbarCommon.imgRight.visibility = View.GONE
        }

        val image = SharedPrefClass().getPrefValue(
            MyApplication.instance.applicationContext,
            GlobalConstants.USER_IAMGE
        )
        // ic_profile
        Glide.with(this)
            .load(image)
            .placeholder(R.drawable.ic_person)
            .into(activityLandingBinding!!.icProfile)

        val name = SharedPrefClass().getPrefValue(
            MyApplication.instance.applicationContext,
            getString(R.string.first_name)
        )
        if (TextUtils.isEmpty(name.toString().trim()) || name.toString().trim().equals("null")) {
            activityLandingBinding!!.tvName.text = getString(R.string.create_profile)
        } else {
            activityLandingBinding!!.tvName.text = name.toString()
        }

    }

    override fun onDialogConfirmAction(mView: View?, mKey: String) {
        when (mKey) {
            "logout" -> {
                confirmationDialog?.dismiss()
                dashboardViewModel!!.callLogoutApi()
                // dashboardViewModel!!.callLogoutApi()

            }
            "Rating" -> {
                ratingDialog?.dismiss()
                // confirmationDialog?.dismiss()
                if (UtilsFunctions.isNetworkConnected()) {
                    val intent = Intent(this, AddRatingReviewsListActivity::class.java)
                    intent.putExtra("orderId", "6ad3c62c-17dd-4bb1-97c5-e757cd2f4ff3")
                    startActivity(intent)
                }
            }
        }
    }

    override fun onDialogCancelAction(mView: View?, mKey: String) {
        when (mKey) {
            "logout" -> confirmationDialog?.dismiss()
            "Rating" -> ratingDialog?.dismiss()
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }


}