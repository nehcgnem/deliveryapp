package com.plusbueno.plusbueno.view

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.plusbueno.plusbueno.R
import com.plusbueno.plusbueno.data.Store
import com.plusbueno.plusbueno.parser.UniversalParser
import kotlinx.android.synthetic.main.activity_shop_catalog.*

class ShopCatalogActivity : AppCompatActivity() {
    private lateinit var adapter : ShopCatalogAdapter
    private var stores = ArrayList<Store>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_catalog)

        adapter = ShopCatalogAdapter(stores)

        catalog_list.adapter = adapter
        catalog_list.layoutManager = LinearLayoutManager(this)

        val task = LoadStoresTask(this)
        task.execute()
    }

    fun onLoadSuccess(s : Array<Store>) {
        stores.clear()
        stores.addAll(s)
        adapter.notifyDataSetChanged()
    }
    fun toastMe(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private class LoadStoresTask(val activity: ShopCatalogActivity) : AsyncTask<String, String, Array<Store>>() {
        var exception : java.lang.Exception? = null
        override fun onPostExecute(result: Array<Store>) {
            super.onPostExecute(result)
            if (exception != null) {
                activity.toastMe("Network error.")
            } else {
                activity.onLoadSuccess(result)
            }
        }

        override fun doInBackground(vararg params: String?): Array<Store>? {
            var result : Array<Store>? = null
            try {
                result = UniversalParser.get(UniversalParser.BASE_URL_RESTFUL + "/allstores/", Array<Store>::class.java)
            } catch (e : Exception) {
                exception = e
            }
            return result
        }
    }
}
