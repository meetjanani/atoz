package com.basicneedscorporation.basicneeds.base.offlinedb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class Order_Summery_Db_Helper(
    context: Context,
    factory: SQLiteDatabase.CursorFactory?
) : SQLiteOpenHelper(
    context, "BasicNeedsDb",
    factory, 1
) {

    private val Db_Version = 1
    private val Db_Name = "BasicNeedsDb"
    private val Table_Name = "Order_tbl"

    private val id = "id"
    private val productID = "productID"
    private val productName = "productName"
    private val productQty = "productQty"
    private val productPrice = "productPrice"
    private val productTotal = "productTotal"
    private val productCategoryId = "productCategoryId"
    private val productCategoryName = "productCategoryName"
    private val productBrandId = "productBrandId"
    private val productBrandName = "productBrandName"
    private val productUrl1 = "productUrl1"
    private val productorderTotal = "productorderTotal"
    private val productCode = "productCode"
    private val Pack_Size = "Pack_Size"
    private val Min_Qty = "Min_Qty"
    private val PcsInUnit = "PcsInUnit"

    override fun onCreate(db: SQLiteDatabase?) {
        val Create_Table = "CREATE TABLE " + Table_Name +
                "(" +
                id + " INTEGER PRIMARY KEY," +
                productID + " TEXT," +
                productName + " TEXT," +
                productQty + " TEXT," +
                productPrice + " TEXT," +
                productTotal + " TEXT," +
                productCategoryId + " TEXT," +
                productCategoryName + " TEXT," +
                productBrandId + " TEXT," +
                productBrandName + " TEXT," +
                productUrl1 + " TEXT," +
                productorderTotal + " TEXT," +
                productCode + " TEXT," +
                Pack_Size + " TEXT," +
                Min_Qty + " TEXT," +
                PcsInUnit + " TEXT" +
                ")"
        db!!.execSQL(Create_Table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + Table_Name)
        onCreate(db)
    }

    // code to get all contacts in a list view
    fun getAllProduct(): List<Order_Summery_Ofline_Bean> {
        val Product_list: MutableList<Order_Summery_Ofline_Bean> =
            ArrayList<Order_Summery_Ofline_Bean>()
        // Select All Query
        val selectQuery =
            "SELECT  * FROM " + Table_Name
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val product = Order_Summery_Ofline_Bean()
                product.productId = (cursor.getString(1))
                product.productName = (cursor.getString(2))
                product.productQty = (cursor.getString(3))
                product.productPrice = (cursor.getString(4))
                product.productTotal = (cursor.getString(5))
                product.productCategoryId = (cursor.getString(6))
                product.productCategoryName = (cursor.getString(7))
                product.productBrandId = (cursor.getString(8))
                product.productBrandName = (cursor.getString(9))
                product.productUrl1 = (cursor.getString(10))
                product.productCode = (cursor.getString(11))
                product.Pack_Size = (cursor.getString(12))
                product.Min_Qty = (cursor.getString(13))
                product.PcsInUnit = (cursor.getString(15))
                // Adding contact to list
                Product_list.add(product)
            } while (cursor.moveToNext())
        }

        // return contact list
        return Product_list
    }

    fun addProduct(order_items: Order_Summery_Ofline_Bean, productId: String?) {
        val db = this.writableDatabase
        val cv = ContentValues()
        // cv.put(User_id,usr.getId());
        cv.put(productID, productId)
        cv.put(productName, order_items.productName)
        cv.put(productQty, order_items.productQty)
        cv.put(productPrice, order_items.productPrice)
        cv.put(productTotal, order_items.productTotal)
        cv.put(productCategoryId, order_items.productCategoryId)
        cv.put(productCategoryName, order_items.productCategoryName)
        cv.put(productBrandId, order_items.productBrandId)
        cv.put(productBrandName, order_items.productBrandName)
        cv.put(productUrl1, order_items.productUrl1)
        cv.put(productCode, order_items.productCode)
        cv.put(Pack_Size, order_items.Pack_Size)
        cv.put(Min_Qty, order_items.Min_Qty)
        cv.put(PcsInUnit, order_items.PcsInUnit)
        db.insert(Table_Name, null, cv)
        db.close()
    }

    // code to update the single order
    fun updateProduct(order_items: Order_Summery_Ofline_Bean, productId: String) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(productQty, order_items.productQty)
        cv.put(productTotal, order_items.productTotal)
        // updating row
        val result =  db.update(
            Table_Name,
            cv,
            productID + "=?",
            arrayOf(productId)
        )
    }

    fun getQtyByProductId(productId: Int): Int {
        //Item_Details_Bean order = new Item_Details_Bean();
        var Total_Qty = 0
        val countQuery =
            "SELECT SUM(" + productQty + ") as productQty FROM " + Table_Name +
                    " WHERE " + productID + " = " + productId + " ;"
        val db = this.readableDatabase
        val cursor = db.rawQuery(countQuery, null)
        if (cursor.moveToFirst()) {
            do {
                Total_Qty = cursor.getInt(0)
            } while (cursor.moveToNext())
            cursor.close()
        }
        return Total_Qty
    }
    fun getOrderTotal(): Float {
        //Item_Details_Bean order = new Item_Details_Bean();
        var Total = 0F
        val countQuery =
            "SELECT SUM(" + productTotal + ") as productTotal FROM " + Table_Name + ";"
        val db = this.readableDatabase
        val cursor = db.rawQuery(countQuery, null)
        if (cursor.moveToFirst()) {
            do {
                Total = cursor.getFloat(0)
            } while (cursor.moveToNext())
            cursor.close()
        }
        updateOrderTotal(Total)
        return Total
    }

    fun updateOrderTotal(orderTotal: Float): Int {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(productorderTotal, orderTotal.toString())
        // updating row
        return db.update(
            Table_Name,
            cv,
            null,
            null
        )
    }

    // Deleting single order by itemid
    fun deleteByItemID(id: Int) {
        val db = this.writableDatabase
        db.delete(
            Table_Name, productID + " = " + id,
            null
        )
        db.close()
    }
    // Deleting single order by itemid
    fun deleteAll() {
        val db = this.writableDatabase
        db.delete(
            Table_Name, null,
            null
        )
        db.close()
    }
}