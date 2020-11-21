package com.basicneedscorporation.basicneeds.ui.manageproduct.addblockbrand

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.base.BaseActivity
import com.growinginfotech.businesshub.base.navigateToPrevious
import kotlinx.android.synthetic.main.activity_add_block_brand.*
import kotlinx.android.synthetic.main.activity_outlet.progressBar

class AddBlockBrandActivity : BaseActivity() {
    private lateinit var viewModel: AddBlockBrandViewModel

    fun observeState(viewModel: AddBlockBrandViewModel) {
        viewModel.BrandList.observe(this, Observer {
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
                selectBrand.adapter = arrayAdapter
                progressBar.visibility = View.GONE
            }
        })
        viewModel.productBrandAPIState.observe(this, Observer {
            when (it) {
                is AddBlockBrandViewModel.ProductBrandAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is AddBlockBrandViewModel.ProductBrandAPIState.Success -> {
                    it.data.data.let { response ->

                    }
                    progressBar.visibility = View.GONE
                }
                is AddBlockBrandViewModel.ProductBrandAPIState.Failure -> {
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
        setContentView(R.layout.activity_add_block_brand)
        setActivityContext(this)
        viewModel =
            ViewModelProviders.of(this).get(AddBlockBrandViewModel::class.java)
        observeState(viewModel)

        buttonBlockProductBrand.setOnClickListener {
            navigateToPrevious {
                putExtra("blockedBrandId", viewModel.BrandList.value?.data?.get(
                    selectBrand.selectedItemPosition
                )?.id?.toInt())
            }
//            "${
//                viewModel.BrandList.value?.data?.get(
//                    selectBrand.selectedItemPosition
//                )?.id
//            },${
//                viewModel.BrandList.value?.data?.get(
//                    selectBrand.selectedItemPosition
//                )?.name
//            }".defaultToast(this)
        }
    }
}