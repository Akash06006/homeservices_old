package  com.example.services.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.view.View
import com.example.services.common.UtilsFunctions
import com.example.services.model.CommonModel
import com.example.services.repositories.ResetPasswordRepository
import com.google.gson.JsonObject

class ResetPasswordModel : BaseViewModel() {
    private var data : MutableLiveData<CommonModel>? = null
    private var resetPasswordRepository = ResetPasswordRepository()
    private val mIsUpdating = MutableLiveData<Boolean>()
    private val postClick = MutableLiveData<String>()

    init {
        data = resetPasswordRepository.getResetPasswordResponse(null)

    }

    override fun isLoading() : LiveData<Boolean> {
        return mIsUpdating
    }

    override fun isClick() : LiveData<String> {
        return postClick
    }

    override fun clickListener(v : View) {
        postClick.value = v.resources.getResourceName(v.id).split("/")[1]
    }

    fun getResetPasswordResponse() : LiveData<CommonModel> {
        return data!!
    }

    fun callResetPassword(mJsonObject : JsonObject) {
        if (UtilsFunctions.isNetworkConnected()) {
            data = resetPasswordRepository.getResetPasswordResponse(mJsonObject)
            mIsUpdating.postValue(true)

        }
    }

    val loading : LiveData<Boolean>
        get() = mIsUpdating

}