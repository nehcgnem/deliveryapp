package com.plusbueno.plusbueno.view

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.plusbueno.plusbueno.R
import com.plusbueno.plusbueno.data.LocalCart
import com.plusbueno.plusbueno.data.Product
import com.plusbueno.plusbueno.data.UserType
import com.plusbueno.plusbueno.parser.LoginManager
import com.plusbueno.plusbueno.parser.UniversalParser
import com.plusbueno.plusbueno.parser.exception.NetworkErrorException
import com.plusbueno.plusbueno.parser.util.HttpUtil
import kotlinx.android.synthetic.main.activity_product.*
import java.io.InputStream

class Product : AppCompatActivity() {

    private lateinit var product: Product
    private lateinit var shopName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        if (LoginManager.getUserType() == UserType.BUSINESS) {
            purchase_btn.visibility = View.INVISIBLE
        }

        product = intent.getParcelableExtra(ShopAdapter.ProductHolder.getKey())
        shopName = intent.getStringExtra("SHOP_NAME")

        productName.text = product.name
//        productStore.text = product.productStore
        productDesc.text = product.description

        // val loadImageTask = LoadImageTask()
        // loadImageTask.execute(UniversalParser.BASE_URL_STATIC + "?")
        purchase_btn.setOnClickListener {
            LocalCart.addItem(shopName, product.name, 1)
            val i = Intent(this, CartActivity::class.java)
            startActivity(i)
        }


    }

    fun onNetworkError() {
        Toast.makeText(this, R.string.network_fail, Toast.LENGTH_SHORT).show()
    }

    fun onImageLoaded(stream : InputStream) {
        val image = Drawable.createFromStream(stream, null)  // srcName used when creating 9patch
        imageView.setImageDrawable(image)
    }


    private inner class LoadImageTask : AsyncTask<String, String, InputStream>() {
        var exception : java.lang.Exception? = null
        override fun doInBackground(vararg p0: String?): InputStream? {
            try{
                return HttpUtil.getStream(p0[0])
            } catch (e : NetworkErrorException) {
                exception = e
            }
            return null

        }

        override fun onPostExecute(result: InputStream?) {
            super.onPostExecute(result)
            if (result != null) {
                onImageLoaded(result)
            } else if (exception != null) {
                onNetworkError()
            }
        }
    }
}
