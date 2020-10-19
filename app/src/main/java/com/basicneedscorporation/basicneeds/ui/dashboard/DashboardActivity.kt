package com.basicneedscorporation.basicneeds.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.basicneedscorporation.basicneeds.BuildConfig
import com.basicneedscorporation.basicneeds.MainActivity
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.base.BaseActivity
import com.basicneedscorporation.basicneeds.ui.addoutlet.AddOutletActivity
import com.basicneedscorporation.basicneeds.ui.login.LoginActivity
import com.basicneedscorporation.basicneeds.ui.manageorder.cart.CartActivity
import com.basicneedscorporation.basicneeds.ui.manageorder.pastorderheader.PastOrderHeaderActivity
import com.basicneedscorporation.basicneeds.ui.manageproduct.productcategorylist.ProductCategoryActivity
import com.basicneedscorporation.basicneeds.ui.report.OrderWiseProductQtyActivity
import com.growinginfotech.businesshub.base.defaultToast
import com.growinginfotech.businesshub.base.navigateTo
import com.growinginfotech.businesshub.base.navigateToAndFinish
import kotlinx.android.synthetic.main.activity_dashboard.*


class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setActivityContext(this)
        textViewPersonName.text = loginUser?.personName
        textViewShopName.text = loginUser?.name

        if(loginUser?.rollId == 4 ){ // brand Owner
            cardViewProductCatelog.visibility = View.VISIBLE
            cardViewMyCart.visibility = View.VISIBLE
            cardViewPastOrder.visibility = View.VISIBLE
            logout.visibility = View.VISIBLE
            cardViewOrderWiseProductQty.visibility = View.VISIBLE
        }
        if(loginUser?.rollId == 3 ){ // outlet user
            cardViewProductCatelog.visibility = View.VISIBLE
            cardViewMyCart.visibility = View.VISIBLE
            cardViewPastOrder.visibility = View.VISIBLE
            logout.visibility = View.VISIBLE
        }
        if(loginUser?.rollId == 2 ){ // salse man
            cardViewProductCatelog.visibility = View.VISIBLE
            cardViewMyCart.visibility = View.VISIBLE
            cardViewPastOrder.visibility = View.VISIBLE
            logout.visibility = View.VISIBLE
            cardViewOutlet.visibility = View.VISIBLE
        }
        if(loginUser?.rollId == 1 ){ // super admin
            cardViewProductCatelog.visibility = View.VISIBLE
            cardViewMyCart.visibility = View.VISIBLE
            cardViewPastOrder.visibility = View.VISIBLE
            logout.visibility = View.VISIBLE
            cardViewOutlet.visibility = View.VISIBLE
            cardViewAddSalseMan.visibility = View.VISIBLE
            cardViewAddBrandOwner.visibility = View.VISIBLE
            cardViewOrderWiseProductQty.visibility = View.VISIBLE
        }
        cardViewProductCatelog.setOnClickListener {
            if(loginUser?.rollId == 3){
                setOrderForUser(loginUser)
                navigateTo<ProductCategoryActivity> {  }
            } else {
                "Please login with as an  Outlet user for place order".defaultToast(this)
            }
        }
        cardViewMyCart.setOnClickListener {
            navigateTo<CartActivity> {  }
        }
        cardViewPastOrder.setOnClickListener {
            navigateTo<PastOrderHeaderActivity> {
                putExtra("batchId", loginUser?.batchId.toString())
            }
        }
        cardViewAddSalseMan.setOnClickListener {
            navigateTo<AddOutletActivity> {
                putExtra("outletUserRoll", "2")
            }
        }
        cardViewAddBrandOwner.setOnClickListener {
            navigateTo<AddOutletActivity> {
                putExtra("outletUserRoll", "4")
                putExtra("brandOwner", true)
            }
        }
        cardViewOrderWiseProductQty.setOnClickListener {
            navigateTo<OrderWiseProductQtyActivity> {
                putExtra("brandOwner", true)
            }
        }
        cardViewOutlet.setOnClickListener {
            navigateTo<MainActivity> {  }
        }
        logout.setOnClickListener {
            setSharedPreferenceloginUser(null, false)
            navigateToAndFinish<LoginActivity>()
        }
        cardViewShare.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
            )
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }
    }
}