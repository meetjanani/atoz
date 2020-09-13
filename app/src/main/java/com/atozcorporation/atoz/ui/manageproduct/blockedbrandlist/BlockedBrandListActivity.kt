package com.atozcorporation.atoz.ui.manageproduct.blockedbrandlist

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.base.offlinedb.Order_Summery_Db_Helper
import com.atozcorporation.atoz.rest.response.login.LoginResponse
import com.atozcorporation.atoz.rest.response.product.ProductBrandListResponse
import com.atozcorporation.atoz.ui.manageproduct.addblockbrand.AddBlockBrandActivity
import com.atozcorporation.atoz.ui.manageproduct.addblockbrand.AddBlockBrandViewModel
import com.atozcorporation.atoz.ui.manageproduct.productbrandlist.ProductBrandListAdapter
import com.atozcorporation.atoz.ui.manageproduct.productbrandlist.ProductBrandListViewModel
import com.atozcorporation.atoz.ui.manageproduct.productcategorylist.ProductCategoryActivity
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.defaultToast
import com.growinginfotech.businesshub.base.navigateForResultTo
import com.growinginfotech.businesshub.base.navigateTo
import kotlinx.android.synthetic.main.activity_add_block_brand.*
import kotlinx.android.synthetic.main.activity_block_brand_list.*
import kotlinx.android.synthetic.main.activity_block_brand_list.progressBar
import kotlinx.android.synthetic.main.activity_outlet.*
import kotlinx.android.synthetic.main.activity_product_brand.*

class BlockedBrandListActivity : BaseActivity(), IAdapterOnClick {
    private lateinit var viewModel: BlockedBrandListViewModel
    protected var adapter = BlockedBrandListAdapter(this)

    fun observeState(viewModel: BlockedBrandListViewModel) {
        viewModel.blockedBrandAPIState.observe(this, Observer {
            when (it) {
                is BlockedBrandListViewModel.BlockedBrandAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is BlockedBrandListViewModel.BlockedBrandAPIState.SuccessUserDetails -> {
                    progressBar.visibility = View.GONE
                    adapter.clearAllData()
                }
                is BlockedBrandListViewModel.BlockedBrandAPIState.SuccessBlockedBrandList -> {
                    val blockedbrandList :  MutableList<ProductBrandListResponse.ProductBrand> = mutableListOf()
                    it.data.map { brandId ->
                        viewModel.brandList.value?.data?.single { brandDetails -> brandDetails.id == brandId }?.let { it1 ->
                            blockedbrandList.add(it1)
                        }
                    }
                    blockedbrandList?.let { it1 ->
                        adapter.BlockedBrandListAdapter(this, it1, "")
                    adapter.notifyDataSetChanged()}
                    progressBar.visibility = View.GONE
                }
                is BlockedBrandListViewModel.BlockedBrandAPIState.Failure -> {
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
        setContentView(R.layout.activity_block_brand_list)
        setActivityContext(this)
        viewModel =
            ViewModelProviders.of(this).get(BlockedBrandListViewModel::class.java)
        observeState(viewModel)
        viewModel.bachId.value = intent.extras?.getString("batchId").toString()
        recyclerViewBlockedBrandList.adapter = adapter
        buttonBLocknewBrand.setOnClickListener {
            navigateForResultTo<AddBlockBrandActivity>(11)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }
        if (requestCode == 11) {
            if (data != null) {
                val brandId = data.extras?.getInt("blockedBrandId") ?: 0
                viewModel.getUserDetailsAPICall(brandId, true)
                // apiCall for getBlockedBrandLatestData
                // apiCall for updateNewBrand
            }
        }
    }

    override fun onClick(item: Any, position: Int) {
        if(item is ProductBrandListResponse.ProductBrand){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Are you want to un-block this brand for  ${viewModel.bachId.value.toString()} ?")
            builder.setMessage("${item.name}  Brand for outlet ${viewModel.bachId.value.toString()} will be un-blocke ! Are you sure ?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                viewModel.getUserDetailsAPICall(item.id, false)
            }
            builder.setNegativeButton("No") { dialogInterface, which ->
                //
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }
}