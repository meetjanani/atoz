package com.basicneedscorporation.basicneeds.ui.manageproduct.productlist

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.base.offlinedb.Order_Summery_Db_Helper
import com.basicneedscorporation.basicneeds.base.offlinedb.Order_Summery_Ofline_Bean
import com.basicneedscorporation.basicneeds.rest.response.product.ProductListResponse
import com.basicneedscorporation.basicneeds.ui.manageorder.managestock.ManageProductStockActivity
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.loadImage
import com.growinginfotech.businesshub.base.navigateTo
import com.growinginfotech.businesshub.base.total_amount

class ProductListAdapter(val iAdapterOnClick: IAdapterOnClick) :
    RecyclerView.Adapter<ProductListAdapter.MyViewHolder>() {

    private lateinit var context: Activity
    private lateinit var userRoll: String
    private var arrayList: List<ProductListResponse.ProductDetails> = listOf()
    private lateinit var db_helper : Order_Summery_Db_Helper

    //private ContactsAdapterListener listener;
    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var TextViewCategoryBrandName: TextView
        var TextViewProductName: TextView
        var Img_Item: ImageView
        var Btn_Remove_Item: TextView
        var Btn_Display_Item: TextView
        var Btn_Add_Item: TextView
        var TextViewProductPrice: TextView
        var TextViewDiscountedPrice: TextView
        var TextViewProductCode: TextView
        var LL_Outer: LinearLayout
        var ManageProduct: LinearLayout
        var buttonManageStock: AppCompatButton
        var buttonDeleteProduct: AppCompatButton

        init {
            TextViewCategoryBrandName = itemView.findViewById(R.id.TextViewCategoryBrandName)
            TextViewProductCode = itemView.findViewById(R.id.TextViewProductCode)
            TextViewProductName = itemView.findViewById(R.id.TextViewProductName)
            Btn_Remove_Item = itemView.findViewById(R.id.Btn_Remove_Item)
            Btn_Display_Item = itemView.findViewById(R.id.Btn_Display_Item)
            Btn_Add_Item = itemView.findViewById(R.id.Btn_Add_Item)
            TextViewProductPrice = itemView.findViewById(R.id.TextViewProductPrice)
            TextViewDiscountedPrice = itemView.findViewById(R.id.TextViewDiscountedPrice)
            Img_Item = itemView.findViewById(R.id.Img_Item)
            LL_Outer = itemView.findViewById(R.id.LL_Outer)
            ManageProduct = itemView.findViewById(R.id.ManageProduct)
            buttonManageStock = itemView.findViewById(R.id.buttonManageStock)
            buttonDeleteProduct = itemView.findViewById(R.id.buttonDeleteProduct)
        }
    }

    // Removed ContactsAdapterListener listener
    fun ProductListAdapter(
        context: Activity,
        arrayList: List<ProductListResponse.ProductDetails>,
        userRoll: String
    ) {
        this.context = context
          this.userRoll = userRoll;
        this.arrayList = arrayList
        db_helper = Order_Summery_Db_Helper(context, null)
        getOrderTotal()
    }


    override fun onCreateViewHolder(parent: ViewGroup, i: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.pattern_product_list_01, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, i: Int) {
        holder.TextViewCategoryBrandName.text = "${arrayList.get(i).productCategoryName}, ${arrayList.get(
            i
        ).productBrandName}"
        holder.TextViewProductName.text = "${arrayList.get(i).Name}"
        holder.TextViewProductCode.text = "${arrayList.get(i).productCode}"
        holder.TextViewDiscountedPrice.text = "${arrayList.get(i).MRP_1}"
        holder.TextViewDiscountedPrice.setPaintFlags(holder.TextViewDiscountedPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
        context?.let { loadImage(arrayList.get(i).URL_1, holder.Img_Item, it) }
//        holder.category_card.setOnClickListener {
//            iAdapterOnClick.onClick(arrayList.get(i), i)
//        }
//        Glide.with(context)
//                .load(Records.get(i).getImageUrl1() +"")
//                .into(holder.draweeView);
        val itemprice: Int = arrayList.get(i).MRP_2.toInt()

        if ((arrayList.get(i).MRP_2.toInt().toString() + "").toInt() > 0) {

            // itemprice = Records.get(i).getProductPrice();
            holder.TextViewProductPrice.setText(itemprice.toString() + "")
        }
        val item_count = intArrayOf(0)

        val finalItemprice = itemprice

        //Toast.makeText(context, Records.get(i).getId() + "", Toast.LENGTH_SHORT).show();
        item_count[0] = db_helper.getQtyByProductId(arrayList.get(i).ID)
        if (item_count[0] >= 1) {
            holder.Btn_Display_Item.text = "" + item_count[0]
            holder.Btn_Remove_Item.text = "-"
            holder.Btn_Add_Item.text = "+"
        }

        // Button Remove ( - )

        // Button Remove ( - )
        holder.Btn_Remove_Item.setOnClickListener {
            // Run
            if (item_count[0] == 0 || item_count[0] <= 0) {
                holder.Btn_Display_Item.text = "Add"
                holder.Btn_Remove_Item.text = " "
                holder.Btn_Add_Item.text = " "
                item_count[0] = 0
                total_amount = total_amount - finalItemprice;
               // holder.Tv_total_amount.setText("₹ " + total_amount)
            } else {
                if ((arrayList.get(i).MRP_2.toInt().toString() + "").toInt() > 0) {
                    item_count[0] = item_count[0] - arrayList.get(i).Min_Qty
                    holder.Btn_Display_Item.text = item_count[0].toString() + ""
                    total_amount = total_amount + -finalItemprice
                    // holder.Tv_total_amount.setText("₹ " + total_amount)
                    if (item_count[0] == 0) {
                        holder.Btn_Display_Item.text = "Add"
                        holder.Btn_Remove_Item.text = " "
                        holder.Btn_Add_Item.text = " "
                    }
                    //}
                    if (item_count[0] == 0) {
                        db_helper.deleteByItemID(arrayList.get(i).ID)
                        getOrderTotal()
                    } else {
                        val total: String = total_amount.toString() + ""
                        db_helper.updateProduct(
                            Order_Summery_Ofline_Bean(
                                productId = arrayList.get(i).ID.toString() + "",
                                productQty = item_count[0].toString() + "",
                                productPrice = itemprice.toString() + "",
                                productTotal = (item_count[0] * itemprice).toString() + ""
                            ), arrayList.get(i).ID.toString()
                        )
                        getOrderTotal()
                    }
                }
            }
        }


        // Button ( Display Count )

        // Button ( Display Count )
        holder.Btn_Display_Item.setOnClickListener {
            if (item_count[0] == 0) {
                holder.Btn_Display_Item.text = "Add"
                item_count[0] = item_count[0] + arrayList.get(i).Min_Qty
                holder.Btn_Display_Item.text = item_count[0].toString() + ""
                holder.Btn_Remove_Item.text = "-"
                holder.Btn_Add_Item.text = "+"
                //LL_Order_Details.setVisibility(View.VISIBLE);
                total_amount =
                    total_amount + item_count[0] * finalItemprice
                // holder.Tv_total_amount.setText("₹ " + total_amount)
                val total: Int = item_count[0] * arrayList.get(i).MRP_2.toInt()
                val productId: String = arrayList.get(i).ID.toString() + ""
                db_helper.addProduct(
                    Order_Summery_Ofline_Bean(
                        arrayList.get(i).ID.toString() + "",
                        arrayList.get(i).Name, item_count[0].toString() + "",
                        itemprice.toString(),
                        total.toString() + "",
                        arrayList.get(i).productCategoryId.toString(),
                        arrayList.get(i).productCategoryName,
                        arrayList.get(i).productBrandId.toString(),
                        arrayList.get(i).productBrandName,
                        arrayList.get(i).URL_1
                    ), productId
                )
                if (item_count[0] == 0) {
                    db_helper.deleteByItemID(arrayList.get(i).ID)
                }
                getOrderTotal()
            }
        }

        // Button Add ( + )

        // Button Add ( + )
        holder.Btn_Add_Item.setOnClickListener {
            if ((arrayList.get(i).MRP_2.toInt().toString() + "").toInt() > 0) {
                item_count[0] = item_count[0] + arrayList.get(i).Min_Qty
                if (item_count[0] == 0) {
                    item_count[0] = arrayList.get(i).Min_Qty
                }
                if (item_count[0] == arrayList.get(i).Min_Qty) {
                    //Btn_Display_Item.setText("Add");
                    holder.Btn_Remove_Item.text = "-"
                    holder.Btn_Add_Item.text = "+"
                    holder.Btn_Display_Item.text = item_count[0].toString() + ""
                    //LL_Order_Details.setVisibility(View.VISIBLE);
                    total_amount = total_amount + finalItemprice
                    // holder.Tv_total_amount.setText("₹ " + total_amount)
                    val productId: String = arrayList.get(i).ID.toString() + ""
                    val total: Int = item_count[0] * arrayList.get(i).MRP_2.toInt()
                    //
                    db_helper.addProduct(
                        Order_Summery_Ofline_Bean(
                            arrayList.get(i).ID.toString() + "",
                            arrayList.get(i).Name,
                            item_count[0].toString() + "",
                            itemprice.toString(),
                            total.toString(),
                            arrayList.get(i).productCategoryId.toString(),
                            arrayList.get(i).productCategoryName,
                            arrayList.get(i).productBrandId.toString(),
                            arrayList.get(i).productBrandName,
                            arrayList.get(i).URL_1
                        ), productId
                    )
                    getOrderTotal()
                    //Toast.makeText(context, t + " : " + Records.get(i).getId() , Toast.LENGTH_SHORT).show();
                } else {
                    holder.Btn_Remove_Item.text = "-"
                    holder.Btn_Add_Item.text = "+"
                    holder.Btn_Display_Item.text = item_count[0].toString() + ""
                    //LL_Order_Details.setVisibility(View.VISIBLE);
                    total_amount = total_amount + finalItemprice
                    // holder.Tv_total_amount.setText("₹ " + total_amount)
                    val total = itemprice * item_count[0]
                    val productId: String = arrayList.get(i).ID.toString() + ""
                    db_helper.updateProduct(
                        Order_Summery_Ofline_Bean(
                            productId = productId,
                            productQty = item_count[0].toString() + "",
                            productTotal = total.toString() + ""
                        ), productId
                    )
                    getOrderTotal()
                }
            }
        }

        holder.buttonDeleteProduct.setOnClickListener {
            if(userRoll.equals("1")){
                val alertDialog2 = AlertDialog.Builder(
                    context
                )
                alertDialog2.setTitle("Confirm Delete...")
                alertDialog2.setMessage("Are you sure you want delete this file?")
                alertDialog2.setIcon(R.drawable.ic_delete_black_24dp)
                alertDialog2.setPositiveButton(
                    "YES"
                ) { dialog, which -> // Write your code here to execute after dialog
                    // delete api call
                    iAdapterOnClick.onClick(arrayList.get(i), -1)
                }

// Setting Negative "NO" Btn

// Setting Negative "NO" Btn
                alertDialog2.setNegativeButton(
                    "NO"
                ) { dialog, which -> // Write your code here to execute after dialog
                    Toast.makeText(
                        context,
                        "You clicked on NO", Toast.LENGTH_SHORT
                    )
                        .show()
                    dialog.cancel()
                }

// Showing Alert Dialog

// Showing Alert Dialog
                alertDialog2.show()

            }
        }
        holder.buttonManageStock.setOnClickListener {
            if(userRoll.equals("1")){
                context.navigateTo<ManageProductStockActivity> {
                    putExtra("productId", arrayList.get(i).ID)
                }
            }
        }

        holder.Img_Item.setOnLongClickListener {
            if(userRoll.equals("1")){
                holder.ManageProduct.visibility = View.VISIBLE
            }
            false
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size   ?: 0
    }

    fun getOrderTotal(){
        iAdapterOnClick.onClick(db_helper.getOrderTotal(), 0)
    }
}