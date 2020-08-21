package com.atozcorporation.atoz.ui.outlet

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.ui.addoutlet.AddOutletActivity
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.navigateTo
import kotlinx.android.synthetic.main.activity_outlet.*

class OutletListActivity : BaseActivity() , IAdapterOnClick{

    private lateinit var viewModel: OutletViewModel
    protected var adapter = OutletListAdapter(this)

    fun observeState(viewModel : OutletViewModel){
        viewModel.outletAPIState.observe(this, Observer {
            when(it){
                is  OutletViewModel.OutletAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is  OutletViewModel.OutletAPIState.Success -> {
                    it.data.data.let { response ->
                        adapter.OutletListAdapter(this, response, "")
                        recycler_view_Outlet_List.setAdapter(adapter)
                    }
                    progressBar.visibility = View.GONE
                }
                is  OutletViewModel.OutletAPIState.Failure -> {
                    Toast.makeText(this, it.throwable.message.toString(),Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outlet)
        viewModel =
            ViewModelProviders.of(this).get(OutletViewModel::class.java)
        observeState(viewModel)
        buttonAddOutlet.setOnClickListener {
            navigateTo<AddOutletActivity> {  }
        }
    }

    override fun onClick(item: Any, position: Int) {
        //
    }
}