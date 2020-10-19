package com.basicneedscorporation.basicneeds.ui.manageproduct.addproduct

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.base.BaseActivity
import com.basicneedscorporation.basicneeds.base.askPermissionForUploadImage
import com.basicneedscorporation.basicneeds.base.checkPermissionForUploadImage
import com.growinginfotech.businesshub.base.REQUEST_GALLERY
import com.growinginfotech.businesshub.base.defaultToast
import com.growinginfotech.businesshub.base.imageFilePath
import com.growinginfotech.businesshub.base.loadImage
import com.mediacentric.app.extension.file.FileUtils
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_add_product_category.progressBar
import java.io.IOException

class AddProductsActivity : BaseActivity() {

    private lateinit var viewModel: AddProductsViewModel
    var isImageSelected = false
    var productCategoryId = 0
    var productCategoryName = ""
    var productBrandId = 0
    var productBrandName = ""

    fun observeState(viewModel : AddProductsViewModel){
        viewModel.addProductsAPIState.observe(this, Observer {
            when(it){
                is  AddProductsViewModel.AddProductsAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is  AddProductsViewModel.AddProductsAPIState.Success -> {
                    it.data.let { response ->
                        Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                    }
                    progressBar.visibility = View.GONE
                    finish()
                }
                is  AddProductsViewModel.AddProductsAPIState.Failure -> {
                    Toast.makeText(this, it.throwable.message.toString(), Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        viewModel =
            ViewModelProviders.of(this).get(AddProductsViewModel::class.java)
        observeState(viewModel)

        productCategoryId = intent.extras?.getInt("productCategoryId") ?: 0
        productCategoryName =  intent.extras?.getString("productCategoryName").toString()
        productBrandId = intent.extras?.getInt("productBrandId") ?: 0
        productBrandName =  intent.extras?.getString("productBrandName").toString()
        textViewCategory.text = productCategoryName
        textViewCategoryBrand.text = productBrandName
        imageViewProduct.setOnClickListener {
            checkForPermission()
        }

        buttonSubmitProduct.setOnClickListener {
            if(isImageSelected){
                viewModel.addProductsAPICall(editTextProductName.text.toString(), editTextPackSize.text.toString(), editTextMRP1.text.toString(), editTextMRP2.text.toString(),
                    editTextMinQty.text.toString(), productCategoryId, textViewCategory.text.toString(), productBrandId, textViewCategoryBrand.text.toString(),
                    editTextDescription.text.toString(), editTextProductCode.text.toString())
            } else{
                "Please Select Image".defaultToast(this)
            }
        }
    }

    // Code for upload Profile Picture
    private fun checkForPermission() {
        if (checkPermissionForUploadImage(this)) {
            choosePhotoFromGallary()
        } else {
            askPermissionForUploadImage(this)
        }
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, REQUEST_GALLERY)
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }
        if (requestCode == REQUEST_GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    contentURI?.let {
                        imageFilePath = FileUtils.getPath(this, it).toString()
                        isImageSelected = true
                        loadImage(imageFilePath, imageViewProduct,this)

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}