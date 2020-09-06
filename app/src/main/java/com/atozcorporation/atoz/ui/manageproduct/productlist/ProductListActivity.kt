package com.atozcorporation.atoz.ui.manageproduct.productlist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.ui.manageproduct.addproduct.AddProductsActivity
import com.atozcorporation.atoz.ui.manageproduct.addproductbrand.AddProductBrandActivity
import com.atozcorporation.atoz.ui.manageproduct.addproductcategory.AddProductCategoryActivity
import com.atozcorporation.atoz.ui.manageproduct.productbrandlist.ProductBrandListAdapter
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.defaultToast
import com.growinginfotech.businesshub.base.navigateTo
import kotlinx.android.synthetic.main.activity_outlet.progressBar
import kotlinx.android.synthetic.main.activity_product_brand.*
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.fragment_product_category.buttonAddProductCategory

class ProductListActivity : BaseActivity(), IAdapterOnClick {

    private lateinit var viewModel: ProductListViewModel
    protected var adapter = ProductListAdapter(this)
    var productCategoryId = 0
    var productCategoryName = ""
    var productBrandId = 0
    var productBrandName = ""

    fun observeState(viewModel: ProductListViewModel) {
        viewModel.productListAPIState.observe(this, Observer {
            when (it) {
                is ProductListViewModel.ProductListAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is ProductListViewModel.ProductListAPIState.Success -> {
                    it.data.data?.let { response ->
                        adapter.ProductListAdapter(this, response, "")
                        recyclerViewProductsList.adapter = adapter
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

    }
}