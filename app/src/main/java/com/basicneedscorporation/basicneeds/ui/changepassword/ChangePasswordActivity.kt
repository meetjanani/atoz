package com.basicneedscorporation.basicneeds.ui.changepassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.base.BaseActivity
import com.basicneedscorporation.basicneeds.ui.login.LoginActivity
import com.growinginfotech.businesshub.base.defaultToast
import com.growinginfotech.businesshub.base.navigateToAndFinish
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : BaseActivity() {

     private lateinit var viewModel: ChangePasswordViewModel

    fun observeState(viewModel: ChangePasswordViewModel) {
        viewModel.changePasswordAPIState.observe(this, Observer {
            when (it) {
                is ChangePasswordViewModel.ChangePasswordAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is ChangePasswordViewModel.ChangePasswordAPIState.Success -> {
                    it.data.let { response ->
                        Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                    }
                    progressBar.visibility = View.GONE
                    setSharedPreferenceloginUser(null, false)
//                    finish()
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    val newIntent = Intent(this, LoginActivity::class.java)
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(newIntent)
//                    navigateToAndFinish<LoginActivity>()
                }
                is ChangePasswordViewModel.ChangePasswordAPIState.Failure -> {
                    Toast.makeText(this, it.throwable.message.toString(), Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        setActivityContext(this)
        viewModel =
            ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java)
        observeState(viewModel)

        buttonChangePassword.setOnClickListener {
            if(loginUser?.password == editTextCurrentPassword.text.toString()){
                if(editTextNewPassword.text.toString() == editTextNewConfirmPassword.text.toString()){
                    viewModel.changePasswordAPICall(
                        "`password` = '${editTextNewConfirmPassword.text}'",
                        "`id`= ${loginUser?.id}"
                    )
                } else {
                    "Password dose not matched.".defaultToast(this)
                }
            } else {
                "Please Enter Correct Old Password.".defaultToast(this)
            }
        }
    }
}