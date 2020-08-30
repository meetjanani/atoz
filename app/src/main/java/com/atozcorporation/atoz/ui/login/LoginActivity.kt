package com.atozcorporation.atoz.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.atozcorporation.atoz.MainActivity
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.base.SharedPref
import com.atozcorporation.atoz.ui.dashboard.DashboardActivity
import com.growinginfotech.businesshub.base.defaultToast
import com.growinginfotech.businesshub.base.navigateToAndFinish
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    private lateinit var viewModel: LoginViewModel

    fun observeState(viewModel: LoginViewModel) {
        viewModel.loginAPIState.observe(this, Observer {
            when (it) {
                is LoginViewModel.LoginAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is LoginViewModel.LoginAPIState.SuccessLogin -> {
                    it.data.let { response ->
                        if (response.status == 1) {
                            setSharedPreferenceloginUser(response.data, true)
                            navigateToAndFinish<DashboardActivity>()
                        } else {
                            "Please Provide Valid Credentials".defaultToast(this)
                        }
                    }
                    progressBar.visibility = View.GONE
                }
                is LoginViewModel.LoginAPIState.Failure -> {
                    Toast.makeText(this, it.throwable.message.toString(), Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setActivityContext(this)

        viewModel =
            ViewModelProviders.of(this).get(LoginViewModel::class.java)
        observeState(viewModel)

        loginBtn.setOnClickListener {
            viewModel.loginAPICall(user_email.text.toString(), user_password.text.toString())
        }

        if(getIs_Login()){
            navigateToAndFinish<DashboardActivity>()
        }
    }
}