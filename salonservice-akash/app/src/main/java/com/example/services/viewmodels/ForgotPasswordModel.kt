package  com.example.services.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.view.View
import com.example.services.common.UtilsFunctions
import com.example.services.model.CommonModel
import com.example.services.repositories.ForgotPasswordRepository
import com.example.services.sharedpreference.SharedPrefClass
import com.google.gson.JsonObject

class ForgotPasswordModel : BaseViewModel() {
    private var data : MutableLiveData<CommonModel>? = null
    private var forgotPasswordRepository : ForgotPasswordRepository? = null
    private var sharedPrefClass : SharedPrefClass? = null
    private val mIsUpdating = MutableLiveData<Boolean>()
    private val postClick = MutableLiveData<String>()
    private val cancel = MutableLiveData<Boolean>()

    init {
        forgotPasswordRepository = ForgotPasswordRepository()
        sharedPrefClass = SharedPrefClass()
        data = forgotPasswordRepository!!.getForgotPasswordResponse("","")

    }

    fun getForgotPasswordResponse() : LiveData<CommonModel> {
        return data!!
    }

    override fun isLoading() : LiveData<Boolean> {
        return mIsUpdating
    }

    override fun isClick() : LiveData<String> {
        return postClick
    }

    override fun clickListener(v : View) {

        postClick.value=v.resources.getResourceName(v.id).split("/")[1]

    }

    fun forgotApi(mobileNumber: String, strCountryCode: String) {
        val mJsonObject = JsonObject()
        mJsonObject.addProperty("mobileNumber", mobileNumber)
        if (UtilsFunctions.isNetworkConnected()) {
            data = forgotPasswordRepository!!.getForgotPasswordResponse(mobileNumber,strCountryCode)
            mIsUpdating.postValue(true)
        }
    }


}