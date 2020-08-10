package com.example.services.common

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import com.example.services.R
import com.example.services.application.MyApplication
import com.example.services.databinding.CustomToastBinding
import com.example.services.firebaseMobile.OtpFirebaseActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.text.SimpleDateFormat
import java.util.*
private val mPhoneList = ArrayList<String>()

object UtilsFunctions {
    @JvmStatic
    fun showToastError(message: String) {
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(MyApplication.instance),
                R.layout.custom_toast,
                null,
                false
            ) as CustomToastBinding
        val toast = Toast(MyApplication.instance)
        binding.tvText.text = message
        binding.image.setImageResource(R.drawable.ic_cross)
        binding.toastLayoutRoot.setBackgroundColor(MyApplication.instance.resources.getColor(R.color.colorRed))
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        toast.view = binding.root
        toast.show()
    }
    @JvmStatic
    fun showToastSuccess(message: String) {
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(MyApplication.instance),
                R.layout.custom_toast,
                null,
                false
            ) as CustomToastBinding
        val toast = Toast(MyApplication.instance)
        binding.tvText.text = message
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        toast.view = binding.root
        // val view = toast.view
        // val group = toast.view as ViewGroup
        // val messageTextView = group.getChildAt(0) as TextView
        // messageTextView.textSize = 15.0f
        //messageTextView.gravity = Gravity.CENTER
        // view.setBackgroundColor(ContextCompat.getColor(MyApplication.instance, R.color.colorSuccess))
        toast.show()

    }

    @SuppressLint("SimpleDateFormat")
    fun getParticularDay(amount : Int) : String {
        val dateFormat = SimpleDateFormat("MMM dd")
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, amount)
        val newDate = calendar.time
        return dateFormat.format(newDate)
    }

    @JvmStatic
    fun checkObjectNull(obj : Any?) : Boolean {
        return obj != null
    }

    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getAndroidID() : String {
        return Settings.Secure.getString(
            MyApplication.instance.contentResolver,
            Settings.Secure.ANDROID_ID
        )

    }

    @JvmStatic
    @TargetApi(Build.VERSION_CODES.M)
    fun isNetworkConnected() : Boolean {
        val cm = MyApplication.instance.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetwork : NetworkInfo? = null
        activeNetwork = cm.activeNetworkInfo

        return if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
            activeNetwork != null && activeNetwork.isConnectedOrConnecting
        } else {
            showToastWarning(MyApplication.instance.getString(R.string.internet_connection))
            // Toast.makeText(MyApplication.getInstance().getApplicationContext(), R.string.internet_connection, Toast.LENGTH_SHORT).show();
            false
        }
    }


    @JvmStatic
    @TargetApi(Build.VERSION_CODES.M)
    fun isNetworkConnectedWithoutToast() : Boolean {
        val cm = MyApplication.instance.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetwork : NetworkInfo? = null
        activeNetwork = cm.activeNetworkInfo

        return if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
            activeNetwork != null && activeNetwork.isConnectedOrConnecting
        } else {
           // showToastWarning(MyApplication.instance.getString(R.string.internet_connection))
            // Toast.makeText(MyApplication.getInstance().getApplicationContext(), R.string.internet_connection, Toast.LENGTH_SHORT).show();
            false
        }
    }

    @JvmStatic
    @TargetApi(Build.VERSION_CODES.M)
    fun isNetworkConnectedReturn() : Boolean {
        val cm = MyApplication.instance.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetwork : NetworkInfo? = null
        activeNetwork = cm.activeNetworkInfo

        return if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
            activeNetwork.isConnectedOrConnecting
        } else {
            false
        }
    }

    @JvmStatic
    fun showToastWarning(message : String?) {
        if (message == null)
            return
        val inflater = MyApplication.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(com.example.services.R.layout.layout_toast, null)
        val image = layout.findViewById<ImageView>(R.id.image)
        image.setImageResource(R.drawable.ic_warning)
        val text = layout.findViewById<TextView>(R.id.text)
        text.text = message
        val toast = Toast(MyApplication.instance)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = LENGTH_SHORT
        layout.setBackgroundColor(ContextCompat.getColor(MyApplication.instance, R.color.colorOrange))
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        toast.view = layout
        toast.show()

    }

    @JvmStatic
    @SuppressLint("SimpleDateFormat")
    fun addDatetoCurrentDate(add : Int) : String {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, add)
        return dateFormat.format(c.time)

    }

    @SuppressLint("SimpleDateFormat")
    fun getTodayDayName() : String {
        val sdf = SimpleDateFormat("EEE")
        val d = Date()
        val dayOfTheWeek = sdf.format(d)
        return dayOfTheWeek.toLowerCase()
    }

    fun getRandomColor() : String {
        val colors = ArrayList<String>()

        colors.add("#F366E0")
        colors.add("#F98D38")
        colors.add("#3A91E2")
        colors.add("#6FBA68")
        colors.add("#9FA8DA")
        colors.add("#DC4378")
        colors.add("#AED581")
        colors.add("#C155C8")
        colors.add("#ECC94A")
        colors.add("#4DD0E1")
        colors.add("#F3735A")
        colors.add("#31BFF0")
        val r = Random()
        val i = r.nextInt(11 - 0) + 0
        return colors[i]
    }

    @JvmStatic
    fun hideKeyBoard(view : View) {
        val imm = MyApplication.instance
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }





}
