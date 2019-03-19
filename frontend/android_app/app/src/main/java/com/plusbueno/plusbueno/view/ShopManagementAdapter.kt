package com.plusbueno.plusbueno.view

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.plusbueno.plusbueno.R
import com.plusbueno.plusbueno.data.Product
import com.plusbueno.plusbueno.data.Store
import kotlinx.android.synthetic.main.product_list_item.view.*

class ShopManagementAdapter(private val products: ArrayList<Product>) : RecyclerView.Adapter<ShopManagementAdapter.ProductHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopManagementAdapter.ProductHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.product_list_edit_item, parent, false)

        return ProductHolder(itemView)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ShopManagementAdapter.ProductHolder, position: Int) {
        val product = products[position]
        holder.bindProduct(product)
    }


    class ProductHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var product: Product? = null
        private var store: Store? = null

        init {
            v.showProductBtn.setOnClickListener(this)
        }

        fun bindProduct(product: Product) {
            this.product = product
            view.productName.text = product.name
            view.productPrice.text = product.price.toString()
        }

        override fun onClick(v: View) {
            val context = itemView.context
            val showProductIntent = Intent(context, ProductEditActivity::class.java).apply {
                putExtra(PRODUCT_KEY, product)  // TODO Store id
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