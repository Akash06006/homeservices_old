package com.example.services.utils

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.StrictMode
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.services.R
import com.example.services.common.UtilsFunctions
import com.example.services.constants.GlobalConstants
import com.example.services.views.authentication.LoginActivity
import com.google.gson.GsonBuilder
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by Saira on 2018-12-9.
 */
abstract class BaseActivity : AppCompatActivity() {
    private var colorRes: Res? = null
    protected var viewDataBinding: ViewDataBinding? = null
    private var utils: Utils? = null
    private var inputMethodManager: InputMethodManager? = null
    private var progressDialog: Dialog? = null
    private var mFragmentManager: androidx.fragment.app.FragmentManager? = null
    private val gson = GsonBuilder().serializeNulls().create()
    private var permCallback: PermissionCallback? = null
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this@BaseActivity.overridePendingTransition(
                R.anim.slide_in,
                R.anim.slide_out
        )


        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        //  mtoolbar = findViewById(R.id.toolbar);
        //        retrofitClient = (ApiService) RetrofitClient.with(this).getClient(Constants.API_BASE_URL, false, this).create(ApiService.class);
        inputMethodManager = this
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //store = new PrefStore(this);
        //    public ApiService retrofitClient;
        strictModeThread()
        initializeProgressDialog()
        initViews()



    }

    override fun onResume() {
        super.onResume()
        colorRes = null
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary))
    }

    protected abstract fun initViews()
    protected abstract fun getLayoutId(): Int

    private fun strictModeThread() {
        StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                        .permitAll().build()
        )
    }

    override fun getResources(): Resources {
        if (colorRes == null) {
            colorRes = Res(super.getResources(), getRequiredColor())
        }
        return colorRes!!
    }

    fun getRequiredColor(): Int? {
        return Color.parseColor(GlobalConstants.COLOR_CODE)
    }

    fun eventCreatedDialog(activity: Activity, key: String, message: String) {
        showToastSuccess(message)
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.activity_dialog_tick)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val imgTick = dialog.findViewById(R.id.img_tick) as ImageView
        object : CountDownTimer(500, 200) {
            override fun onTick(millisUntilFinished: Long) {
                (imgTick.drawable as Animatable).start()
            }

            override fun onFinish() {
                when (key) {
                    "reset_password" -> {
                        val intent1 = Intent(activity, LoginActivity::class.java)
                        activity.startActivity(intent1)
                        ActivityCompat.finishAffinity(activity)
                    }

                    "cancel_activity" -> {
                        activity.finish()
                    }
                    "change_password" -> {
                        activity.finish()
                    }

                    "cancel" -> dialog.dismiss()
                    "update" -> dialog.dismiss()
                    "update_profile" -> dialog.dismiss()


                }
            }

        }.start()
        dialog.show()

    }

    fun checkObjectNull(obj: Any?): Boolean {
        return obj != null
    }


    fun showAlert(activity: Activity, message1: String) {
        val binding =
                DataBindingUtil.inflate<ViewDataBinding>(
                        LayoutInflater.from(activity),
                        R.layout.layout_custom_alert,
                        null,
                        false
                )

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)
        dialog.setTitle(getString(R.string.app_name))

        // set the custom dialog components - text, image and button
        val text = dialog.findViewById(R.id.text) as TextView
        text.text = message1

        val dialogButton = dialog.findViewById(R.id.dialogButtonOK) as Button
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }


    /*}
     *Method called  progress dialog */
    private fun initializeProgressDialog() {
        progressDialog = Dialog(this, R.style.transparent_dialog_borderless)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(this),
                R.layout.progress_dialog,
                null,
                false
        )
       // binding.loader.setColor(Color.parseColor(GlobalConstants.COLOR_CODE))
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog!!.setContentView(binding.root)

        mFragmentManager = supportFragmentManager
        // txtMsgTV = (TextView) view.findViewById(R.id.txtMsgTV);
        progressDialog!!.setCancelable(false)
    }

    fun showToastSuccess(message: String?) {
        UtilsFunctions.showToastSuccess(message!!)

    }

    fun showToastError(message: String?) {
        UtilsFunctions.showToastError(message!!)

    }

    private fun showToastWarning(message: String?) {
        UtilsFunctions.showToastWarning(message)

    }

    /*
     * Method to show snack bar*/
    /*
     * Method to start progress dialog*/
    fun startProgressDialog() {
        if (progressDialog != null && !progressDialog!!.isShowing) {
            try {
                progressDialog!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /*
     * Method to stop progress dialog*/
    fun stopProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            try {
                progressDialog!!.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun utils(): Utils {
        if (utils == null)
            utils = Utils(this)
        return utils!!
    }


    fun callFragments(
            fragment: androidx.fragment.app.Fragment?,
            mFragmentManager: androidx.fragment.app.FragmentManager,
            mSendDataCheck: Boolean,
            key: String?,
            mObject: Any
    ) {
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        if (fragment != null) {
            if (mSendDataCheck && key != null) {
                when (key) {
                    "deleteAll" -> mFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    "send_data" -> {
                        val mBundle = Bundle()
                        mBundle.putString(GlobalConstants.SEND_DATA, gson.toJson(mObject))
                        fragment.arguments = mBundle
                    }
                }
            }
            mFragmentTransaction.addToBackStack(null)
            mFragmentTransaction.replace(R.id.frame_layout, fragment)
            // mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            mFragmentTransaction.commit()
        }
    }

    fun callFragmentsContainer(
            fragment: androidx.fragment.app.Fragment?,
            mFragmentManager: androidx.fragment.app.FragmentManager,
            mSendDataCheck: Boolean,
            key: String?,
            mObject: Any,
            id: Int
    ) {
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        if (fragment != null) {
            if (mSendDataCheck && key != null) {
                when (key) {
                    "deleteAll" -> mFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    "send_data" -> {
                        val mBundle = Bundle()
                        mBundle.putString(GlobalConstants.SEND_DATA, gson.toJson(mObject))
                        fragment.arguments = mBundle
                    }
                }
            }
            mFragmentTransaction.addToBackStack(null)
            mFragmentTransaction.replace(id, fragment)
            // mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            mFragmentTransaction.commit()
        }
    }

    interface PermissionCallback {
        fun permGranted()
        fun permDenied()
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {

                val perms = HashMap<String, Int>()
                // Initialize the map with both permissions
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.RECORD_AUDIO] = PackageManager.PERMISSION_GRANTED
                // Fill with actual results from user
                if (grantResults.isNotEmpty()) {
                    for (i in permissions.indices)
                        perms[permissions[i]] = grantResults[i]
                    // Check for both permissions
                    if (perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED
                            && perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                            && perms[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED
                            && perms[Manifest.permission.RECORD_AUDIO] == PackageManager.PERMISSION_GRANTED) {
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        //                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                            showDialogOK("Service Permissions are required for this app",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        when (which) {
                                            DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                            DialogInterface.BUTTON_NEGATIVE ->
                                                // proceed with logic by disabling the related features or quit the app.
                                                finish()
                                        }
                                    })
                        } else {
                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?")
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }//permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                    }
                }
            }
        }
    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show()
    }

    private fun explain(msg: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage(msg)
                .setPositiveButton("Yes") { paramDialogInterface, paramInt ->
                    //  permissionsclass.requestPermission(type,code);
                    startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.uniongoods")))
                }
                .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> finish() }
        dialog.show()
    }

    public fun checkAndRequestPermissions(): Boolean {
        val camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val permissionRecordAudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)


        val listPermissionsNeeded = ArrayList<String>()

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (permissionRecordAudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
            return false
        }
        return true
    }


}
