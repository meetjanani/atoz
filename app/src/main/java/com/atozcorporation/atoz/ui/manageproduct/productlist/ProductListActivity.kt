package com.atozcorporation.atoz.ui.manageproduct.productlist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.ui.manageproduct.addproductbrand.AddProductBrandActivity
import com.atozcorporation.atoz.ui.manageproduct.addproductcategory.AddProductCategoryActivity
import com.atozcorporation.atoz.ui.manageproduct.productbrandlist.ProductBrandListAdapter
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.defaultToast
import com.growinginfotech.businesshub.base.navigateTo
import kotlinx.android.synthetic.main.activity_outlet.progressBar
import kotlinx.android.synthetic.main.activity_product_brand.*
import kotlinx.android.synthetic.main.fragment_product_category.buttonAddProductCategory

class ProductListActivity : BaseActivity(), IAdapterOnClick {

    private lateinit var viewModel: ProductListViewModel
    protected var adapter = ProductBrandListAdapter(this)
    var productCategoryId = 0
    var productCategoryName = ""

    fun observeState(viewModel: ProductListViewModel) {
        viewModel.productListAPIState.observe(this, Observer {
            when (it) {
                is ProductListViewModel.ProductListAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is ProductListViewModel.ProductListAPIState.Success -> {
                    it.data.data.let { response ->
                        adapter.ProductBrandListAdapter(this, response, "")
                        recyclerViewProductBrandList.adapter = adapter
                    }
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
        setContentView(R.layout.activity_product_brand)
        setActivityContext(this)
        viewModel =
            ViewModelProviders.of(this).get(ProductListViewModel::class.java)
        observeState(viewModel)
        productCategoryId = intent.extras?.getInt("productCategoryId") ?: 0
        productCategoryName =  intent.extras?.getString("productCategoryName").toString()
        productBrandName.text =  productCategoryName
        viewModel.getProductListAPICall(productCategoryId, 1)
        if (loginUser?.rollId != 3) {
            buttonAddProductBrand.visibility = View.VISIBLE
        }
        buttonAddProductBrand.setOnClickListener {
            activity?.navigateTo<AddProductBrandActivity> {
                putExtra("productCategoryId", productCategoryId)
                putExtra("productCategoryName", productCategoryName)
                putExtra("productBrandyName", productCategoryName)
                putExtra("productBrandName", productCategoryName)
            }
        }
    }

    override fun onClick(item: Any, position: Int) {

    }
}