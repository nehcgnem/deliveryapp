package com.plusbueno.plusbueno.presenter

import android.util.Log
import com.plusbueno.plusbueno.data.Order
import com.plusbueno.plusbueno.data.ServerOrder
import com.plusbueno.plusbueno.networking.SimpleLoadTask
import com.plusbueno.plusbueno.parser.LoginManager
import com.plusbueno.plusbueno.parser.UniversalParser
import com.plusbueno.plusbueno.view.OrderListActivity

/**
 * Created by LZMA on 2018/11/11.
 */
class OrderListPresenter(val view : OrderListActivity) : SimpleLoadPresenter<Array<ServerOrder>> {

    fun loadOrders() {
        val task = SimpleLoadTask<Array<ServerOrder>>(this, Array<ServerOrder>::class.java)
        task.execute(UniversalParser.BASE_URL_RESTFUL + "/order/" + LoginManager.getUsername() + "/")
    }

    override fun onSimpleLoadSuccess(result: Array<ServerOrder>) {
        view.onLoadSuccess(result)
        Log.e("LOADODR", "load success" + result.size)
    }

    override fun onSimpleLoadFail(reason: String) {
        view.onLoadFailed(reason)
    }
}