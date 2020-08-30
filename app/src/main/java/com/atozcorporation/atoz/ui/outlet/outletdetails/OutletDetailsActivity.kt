package com.atozcorporation.atoz.ui.outlet.outletdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
        val outletDetails = intent.extras?.get("outletDetails") as OutletListResponse.Outlet

        btnEditOutletDetails.setOnClickListener {
            navigateTo<AddOutletActivity> {
                putExtra("isEditMode", true)
                putExtra(
                    "outletDetails",
                    outletDetails
                )
                putExtra("outletUserRoll", outletDetails.rollId)
            }
        }
        btnNavigationOutlet.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=${outletDetails.latitude},${outletDetails.longitude}")
            )
            startActivity(intent)
        }
    }
}