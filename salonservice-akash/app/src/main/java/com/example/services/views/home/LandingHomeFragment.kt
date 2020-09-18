package com.example.services.views.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.services.R
import com.example.services.common.UtilsFunctions
import com.example.services.common.UtilsFunctions.showToastError
import com.example.services.constants.GlobalConstants
import com.example.services.databinding.FragmentHomeLandingBinding
import com.example.services.maps.FusedLocationClass
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
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonObject
import com.uniongoods.adapters.*
import java.util.*
import kotlin.collections.ArrayList

class
LandingHomeFragment : BaseFragment(), DialogssInterface {
    private var categoriesList = ArrayList<Services>()
    lateinit var homeViewModel: HomeViewModel
    private lateinit var fragmentHomeBinding: FragmentHomeLandingBinding
    private var offersList =
        ArrayList<com.example.services.viewmodels.home.Banners>()
    private var confirmationDialog: Dialog? = null
    private var mDialogClass = DialogClass()

    private var mFusedLocationClass: FusedLocationClass? = null
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var currentLat = ""
    var currentLong = ""

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_landing
    }

    override fun onResume() {
        super.onResume()
        mFusedLocationClass = FusedLocationClass(activity)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        getLastLocation()

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
                    /*GlobalConstants.COLOR_CODE =
                        "#" + categoriesList[position].colorCode.toString().trim()*/
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


    //region LOCATION FUNCTIONALITY
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(activity!!) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        currentLat = location.latitude.toString()
                        currentLong = location.longitude.toString()
                        // GlobalConstants.CURRENT_LAT = currentLat
                        // GlobalConstants.CURRENT_LONG = currentLong

                        val loc = LatLng(currentLat.toDouble(), currentLong.toDouble())
                        getAddress(loc)
                        Log.e("currentLat_currentLong", "" + currentLat + "---" + currentLong)
                    }
                }
            } else {
                Toast.makeText(activity, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )

    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            currentLat = mLastLocation.latitude.toString()
            currentLong = mLastLocation.longitude.toString()
            // GlobalConstants.CURRENT_LAT = currentLat
            // GlobalConstants.CURRENT_LONG = currentLong

            val loc = LatLng(currentLat.toDouble(), currentLong.toDouble())
            getAddress(loc)
            Log.e("currentLat_currentLong", "" + currentLat + "---" + currentLong)

        }
    }

    private fun getAddress(loc: LatLng?) {
        // Geocoder geocoder
        //  List<Address> addresses;
        val geocoder = Geocoder(activity, Locale.getDefault());
        var addresses = geocoder.getFromLocation(
            loc?.latitude!!,
            loc.longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        if (addresses.size > 0) {
            //selectedLat = loc?.latitude!!.toString()
            // selectedLong = loc.longitude.toString()
            var address = addresses?.get(0)
                ?.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            var city = addresses.get(0).getLocality()
            var state = addresses.get(0).getAdminArea()
            var country = addresses.get(0).getCountryName()
            var postalCode = addresses.get(0).getPostalCode()
            var knownName = addresses.get(0).getFeatureName()
            fragmentHomeBinding.txtLoc.setText(address)
            // addressBinding.tvAddress.setText(address)
        }
    }
    //endregion


}