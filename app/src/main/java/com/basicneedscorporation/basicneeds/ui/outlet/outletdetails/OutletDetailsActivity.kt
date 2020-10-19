package com.basicneedscorporation.basicneeds.ui.outlet.outletdetails

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.base.BaseActivity
import com.basicneedscorporation.basicneeds.base.offlinedb.Order_Summery_Db_Helper
import com.basicneedscorporation.basicneeds.rest.response.login.LoginResponse
import com.basicneedscorporation.basicneeds.rest.response.outlet.OutletListResponse
import com.basicneedscorporation.basicneeds.ui.addoutlet.AddOutletActivity
import com.basicneedscorporation.basicneeds.ui.manageproduct.blockedbrandlist.BlockedBrandListActivity
import com.basicneedscorporation.basicneeds.ui.manageorder.pastorderheader.PastOrderHeaderActivity
import com.basicneedscorporation.basicneeds.ui.manageproduct.productcategorylist.ProductCategoryActivity
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
        btnBlockedBrand.setOnClickListener {
            navigateTo<BlockedBrandListActivity> {
                putExtra("batchId", outletDetails.batchId)
                putExtra("id", outletDetails.id)
            }
        }
        buttonPastOrder.setOnClickListener {
            navigateTo<PastOrderHeaderActivity> {
                putExtra("batchId", outletDetails.batchId)
            }
        }
        buttonPlaceOrder.setOnClickListener {
            if (orderFor?.id != outletDetails.id) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Are you want to place order for ${outletDetails.batchId} ?")
                builder.setMessage("cart items for other outlet ${orderFor?.batchId} will be deleted ! Are you sure ?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                builder.setPositiveButton("Yes") { dialogInterface, which ->
                    Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG).show()
                    val dbHelper = Order_Summery_Db_Helper(this, null)
                    dbHelper.deleteAll()
                    val orderForUser = LoginResponse.UserDetails()
                    orderForUser.id = outletDetails.id
                    orderForUser.batchId = outletDetails.batchId
                    orderForUser.name = outletDetails.name
                    orderForUser.personName = outletDetails.personName
                    orderForUser.contactNumber = outletDetails.contactNumber
                    setOrderForUser(orderForUser)
                    navigateTo<ProductCategoryActivity> { }
                }
                builder.setNegativeButton("No") { dialogInterface, which ->
                    //
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            } else {
                navigateTo<ProductCategoryActivity> { }
            }
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