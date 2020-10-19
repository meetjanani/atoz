package com.basicneedscorporation.basicneeds.ui.manageorder.pastorderheader

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.base.BaseActivity
import com.basicneedscorporation.basicneeds.rest.response.pastorder.PastOrderHeaderResponse
import com.basicneedscorporation.basicneeds.ui.manageorder.pastorderdproductdetails.PastOrderedProductDetailsActivity
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
                    if(it.data.data != null){
                        adapter.PastOrderListAdapter(this, it.data.data, "")
                        adapter.notifyDataSetChanged()
                    }
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
        val batchId = intent?.extras?.getString("batchId").toString()
        textViewOrderForBatchId.text = batchId
        viewModel.getPastOrderAPICall(batchId, loginUser?.rollId  ?: 0)
    }

    override fun onClick(item: Any, position: Int) {
        if(item is PastOrderHeaderResponse.PastOrderHeaderDetails){
            navigateTo<PastOrderedProductDetailsActivity> {
                putExtra("orderId", item.Order_ID.toString())
            }
        }
    }
}