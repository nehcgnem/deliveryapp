package com.plusbueno.plusbueno.presenter

import com.plusbueno.plusbueno.data.Product
import com.plusbueno.plusbueno.networking.SimpleLoadTask
import com.plusbueno.plusbueno.networking.URLStreamTask
import com.plusbueno.plusbueno.parser.UniversalParser
import com.plusbueno.plusbueno.view.ProductEditActivity
import java.io.InputStream

/**
 * Created by LZMA on 2018/11/14.
 */
class ProductEditPresenter(val view : ProductEditActivity) : SimpleLoadPresenter<Product>, URLStreamPresenter{

    fun loadInfo(id : Long) {
        val task = SimpleLoadTask<Product>(this, Product::class.java)
        task.execute(UniversalParser.BASE_URL_RESTFUL + "/myProduct/" + id.toString())
    }

    fun loadImage(url : String) {
        val task = URLStreamTask(this)
        task.execute(url)
    }

    override fun onSimpleLoadSuccess(result: Product) {
        view.onGetProductInfo(result)
    }

    override fun onSimpleLoadFail(reason: String) {
        view.onFailProductInfo(reason)
    }

    override fun onStreamOpened(stream: InputStream) {
        view.onGetImage(stream)
    }

    override fun onStreamError(reason: String) {
        view.onFailImage(reason)
    }
}