package com.atozcorporation.atoz.ui.addoutlet

import android.Manifest
import android.app.DatePickerDialog
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
    private var locationManager: LocationManager? = null
    private var currentLocation: Location? = null
    private var picker: DatePickerDialog? = null
    var isoutletSinceSelected = false
    fun observeState(viewModel: AddOutletViewModel) {
        viewModel.outletCategoryList.observe(this, Observer {
            it.data.let { response ->
                val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                    this,
                    android.R.layout.simple_spinner_item,
                    ArrayList<String>().apply {
                        response.map { category ->
                            add(category.name)
                        }
                    } as List<String>
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                selectOutlet.adapter = arrayAdapter
                if (viewModel.isEditOutlet.value == true) {
                    selectOutlet.setSelection(
                        viewModel.outletCategoryList.value?.data?.indexOf(
                            viewModel.outletCategoryList.value?.data?.singleOrNull { it.id == viewModel.outletDetails.value?.categoryId })
                            ?: 0
                    )
                }
                progressBar.visibility = View.GONE
            }
        })
        viewModel.outletCityList.observe(this, Observer {
            it.data.let { response ->
                val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                    this,
                    android.R.layout.simple_spinner_item,
                    ArrayList<String>().apply {
                        response.map { city ->
                            add(city.name)
                        }
                    } as List<String>
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                selectCity.adapter = arrayAdapter
                if (viewModel.isEditOutlet.value == true) {
                    selectCity.setSelection(
                        viewModel.outletCityList.value?.data?.indexOf(viewModel.outletCityList.value?.data?.singleOrNull { it.id == viewModel.outletDetails.value?.cityId })
                            ?: 0
                    )
                }
                progressBar.visibility = View.GONE
            }
        })
        viewModel.outletAreaList.observe(this, Observer {
            it.data.let { response ->
                val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                    this,
                    android.R.layout.simple_spinner_item,
                    ArrayList<String>().apply {
                        response.map { area ->
                            add(area.name)
                        }
                    } as List<String>
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                selectArea.adapter = arrayAdapter
                if (viewModel.isEditOutlet.value == true) {
                    selectArea.setSelection(
                        viewModel.outletAreaList.value?.data?.indexOf(viewModel.outletAreaList.value?.data?.singleOrNull { it.id == viewModel.outletDetails.value?.areaId })
                            ?: 0
                    )
                }
                progressBar.visibility = View.GONE
            }
        })
        viewModel.outletOnList.observe(this, Observer {
            it.data.let { response ->
                val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                    this,
                    android.R.layout.simple_spinner_item,
                    ArrayList<String>().apply {
                        response.map { area ->
                            add(area.name)
                        }
                    } as List<String>
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                selectOutletOn.adapter = arrayAdapter
                if (viewModel.isEditOutlet.value == true) {
                    selectOutletOn.setSelection(
                        viewModel.outletOnList.value?.data?.indexOf(
                            viewModel.outletOnList.value?.data?.singleOrNull { it.id == viewModel.outletDetails.value?.outletOnId })
                            ?: 0
                    )
                }
                progressBar.visibility = View.GONE
            }
        })
        viewModel.addOutletAPIState.observe(this, Observer {
            when (it) {
                is AddOutletViewModel.AddOutletAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is AddOutletViewModel.AddOutletAPIState.Success -> {
                    it.data.let { response ->
                        Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                    }
                    progressBar.visibility = View.GONE
                    finish()
                }
                is AddOutletViewModel.AddOutletAPIState.SuccessVerifyOutlet -> {
                    it.data.let { response ->
                        if(response){
                            "Already Outlet is register with !!".defaultToast(this)
                        }
                        else {
                            insertOutletAPICall()
                            finish()
                        }
                    }
                    progressBar.visibility = View.GONE
                }
                is AddOutletViewModel.AddOutletAPIState.Failure -> {
                    Toast.makeText(this, it.throwable.message.toString(), Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_outlet)
        setActivityContext(this)
        loginUser?.personName?.defaultToast(this)
        viewModel =
            ViewModelProviders.of(this).get(AddOutletViewModel::class.java)
        observeState(viewModel)

        viewModel.isEditOutlet.value = intent.extras?.getBoolean("isEditMode") ?: false
        viewModel.outletUserRoll.value = loginUser?.rollId.toString() ?: "3"
        viewModel.getOutletCategoryAPICall()
        if (viewModel.isEditOutlet.value == true) {
            viewModel.outletDetails.value =
                intent.extras?.get("outletDetails") as OutletListResponse.Outlet
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
            textViewOutletSince.text = viewModel.outletDetails.value?.outletSince
            editTextAadharCard.setText(viewModel.outletDetails.value?.aadharCard)
            editTextPanCard.setText(viewModel.outletDetails.value?.panCard)
            editTextOtherDetail.setText(viewModel.outletDetails.value?.other1)
            isoutletSinceSelected = true

            // will again block below for update
//            editTextBachNumber.isEnabled = false
//            editTextContactNumber.isEnabled = false
        }


        buttonSubmitOutlet.setOnClickListener {
            if (isoutletSinceSelected == true) {
                if (viewModel.isEditOutlet.value == true) {
                    updateOutletAPICall()
                }
                else {
                    viewModel.verifyOutletAPICall(editTextBachNumber.text.toString(),editTextContactNumber.text.toString())
                }
            } else {
                "Plese Select Outlet Since Date".defaultToast(this)
            }
        }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        buttonGetCurrentLocation.setOnClickListener { view ->
            getLocation()
        }
        textViewOutletSince.setOnClickListener { view ->
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val year = cldr[Calendar.YEAR]
            // date picker dialog

            // date picker dialog
            picker = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth -> textViewOutletSince.text = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString() },
                year,
                month,
                day
            )
            picker!!.show()
            textViewOutletSince.text = year.toString() + "-" + (month + 1) + "-" + day.toString()
            isoutletSinceSelected = true
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
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )
            return
        }
        locationManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0L,
            0f,
            locationListener
        ) ?: "Canont get Address!".defaultToast(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> getLocation()
                PackageManager.PERMISSION_DENIED -> {
                    "Please Enable Permission".defaultToast(this)
                }
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
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i))
                }
                editTextAddressPrimary.setText(strReturnedAddress)
                editTextPinCode.setText(returnedAddress.postalCode)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Canont get Address!".defaultToast(this)
        }
        return strAdd
    }

    fun updateOutletAPICall(){
        viewModel.updateOutletAPICall(
            "`name` = '${editTextOutletName.text}', " +
                    "`personName`    = '${editTextPersonName.text}'," +
                    "`contactNumber` = '${editTextContactNumber.text}', " +
                    "`address1` = '${editTextAddressPrimary.text}', `address2` = '${editTextAddressSecondary.text}', `pinCode` = '${editTextPinCode.text}', " +
                    "`gst` = '${editTextGst.text}', `latitude` = '${currentLocation?.latitude.toString()}', `longitude` = '${currentLocation?.longitude.toString()}', " +
                    "`categoryId` = '${
                        viewModel.outletCategoryList.value?.data?.get(
                            selectOutlet.selectedItemPosition
                        )?.id
                    }', `categoryName` = '${
                        viewModel.outletCategoryList.value?.data?.get(
                            selectOutlet.selectedItemPosition
                        )?.name
                    }'," +
                    "`cityId` = '${viewModel.outletCityList.value?.data?.get(selectCity.selectedItemPosition)?.id}', `cityName` = '${
                        viewModel.outletCityList.value?.data?.get(
                            selectCity.selectedItemPosition
                        )?.name
                    }'," +
                    "`areaId` = '${viewModel.outletAreaList.value?.data?.get(selectArea.selectedItemPosition)?.id}', `areaName` = '${
                        viewModel.outletAreaList.value?.data?.get(
                            selectArea.selectedItemPosition
                        )?.name
                    }'," +
                    "`outletOnId` = '${
                        viewModel.outletOnList.value?.data?.get(
                            selectOutletOn.selectedItemPosition
                        )?.id
                    }', `outletOnName` = '${
                        viewModel.outletOnList.value?.data?.get(
                            selectOutletOn.selectedItemPosition
                        )?.name
                    }'," +
                    "`batchId` = '${editTextBachNumber.text}'," +
                    "`password` = '${editTextBachNumber.text}'," +
                    "`outletSince` = '${textViewOutletSince.text}'," +
            "`aadharCard` = '${editTextAadharCard.text}'," +
            "`panCard` = '${editTextPanCard.text}'," +
            "`other1` = '${editTextOtherDetail.text}'",
            "`id`= ${viewModel.outletDetails.value?.id}"
        )
    }

    fun insertOutletAPICall(){
            viewModel.addOutletAPICall(
                "`name`, `personName`, `contactNumber`, `address1`,`address2`, `pinCode`, `gst`,`latitude`,`longitude`,`categoryId`,`categoryName`,`cityId`,`cityName`,`areaId`,`areaName`,`batchId`, `userId`, `userName`, `password`,`rollId`, `outletOnId`,`outletOnName`, `outletSince`,`aadharCard`,`panCard`,`other1`",
                "'${editTextOutletName.text}', '${editTextPersonName.text}', '${editTextContactNumber.text}', '${editTextAddressPrimary.text}','${editTextAddressSecondary.text}', '${editTextPinCode.text}', '${editTextGst.text}','${currentLocation?.latitude.toString()}','${currentLocation?.longitude.toString()}', " +
                        "'${viewModel.outletCategoryList.value?.data?.get(selectOutlet.selectedItemPosition)?.id}','${
                            viewModel.outletCategoryList.value?.data?.get(
                                selectOutlet.selectedItemPosition
                            )?.name
                        }'," +
                        "'${viewModel.outletCityList.value?.data?.get(selectCity.selectedItemPosition)?.id}','${
                            viewModel.outletCityList.value?.data?.get(
                                selectCity.selectedItemPosition
                            )?.name
                        }'," +
                        "'${viewModel.outletAreaList.value?.data?.get(selectArea.selectedItemPosition)?.id}','${
                            viewModel.outletAreaList.value?.data?.get(
                                selectArea.selectedItemPosition
                            )?.name
                        }'," +
                        "'${editTextBachNumber.text}'," +
                        "'${loginUser?.id}'," +
                        " '${loginUser?.personName}', " +
                        "'${editTextBachNumber.text}'," +
                        "'${viewModel.outletUserRoll.value}'," + // UserLevel Roll Id = 3
                        "'${viewModel.outletOnList.value?.data?.get(selectOutletOn.selectedItemPosition)?.id}','${
                            viewModel.outletOnList.value?.data?.get(
                                selectOutletOn.selectedItemPosition
                            )?.name
                        }'," +
                        "'${textViewOutletSince.text}'," +
                        "'${editTextAadharCard.text}'," +
                        "'${editTextPanCard.text}'," +
                        "'${editTextOtherDetail.text}'"
            )
    }
}