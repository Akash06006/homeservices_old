package com.example.services.database;

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.services.application.MyApplication
import com.example.services.constants.GlobalConstants
import com.example.services.sharedpreference.SharedPrefClass
import okhttp3.RequestBody
import java.io.File
import kotlin.collections.HashMap

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NetworkJobService : JobService() {
    private lateinit var locationsViewModel : LocationsViewModel

    override fun onStopJob(p0 : JobParameters?) : Boolean {
        return false
    }

    override fun onStartJob(p0 : JobParameters?) : Boolean {
        var currentSortDate = ""
        //upload data asynchronously
        if (GlobalConstants.JOB_STARTED != "true") {
            var upload = UploadDataToServer()
            upload.synchData("0", "offline")
        }

        return true
    }

}

