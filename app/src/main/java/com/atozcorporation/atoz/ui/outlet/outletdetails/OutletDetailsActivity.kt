package com.atozcorporation.atoz.ui.outlet.outletdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.base.offlinedb.Order_Summery_Db_Helper
import com.atozcorporation.atoz.rest.response.login.LoginResponse
import com.atozcorporation.atoz.rest.response.outlet.OutletListResponse
import com.atozcorporation.atoz.ui.addoutlet.AddOutletActivity
import com.atozcorporation.atoz.ui.manageproduct.productcategorylist.ProductCategoryActivity
import com.growinginfotech.businesshub.base.navigateTo
import kotlinx.android.synthetic.main.activity_outlet_details.*


class OutletDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outlet_details)
        setActivityContext(this)
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
        buttonPlaceOrder.setOnClickListener {
           val dbHelper = Order_Summery_Db_Helper(this, null);
            dbHelper.deleteAll()
            val orderForUser = LoginResponse.UserDetails()
            orderForUser.id = outletDetails.id
            orderForUser.batchId = outletDetails.batchId
            orderForUser.name = outletDetails.name
            orderForUser.personName = outletDetails.personName
            orderForUser.contactNumber = outletDetails.contactNumber
            setOrderForUser(orderForUser)
            navigateTo<ProductCategoryActivity> {  }
        }

        textViewBusinessName.text = outletDetails.name
        textViewContactPersonName.text = outletDetails.personName
        textViewContactPersonNumber.text = outletDetails.contactNumber
        textViewOutletId.text = "${outletDetails.batchId} | ${outletDetails.password}"
        textViewGSTNumber.text = outletDetails.gst
        city.text = outletDetails.cityName
        area.text = outletDetails.areaName
        textViewAddress.text = "${outletDetails.address1} \n ${outletDetails.address2}"
        pincode.text = outletDetails.pinCode
    }
}