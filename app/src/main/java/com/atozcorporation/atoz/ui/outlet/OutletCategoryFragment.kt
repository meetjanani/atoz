package com.atozcorporation.atoz.ui.outlet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.base.BaseFragment
import com.atozcorporation.atoz.rest.response.outlet.OutletCategoryResponse
import com.atozcorporation.atoz.ui.addoutlet.AddOutletActivity
import com.growinginfotech.businesshub.base.CurrentSelectedOutletCategoryId
import com.growinginfotech.businesshub.base.CurrentSelectedOutletCategoryName
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.navigateTo
import kotlinx.android.synthetic.main.activity_outlet.*

class OutletCategoryFragment : BaseFragment() , IAdapterOnClick{

    private lateinit var viewModel: OutletCategoryViewModel
    protected var adapter = OutletCategoryAdapter(this)

    fun observeState(viewModel : OutletCategoryViewModel){
        viewModel.outletAPIState.observe(viewLifecycleOwner, Observer {
            when(it){
                is  OutletCategoryViewModel.OutletAPIState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is  OutletCategoryViewModel.OutletAPIState.   Success -> {
                    it.data.data.let { response ->
                        adapter.OutletListAdapter(requireContext(), response, "")
                        recycler_view_Outlet_List.layoutManager = GridLayoutManager(requireContext(), 2)
                        recycler_view_Outlet_List.setAdapter(adapter)
                    }
                    progressBar.visibility = View.GONE
                }
                is  OutletCategoryViewModel.OutletAPIState.Failure -> {
                    Toast.makeText(requireContext(), it.throwable.message.toString(),Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
                ViewModelProviders.of(this).get(OutletCategoryViewModel::class.java)
        val root = inflater.inflate(R.layout.activity_outlet, container, false)
        observeState(viewModel)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        buttonAddOutlet.setOnClickListener {
//           requireActivity().navigateTo<AddOutletActivity> {  }
//        }
    }

    override fun onClick(item: Any, position: Int) {
        if(item is OutletCategoryResponse.Data){
            CurrentSelectedOutletCategoryId = item.id
            CurrentSelectedOutletCategoryName = item.name
            requireActivity().navigateTo<OutletListActivity>()
        }
    }
}