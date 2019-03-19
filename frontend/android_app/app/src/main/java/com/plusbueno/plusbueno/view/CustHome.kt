package com.plusbueno.plusbueno.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.plusbueno.plusbueno.R

class CustHome : AppCompatActivity(), CustHomeView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cust_home)
    }

    fun openStore(view: View) {
        val intent = Intent(this, ShopCatalogActivity::class.java)
        startActivity(intent)
    }

    fun trackPackage(view: View) {
        val intent = Intent(this, OrderListActivity::class.java)
        startActivity(intent)
    }
}
