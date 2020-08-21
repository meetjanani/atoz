package com.atozcorporation.atoz.ui.addoutlet

import android.Manifest
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import kotlinx.android.synthetic.main.activity_add_outlet.*
import java.util.*
import kotlin.collections.ArrayList


class AddOutletActivity : BaseActivity() {

    private lateinit var viewModel: AddOutletViewModel
    private var locationManager : LocationManager? = null
    private var currentLocation : Location? = null

    fun observeState(viewModel : AddOutletViewModel){
        viewModel.addOutletAPIState.observe(this, Observer {
            when(it){
                is  AddOutletViewModel.AddOutletAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is  AddOutletViewModel.AddOutletAPIState.Success -> {
                    it.data.let { response ->
                        Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                    }
                    progressBar.visibility = View.GONE
                }is  AddOutletViewModel.AddOutletAPIState.SuccessOutletCategory -> {
                    it.data.let { response ->

                        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(
                            this,
                            android.R.layout.simple_spinner_item,
                            ArrayList<String>().apply { response.data.map { category ->
                                add(category.name)
                            } } as List<String>
                        )
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        selectOutlet.setAdapter(aa)
                    }
                    progressBar.visibility = View.GONE
                }
                is  AddOutletViewModel.AddOutletAPIState.Failure -> {
                    Toast.makeText(this, it.throwable.message.toString(),Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_outlet)

        viewModel =
            ViewModelProviders.of(this).get(AddOutletViewModel::class.java)
        observeState(viewModel)
        buttonSubmitOutlet.setOnClickListener {
            viewModel.addOutletAPICall("`name`, `personName`, `contactNumber`, `address1`,`address2`, `pinCode`, `gst`,`latitude`,`longitude`,`categoryId`,`categoryName`,`batchId`, `userId`, `userName`",
                "'${editTextOutletName.text}', '${editTextPersonName.text}', '${editTextContactNumber.text}', '${editTextAddressPrimary.text}','${editTextAddressSecondary.text}', '${editTextPinCode.text}', '${editTextGst.text}','${currentLocation?.latitude.toString()}','${currentLocation?.longitude.toString()}', '${viewModel.outletCategoryList.value?.data?.get(selectOutlet.selectedItemPosition)?.id}','${viewModel.outletCategoryList.value?.data?.get(selectOutlet.selectedItemPosition)?.name}','${editTextBachNumber.text}','1', 'Meet'")
        }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        buttonGetCurrentLocation.setOnClickListener { view ->
                getLocation()
        }
    }

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
           //  ("" + location.longitude + ":" + location.latitude).defaultToast(this@AddOutletActivity)
            currentLocation = location
            getCompleteAddressString(location.latitude, location.longitude)
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
            return
        }
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> getLocation()
                PackageManager.PERMISSION_DENIED -> {}
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

    private fun getCompleteAddressString(
        LATITUDE: Double,
        LONGITUDE: Double
    ): String? {
        val strAdd = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address>? =
                geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.getMaxAddressLineIndex()) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i))
                }
                editTextAddressPrimary.setText(strReturnedAddress)
                editTextPinCode.setText(returnedAddress.postalCode)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Log.w("My Current loction address", "Canont get Address!")
        }
        return strAdd
    }
}