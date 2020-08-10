package com.example.services.views.address

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.services.R
import com.example.services.common.UtilsFunctions
import com.example.services.constants.GlobalConstants
import com.example.services.utils.BaseActivity
import com.example.services.viewmodels.address.AddressViewModel
import com.example.services.databinding.ActivityAddAddressBinding
import com.example.services.model.LoginResponse
import com.example.services.model.address.AddressResponse
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.JsonObject
import java.util.*

class AddAddressActivity : BaseActivity(), OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnCameraIdleListener {
    lateinit var addressBinding: ActivityAddAddressBinding
    lateinit var addressViewModel: AddressViewModel
    private var mMap: GoogleMap? = null
    internal lateinit var mLastLocation: Location
    internal lateinit var mLocationCallback: LocationCallback
    internal var mCurrLocationMarker: Marker? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    internal var mFusedLocationClient: FusedLocationProviderClient? = null
    var selectedLat = ""
    var selectedLong = ""
    var selectedAddressType = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_add_address
    }

    override fun initViews() {
        addressBinding = viewDataBinding as ActivityAddAddressBinding
        addressViewModel = ViewModelProviders.of(this).get(AddressViewModel::class.java)
        addressBinding.addressViewModel = addressViewModel
        addressBinding.commonToolBar.imgRight.visibility = View.GONE
        addressBinding.commonToolBar.imgRight.setImageResource(R.drawable.ic_cart)
        addressBinding.commonToolBar.imgToolbarText.text =
                resources.getString(R.string.add_address)
        addressBinding.btnConfirm.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(GlobalConstants.COLOR_CODE))/*mContext.getResources().getColorStateList(R.color.colorOrange)*/)
        addressBinding.btnSubmit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(GlobalConstants.COLOR_CODE))/*mContext.getResources().getColorStateList(R.color.colorOrange)*/)


        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        selectedAddressType = this.resources.getString(R.string.home)
        // Get radio group selected item using on checked change listener
        addressBinding.radioGroup.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton = findViewById(checkedId)
                    selectedAddressType = radio.text.toString()
                    /* Toast.makeText(
                        applicationContext, " On checked change : ${radio.text}",
                        Toast.LENGTH_SHORT
                    ).show()*/
                })

        addressViewModel.getAddress().observe(this,
                Observer<AddressResponse> { response ->
                    stopProgressDialog()
                    if (response != null) {
                        val message = response.message
                        when {
                            response.code == 200 -> {
                                // profileBinding.profileModel = response.data
                                showToastSuccess(message)
                                val intent = Intent()
                                setResult(Activity.RESULT_OK, intent)
                                finish()

                            }
                            else -> showToastError(message)
                        }

                    }
                })

        addressViewModel.isClick().observe(
                this, Observer<String>(function =
        fun(it: String?) {
            when (it) {
                "btn_confirm" -> {
                    if (!TextUtils.isEmpty(addressBinding.etAddress.text.toString()) && !addressBinding.etAddress.text.toString().equals(
                                    "null"
                            )
                    ) {
                        mMap?.uiSettings?.setAllGesturesEnabled(false)
                        mMap?.uiSettings?.isZoomControlsEnabled = false
                        mMap?.uiSettings?.isScrollGesturesEnabled = false
                        addressBinding.etCity.setText("")
                        addressBinding.etHouseNo.setText("")
                        addressBinding.detailLayout.visibility = View.VISIBLE
                        var animation = AnimationUtils.loadAnimation(this, R.anim.anim)
                        animation.setDuration(500)
                        addressBinding.detailLayout.setAnimation(animation)
                        addressBinding.detailLayout.animate()
                        animation.start()
                        addressBinding.confirmLayout.visibility = View.GONE
                    }
                    //   addressBinding.mapPin.visibility = View.GONE
                }
                "img_cross" -> {
                    /* addressBinding.detailLayout.visibility = View.VISIBLE
                     var animation = AnimationUtils.loadAnimation(this, R.anim.anim)
                     animation.setDuration(500)
                     addressBinding.detailLayout.setAnimation(animation)
                     addressBinding.detailLayout.animate()
                     animation.start()*/
                    mMap?.uiSettings?.setAllGesturesEnabled(true)
                    mMap?.uiSettings?.isScrollGesturesEnabled = true
                    mMap?.uiSettings?.isZoomControlsEnabled = true
                    addressBinding.confirmLayout.visibility = View.VISIBLE
                    addressBinding.detailLayout.visibility = View.GONE
                }
                "btn_submit" -> {
                    val houseNo = addressBinding.etHouseNo.text.toString()
                    val city = addressBinding.etCity.text.toString()
                    val addresss = addressBinding.etAddress.text.toString()
                    val address = addressBinding.etAddress.text.toString()

                    when {
                        addresss.isEmpty() -> showError(
                                addressBinding.etCity,
                                getString(R.string.empty) + " " + getString(
                                        R.string.city
                                )
                        )
                        houseNo.isEmpty() -> showError(
                                addressBinding.etHouseNo,
                                getString(R.string.empty) + " " + getString(
                                        R.string.house_no
                                )
                        )
                        city.isEmpty() -> showError(
                                addressBinding.etCity,
                                getString(R.string.empty) + " " + getString(
                                        R.string.city
                                )
                        )
                        else -> {
                            var addAddressObject = JsonObject()
                            addAddressObject.addProperty(
                                    "addressName", addresss
                            )
                            addAddressObject.addProperty(
                                    "city", city
                            )
                            addAddressObject.addProperty(
                                    "defautlt", "0"
                            )
                            addAddressObject.addProperty(
                                    "latitude", selectedLat
                            )
                            addAddressObject.addProperty(
                                    "longitude", selectedLong
                            )
                            addAddressObject.addProperty(
                                    "addressType", selectedAddressType
                            )
                            addAddressObject.addProperty(
                                    "houseNo", houseNo
                            )

                            if (UtilsFunctions.isNetworkConnected()) {
                                startProgressDialog()
                                addressViewModel.addAddressDetail(addAddressObject)
                            }


                        }
                    }

                }
            }
        })
        )

    }

    //region mp
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
                mMap!!.isMyLocationEnabled = true
            }
        } else {
            buildGoogleApiClient()
            mMap!!.isMyLocationEnabled = true
        }
        mMap?.setOnCameraIdleListener(this)
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient
        )
        if (mLastLocation != null) {
            val latLng = LatLng(mLastLocation.latitude, mLastLocation.longitude)
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title("Current Position")
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        }
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 100000 //5 seconds
        mLocationRequest.fastestInterval = 100000 //3 seconds
        mLocationRequest.smallestDisplacement = 0.1f //added
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        )
    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }
        //Place current location marker
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        //mCurrLocationMarker = mMap!!.addMarker(markerOptions)
        //move map camera
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(18f))
        //stop location updates
        if (mGoogleApiClient != null) {
            mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
        }
        mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Toast.makeText(applicationContext, "connection failed", Toast.LENGTH_SHORT).show()
    }

    override fun onConnectionSuspended(p0: Int) {
        Toast.makeText(applicationContext, "connection suspended", Toast.LENGTH_SHORT).show()
    }

    //endregion
    private fun showError(textView: TextView, error: String) {
        textView.requestFocus()
        textView.error = error
    }

    override fun onCameraIdle() {
        var loc = mMap?.cameraPosition?.target
        Log.d("Location", "---" + loc)
        getAddress(loc)
    }

    private fun getAddress(loc: LatLng?) {
        // Geocoder geocoder
        //  List<Address> addresses;
        val geocoder = Geocoder(this, Locale.getDefault());
        var addresses = geocoder.getFromLocation(
                loc?.latitude!!,
                loc.longitude,
                1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        if (addresses.size > 0) {
            selectedLat = loc?.latitude!!.toString()
            selectedLong = loc.longitude.toString()
            var address = addresses?.get(0)
                    ?.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            var city = addresses.get(0).getLocality()
            var state = addresses.get(0).getAdminArea()
            var country = addresses.get(0).getCountryName()
            var postalCode = addresses.get(0).getPostalCode()
            var knownName = addresses.get(0).getFeatureName()
            addressBinding.etAddress.setText(address)
            addressBinding.tvAddress.setText(address)
        }
    }
}
