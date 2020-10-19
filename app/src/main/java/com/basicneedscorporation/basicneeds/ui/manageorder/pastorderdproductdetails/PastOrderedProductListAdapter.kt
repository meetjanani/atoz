package com.basicneedscorporation.basicneeds.ui.manageorder.pastorderdproductdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.rest.response.pastorder.OrderDetailsResponse
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.loadImage

class PastOrderedProductListAdapter(val iAdapterOnClick: IAdapterOnClick) :
    RecyclerView.Adapter<PastOrderedProductListAdapter.MyViewHolder>() {

    private var context: Context? = null
    private var arrayList: List<OrderDetailsResponse.OrderDetails> = listOf()


    //private ContactsAdapterListener listener;
    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var product_image: ImageView
        var product_name: TextView
        var product_price: TextView
        var product_quantity: TextView

        init {
            product_image = itemView.findViewById(R.id.product_image)
            product_name = itemView.findViewById(R.id.product_name)
            product_price = itemView.findViewById(R.id.product_price)
            product_quantity = itemView.findViewById(R.id.product_quantity)
        }
    }

    // Removed ContactsAdapterListener listener
    fun PastOrderedProductListAdapter(
        context: Context?,
        arrayList: List<OrderDetailsResponse.OrderDetails>,
        string: String?
    ) {
        this.context = context
        //  this.listener = listener;
        this.arrayList = arrayList
    }

    // setting Toolbar




    override fun onCreateViewHolder(parent: ViewGroup, i: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.pattern_ordered_product_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, i: Int) {
        context?.let { loadImage(arrayList.get(i).URL_1, holder.product_image, it) }
        holder.product_name.text = arrayList.get(i).Product_Name
        holder.product_price.text = arrayList.get(i).Product_Total
        holder.product_quantity.text = "₹ ${arrayList.get(i).Product_Price} X ${arrayList.get(i).Qty}"
    }

    override fun getItemCount(): Int {
        return arrayList.size   ?: 0
    }
}