package com.basicneedscorporation.basicneeds.ui.manageproduct.blockedbrandlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.rest.response.product.ProductBrandListResponse
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.loadImage

class BlockedBrandListAdapter(val iAdapterOnClick: IAdapterOnClick) :
    RecyclerView.Adapter<BlockedBrandListAdapter.MyViewHolder>() {

    private var context: Context? = null
    private var arrayList: MutableList<ProductBrandListResponse.ProductBrand> = mutableListOf()

    //private ContactsAdapterListener listener;
    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textViewBrandName: TextView
        var product_brand_icon: ImageView
        var buttonUnblock: Button

        init {
            textViewBrandName = itemView.findViewById(R.id.textViewBrandName)
            product_brand_icon = itemView.findViewById(R.id.product_brand_icon)
            buttonUnblock = itemView.findViewById(R.id.buttonUnblock)
        }
    }

    // Removed ContactsAdapterListener listener
    fun BlockedBrandListAdapter(
        context: Context,
        arrayList: MutableList<ProductBrandListResponse.ProductBrand>,
        string: String?
    ) {
        this.context = context
        //  this.listener = listener;
        this.arrayList = arrayList
    }

    fun clearAllData(){
        arrayList.clear()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.pattern_blocked_brand_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, i: Int) {
        holder.textViewBrandName.text = arrayList.get(i).name
        context?.let { loadImage(arrayList.get(i).url1, holder.product_brand_icon, it) }
        holder.buttonUnblock.setOnClickListener {
            iAdapterOnClick.onClick(arrayList.get(i), i)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size   ?: 0
    }
}