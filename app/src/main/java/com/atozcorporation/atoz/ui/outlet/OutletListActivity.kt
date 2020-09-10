package com.atozcorporation.atoz.ui.outlet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseActivity
import com.atozcorporation.atoz.rest.response.outlet.OutletListResponse
import com.atozcorporation.atoz.ui.outlet.outletdetails.OutletDetailsActivity
import com.growinginfotech.businesshub.base.CurrentSelectedOutletCategoryName
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.navigateTo
import kotlinx.android.synthetic.main.activity_outlet.*


class OutletListActivity : BaseActivity() , IAdapterOnClick{

    private lateinit var viewModel: OutletViewModel
    protected var adapter = OutletListAdapter(this)

    fun observeState(viewModel: OutletViewModel){
        viewModel.outletAPIState.observe(this, Observer {
            when (it) {
                is OutletViewModel.OutletAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is OutletViewModel.OutletAPIState.Success -> {
                    it.data.data.let { response ->
                        adapter.OutletListAdapter(this, response, "")
                        recycler_view_Outlet_List.setAdapter(adapter)
                    }
                    progressBar.visibility = View.GONE
                }
                is OutletViewModel.OutletAPIState.Failure -> {
                    Toast.makeText(this, it.throwable.message.toString(), Toast.LENGTH_SHORT).show()
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
        setActivityContext(this)
        categoryName.text = CurrentSelectedOutletCategoryName
        viewModel.outletUserRoll.value = loginUser?.rollId.toString() ?: "3"
        viewModel.loginUserDetails.value = loginUser
        viewModel.getCategoryWiseOutletList()
        search_editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                searchOutlet(s.toString().toLowerCase())
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun searchOutlet(searchString : String) {
        viewModel.outletListRecords.value?.filter { it.name.toLowerCase().contains(searchString)  || it.personName.toLowerCase().contains(searchString) || it.batchId.toLowerCase().contains(searchString) || it.contactNumber.toLowerCase().contains(searchString) }?.let {outList ->
            adapter.OutletListAdapter(this, outList, "")
            adapter.notifyDataSetChanged()
        }
    }

    override fun onClick(item: Any, position: Int) {
        if(item is OutletListResponse.Outlet){
            navigateTo<OutletDetailsActivity> {
                putExtra("outletDetails", item)
            }
        }
    }
}