package com.atozcorporation.atoz.ui.outlet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.rest.response.outlet.OutletListResponse
import com.growinginfotech.businesshub.base.IAdapterOnClick

class OutletListAdapter(val iAdapterOnClick: IAdapterOnClick) :
    RecyclerView.Adapter<OutletListAdapter.MyViewHolder>() {

    private var context: Context? = null
    private var arrayList: List<OutletListResponse.Outlet> = listOf()
    var toolbar: Toolbar? = null



    //private ContactsAdapterListener listener;
    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textViewBusinessName: TextView
        var textViewContactPersonName: TextView
        var textViewContactPersonNumber: TextView
        var textViewAddress: TextView
        var textViewGSTNumber: TextView
        var textViewOutletId: TextView
        var rootContraintLayout: ConstraintLayout

        init {
            textViewBusinessName = itemView.findViewById(R.id.textViewBusinessName)
            textViewContactPersonName = itemView.findViewById(R.id.textViewContactPersonName)
            textViewContactPersonNumber = itemView.findViewById(R.id.textViewContactPersonNumber)
            textViewGSTNumber = itemView.findViewById(R.id.textViewGSTNumber)
            textViewAddress = itemView.findViewById(R.id.textViewAddress)
            textViewOutletId = itemView.findViewById(R.id.textViewOutletId)
            rootContraintLayout = itemView.findViewById(R.id.rootContraintLayout)
        }
    }

    // Removed ContactsAdapterListener listener
    fun OutletListAdapter(
        context: Context?,
        arrayList: List<OutletListResponse.Outlet>,
        string: String?
    ) {
        this.context = context
        //  this.listener = listener;
        this.arrayList = arrayList
    }

    // setting Toolbar




    override fun onCreateViewHolder(parent: ViewGroup, i: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.pattern_outlet_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, i: Int) {
        holder.textViewOutletId.text = arrayList.get(i).batchId
        holder.textViewBusinessName.text = arrayList.get(i).name
        holder.textViewContactPersonName.text = arrayList.get(i).personName
        holder.textViewGSTNumber.text = arrayList.get(i).gst
        holder.textViewAddress.text = "${arrayList.get(i).address1}\n${arrayList.get(i).address2}"
        holder.textViewContactPersonNumber.text = arrayList.get(i).contactNumber
        holder.rootContraintLayout.setOnClickListener {
            iAdapterOnClick.onClick(arrayList.get(i), i)
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size   ?: 0
    }
}