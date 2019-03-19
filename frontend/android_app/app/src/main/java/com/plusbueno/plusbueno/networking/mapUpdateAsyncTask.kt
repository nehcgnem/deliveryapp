package com.plusbueno.plusbueno.networking

import android.os.AsyncTask
import android.util.Log
import com.plusbueno.plusbueno.data.ServerCoord
import com.plusbueno.plusbueno.parser.LoginManager
import com.plusbueno.plusbueno.parser.UniversalParser
import com.plusbueno.plusbueno.parser.util.HttpUtil
import com.plusbueno.plusbueno.presenter.MapsActivityPresenter
import java.lang.Exception

class mapUpdateAsyncTask (val presenter: MapsActivityPresenter) : AsyncTask<String, String, String>() {
    var exception: Exception? = null

    override fun doInBackground(vararg p0: String?): String {
//        Log.v("ASYNC", "Running task in background!")
        return try {
            Log.v("MAP_UPDATE", "trying new coordinate")
            val result = UniversalParser.get(UniversalParser.BASE_URL_RESTFUL + "/drone-coord/" + LoginManager.getUsername() + "/" + p0[0] + "/", ServerCoord::class.java)
            result.currentCoord
        } catch (e : Exception) {
            exception = e
            Log.e("MAP_UPDATE", e.toString() + e.message)
            "Failed."
        }



//        return "Failed." // Or return the lattitude and longitude as a string
    }

    override fun onPostExecute(result: String?) {
        if (result != "Failed.") {
            presenter.updateLatLong(result)
        }
    }
}