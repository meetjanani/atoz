package com.atozcorporation.atoz.ui.report

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.growinginfotech.businesshub.base.IAdapterOnClick
import kotlinx.android.synthetic.main.activity_order_wise_product_qty.*
import kotlinx.android.synthetic.main.activity_past_order_header.progressBar

class OrderWiseProductQtyActivity : BaseActivity(), IAdapterOnClick {
    private lateinit var viewModel: OrderWiseProductQtyViewModel
    protected var adapter = OrderWiseProductQtyAdapter(this)
    fun observeState(viewModel: OrderWiseProductQtyViewModel) {
        viewModel.orderWiseProductQtyAPIState.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is OrderWiseProductQtyViewModel.OrderWiseProductQtyAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is OrderWiseProductQtyViewModel.OrderWiseProductQtyAPIState.Success -> {
                    progressBar.visibility = View.GONE
                    if (it.data.data.isNotEmpty()) {
                        adapter.OrderWiseProductQtyAdapter(this, it.data.data, "")
                        adapter.notifyDataSetChanged()
                    }
                }
                is OrderWiseProductQtyViewModel.OrderWiseProductQtyAPIState.Failure -> {
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
        setContentView(R.layout.activity_order_wise_product_qty)
        setActivityContext(this)
        viewModel =
            ViewModelProviders.of(this).get(OrderWiseProductQtyViewModel::class.java)
        observeState(viewModel)

        recyclerViewOrderWiseProductQty.adapter = adapter
        if (loginUser?.rollId == 4) {
            if (loginUser?.isBrandOwner?.length ?: 0 > 0) {
                viewModel.getOrderWiseProductQtyAPICall(loginUser?.isBrandOwner.toString(), false)
            }
        }
        if (loginUser?.rollId == 1) {
            viewModel.getOrderWiseProductQtyAPICall(loginUser?.isBrandOwner.toString(), true)
        }
    }

    override fun onClick(item: Any, position: Int) {
        //
    }
}