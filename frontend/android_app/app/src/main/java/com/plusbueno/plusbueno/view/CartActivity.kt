package com.plusbueno.plusbueno.view

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.plusbueno.plusbueno.R
import com.plusbueno.plusbueno.data.LocalCart
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity() {
    private var cartList : MutableList<Any> = ArrayList()
    private lateinit var viewAdapter : RecyclerView.Adapter<*>
    private lateinit var viewManager : RecyclerView.LayoutManager
    private var total : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        viewManager = LinearLayoutManager(this)
        viewAdapter = CartListAdapter(cartList, this)

        cart_recyclerView.layoutManager = viewManager
        cart_recyclerView.adapter = viewAdapter
        cart_checkout.setOnClickListener {
//            val paymentIntent = Intent(this, PaymentActivity::class.java)
//            paymentIntent.putExtra("TOTAL", total)
            if (total == 0) {
                toastMe("Please wait")
            } else {
                val task = SubmitOrderTask(this)
                task.execute()
            }

        }
        cart_total.text = "loading..."

        notifyReloadList()
        notifyPriceUpdate()
    }

    fun notifyPriceUpdate() {
        val loadTotalTask = LoadTotalTask(this)
        loadTotalTask.execute()
    }

    fun notifyReloadList() {
        val reloadTask = ReloadTask(this)
        reloadTask.execute()
    }

    fun onTotalCallback(total : Int) {
        this.total = total
        cart_total.text = "$" + total/100 + "." + total%100

    }

    fun onReloadCallback(cart : List<Any>) {
        cartList.clear()
        cartList.addAll(cart)
        cart_recyclerView.adapter.notifyDataSetChanged()
    }

    fun toastMe(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun onSubmitOrderSuccessful(orderName : String) {
        val intent = Intent(this, PaymentActivity::class.java).apply {
            putExtra("ORDER_NAME", orderName)
            putExtra("TOTAL", total)
        }
        startActivity(intent)
        finish()
    }



}

class LoadTotalTask(val activity: CartActivity) : AsyncTask<String, String, Int>() {
    var exception : Exception? = null

    override fun onPostExecute(result: Int) {
        super.onPostExecute(result)
        if (exception != null) {
            activity.toastMe("Network error.")
        } else {
            activity.onTotalCallback(result)
        }

    }

    override fun doInBackground(vararg params: String?): Int {
        try {
            return LocalCart.getPrice()
        }
        catch (e : Exception) {
            exception = e
            return -1
        }
    }
}

class ReloadTask(val activity: CartActivity) : AsyncTask<String, String, List<Any>>() {
    var exception : Exception? = null

    override fun onPostExecute(result: List<Any>) {
        super.onPostExecute(result)
        if (exception == null) {
            activity.onReloadCallback(result)
        } else {
            activity.toastMe("Network error.")
        }
    }

    override fun doInBackground(vararg params: String?): List<Any>? {
        try {
            return LocalCart.getGroupedProductAsList()
        } catch (e : Exception) {
            exception = e
            return null
        }
    }

}

class SubmitOrderTask(val activity: CartActivity) : AsyncTask<String, String, String>() {
    var exception: java.lang.Exception? = null

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        if (exception == null) {
            activity.onSubmitOrderSuccessful(result)
        } else {
            activity.toastMe("Network error")
        }


    }

    override fun doInBackground(vararg p0: String?): String? {
        return try {
            LocalCart.checkout();
        } catch (e: java.lang.Exception) {
            exception = e
            null
        }

    }
}
