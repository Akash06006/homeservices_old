package com.example.services.views.settings

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.services.R
import com.example.services.databinding.ActivityMyAccountsBinding
import com.example.services.utils.BaseActivity
import com.example.services.viewmodels.MyAccountsViewModel

class MyAccountsActivity : BaseActivity() {
    lateinit var binding: ActivityMyAccountsBinding
    private var accountsViewModel: MyAccountsViewModel? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_my_accounts
    }

    override fun initViews() {
        binding = viewDataBinding as ActivityMyAccountsBinding
        accountsViewModel = ViewModelProviders.of(this).get(MyAccountsViewModel::class.java)
        binding.myaccountsViewModel = accountsViewModel
        binding.toolbarCommon.imgToolbarText.text = getString(R.string.settings)
        accountsViewModel!!.isClick().observe(
            this, Observer<String>(function =
            fun(it: String?) {
                when (it) {
                    "tv_terms" -> {
                        val intent1 = Intent(this, WebViewActivity::class.java)
                        intent1.putExtra("title", getString(R.string.terms_condition))
                        startActivity(intent1)
                        // binding.tvChangePassword.isEnabled = false
                    }
                    "tv_privacy" -> {
                        val intent1 = Intent(this, WebViewActivity::class.java)
                        intent1.putExtra("title", getString(R.string.privacy_policy))
                        startActivity(intent1)
                        /*  val intent1 = Intent(this, ChangePasswrodActivity::class.java)
                          startActivity(intent1)*/
                        // binding.tvChangePassword.isEnabled = false
                    }
                    "tv_faq" -> {
                      /*  val intent1 = Intent(this, FAQListActivity::class.java)
                        startActivity(intent1)*/
                        val intent1 = Intent(this, WebViewActivity::class.java)
                        intent1.putExtra("title", getString(R.string.faq))
                        startActivity(intent1)
                        // binding.tvChangePassword.isEnabled = false
                    }
                    "tv_feedback" -> {
                        /*  val intent1 = Intent(this, ChangePasswrodActivity::class.java)
                          startActivity(intent1)*/
                        // binding.tvChangePassword.isEnabled = false
                        showToastSuccess("Coming Soon")
                    }
                    "tv_contact_us" -> {
                        /*  val intent1 = Intent(this, ChangePasswrodActivity::class.java)
                          startActivity(intent1)*/
                        showToastSuccess("Coming Soon")
                        // binding.tvChangePassword.isEnabled = false
                    }
                }

            })
        )

    }

}
