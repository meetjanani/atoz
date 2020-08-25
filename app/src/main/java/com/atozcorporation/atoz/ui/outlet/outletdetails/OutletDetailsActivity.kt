package com.atozcorporation.atoz.ui.outlet.outletdetails

import android.os.Bundle
import android.os.PersistableBundle
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.rest.response.outlet.OutletListResponse
import com.atozcorporation.atoz.ui.addoutlet.AddOutletActivity
import com.growinginfotech.businesshub.base.navigateTo
import kotlinx.android.synthetic.main.activity_outlet_details.*

class OutletDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outlet_details)

        btnEditOutletDetails.setOnClickListener {
            navigateTo<AddOutletActivity> {
                putExtra("isEditMode",true)
                putExtra("outletDetails",intent.extras?.get("outletDetails") as OutletListResponse.Outlet)
            }
        }
    }
}