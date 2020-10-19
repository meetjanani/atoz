package com.basicneedscorporation.basicneeds.ui.manageproduct.productlist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.base.BaseActivity
import com.basicneedscorporation.basicneeds.rest.response.login.LoginResponse
import com.basicneedscorporation.basicneeds.rest.response.product.ProductListResponse
import com.basicneedscorporation.basicneeds.ui.manageproduct.addproduct.AddProductsActivity
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.navigateTo
import kotlinx.android.synthetic.main.activity_outlet.progressBar
import kotlinx.android.synthetic.main.activity_product_list.buttonAddProducts
import kotlinx.android.synthetic.main.activity_product_list.recyclerViewProductsList
import kotlinx.android.synthetic.main.activity_product_list.textViewProductBrandName

class ProductListActivity : BaseActivity(), IAdapterOnClick {

    private lateinit var viewModel: ProductListViewModel
    protected var adapter = ProductListAdapter(this)
    var productCategoryId = 0
    var productCategoryName = ""
    var productBrandId = 0
    var productBrandName = ""
    val orderForUserDetails = LoginResponse.UserDetails()

    fun observeState(viewModel: ProductListViewModel) {
        viewModel.productListAPIState.observe(this, Observer {
            when (it) {
                is ProductListViewModel.ProductListAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is ProductListViewModel.ProductListAPIState.Success -> {
                    it.data.data?.let { response ->
                        adapter.ProductListAdapter(this, response, loginUser?.rollId.toString())
                        recyclerViewProductsList.adapter = adapter
                    }
                    progressBar.visibility = View.GONE
                }
                is ProductListViewModel.ProductListAPIState.SuccessDeleteProduct -> {
//                    viewModel.getProductListAPICall(productCategoryId, productBrandId)
                    finish()
                    progressBar.visibility = View.GONE
                }
                is ProductListViewModel.ProductListAPIState.Failure -> {
                    Toast.makeText(
                        this,
                        it.throwable.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        setActivityContext(this)
        viewModel =
            ViewModelProviders.of(this).get(ProductListViewModel::class.java)
        observeState(viewModel)
        productCategoryId = intent.extras?.getInt("productCategoryId") ?: 0
        productCategoryName =  intent.extras?.getString("productCategoryName").toString()
        productBrandId = intent.extras?.getInt("productBrandId") ?: 0
        productBrandName =  intent.extras?.getString("productBrandName").toString()
        textViewProductBrandName.text =  productBrandName
        viewModel.getProductListAPICall(productCategoryId, productBrandId)

        if (loginUser?.rollId != 3) {
            buttonAddProducts.visibility = View.VISIBLE
        }
        buttonAddProducts.setOnClickListener {
            activity?.navigateTo<AddProductsActivity> {
                putExtra("productCategoryId", productCategoryId)
                putExtra("productCategoryName", productCategoryName)
                putExtra("productBrandId", productBrandId)
                putExtra("productBrandName", productBrandName)
            }
        }
    }

    override fun onClick(item: Any, position: Int) {
        if(item is Int){
            // item.toString().defaultToast(this)
        }
        if (item is ProductListResponse.ProductDetails){
            viewModel.deleteProductAPICall(item.ID)
        }
    }
}