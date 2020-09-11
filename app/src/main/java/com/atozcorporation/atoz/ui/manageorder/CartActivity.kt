package com.atozcorporation.atoz.ui.manageorder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.base.offlinedb.Order_Summery_Db_Helper
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.defaultToast
import kotlinx.android.synthetic.main.activity_cart_list.*

class CartActivity : BaseActivity(), IAdapterOnClick {
    private lateinit var viewModel: CartViewModel
    protected var adapter = CartListAdapter(this)

    fun observeState(viewModel: CartViewModel) {
        viewModel.placeOrderAPIState.observe(this, Observer {
            when (it) {
                is CartViewModel.PlaceOrderAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is CartViewModel.PlaceOrderAPIState.Success -> {
                    progressBar.visibility = View.GONE
                }
                is CartViewModel.PlaceOrderAPIState.Failure -> {
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
        setContentView(R.layout.activity_cart_list)
        setActivityContext(this)
        viewModel =
            ViewModelProviders.of(this).get(CartViewModel::class.java)
        observeState(viewModel)
        viewModel.getUserDetailsAPICall(loginUser?.batchId.toString())
        textViewOrderForUserDetails.text = "Order For ::> ${orderFor?.batchId} : ${orderFor?.name} : ${orderFor?.contactNumber}"

        val dbHelper = Order_Summery_Db_Helper(this, null)
        dbHelper.getAllProduct()?.let {
            adapter.CartListAdapter(this, it, "")
        }
        recyclerViewProductsList.adapter = adapter
        buttonSubmitOrder.setOnClickListener {
            "Submit Order".defaultToast(this)
            dbHelper.getAllProduct()?.let {
                it.map { it.orderTotal = dbHelper.getOrderTotal().toString() }
                viewModel.productsListForPlaceOrder.value = it
                viewModel.generateNewOrderID()
            }
        }
    }

    override fun onClick(item: Any, position: Int) {
        if (item is Int) {
            textViewOrderAmount.text = "₹ " + item.toString()
        }
    }
}