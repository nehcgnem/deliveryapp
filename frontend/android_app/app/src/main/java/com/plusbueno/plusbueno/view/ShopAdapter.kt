package com.plusbueno.plusbueno.view

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.plusbueno.plusbueno.R
import com.plusbueno.plusbueno.data.Product
import com.plusbueno.plusbueno.data.Store
import com.plusbueno.plusbueno.data.UserType
import com.plusbueno.plusbueno.parser.LoginManager
import kotlinx.android.synthetic.main.product_list_item.view.*

class ShopAdapter(private val products: ArrayList<Product>, val shopName: String) : RecyclerView.Adapter<ShopAdapter.ProductHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopAdapter.ProductHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.product_list_item, parent, false)

        return ProductHolder(itemView, shopName)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ShopAdapter.ProductHolder, position: Int) {
        val product = products[position]
        holder.bindProduct(product)
    }


    class ProductHolder(v: View, val shopName: String) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var product: Product? = null
        private var store: Store? = null

        init {
            v.showProductBtn.setOnClickListener(this)
        }

        fun bindProduct(product: Product) {
            this.product = product
            view.productName.text = product.name
            view.productPrice.text = "$" +  product.price/100 + "." + product.price%100
        }

        override fun onClick(v: View) {
            val context = itemView.context
            val showProductIntent = when(LoginManager.getUserType()) {
                UserType.CUSTOMER, null -> Intent(context, com.plusbueno.plusbueno.view.Product::class.java).apply {
                    putExtra(PRODUCT_KEY, product)
                    putExtra("SHOP_NAME", shopName)
                }
                UserType.BUSINESS -> Intent(context, com.plusbueno.plusbueno.view.ProductEditActivity::class.java).apply {
                    putExtra(PRODUCT_KEY, product)
                    putExtra("SHOP_NAME", shopName)
                }
            }
            context.startActivity(showProductIntent)

        }

        companion object {
            private val PRODUCT_KEY = "PRODUCT"

            fun getKey() : String {
                return PRODUCT_KEY
            }
        }
    }
}