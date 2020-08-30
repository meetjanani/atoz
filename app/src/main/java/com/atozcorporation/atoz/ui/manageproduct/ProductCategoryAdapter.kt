package com.atozcorporation.atoz.ui.manageproduct

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.rest.response.product.ProductCategoryResponse
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.loadImage

class ProductCategoryAdapter(val iAdapterOnClick: IAdapterOnClick) :
    RecyclerView.Adapter<ProductCategoryAdapter.MyViewHolder>() {

    private var context: Context? = null
    private var arrayList: List<ProductCategoryResponse.ProductCategory> = listOf()

    //private ContactsAdapterListener listener;
    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var category_title: TextView
        var category_icon: AppCompatImageView
        var category_card: LinearLayout

        init {
            category_title = itemView.findViewById(R.id.category_title)
            category_icon = itemView.findViewById(R.id.category_icon)
            category_card = itemView.findViewById(R.id.category_card)
        }
    }

    // Removed ContactsAdapterListener listener
    fun ProductCategoryAdapter(
        context: Context,
        arrayList: List<ProductCategoryResponse.ProductCategory>,
        string: String?
    ) {
        this.context = context
        //  this.listener = listener;
        this.arrayList = arrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, i: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.pattern_product_category, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, i: Int) {
        holder.category_title.text = arrayList.get(i).name
        context?.let { loadImage(arrayList.get(i).url1, holder.category_icon, it) }
        holder.category_card.setOnClickListener {
            iAdapterOnClick.onClick(arrayList.get(i), i)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size   ?: 0
    }
}