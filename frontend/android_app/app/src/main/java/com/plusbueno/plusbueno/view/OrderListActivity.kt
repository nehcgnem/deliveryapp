package com.plusbueno.plusbueno.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.plusbueno.plusbueno.R
import com.plusbueno.plusbueno.data.Order
import com.plusbueno.plusbueno.data.ServerOrder
import com.plusbueno.plusbueno.presenter.OrderListPresenter
import kotlinx.android.synthetic.main.activity_order_list.*

class OrderListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager : RecyclerView.LayoutManager
    private val presenter = OrderListPresenter(this)
    private var orders = ArrayList<ServerOrder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        viewManager = LinearLayoutManager(this)
        viewAdapter = OrderListAdapter(orders)

        order_list_recycler_view.layoutManager = viewManager
        order_list_recycler_view.adapter = viewAdapter

        presenter.loadOrders()
    }

    fun onLoadSuccess(orders : Array<ServerOrder>) {
        this.orders.addAll(orders)
        order_list_recycler_view.adapter.notifyDataSetChanged()
    }

    fun onLoadFailed(reason : String) {
        Toast.makeText(this, reason, Toast.LENGTH_SHORT).show()
    }
}
