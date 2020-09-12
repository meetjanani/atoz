package com.atozcorporation.atoz.ui.manageorder.pastorderheader

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.rest.response.pastorder.PastOrderHeaderResponse
import com.atozcorporation.atoz.ui.manageorder.pastorderdproductdetails.PastOrderedProductDetailsActivity
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.navigateTo
import kotlinx.android.synthetic.main.activity_past_order_header.*

class PastOrderHeaderActivity : BaseActivity(), IAdapterOnClick {
    private lateinit var viewModel: PastOrderHeaderViewModel
    protected var adapter = PastOrderListAdapter(this)

    fun observeState(viewModel: PastOrderHeaderViewModel) {
        viewModel.pastOrderAPIState.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is PastOrderHeaderViewModel.PastOrderAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is PastOrderHeaderViewModel.PastOrderAPIState.Success -> {
                    progressBar.visibility = View.GONE
                    adapter.PastOrderListAdapter(this, it.data.data, "")
                    adapter.notifyDataSetChanged()
                }
                is PastOrderHeaderViewModel.PastOrderAPIState.Failure -> {
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
        setContentView(R.layout.activity_past_order_header)
        setActivityContext(this)
        viewModel =
            ViewModelProviders.of(this).get(PastOrderHeaderViewModel::class.java)
        observeState(viewModel)
        recyclerViewPastOrdersList.adapter = adapter
        viewModel.getPastOrderAPICall(loginUser?.batchId.toString())
    }

    override fun onClick(item: Any, position: Int) {
        if(item is PastOrderHeaderResponse.PastOrderHeaderDetails){
            navigateTo<PastOrderedProductDetailsActivity> {
                putExtra("orderId", item.Order_ID.toString())
            }
        }
    }
}