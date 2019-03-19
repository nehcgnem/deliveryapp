package com.plusbueno.plusbueno.view

import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.plusbueno.plusbueno.R
import com.plusbueno.plusbueno.data.Product
import com.plusbueno.plusbueno.parser.LoginManager
import com.plusbueno.plusbueno.parser.UniversalParser
import com.plusbueno.plusbueno.parser.util.HttpUtil
import com.plusbueno.plusbueno.presenter.ProductEditPresenter
import kotlinx.android.synthetic.main.activity_product_edit.*
import java.io.InputStream
import kotlin.math.roundToInt

class ProductEditActivity : AppCompatActivity() {
    private val productEditPresenter: ProductEditPresenter = ProductEditPresenter(this)
    var productId : Long = -1
    var product : Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_edit)

        product = intent.getParcelableExtra<Product>("PRODUCT")
        val p = product
        if (p != null) productId = p.id

        product_edit_btn_confirm.setOnClickListener {
            val task = PublishTask(this)
            val product = Product()
            if (productId.compareTo(-1) != 0) product.id = productId
            product.name = product_edit_name.text.toString()
            product.description = product_edit_desc.text.toString()
            product.price =  (product_edit_price.text.toString().toFloat() * 100).roundToInt()
            product.quantity = product_edit_qty.text.toString().toInt()

            task.execute(product)
        }

        // productId = intent.getLongExtra("PRODUCT", -1)
        if (productId.compareTo(-1) == 0) {
            // Log.e("PRODUCT_EDIT", "No ID given!!")
            Toast.makeText(this, "Create new product...", Toast.LENGTH_SHORT).show()
        } else {
            // try load product info
            // productEditPresenter.loadInfo(productId)
            if (p != null) onGetProductInfo(p)
        }

    }

    fun onGetProductInfo(product: com.plusbueno.plusbueno.data.Product) {
        product_edit_name.setText(product.name)
        product_edit_desc.setText(product.description)
        product_edit_price.setText((product.price.toFloat() /100).toString())
        product_edit_qty.setText(product.quantity.toString())

        // try load img
        // productEditPresenter.loadImage(product.imageUrl)
    }

    fun onFailProductInfo(reason: String) {
        Toast.makeText(this, reason, Toast.LENGTH_SHORT).show()
        finish()
    }

    fun onGetImage(inputStream: InputStream) {
        val d = BitmapDrawable(resources, inputStream)
        product_edit_image.setImageDrawable(d)
    }

    fun onFailImage(reason: String) {
        // TODO show a refresh icon

    }

    fun onUploadSuccess() {
        Toast.makeText(this, R.string.upload_success, Toast.LENGTH_SHORT).show()
        finish()
    }

    fun onUploadFail(reason : String) {
        Toast.makeText(this, reason, Toast.LENGTH_SHORT).show()
    }

    private class PublishTask(val activity : ProductEditActivity) : AsyncTask<Product, String, String>() {
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            activity.onUploadSuccess()
        }

        override fun doInBackground(vararg params: Product?): String {
            if(activity.productId.compareTo(-1) == 0) {
                HttpUtil.post(UniversalParser.BASE_URL_RESTFUL+"/storeuser/"+LoginManager.getUsername() + "/", params[0], Product::class.java)
            } else {
                HttpUtil.put(UniversalParser.BASE_URL_RESTFUL+"/storeuser/"+LoginManager.getUsername() + "/", params[0], Product::class.java)
            }
            return ""
        }
    }
}
