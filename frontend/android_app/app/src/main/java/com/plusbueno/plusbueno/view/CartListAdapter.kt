package com.plusbueno.plusbueno.view

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.plusbueno.plusbueno.R
import com.plusbueno.plusbueno.data.LineItemDetail
import com.plusbueno.plusbueno.data. LocalCart
import com.plusbueno.plusbueno.data.Store
import kotlinx.android.synthetic.main.cart_product_item.view.*

/**
 * Created by LZMA on 2018/11/17.
 */
class CartListAdapter(val cartList : List<Any>, val activity: CartActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class StoreViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var item : Store

        fun bindStore(store: Store) {
            item = store
            view.findViewById<TextView>(R.id.cart_store_name).text = item.name
        }
    }

    class ProductViewHolder(val view: View, val activity: CartActivity) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var item : LineItemDetail

        init {
            view.cart_btn_minus.setOnClickListener(this)
            view.cart_btn_plus.setOnClickListener(this)
            view.cart_btn_remove.setOnClickListener(this)
        }

        fun bindLineItem(lineItemDetail: LineItemDetail) {
            item = lineItemDetail
            view.findViewById<TextView>(R.id.cart_product_name).text = item.product.name
            view.findViewById<TextView>(R.id.cart_product_price).text = "$" + item.product.price/100 + "." + item.product.price%100
            view.findViewById<TextView>(R.id.cart_product_qty).text = item.qty.toString()
        }

        override fun onClick(v: View) {
            when (v.id) {
                R.id.cart_btn_minus -> {
                    if (item.qty > 1) {
                        LocalCart.minusOne(item)
                        item.qty--
                        view.findViewById<TextView>(R.id.cart_product_qty).text = item.qty.toString()
                        activity.notifyPriceUpdate()
                    }
                }
                R.id.cart_btn_plus -> {
                    LocalCart.plusOne(item)
                    item.qty++
                    view.findViewById<TextView>(R.id.cart_product_qty).text = item.qty.toString()
                    activity.notifyPriceUpdate()
                }
                R.id.cart_btn_remove -> {
                    LocalCart.removeItem(item.storeId, item.product.name)
                    activity.notifyReloadList()
                    activity.notifyPriceUpdate()
                }
                else -> {

                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {  //store
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.cart_store_item, parent, false) as View
            return StoreViewHolder(view)
        } // line item
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cart_product_item, parent, false) as View
        return ProductViewHolder(view, activity)

    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is StoreViewHolder) {
            holder.bindStore(cartList[position] as Store)
        } else if (holder is ProductViewHolder) {
            holder.bindLineItem(cartList[position] as LineItemDetail)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (cartList[position] is LineItemDetail) {
            return 0
        } else if (cartList[position] is Store) {
            return 1
        }
        Log.e("CartListAdapter", "Unexpected item" + cartList[position].toString())
        return -1
    }

}