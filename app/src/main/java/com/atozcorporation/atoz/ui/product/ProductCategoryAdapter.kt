package com.atozcorporation.atoz.ui.product

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.atozcorporation.atoz.R
import com.atozcorporation.atoz.rest.response.outlet.OutletCategoryResponse
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
        var textViewOutletCategory: TextView
        var imageViewOutletCategory: AppCompatImageView
        var rootContraintLayout: ConstraintLayout

        init {
            textViewOutletCategory = itemView.findViewById(R.id.textViewOutletCategory)
            imageViewOutletCategory = itemView.findViewById(R.id.imageViewOutletCategory)
            rootContraintLayout = itemView.findViewById(R.id.rootContraintLayout)
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
            .inflate(R.layout.pattern_outlet_category, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, i: Int) {
        holder.textViewOutletCategory.text = arrayList.get(i).name
        context?.let { loadImage(arrayList.get(i).url1, holder.imageViewOutletCategory, it) }
        holder.rootContraintLayout.setOnClickListener {
            iAdapterOnClick.onClick(arrayList.get(i), i)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size   ?: 0
    }
}