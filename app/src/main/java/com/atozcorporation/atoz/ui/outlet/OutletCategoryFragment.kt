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
import com.growinginfotech.businesshub.base.*
import kotlinx.android.synthetic.main.activity_outlet.progressBar
import kotlinx.android.synthetic.main.fragment_outlet_category.*

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
                        recycler_view_Outlet_CategoryList.layoutManager = GridLayoutManager(requireContext(), 2)
                        recycler_view_Outlet_CategoryList.setAdapter(adapter)
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
        val root = inflater.inflate(R.layout.fragment_outlet_category, container, false)
        observeState(viewModel)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActivityContext(requireActivity())
        if(loginUser?.rollId != 3 ){
            buttonAddOutlet.visibility = View.VISIBLE
        }
        buttonAddOutlet.setOnClickListener {
           requireActivity().navigateTo<AddOutletActivity> {
               putExtra("outletUserRoll", "3")
           }
        }
    }

    override fun onClick(item: Any, position: Int) {
        if(item is OutletCategoryResponse.Data){
            CurrentSelectedOutletCategoryId = item.id
            CurrentSelectedOutletCategoryName = item.name
            requireActivity().navigateTo<OutletListActivity>()
        }
    }
}