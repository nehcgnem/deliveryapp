package com.plusbueno.plusbueno.view

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.plusbueno.plusbueno.R
import com.plusbueno.plusbueno.data.Order
import com.plusbueno.plusbueno.data.ServerOrder
import kotlinx.android.synthetic.main.order_list_item.view.*

/**
 * Created by LZMA on 2018/11/11.
 */
class OrderListAdapter(private val orders : ArrayList<ServerOrder>) : RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var order : ServerOrder
        // private var view: View = v

        init {
            view.setOnClickListener(this)
        }
        fun bindOrder(order : ServerOrder) {
            this.order = order
            view.order_list_detail.text = order.orderedProduct1+" ..."
            view.order_list_id.text = "#" + order.orderName
            view.order_list_status.text = order.statusText()
        }

        override fun onClick(p0: View?) {
            val showDetailIntent = Intent(itemView.context, MapsActivity::class.java).apply {
                putExtra("ORDER_NAME", order.orderName)
//                putExtra("ORDER_STATUS", order.statusText())
            }
            itemView.context.startActivity(showDetailIntent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.order_list_item, parent, false) as View

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        holder.bindOrder(order)

    }
}