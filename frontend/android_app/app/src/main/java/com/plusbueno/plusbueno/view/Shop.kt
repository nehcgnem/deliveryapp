package com.plusbueno.plusbueno.view

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.plusbueno.plusbueno.R
import com.plusbueno.plusbueno.data.Store
import com.plusbueno.plusbueno.data.User
import com.plusbueno.plusbueno.data.UserType
import com.plusbueno.plusbueno.parser.LoginManager
import com.plusbueno.plusbueno.parser.UniversalParser
import com.plusbueno.plusbueno.parser.exception.AuthorizationException
import com.plusbueno.plusbueno.presenter.ShopPresenter
import kotlinx.android.synthetic.main.activity_shop.*

class Shop : AppCompatActivity(), ShopView {
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ShopAdapter
    private var products = ArrayList<com.plusbueno.plusbueno.data.Product>()
    private lateinit var shopName : String
    private lateinit var viewType : UserType

    private val presenter = ShopPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        shopName = intent.getStringExtra("MYSTORE")
        viewType = if (LoginManager.getUserType() != null) {
            LoginManager.getUserType()
        } else {
            UserType.CUSTOMER
        }
        if (viewType == UserType.BUSINESS) {
            shopName = LoginManager.getUsername()
        }

        layoutManager = LinearLayoutManager(this)
        adapter = ShopAdapter(products, shopName)

        productsView.layoutManager = layoutManager
        productsView.adapter = adapter


        val task = LoadProductTask(this)
        task.execute(shopName)

        //presenter.loadData(storeName)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (LoginManager.getUserType() == UserType.BUSINESS) {
            val item = menu.add("Add")
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.title) {
            "Add" -> {
                val addIntent = Intent(this, ProductEditActivity::class.java)
                startActivity(addIntent)
                true
            }

            else -> {
                true
            }
        }
    }

    fun onUpdate(arr : Store?) {
        products.clear()
        if (arr != null) {
            products.addAll(arr.products)
            shopName = arr.name
        }
        adapter.notifyDataSetChanged()

    }

    fun onFail(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private class LoadProductTask(val activity: Shop) : AsyncTask<String, String, Store>() {
        var exception : java.lang.Exception? = null
        override fun onPostExecute(result: Store?) {
            super.onPostExecute(result)
            if (exception == null) {  // on success
                activity.onUpdate(result)
            } else {
                if (exception is AuthorizationException) {
                    activity.onFail("Login expired.")
                } else {
                    activity.onFail("Network error.")
                }

            }
        }

        override fun doInBackground(vararg p0: String?): Store? {
            var store : Store? = null
            try {
                store = when (LoginManager.getUserType()) {
                    UserType.BUSINESS -> UniversalParser.get(UniversalParser.BASE_URL_RESTFUL + "/storeuser/" + p0[0], Store::class.java)
                    UserType.CUSTOMER, null -> UniversalParser.get(UniversalParser.BASE_URL_RESTFUL + "/store/" + p0[0], Store::class.java)
                }
            } catch (e : java.lang.Exception) {
                exception = e
            }
            return store

        }
    }
}
