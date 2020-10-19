package com.basicneedscorporation.basicneeds.ui.manageorder.managestock

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.base.BaseActivity
import com.basicneedscorporation.basicneeds.rest.response.login.LoginResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_manage__product__stock_.*

class ManageProductStockActivity : BaseActivity() {
    private lateinit var viewModel: ManageProductStockViewModel
    var Avaiable_Stock = 0
    var Total_Strock:Int = 0
    var Sold_Stock:Int = 0
    var Final_Stock:Int = 0
    var Temp_Total_Strock:Int = 0
    val orderForUserDetails = LoginResponse.UserDetails()

    fun observeState(viewModel: ManageProductStockViewModel) {
        viewModel.productListAPIState.observe(this, Observer {
            when (it) {
                is ManageProductStockViewModel.ProductListAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is ManageProductStockViewModel.ProductListAPIState.Success -> {
                    it.data.data.let { response ->
                        Avaiable_Stock = response.get(0).Avaiable_Stock.toInt()
                        Sold_Stock = response.get(0).Sold_Stock.toInt()
                        Total_Strock = response.get(0).Total_Stock.toInt()
                        Final_Stock = response.get(0).Avaiable_Stock.toInt()
                        Temp_Total_Strock = Total_Strock

                        product_name.text = response.get(0).Name
                        TV_Avaiable.text = "Avaiable: $Avaiable_Stock"
                        TV_Sold.text = "Sold: $Sold_Stock"
                        TV_Total.text = "Total: $Total_Strock"
                        TV_Final.text = "Final: $Final_Stock"

                        Picasso.with(this)
                            .load(response.get(0).URL_1.toString() + "")
                            .placeholder(R.drawable.ic_add_image)
                            .into(product_image)
                    }
                    progressBar.visibility = View.GONE
                }
                is ManageProductStockViewModel.ProductListAPIState.SuccessStockUpdated -> {
                    it.data.let { response ->
                        finish()
                    }
                    progressBar.visibility = View.GONE
                }
                is ManageProductStockViewModel.ProductListAPIState.Failure -> {
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
        setContentView(R.layout.activity_manage__product__stock_)
        setActivityContext(this)
        viewModel =
            ViewModelProviders.of(this).get(ManageProductStockViewModel::class.java)
        observeState(viewModel)
        viewModel.getProductListAPICall(intent?.extras?.getInt("productId").toString())

        ET_StockCount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                ManageStock()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })
        RD_Load_Stock.setOnClickListener { ManageStock() }

        RD_UnLoad_Stock.setOnClickListener { ManageStock() }

        Btn_Update_Stock.setOnClickListener {
            if (Avaiable_Stock != Final_Stock) {
                viewModel.manageProductStockAPICall(
                    intent?.extras?.getInt("productId").toString(),
                    Final_Stock.toString(),
                    Temp_Total_Strock.toString()
                )
            }
        }
    }

    private fun ManageStock() {
        if (!ET_StockCount.text.toString().isEmpty()) {
            if (RD_Load_Stock.isChecked) {
                Final_Stock = Avaiable_Stock + ET_StockCount.text.toString().toInt()
                Temp_Total_Strock = Total_Strock + ET_StockCount.text.toString().toInt()
            } else {
                Final_Stock = Avaiable_Stock - ET_StockCount.text.toString().toInt()
                Temp_Total_Strock = Total_Strock - ET_StockCount.text.toString().toInt()
            }
            TV_Final.text = "Final: $Final_Stock"
            TV_Total.text = "Total: $Temp_Total_Strock"
        } else {
            Temp_Total_Strock = Total_Strock
            TV_Final.text = "Final: $Avaiable_Stock"
            TV_Total.text = "Total: $Temp_Total_Strock"
        }
    }
}