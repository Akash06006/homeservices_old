package com.example.services.utils

/*
 * Created by saira on 22-12-2017.
 */


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.annotation.RequiresApi
import com.example.services.R
import com.example.services.callbacks.ChoiceCallBack
import com.example.services.constants.GlobalConstants
import com.example.services.sharedpreference.SharedPrefClass
import com.example.services.socket.DriverTrackingActivity
import com.example.services.socket.TrackingActivity
import com.example.services.views.subcategories.ServiceDetailActivity

class DialogClass {
    private var checkClick = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setDefaultDialog(
            mContext: Context,
            mInterface: DialogssInterface,
            mKey: String,
            mTitle: String
    ): Dialog {
        val dialogView = Dialog(mContext)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding =
                DataBindingUtil.inflate<ViewDataBinding>(
                        LayoutInflater.from(mContext),
                        R.layout.custom_dialog,
                        null,
                        false
                )

        dialogView.setContentView(binding.root)
        dialogView.setCancelable(false)
        dialogView.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialogView.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val yes = dialogView.findViewById<Button>(R.id.yes)
        val no = dialogView.findViewById<Button>(R.id.no)
        no.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(GlobalConstants.COLOR_CODE))/*mContext.getResources().getColorStateList(R.color.colorOrange)*/)

        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radio_group)
        val tvLogout = dialogView.findViewById<TextView>(R.id.tv_dialog_logout)
        if (mKey.equals("logout"))
            tvLogout.visibility = View.VISIBLE
        else
            tvLogout.visibility = View.GONE

        if (mKey.equals("Address Type")) {
            radioGroup.visibility = View.VISIBLE
            yes.setText(mContext.resources.getString(R.string.submit))
            no.setText(mContext.resources.getString(R.string.cancel))
            SharedPrefClass().putObject(
                    mContext,
                    GlobalConstants.SelectedAddressType,
                    "Shop"
            )
        } else
            radioGroup.visibility = View.GONE

        radioGroup.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton = dialogView.findViewById(checkedId)
                    if (radio.text.toString().equals(mContext.resources.getString(R.string.at_home))) {
                        // selectedAddressType = "0"
                        //GlobalConstants.SelectedAddresssType = "home"
                        SharedPrefClass().putObject(
                                mContext,
                                GlobalConstants.SelectedAddressType,
                                "Home"
                        )
                    } else {
                        // selectedAddressType = "1"
                        //GlobalConstants.SelectedAddresssType = "shop"
                        SharedPrefClass().putObject(
                                mContext,
                                GlobalConstants.SelectedAddressType,
                                "Shop"
                        )
                    }
                    // mKey=radio.text.toString()
                })

        if (!ValidationsClass().checkStringNull(mTitle))
            (dialogView.findViewById<View>(R.id.txt_dia) as TextView).text = mTitle
//        (dialogView.findViewById<View>(R.id.txt_dia) as TextView).text = mTitle

        yes.setOnClickListener {
            mInterface.onDialogConfirmAction(null, mKey)
        }


        no.setOnClickListener {
           // GlobalConstants.SelectedAddresssType=""
            SharedPrefClass().putObject(
                    mContext,
                    GlobalConstants.SelectedAddressType,
                    "null"
            )
            mInterface.onDialogCancelAction(null, mKey)
        }
        // Create the AlertDialog object and return it
        return dialogView

    }

    fun setPermissionDialog(mContext: Context, homeActivity: DriverTrackingActivity): Dialog {
        val dialogView = Dialog(mContext)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val activity = mContext as Activity
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(mContext),
                R.layout.location_popup,
                null,
                false
        )

        dialogView.setContentView(binding.root)
        dialogView.setCancelable(false)
        dialogView.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val btn_turn_on_location = dialogView.findViewById<Button>(R.id.btn_turn_on_location)

        btn_turn_on_location.setOnClickListener { homeActivity.checkPermission() }

        return dialogView

    }

    fun setConfirmationDialog(
            mContext: Context,
            mInterface: DialogssInterface,
            mKey: String
    ): Dialog {
        val dialogView = Dialog(mContext, R.style.transparent_dialog_borderless)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding =
                DataBindingUtil.inflate<ViewDataBinding>(
                        LayoutInflater.from(mContext),
                        R.layout.layout_confirmation_popup,
                        null,
                        false
                )


        dialogView.setContentView(binding.root)
        dialogView.setCancelable(false)

        dialogView.window!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialogView.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        val submit = dialogView.findViewById<Button>(R.id.btn_continue)


        submit.setOnClickListener {
            mInterface.onDialogConfirmAction(null, mKey)
        }


        return dialogView

    }

    fun setCancelDialog(
            mContext: Context,
            mInterface: DialogssInterface,
            mKey: String
    ): Dialog {
        val dialogView = Dialog(mContext, R.style.transparent_dialog_borderless)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding =
                DataBindingUtil.inflate<ViewDataBinding>(
                        LayoutInflater.from(mContext),
                        R.layout.layout_cancel_popup,
                        null,
                        false
                )


        dialogView.setContentView(binding.root)
        dialogView.setCancelable(false)

        dialogView.window!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialogView.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        val submit = dialogView.findViewById<Button>(R.id.btn_continue)
        val cancel = dialogView.findViewById<Button>(R.id.btn_back)

        submit.setOnClickListener {
            mInterface.onDialogConfirmAction(null, mKey)
        }
        cancel.setOnClickListener {
            mInterface.onDialogCancelAction(null, mKey)
        }




        return dialogView
    }


    fun setUploadConfirmationDialog(
            mContext: Context,
            mInterface: ChoiceCallBack,
            mKey: String
    ): Dialog {
        val dialogView = Dialog(mContext)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding =
                DataBindingUtil.inflate<ViewDataBinding>(
                        LayoutInflater.from(mContext),
                        R.layout.dialog_image_choice,
                        null,
                        false
                )

        dialogView.setContentView(binding.root)
        dialogView.setCancelable(true)
        dialogView.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialogView.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val camera = dialogView.findViewById<LinearLayout>(R.id.ll_camera)
        val gallery = dialogView.findViewById<LinearLayout>(R.id.ll_gallery)
        // Create the AlertDialog object and return it
        camera.setOnClickListener {
            mInterface.photoFromCamera(mKey)
            dialogView.dismiss()
        }
        gallery.setOnClickListener {
            mInterface.photoFromGallery(mKey)
            dialogView.dismiss()
        }



        dialogView.show()

        return dialogView

    }

}
