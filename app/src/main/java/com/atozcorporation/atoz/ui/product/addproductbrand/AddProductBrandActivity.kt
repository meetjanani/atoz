package com.atozcorporation.atoz.ui.product.addproductbrand

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.ui.product.addproductcategory.AddProductCategoryViewModel
import kotlinx.android.synthetic.main.activity_add_product_category.*

class AddProductBrandActivity : BaseActivity() {

    private lateinit var viewModel: AddProductBrandViewModel

    fun observeState(viewModel : AddProductBrandViewModel){
        viewModel.addProductBrandAPIState.observe(this, Observer {
            when(it){
                is  AddProductBrandViewModel.AddProductBrandAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is  AddProductBrandViewModel.AddProductBrandAPIState.Success -> {
                    it.data.let { response ->
                        Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                    }
                    progressBar.visibility = View.GONE
                    finish()
                }
                is  AddProductBrandViewModel.AddProductBrandAPIState.Failure -> {
                    Toast.makeText(this, it.throwable.message.toString(), Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product_category)
        viewModel =
            ViewModelProviders.of(this).get(AddProductBrandViewModel::class.java)
        observeState(viewModel)


    }
}