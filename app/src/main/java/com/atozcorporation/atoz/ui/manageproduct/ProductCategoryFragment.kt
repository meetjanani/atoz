package com.atozcorporation.atoz.ui.manageproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseFragment
import com.atozcorporation.atoz.ui.manageproduct.addproductcategory.AddProductCategoryActivity
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.navigateTo
import kotlinx.android.synthetic.main.activity_outlet.progressBar
import kotlinx.android.synthetic.main.fragment_product_category.*

class ProductCategoryFragment : BaseFragment(), IAdapterOnClick {

    private lateinit var viewModel: ProductCategoryViewModel
    protected var adapter = ProductCategoryAdapter(this)

    fun observeState(viewModel: ProductCategoryViewModel) {
        viewModel.productCategoryAPIState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ProductCategoryViewModel.ProductCategoryAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is ProductCategoryViewModel.ProductCategoryAPIState.Success -> {
                    it.data.data.let { response ->
                        adapter.ProductCategoryAdapter(requireContext(), response, "")
                        recycler_view_Product_CategoryList.adapter = adapter
                    }
                    progressBar.visibility = View.GONE
                }
                is ProductCategoryViewModel.ProductCategoryAPIState.Failure -> {
                    Toast.makeText(
                        requireContext(),
                        it.throwable.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(ProductCategoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_product_category, container, false)
        observeState(viewModel)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActivityContext(requireActivity())
        if(loginUser?.rollId != 3 ){
            buttonAddProductCategory.visibility = View.VISIBLE
        }
        buttonAddProductCategory.setOnClickListener {
            requireActivity().navigateTo<AddProductCategoryActivity> {  }
        }
    }
    override fun onClick(item: Any, position: Int) {

    }
}