package com.atozcorporation.atoz.ui.spalsh

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.atozcorporation.atoz.MainActivity
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.base.SharedPref
import com.atozcorporation.atoz.ui.login.LoginActivity
import com.growinginfotech.businesshub.base.navigateToAndFinish
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//Set animation to elements
        lifecycleScope.launch {
            delay(3000)
            if(SharedPref().getIs_Login(this@SplashScreenActivity)){
                navigateToAndFinish<MainActivity>()
            }
            else{
                navigateToAndFinish<LoginActivity>()
            }
        }
        imageView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_animation));
        textView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_animation));
        textView2.setAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_animation));
    }
}