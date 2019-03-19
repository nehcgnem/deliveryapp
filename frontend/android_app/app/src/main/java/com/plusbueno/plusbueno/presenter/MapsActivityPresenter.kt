package com.plusbueno.plusbueno.presenter

import com.plusbueno.plusbueno.networking.mapUpdateAsyncTask
import com.plusbueno.plusbueno.view.MapsActivityView

class MapsActivityPresenter (val view: MapsActivityView, val orderName: String) {
    fun updateMap() {
        val task = mapUpdateAsyncTask(this)
        task.execute(orderName)
    }

    fun updateLatLong(latLong: String?) {
        if (latLong != null) {
            val v = latLong.split(",")
            //Parse double lat and long from string
            //Pass them into the below function
            view.updateMap(v[0].toDouble(), -1*v[1].toDouble())
        }

    }
}