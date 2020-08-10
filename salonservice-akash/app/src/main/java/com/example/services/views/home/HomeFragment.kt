package com.example.services.views.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import android.provider.Settings
import android.widget.AdapterView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.services.R
import com.example.services.application.MyApplication
import com.example.services.common.UtilsFunctions
import com.example.services.common.UtilsFunctions.showToastError
import com.example.services.constants.GlobalConstants
import com.example.services.databinding.FragmentHomeBinding
import com.example.services.maps.FusedLocationClass
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.socket.SocketClass
import com.example.services.socket.SocketInterface
import com.example.services.utils.BaseFragment
import com.example.services.utils.DialogClass
import com.example.services.viewmodels.home.Banners
import com.example.services.viewmodels.home.CategoriesListResponse
import com.example.services.viewmodels.home.HomeViewModel
import com.example.services.viewmodels.home.Subcat
import com.example.services.views.subcategories.ServicesListActivity
import com.google.android.gms.location.*
import com.google.gson.JsonObject
import com.uniongoods.adapters.*
import org.json.JSONObject

class
HomeFragment : BaseFragment(), SocketInterface {
    private var mFusedLocationClass: FusedLocationClass? = null
    private var socket = SocketClass.socket
    private var categoriesList = ArrayList<Subcat>()
    private var trendingServiceList =
        ArrayList<com.example.services.viewmodels.home.Trending>()
    private var offersList =
        ArrayList<com.example.services.viewmodels.home.Offers>()
    private var banners =
        ArrayList<Banners>()
    private var myJobsListAdapter: CategoriesListAdapter? = null
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private val mJsonObject = JsonObject()
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var currentLat = ""
    var currentLong = ""
    var mJsonObjectStartJob = JsonObject()
    private var confirmationDialog: Dialog? = null
    private var mDialogClass = DialogClass()
    //var categoriesList = null
    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun onResume() {
        super.onResume()

    }

    //api/mobile/services/getSubcat/b21a7c8f-078f-4323-b914-8f59054c4467
    override fun initView() {
        fragmentHomeBinding = viewDataBinding as FragmentHomeBinding
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        fragmentHomeBinding.homeViewModel = homeViewModel
        // categoriesList=List<Service>()
        mFusedLocationClass = FusedLocationClass(activity)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        // baseActivity.startProgressDialog()
        categoriesList?.clear()
        //getLastLocation()
        /*socket.updateSocketInterface(this)
        Log.e("Connect Socket", "Home Fragment")
        socket.onConnect()*/
        //acceptStatus
        mJsonObject.addProperty(
            "acceptStatus", "1"
        )
        // initRecyclerView()

        if (UtilsFunctions.isNetworkConnected()) {
            homeViewModel.getSubServices(GlobalConstants.CATEGORY_SELECTED)
            baseActivity.startProgressDialog()
        }
        homeViewModel.getGetSubServices().observe(this,
            Observer<CategoriesListResponse> { response ->
                baseActivity.stopProgressDialog()
                if (response != null) {
                    val message = response.message
                    when {
                        response.code == 200 -> {
                            categoriesList.addAll(response.body.subcat)
                            trendingServiceList.addAll(response.body.trending)
                            offersList.addAll(response.body.offers)
                            banners.addAll(response.body.banners)
                            fragmentHomeBinding.rvJobs.visibility = View.VISIBLE

                            initRecyclerView()
                            if (banners.size > 0) {
                                fragmentHomeBinding.rvBanners.visibility = View.VISIBLE
                                initBannersRecyclerView()
                            } else {
                                fragmentHomeBinding.rvBanners.visibility = View.GONE
                            }
                            if (trendingServiceList.size > 0) {
                                fragmentHomeBinding.trendingLayout.visibility = View.VISIBLE
                                trendingServiceListViewPager()
                            } else {
                                fragmentHomeBinding.trendingLayout.visibility = View.GONE
                            }

                            if (offersList.size > 0) {
                                fragmentHomeBinding.offersLayout.visibility = View.VISIBLE
                                offerListViewPager()
                            } else {
                                fragmentHomeBinding.offersLayout.visibility = View.GONE
                            }

                        }
                        else -> message?.let {
                            showToastError(it)
                            fragmentHomeBinding.rvJobs.visibility = View.GONE
                        }
                    }

                }
            })

        fragmentHomeBinding.gridview.onItemClickListener =
            AdapterView.OnItemClickListener { parent, v, position, id ->
                //showToastSuccess(" Clicked Position: " + (position + 1))
                // if (categoriesList[position].isService.equals("false")) {
                val intent = Intent(activity!!, ServicesListActivity::class.java)
                intent.putExtra("catId", categoriesList[position].id)
                startActivity(intent)
                /* } else {
                     val intent = Intent(activity!!, ServicesListActivity::class.java)
                     intent.putExtra("catId", categoriesList[position].id)
                     startActivity(intent)
                 }*/
            }
    }

    private fun callSocketMethods(methodName: String) {
        val object5 = JSONObject()
        when (methodName) {
            "updateVehicleLocation" -> try {
                object5.put("methodName", methodName)
                object5.put("latitude", currentLat)
                object5.put("longitude", currentLong)
                object5.put(
                    "driver_id", SharedPrefClass().getPrefValue(
                        MyApplication.instance,
                        GlobalConstants.USERID
                    ).toString()
                )
                socket.sendDataToServer(methodName, object5)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun initRecyclerView() {
        /*val adapter = CategoriesGridListAdapter(this@HomeFragment, categoriesList, activity!!)
        fragmentHomeBinding.gridview.adapter = adapter*/

        val vendorsListAdapter =
            DashboardSubCatsRecyclerAdapter(this@HomeFragment, categoriesList, activity!!)
        // val linearLayoutManager = LinearLayoutManager(this)
        val gridLayoutManager = GridLayoutManager(activity!!, 4)
        fragmentHomeBinding.rvJobs.layoutManager = gridLayoutManager
        fragmentHomeBinding.rvJobs.setHasFixedSize(true)
        // linearLayoutManager.orientation = RecyclerView.VERTICAL
        // favoriteBinding.rvFavorite.layoutManager = linearLayoutManager
        fragmentHomeBinding.rvJobs.adapter = vendorsListAdapter
        fragmentHomeBinding.rvJobs.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })

    }

    private fun initBannersRecyclerView() {
        /*val adapter = CategoriesGridListAdapter(this@HomeFragment, categoriesList, activity!!)
        fragmentHomeBinding.gridview.adapter = adapter*/

        val vendorsListAdapter =
            DashboardBannersRecyclerAdapter(this@HomeFragment, banners, activity!!)
        val linearLayoutManager = LinearLayoutManager(activity!!)
        //val gridLayoutManager = GridLayoutManager(activity!!, 1)
        fragmentHomeBinding.rvBanners.layoutManager = linearLayoutManager
        fragmentHomeBinding.rvBanners.setHasFixedSize(true)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        // favoriteBinding.rvFavorite.layoutManager = linearLayoutManager
        fragmentHomeBinding.rvBanners.adapter = vendorsListAdapter
        fragmentHomeBinding.rvBanners.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })

    }

    private fun trendingServiceListViewPager() {
        val adapter = TrendingServiceListAdapter(this@HomeFragment, trendingServiceList, activity!!)
        fragmentHomeBinding.viewpager.adapter = adapter

    }

    private fun offerListViewPager() {
        val offersListRecyclerAdapter =
            OffersListRecyclerAdapter(this@HomeFragment, offersList, activity!!)
        val linearLayoutManager = LinearLayoutManager(this.baseActivity)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        fragmentHomeBinding.rvOffers.layoutManager = linearLayoutManager
        fragmentHomeBinding.rvOffers.adapter = offersListRecyclerAdapter
        fragmentHomeBinding.rvOffers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
        /* val adapter = OffersListAdapter(this@HomeFragment, offersList, activity!!)
         fragmentHomeBinding.offersViewpager.adapter = adapter
 */
    }

    override fun onSocketCall(onMethadCall: String, vararg args: Any) {
        try {
            when (onMethadCall) {
                "updateVehicleLocation" -> try {
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                } catch (e1: Exception) {
                    e1.printStackTrace()
                }
            }
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
    }

    override fun onSocketConnect(vararg args: Any) {
        //OnSocket Connect Call It
    }

    override fun onSocketDisconnect(vararg args: Any) {
        // //OnSocket Disconnect Call It
    }

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
                        Handler().postDelayed({
                            callSocketMethods("updateVehicleLocation")
                        }, 2000)

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
            var mLastLocation: Location = locationResult.lastLocation
            currentLat = mLastLocation.latitude.toString()
            currentLong = mLastLocation.longitude.toString()
            Handler().postDelayed({
                callSocketMethods("updateVehicleLocation")
            }, 2000)

        }
    }

}