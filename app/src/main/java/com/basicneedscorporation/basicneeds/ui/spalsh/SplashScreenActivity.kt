package com.basicneedscorporation.basicneeds.ui.spalsh

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.base.BaseActivity
import com.basicneedscorporation.basicneeds.ui.dashboard.DashboardActivity
import com.basicneedscorporation.basicneeds.ui.login.LoginActivity
import com.growinginfotech.businesshub.base.navigateToAndFinish
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setActivityContext(this)
//Set animation to elements
        lifecycleScope.launch {
            delay(3000)
            if(getIs_Login()){
                navigateToAndFinish<DashboardActivity>()
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