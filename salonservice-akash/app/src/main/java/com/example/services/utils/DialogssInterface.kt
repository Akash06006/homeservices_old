package com.example.services.utils

/*
 * Created by admin on 22-12-2017.
 */

import android.view.View

interface DialogssInterface {
    fun onDialogConfirmAction(mView : View?, mKey : String)
    fun onDialogCancelAction(mView : View?, mKey : String)
}
