package com.atozcorporation.atoz.ui.manageorder.pastorderdproductdetails

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.rest.response.pastorder.OrderDetailsResponse
import com.atozcorporation.atoz.ui.manageorder.pastorderheader.PastOrderHeaderViewModel
import com.atozcorporation.atoz.ui.manageorder.pastorderheader.PastOrderListAdapter
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.dateFormatJoinedAt
import com.growinginfotech.businesshub.base.dateFormatSimpleDate
import com.growinginfotech.businesshub.base.getFormatedDateTime
import kotlinx.android.synthetic.main.activity_add_outlet.*
import kotlinx.android.synthetic.main.activity_past_order_header.*
import kotlinx.android.synthetic.main.activity_past_order_header.progressBar
import kotlinx.android.synthetic.main.activity_past_ordered_products.*
import kotlinx.android.synthetic.main.pattern_past_order_list.*

class PastOrderedProductDetailsActivity : BaseActivity(), IAdapterOnClick {
    private lateinit var viewModel: PastOrderedProductDetailsViewModel
    protected var adapter = PastOrderedProductListAdapter(this)

    fun observeState(viewModel: PastOrderedProductDetailsViewModel) {
        viewModel.orderWiseProductListAPIState.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is PastOrderedProductDetailsViewModel.OrderWiseProductListAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is PastOrderedProductDetailsViewModel.OrderWiseProductListAPIState.Success -> {
                    progressBar.visibility = View.GONE
                    adapter.PastOrderedProductListAdapter(this, it.data.data, "")
                    adapter.notifyDataSetChanged()
                    orderBasicDetails(it.data.data[0], it.data.data.size)
                }
                is PastOrderedProductDetailsViewModel.OrderWiseProductListAPIState.Failure -> {
                    Toast.makeText(
                        this,
                        it.throwable.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        })

        viewModel.orderStatus.observe(this, Observer {
            it.data.let { response ->
                val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                    this,
                    android.R.layout.simple_spinner_item,
                    ArrayList<String>().apply {
                        response.map { orderStatus ->
                            add(orderStatus.name)
                        }
                    } as List<String>
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerOrderStatus.adapter = arrayAdapter
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_ordered_products)
        setActivityContext(this)
        viewModel =
            ViewModelProviders.of(this).get(PastOrderedProductDetailsViewModel::class.java)
        observeState(viewModel)
       recyclerViewPastOrderdProductList.adapter = adapter
        viewModel.orderId.value = intent.extras?.getString("orderId").toString()

        buttonUpdateOrderStatus.setOnClickListener {
            viewModel.updateOrderStatusAPICall(viewModel.orderStatus.value?.data?.get(spinnerOrderStatus.selectedItemPosition)?.id ?: 0,
                viewModel.orderStatus.value?.data?.get(spinnerOrderStatus.selectedItemPosition)?.name.toString())
        }

        if(loginUser?.rollId != 3){
            textViewOrderStatusLable.visibility = View.VISIBLE
            spinnerOrderStatus.visibility = View.VISIBLE
            buttonUpdateOrderStatus.visibility = View.VISIBLE
        }
    }
    override fun onClick(item: Any, position: Int) {
        //
    }

    fun orderBasicDetails(orderDetails : OrderDetailsResponse.OrderDetails, productsCount : Int){
        spinnerOrderStatus.setSelection(
            viewModel.orderStatus.value?.data?.indexOf(
                viewModel.orderStatus.value?.data?.singleOrNull { it.id == orderDetails.Order_Type})
                ?: 0
        )

        // textViewOrderAmount.text = "₹ ${orderDetails.Order_Total}"
        textViewOrderStatus.text = orderDetails.Order_Status
        textViewOrderId.text = orderDetails.Order_ID
        textViewProductsCount.text = productsCount.toString()
        textViewOrderPrice.text = "₹ ${orderDetails.Order_Total}"
        textViewOrderDate.text = orderDetails.Created_Date.getFormatedDateTime(dateFormatJoinedAt, dateFormatSimpleDate)
        textViewOrderFor.text = "${ orderDetails.orderForBatchId},${ orderDetails.orderForName}"
        textViewOrderBy.text = "${ orderDetails.orderByBatchId},${ orderDetails.orderByName}"
    }
}