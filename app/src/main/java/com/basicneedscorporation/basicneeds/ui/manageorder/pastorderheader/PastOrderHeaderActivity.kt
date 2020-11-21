package com.basicneedscorporation.basicneeds.ui.manageorder.pastorderheader

import android.app.DatePickerDialog
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
import java.text.SimpleDateFormat
import java.util.*

class PastOrderHeaderActivity : BaseActivity(), IAdapterOnClick {
    var year = 0
    var month = 0
    var dayOfMonth = 0
    var datePickerDialog: DatePickerDialog? = null
    var calendar: Calendar? = null
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
                    if (it.data.data != null) {
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

        calendar = Calendar.getInstance()
        //SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
        //SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
        val mdformat = SimpleDateFormat("yyyy-MM-dd")
        calendar?.let {
            val strDate = mdformat.format(it.getTime())
            if(loginUser?.rollId == 3){
                Tv_From_Date.text = "${it.get(Calendar.YEAR)}-01-01"
            } else {
                Tv_From_Date.text = "${it.get(Calendar.YEAR)}-${it.get((Calendar.MONTH)) + 1}-01"
            }
            Tv_To_Date.text = "${it.get(Calendar.YEAR)}-${it.get((Calendar.MONTH)) + 1}-${(it.get(Calendar.DAY_OF_MONTH) + 1)}"
        }

        val batchId = intent?.extras?.getString("batchId").toString()
        textViewOrderForBatchId.text = batchId
        viewModel.getPastOrderAPICall(
            batchId,
            loginUser?.rollId ?: 0,
            Tv_From_Date.text.toString(),
            Tv_To_Date.text.toString()
        )



        Tv_From_Date.setOnClickListener {
            calendar = Calendar.getInstance()
            calendar?.let {
                year = it.get(Calendar.YEAR)
                month = it.get(Calendar.MONTH)
                dayOfMonth = it.get(Calendar.DAY_OF_MONTH)
            }
            datePickerDialog = DatePickerDialog(
                this,
                { datePicker, year, month, day ->
                    Tv_From_Date.text = year.toString() + "-" + (month + 1) + "-" + (day)
                    viewModel.getPastOrderAPICall(
                        batchId,
                        loginUser?.rollId ?: 0,
                        Tv_From_Date.text.toString(),
                        Tv_To_Date.text.toString()
                    )
                }, year, month, dayOfMonth + 1
            )
            datePickerDialog?.let {
                it.getDatePicker()
                it.show()
            }
        }

        Tv_To_Date.setOnClickListener {
            calendar = Calendar.getInstance()
            calendar?.let {
                year = it.get(Calendar.YEAR)
                month = it.get(Calendar.MONTH)
                dayOfMonth = it.get(Calendar.DAY_OF_MONTH)
            }
            datePickerDialog = DatePickerDialog(
                this,
                { datePicker, year, month, day ->
                    Tv_To_Date.text = year.toString() + "-" + (month + 1) + "-" + (day + 1)
                    viewModel.getPastOrderAPICall(
                        batchId,
                        loginUser?.rollId ?: 0,
                        Tv_From_Date.text.toString(),
                        Tv_To_Date.text.toString()
                    )
                }, year, month, dayOfMonth + 1
            )
            datePickerDialog?.let {
                it.getDatePicker()
                it.show()
            }
        }
    }

    override fun onClick(item: Any, position: Int) {
        if(item is PastOrderHeaderResponse.PastOrderHeaderDetails){
            navigateTo<PastOrderedProductDetailsActivity> {
                putExtra("orderId", item.Order_ID.toString())
            }
        }
    }
}