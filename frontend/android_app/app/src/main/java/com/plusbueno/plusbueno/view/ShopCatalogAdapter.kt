package com.plusbueno.plusbueno.view

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.plusbueno.plusbueno.R
import com.plusbueno.plusbueno.data.Store
import kotlinx.android.synthetic.main.product_list_item.view.*
import kotlinx.android.synthetic.main.shop_catalog_list_item.view.*

class ShopCatalogAdapter(private val stores: ArrayList<Store>) : RecyclerView.Adapter<ShopCatalogAdapter.ProductHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopCatalogAdapter.ProductHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.shop_catalog_list_item, parent, false)

        return ProductHolder(itemView)
    }

    override fun getItemCount() = stores.size

    override fun onBindViewHolder(holder: ShopCatalogAdapter.ProductHolder, position: Int) {
        val store = stores[position]
        holder.bindProduct(store)
    }


    class ProductHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private lateinit var store: Store

        init {
             v.products_btn.setOnClickListener(this)
        }

        fun bindProduct(store: Store) {
            this.store = store
            view.shop_catalog_name.text = store.name
            view.shop_catalog_detail.text = store.description
        }

        override fun onClick(v: View) {
            val context = itemView.context
            val showProductIntent = Intent(context, Shop::class.java).apply {
                putExtra("MYSTORE", store.name)
            }
            context.startActivity(showProductIntent)

        }

    }
}