package com.atozcorporation.atoz.ui.manageproduct.productcategorylist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.ui.manageproduct.addproductcategory.AddProductCategoryActivity
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.navigateTo
import kotlinx.android.synthetic.main.activity_outlet.progressBar
import kotlinx.android.synthetic.main.fragment_product_category.*

class ProductCategoryActivity : BaseActivity(), IAdapterOnClick {

    private lateinit var viewModel: ProductCategoryViewModel
    protected var adapter = ProductCategoryAdapter(this)

    fun observeState(viewModel: ProductCategoryViewModel) {
        viewModel.productCategoryAPIState.observe(this, Observer {
            when (it) {
                is ProductCategoryViewModel.ProductCategoryAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is ProductCategoryViewModel.ProductCategoryAPIState.Success -> {
                    it.data.data.let { response ->
                        adapter.ProductCategoryAdapter(this, response, "")
                        recycler_view_Product_CategoryList.adapter = adapter
                    }
                    progressBar.visibility = View.GONE
                }
                is ProductCategoryViewModel.ProductCategoryAPIState.Failure -> {
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
        setContentView(R.layout.fragment_product_category)
        setActivityContext(this)
        viewModel =
            ViewModelProviders.of(this).get(ProductCategoryViewModel::class.java)
        observeState(viewModel)
        if(loginUser?.rollId != 3 ){
            buttonAddProductCategory.visibility = View.VISIBLE
        }
        buttonAddProductCategory.setOnClickListener {
            navigateTo<AddProductCategoryActivity> {  }
        }
    }

    override fun onClick(item: Any, position: Int) {

    }
}