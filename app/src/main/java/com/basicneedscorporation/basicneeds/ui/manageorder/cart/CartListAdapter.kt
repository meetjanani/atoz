package com.basicneedscorporation.basicneeds.ui.manageorder.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.base.offlinedb.Order_Summery_Db_Helper
import com.basicneedscorporation.basicneeds.base.offlinedb.Order_Summery_Ofline_Bean
import com.growinginfotech.businesshub.base.IAdapterOnClick
import com.growinginfotech.businesshub.base.loadImage
import com.growinginfotech.businesshub.base.total_amount

class CartListAdapter(val iAdapterOnClick: IAdapterOnClick) :
    RecyclerView.Adapter<CartListAdapter.MyViewHolder>() {

    private lateinit var context: Context
    private var arrayList: List<Order_Summery_Ofline_Bean> = listOf()
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

        init {
            TextViewCategoryBrandName = itemView.findViewById(R.id.TextViewCategoryBrandName)
            TextViewProductName = itemView.findViewById(R.id.TextViewProductName)
            Btn_Remove_Item = itemView.findViewById(R.id.Btn_Remove_Item)
            Btn_Display_Item = itemView.findViewById(R.id.Btn_Display_Item)
            Btn_Add_Item = itemView.findViewById(R.id.Btn_Add_Item)
            TextViewProductPrice = itemView.findViewById(R.id.TextViewProductPrice)
            Img_Item = itemView.findViewById(R.id.Img_Item)
        }
    }

    // Removed ContactsAdapterListener listener
    fun CartListAdapter(
        context: Context,
        arrayList: List<Order_Summery_Ofline_Bean>,
        string: String?
    ) {
        this.context = context
        //  this.listener = listener;
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
        holder.TextViewProductName.text = "${arrayList.get(i).productName}"
        context?.let { loadImage(arrayList.get(i).productUrl1, holder.Img_Item, it) }
//        holder.category_card.setOnClickListener {
//            iAdapterOnClick.onClick(arrayList.get(i), i)
//        }
//        Glide.with(context)
//                .load(Records.get(i).getImageUrl1() +"")
//                .into(holder.draweeView);
        // val db_helper = Order_Summery_Db_Helper(context, null)
        val itemprice: Int = arrayList.get(i).productPrice.toInt()

        if ((arrayList.get(i).productPrice.toInt().toString() + "").toInt() > 0) {

            // itemprice = Records.get(i).getProductPrice();
            holder.TextViewProductPrice.setText(itemprice.toString() + "")
        }
        val item_count = intArrayOf(0)

        val finalItemprice = itemprice

        //Toast.makeText(context, Records.get(i).getId() + "", Toast.LENGTH_SHORT).show();
        item_count[0] = db_helper.getQtyByProductId(arrayList.get(i).productId.toInt())
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
                if ((arrayList.get(i).productPrice.toInt().toString() + "").toInt() > 0) {
                    item_count[0]--
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
                        db_helper.deleteByItemID(arrayList.get(i).productId.toInt())
                        getOrderTotal()
                    } else {
                        val total: String = total_amount.toString() + ""
                        db_helper.updateProduct(
                            Order_Summery_Ofline_Bean(
                                productId = arrayList.get(i).productId.toString() + "",
                                productQty = item_count[0].toString() + "",
                                productPrice = itemprice.toString() + "",
                                productTotal =  (   item_count[0] * itemprice).toString() + ""
                            ), arrayList.get(i).productId.toString()
                        )
                        getOrderTotal()
                    }
                } else {
//                    val deductable_price: Int =
//                        db_helper.get_Varient_Price_By_Item_ID(Records.get(i).productId)
//                    Common_Class.total_amount = Common_Class.total_amount - deductable_price
//                    item_count[0]--
//                    holder.Btn_Display_Item.text = item_count[0].toString() + ""
//                    holder.Tv_total_amount.setText("₹ " + Common_Class.total_amount)
//                    if (item_count[0] == 0) {
//                        db_helper.deleteByItemID(Records.get(i).productId)
//                        holder.Btn_Display_Item.text = "Add"
//                        holder.Btn_Remove_Item.text = " "
//                        holder.Btn_Add_Item.text = " "
//                    }
                }
            }
        }


        // Button ( Display Count )

        // Button ( Display Count )
        holder.Btn_Display_Item.setOnClickListener {
            if (item_count[0] == 0) {
                holder.Btn_Display_Item.text = "Add"
                item_count[0] = item_count[0] + 1
                holder.Btn_Display_Item.text = item_count[0].toString() + ""
                holder.Btn_Remove_Item.text = "-"
                holder.Btn_Add_Item.text = "+"
                //LL_Order_Details.setVisibility(View.VISIBLE);
                total_amount =
                    total_amount + item_count[0] * finalItemprice
                // holder.Tv_total_amount.setText("₹ " + total_amount)
                val total: Int = item_count[0] * arrayList.get(i).productPrice.toInt()
                //Toast.makeText(context, item_count[0] + " : " + arrayList.get(i).getId() , Toast.LENGTH_SHORT).show();
                val productId: String = arrayList.get(i).productId.toString() + ""
                db_helper.addProduct(
                    Order_Summery_Ofline_Bean(
                        arrayList.get(i).productId.toString() + "",
                        arrayList.get(i).productName, item_count[0].toString() + "",
                        itemprice.toString(),
                        total.toString() + "",
                        arrayList.get(i).productCategoryId.toString(),
                        arrayList.get(i).productCategoryName,
                        arrayList.get(i).productBrandId.toString(),
                        arrayList.get(i).productBrandName,
                        arrayList.get(i).productUrl1
                    ), productId
                )
                getOrderTotal()
                if (item_count[0] == 0) {
                    db_helper.deleteByItemID(arrayList.get(i).productId.toInt())
                    getOrderTotal()
                }
            }
        }

        // Button Add ( + )

        // Button Add ( + )
        holder.Btn_Add_Item.setOnClickListener {
            //                item_count[0] = item_count[0] + 1;
//
//                if (item_count[0]==0)
//                {
//                    item_count[0] = 1;
//                }
            if ((arrayList.get(i).productPrice.toInt().toString() + "").toInt() > 0) {
                item_count[0] = item_count[0] + 1
                if (item_count[0] == 0) {
                    item_count[0] = 1
                }
                if (item_count[0] == 1) {
                    //Btn_Display_Item.setText("Add");
                    holder.Btn_Remove_Item.text = "-"
                    holder.Btn_Add_Item.text = "+"
                    holder.Btn_Display_Item.text = item_count[0].toString() + ""
                    //LL_Order_Details.setVisibility(View.VISIBLE);
                    total_amount = total_amount + finalItemprice
                    // holder.Tv_total_amount.setText("₹ " + total_amount)
                    val productId: String = arrayList.get(i).productId.toString() + ""
                    //
                    db_helper.addProduct(
                        Order_Summery_Ofline_Bean(
                            arrayList.get(i).productId.toString() + "",
                            arrayList.get(i).productName,
                            item_count[0].toString() + "",
                            itemprice.toString(),
                            itemprice.toString(),
                            arrayList.get(i).productCategoryId.toString(),
                            arrayList.get(i).productCategoryName,
                            arrayList.get(i).productBrandId.toString(),
                            arrayList.get(i).productBrandName,
                            arrayList.get(i).productUrl1
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
                    //Toast.makeText(context, item_count[0] + " : " + total  + " : " + Records.get(i).getId().toString(), Toast.LENGTH_SHORT).show();
                    val productId: String = arrayList.get(i).productId.toString() + ""
                    db_helper.updateProduct(
                        Order_Summery_Ofline_Bean(
                            productId = productId,
                            productQty =  item_count[0].toString() + "",
                            productTotal = total.toString() + ""
                        ), productId
                    )
                    getOrderTotal()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size   ?: 0
    }

    fun getOrderTotal(){
        iAdapterOnClick.onClick(db_helper.getOrderTotal(), 0)
    }
}