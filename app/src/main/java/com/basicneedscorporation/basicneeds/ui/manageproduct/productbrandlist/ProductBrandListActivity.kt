package com.basicneedscorporation.basicneeds.ui.manageproduct.productbrandlist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.base.BaseActivity
import com.basicneedscorporation.basicneeds.rest.response.product.ProductBrandListResponse
import com.basicneedscorporation.basicneeds.ui.manageproduct.addproductbrand.AddProductBrandActivity
import com.basicneedscorporation.basicneeds.ui.manageproduct.productlist.ProductListActivity
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.navigateTo
import kotlinx.android.synthetic.main.activity_outlet.progressBar
import kotlinx.android.synthetic.main.activity_product_brand.*

class ProductBrandListActivity : BaseActivity(), IAdapterOnClick {

    private lateinit var viewModel: ProductBrandListViewModel
    protected var adapter = ProductBrandListAdapter(this)
    var productCategoryName = ""

    fun observeState(viewModel: ProductBrandListViewModel) {
        viewModel.productBrandAPIState.observe(this, Observer {
            when (it) {
                is ProductBrandListViewModel.ProductBrandAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is ProductBrandListViewModel.ProductBrandAPIState.Success -> {
                    it.data.let { response ->
                        adapter.ProductBrandListAdapter(this, response, "")
                        recyclerViewProductBrandList.adapter = adapter
                    }
                    progressBar.visibility = View.GONE
                }
                is ProductBrandListViewModel.ProductBrandAPIState.Failure -> {
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
        setContentView(R.layout.activity_product_brand)
        setActivityContext(this)
        viewModel =
            ViewModelProviders.of(this).get(ProductBrandListViewModel::class.java)
        observeState(viewModel)
        viewModel.productCategory.value = intent.extras?.getInt("productCategoryId") ?: 0
        productCategoryName =  intent.extras?.getString("productCategoryName").toString()
        productBrandName.text =  productCategoryName
        viewModel.getUserDetailsAPICall(orderFor?.batchId.toString())
        if (loginUser?.rollId != 3) {
            buttonAddProductBrand.visibility = View.VISIBLE
        }
        buttonAddProductBrand.setOnClickListener {
            activity?.navigateTo<AddProductBrandActivity> {
                putExtra("productCategoryId", viewModel.productCategory.value?.toInt() ?: 0)
                putExtra("productCategoryName", productCategoryName)
            }
        }
    }

    override fun onClick(item: Any, position: Int) {
        if(item is ProductBrandListResponse.ProductBrand){
            activity?.navigateTo<ProductListActivity> {
                putExtra("productCategoryId", viewModel.productCategory.value)
                putExtra("productCategoryName", productCategoryName)
                putExtra("productBrandId", item.id)
                putExtra("productBrandName", item.name)
            }
        }
    }
}