package com.example.services.views.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.lifecycle.ViewModelProviders
import com.example.services.R
import com.example.services.utils.BaseActivity
import com.example.services.databinding.ActivityWebViewBinding
import com.example.services.viewmodels.MyAccountsViewModel

import android.webkit.WebViewClient

class WebViewActivity : BaseActivity() {
    lateinit var binding: ActivityWebViewBinding
    private var accountsViewModel: MyAccountsViewModel? = null

    override fun initViews() {
        binding = viewDataBinding as ActivityWebViewBinding
        accountsViewModel = ViewModelProviders.of(this).get(MyAccountsViewModel::class.java)
        binding.myaccountsViewModel = accountsViewModel

        //binding.webView.webViewClient = MyWebViewClient(this)
        //binding.webView.loadUrl("https://www.javatpoint.com/")
        val title = intent.extras.get("title").toString()
        binding.commonToolBar.imgToolbarText.setText(title)
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        /*  if (title.equals(resources.getString(R.string.terms_and_conditions))) {
            binding.webView.loadUrl(GlobalConstants.TERMS_CONDITION)
        } else if (title.equals(resources.getString(R.string.privacy_policy))) {
            binding.webView.loadUrl(GlobalConstants.PRIVACY_POLICY)
        } else {
            binding.webView.loadUrl(GlobalConstants.ABOUT_US)
        }*/
        binding.webView.loadUrl("https://www.cerebruminfotech.com/")

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_web_view
    }

}
