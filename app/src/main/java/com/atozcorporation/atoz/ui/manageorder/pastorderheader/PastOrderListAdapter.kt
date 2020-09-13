package com.atozcorporation.atoz.ui.manageorder.pastorderheader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.rest.response.pastorder.PastOrderHeaderResponse
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.dateFormatJoinedAt
import com.growinginfotech.businesshub.base.dateFormatSimpleDate
import com.growinginfotech.businesshub.base.getFormatedDateTime

class PastOrderListAdapter(val iAdapterOnClick: IAdapterOnClick) :
    RecyclerView.Adapter<PastOrderListAdapter.MyViewHolder>() {

    private var context: Context? = null
    private var arrayList: List<PastOrderHeaderResponse.PastOrderHeaderDetails> = listOf()


    //private ContactsAdapterListener listener;
    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textViewOrderId: TextView
        var textViewProductsCount: TextView
        var textViewOrderPrice: TextView
        var textViewOrderDate: TextView
        var textViewOrderStatus: TextView
        var textViewOrderFor: TextView
        var textViewOrderBy: TextView
        var mainCardView: CardView

        init {
            textViewOrderId = itemView.findViewById(R.id.textViewOrderId)
            textViewProductsCount = itemView.findViewById(R.id.textViewProductsCount)
            textViewOrderPrice = itemView.findViewById(R.id.textViewOrderPrice)
            textViewOrderDate = itemView.findViewById(R.id.textViewOrderDate)
            textViewOrderStatus = itemView.findViewById(R.id.textViewOrderStatus)
            textViewOrderFor = itemView.findViewById(R.id.textViewOrderFor)
            textViewOrderBy = itemView.findViewById(R.id.textViewOrderBy)
            mainCardView = itemView.findViewById(R.id.mainCardView)
        }
    }

    // Removed ContactsAdapterListener listener
    fun PastOrderListAdapter(
        context: Context?,
        arrayList: List<PastOrderHeaderResponse.PastOrderHeaderDetails>,
        string: String?
    ) {
        this.context = context
        //  this.listener = listener;
        this.arrayList = arrayList
    }

    // setting Toolbar




    override fun onCreateViewHolder(parent: ViewGroup, i: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.pattern_past_order_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, i: Int) {
        holder.textViewOrderId.text = arrayList.get(i).Order_ID
        holder.textViewProductsCount.text = arrayList.get(i).Product_Count
        holder.textViewOrderPrice.text = "₹ ${arrayList.get(i).Order_Total}"
        holder.textViewOrderDate.text = arrayList.get(i).Created_Date.getFormatedDateTime(dateFormatJoinedAt, dateFormatSimpleDate)
        holder.textViewOrderStatus.text = arrayList.get(i).Order_Status
        holder.textViewOrderFor.text = "${arrayList.get(i).orderForBatchId},${arrayList.get(i).orderForName}"
        holder.textViewOrderBy.text = "${arrayList.get(i).orderByBatchId},${arrayList.get(i).orderByName}"
        holder.mainCardView.setOnClickListener {
            iAdapterOnClick.onClick(arrayList.get(i), i)
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size   ?: 0
    }
}