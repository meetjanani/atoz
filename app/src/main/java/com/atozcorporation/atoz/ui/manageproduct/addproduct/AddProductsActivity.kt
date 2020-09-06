package com.atozcorporation.atoz.ui.manageproduct.addproduct

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.base.askPermissionForUploadImage
import com.atozcorporation.atoz.base.checkPermissionForUploadImage
import com.growinginfotech.businesshub.base.REQUEST_GALLERY
import com.growinginfotech.businesshub.base.defaultToast
import com.growinginfotech.businesshub.base.imageFilePath
import com.growinginfotech.businesshub.base.loadImage
import com.mediacentric.app.extension.file.FileUtils
import kotlinx.android.synthetic.main.activity_add_product_brand.*
import kotlinx.android.synthetic.main.activity_add_product_category.progressBar
import java.io.IOException

class AddProductsActivity : BaseActivity() {

    private lateinit var viewModel: AddProductsViewModel
    var isImageSelected = false
    var productCategoryId = 0
    var productCategoryName = ""

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
        setContentView(R.layout.activity_add_product_brand)
        viewModel =
            ViewModelProviders.of(this).get(AddProductsViewModel::class.java)
        observeState(viewModel)

        productCategoryId = intent.extras?.getInt("productCategoryId") ?: 0
        productCategoryName =  intent.extras?.getString("productCategoryName").toString()
        textViewProductCategoryName.text = productCategoryName
        imageViewProductBrand.setOnClickListener {
            checkForPermission()
        }

        buttonSubmitProductBrand.setOnClickListener {
            if(isImageSelected){
                viewModel.addProductsAPICall(editTextProductBrandName.text.toString(),productCategoryId, productCategoryName)
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
                        loadImage(imageFilePath, imageViewProductBrand,this)

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}