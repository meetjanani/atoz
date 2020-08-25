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
import com.atozcorporation.atoz.rest.response.outlet.OutletListResponse
import com.growinginfotech.businesshub.base.defaultToast
import kotlinx.android.synthetic.main.activity_add_outlet.*
import java.util.*
import kotlin.collections.ArrayList


class AddOutletActivity : BaseActivity() {

    private lateinit var viewModel: AddOutletViewModel
    private var locationManager : LocationManager? = null
    private var currentLocation : Location? = null

    fun observeState(viewModel : AddOutletViewModel){
        viewModel.outletCategoryList.observe(this, Observer {
            it.data.let { response ->
                val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                    this,
                    android.R.layout.simple_spinner_item,
                    ArrayList<String>().apply { response.map { category ->
                        add(category.name)
                    } } as List<String>
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                selectOutlet.setAdapter(arrayAdapter)
                if(viewModel.isEditOutlet.value ?: false){
                    selectOutlet.setSelection(viewModel.outletCategoryList.value?.data?.indexOf(viewModel.outletCategoryList.value?.data?.singleOrNull { it.id == viewModel.outletDetails.value?.categoryId })?: 0)
                }
                progressBar.visibility = View.GONE
            }
        })
        viewModel.outletCityList.observe(this, Observer {
            it.data.let { response ->
                val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                    this,
                    android.R.layout.simple_spinner_item,
                    ArrayList<String>().apply { response.map { city ->
                        add(city.name)
                    } } as List<String>
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                selectCity.setAdapter(arrayAdapter)
                if(viewModel.isEditOutlet.value ?: false){
                    selectCity.setSelection(viewModel.outletCityList.value?.data?.indexOf(viewModel.outletCityList.value?.data?.singleOrNull { it.id == viewModel.outletDetails.value?.cityId })?: 0)
                }
                progressBar.visibility = View.GONE
            }
        })
        viewModel.outletAreaList.observe(this, Observer {
            it.data.let { response ->
                val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                    this,
                    android.R.layout.simple_spinner_item,
                    ArrayList<String>().apply { response.map { area ->
                        add(area.name)
                    } } as List<String>
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                selectArea.setAdapter(arrayAdapter)
                if(viewModel.isEditOutlet.value ?: false){
                    selectArea.setSelection(viewModel.outletAreaList.value?.data?.indexOf(viewModel.outletAreaList.value?.data?.singleOrNull { it.id == viewModel.outletDetails.value?.areaId })?: 0)
                }
                progressBar.visibility = View.GONE
            }
        })
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
                    finish()
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

        viewModel.isEditOutlet.value = intent.extras?.getBoolean("isEditMode") ?: false
        if(viewModel.isEditOutlet.value ?: false){
            viewModel.outletDetails.value =  intent.extras?.get("outletDetails") as OutletListResponse.Outlet
            editTextOutletName.setText(viewModel.outletDetails.value?.name)
            editTextPersonName.setText(viewModel.outletDetails.value?.personName)
            editTextContactNumber.setText(viewModel.outletDetails.value?.contactNumber)
            editTextAddressPrimary.setText(viewModel.outletDetails.value?.address1)
            editTextAddressSecondary.setText(viewModel.outletDetails.value?.address2)
            editTextPinCode.setText(viewModel.outletDetails.value?.pinCode)
            editTextGst.setText(viewModel.outletDetails.value?.gst)
            editTextBachNumber.setText(viewModel.outletDetails.value?.batchId)
            currentLocation?.latitude = viewModel.outletDetails.value?.latitude?.toDouble() ?: 0.0
            currentLocation?.longitude = viewModel.outletDetails.value?.longitude?.toDouble() ?: 0.0
        }


        buttonSubmitOutlet.setOnClickListener {
            if(viewModel.isEditOutlet.value ?: false){
                // `batchId` = 'pS111'
                // `id`= viewModel.outletDetails.value?.id
                viewModel.updateOutletAPICall( "`name` = '${editTextOutletName.text}', " +
                            "`personName`    = '${editTextPersonName.text}'," +
                            "`contactNumber` = '${editTextContactNumber.text}', " +
                            "`address1` = '${editTextAddressPrimary.text}', `address2` = '${editTextAddressSecondary.text}', `pinCode` = '${editTextPinCode.text}', " +
                            "`gst` = '${editTextGst.text}', `latitude` = '${currentLocation?.latitude.toString()}', `longitude` = '${currentLocation?.longitude.toString()}', " +
                            "`categoryId` = '${viewModel.outletCategoryList.value?.data?.get(selectOutlet.selectedItemPosition)?.id}', `categoryName` = '${viewModel.outletCategoryList.value?.data?.get(selectOutlet.selectedItemPosition)?.name}'," +
                            "`cityId` = '${viewModel.outletCityList.value?.data?.get(selectCity.selectedItemPosition)?.id}', `cityName` = '${viewModel.outletCityList.value?.data?.get(selectCity.selectedItemPosition)?.name}'," +
                            "`areaId` = '${viewModel.outletAreaList.value?.data?.get(selectArea.selectedItemPosition)?.id}', `areaName` = '${viewModel.outletAreaList.value?.data?.get(selectArea.selectedItemPosition)?.name}'," +
                            "`batchId` = '${editTextBachNumber.text}'",
                "`id`= ${viewModel.outletDetails.value?.id}")
            } else {
                viewModel.addOutletAPICall("`name`, `personName`, `contactNumber`, `address1`,`address2`, `pinCode`, `gst`,`latitude`,`longitude`,`categoryId`,`categoryName`,`cityId`,`cityName`,`areaId`,`areaName`,`batchId`, `userId`, `userName`, `password`",
                    "'${editTextOutletName.text}', '${editTextPersonName.text}', '${editTextContactNumber.text}', '${editTextAddressPrimary.text}','${editTextAddressSecondary.text}', '${editTextPinCode.text}', '${editTextGst.text}','${currentLocation?.latitude.toString()}','${currentLocation?.longitude.toString()}', " +
                            "'${viewModel.outletCategoryList.value?.data?.get(selectOutlet.selectedItemPosition)?.id}','${viewModel.outletCategoryList.value?.data?.get(selectOutlet.selectedItemPosition)?.name}'," +
                            "'${viewModel.outletCityList.value?.data?.get(selectCity.selectedItemPosition)?.id}','${viewModel.outletCityList.value?.data?.get(selectCity.selectedItemPosition)?.name}'," +
                            "'${viewModel.outletAreaList.value?.data?.get(selectArea.selectedItemPosition)?.id}','${viewModel.outletAreaList.value?.data?.get(selectArea.selectedItemPosition)?.name}'," +
                            "'${editTextBachNumber.text}','1', 'Meet', '${editTextBachNumber.text}'")
            }
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