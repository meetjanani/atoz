package com.atozcorporation.atoz.ui.dashboard

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import com.atozcorporation.atoz.MainActivity
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.ui.addoutlet.AddOutletActivity
import com.atozcorporation.atoz.ui.login.LoginActivity
import com.atozcorporation.atoz.ui.manageproduct.ProductCategoryActivity
import com.growinginfotech.businesshub.base.navigateTo
import com.growinginfotech.businesshub.base.navigateToAndFinish
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_product_category.*

class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setActivityContext(this)
        textViewPersonName.text = loginUser?.personName
        textViewShopName.text = loginUser?.name

        if(loginUser?.rollId == 3 ){
            cardViewProductCatelog.visibility = View.VISIBLE
            cardViewMyCart.visibility = View.VISIBLE
            cardViewPastOrder.visibility = View.VISIBLE
            logout.visibility = View.VISIBLE
        }
        if(loginUser?.rollId == 2 ){
            cardViewProductCatelog.visibility = View.VISIBLE
            cardViewMyCart.visibility = View.VISIBLE
            cardViewPastOrder.visibility = View.VISIBLE
            logout.visibility = View.VISIBLE
            cardViewOutlet.visibility = View.VISIBLE
        }
        if(loginUser?.rollId == 1 ){
            cardViewProductCatelog.visibility = View.VISIBLE
            cardViewMyCart.visibility = View.VISIBLE
            cardViewPastOrder.visibility = View.VISIBLE
            logout.visibility = View.VISIBLE
            cardViewOutlet.visibility = View.VISIBLE
            cardViewAddSalseMan.visibility = View.VISIBLE
        }
        cardViewProductCatelog.setOnClickListener {
            navigateTo<ProductCategoryActivity> {  }
        }
        cardViewAddSalseMan.setOnClickListener {
            navigateTo<AddOutletActivity> {
                putExtra("outletUserRoll", "2")
            }
        }
        cardViewOutlet.setOnClickListener {
            navigateTo<MainActivity> {  }
        }
        logout.setOnClickListener {
            setSharedPreferenceloginUser(null, false)
            navigateToAndFinish<LoginActivity>()
        }
    }
}