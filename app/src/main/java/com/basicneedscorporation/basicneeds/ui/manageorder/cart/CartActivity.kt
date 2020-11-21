package com.basicneedscorporation.basicneeds.ui.manageorder.cart

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.base.BaseActivity
import com.basicneedscorporation.basicneeds.base.offlinedb.Order_Summery_Db_Helper
import com.growinginfotech.businesshub.base.IAdapterOnClick
import kotlinx.android.synthetic.main.activity_cart_list.*

class CartActivity : BaseActivity(), IAdapterOnClick {
    private lateinit var viewModel: CartViewModel
    protected var adapter = CartListAdapter(this)
    private lateinit var dbHelper : Order_Summery_Db_Helper

    fun observeState(viewModel: CartViewModel) {
        viewModel.placeOrderAPIState.observe(this, Observer {
            when (it) {
                is CartViewModel.PlaceOrderAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    buttonSubmitOrder.visibility = View.GONE
                }
                is CartViewModel.PlaceOrderAPIState.Success -> {
                    progressBar.visibility = View.GONE
                    buttonSubmitOrder.visibility = View.VISIBLE
                }
                is CartViewModel.PlaceOrderAPIState.SuccessProductSubmited -> {
                    deleteProductByProductId(it.productId)
                }
                is CartViewModel.PlaceOrderAPIState.Failure -> {
                    Toast.makeText(
                        this,
                        it.throwable.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                    buttonSubmitOrder.visibility = View.VISIBLE
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
        recyclerViewProductsList.adapter = adapter
        viewModel.getUserDetailsAPICall(loginUser?.batchId.toString())
        viewModel.userDetailsOfOrderBy.value = loginUser
        viewModel.userDetailsOfOrderFor.value = orderFor
        textViewOrderForUserDetails.text = "Order For ::> ${orderFor?.batchId} : ${orderFor?.name} : ${orderFor?.contactNumber}"

        dbHelper = Order_Summery_Db_Helper(this, null)
        prepareProductList()
        buttonSubmitOrder.setOnClickListener {
            dbHelper.getAllProduct()?.let {
                it.map { it.orderTotal = dbHelper.getOrderTotal().toString() }
                viewModel.productsListForPlaceOrder.value = it
                viewModel.generateNewOrderID()
            }
        }
    }

    override fun onClick(item: Any, position: Int) {
        if (item is Float) {
            textViewOrderAmount.text = "₹ " + item.toString()
        }
    }

    fun prepareProductList(){
        val productList = dbHelper.getAllProduct()
        adapter.CartListAdapter(this, productList, "")
        if(productList.isEmpty()){
            recyclerViewProductsList.visibility = View.GONE
        }
    }
    fun deleteProductByProductId(productId : Int){
        dbHelper.deleteByItemID(productId)
        prepareProductList()
    }
}