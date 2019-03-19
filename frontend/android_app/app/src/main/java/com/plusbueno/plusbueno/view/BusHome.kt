package com.plusbueno.plusbueno.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.plusbueno.plusbueno.R
import com.plusbueno.plusbueno.parser.LoginManager
import kotlinx.android.synthetic.main.activity_bus_home.*

class BusHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_home)

        bushome_btn_add.setOnClickListener {
            val addItemIntent = Intent(this, ProductEditActivity::class.java)
            startActivity(addItemIntent)
        }

        bushome_btn_view.setOnClickListener {
            val addItemIntent = Intent(this, Shop::class.java).apply {
                putExtra("MYSTORE", LoginManager.getUsername())
            }
            startActivity(addItemIntent)
        }
    }
}
