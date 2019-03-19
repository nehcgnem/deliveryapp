package com.plusbueno.plusbueno.networking

import android.os.AsyncTask
import com.plusbueno.plusbueno.parser.exception.AuthorizationException
import com.plusbueno.plusbueno.parser.util.HttpUtil
import com.plusbueno.plusbueno.presenter.URLStreamPresenter
import java.io.InputStream

/**
 * Created by LZMA on 2018/11/14.
 */

class URLStreamTask(val presenter : URLStreamPresenter) : AsyncTask<String, String, InputStream>() {
    private var errorMessage : String? = null

    override fun doInBackground(vararg params: String?): InputStream? {
        var stream : InputStream? = null
        try{
            stream = HttpUtil.getStream(params[0])
        } catch (e : AuthorizationException) {
            errorMessage = "Authorization error."
        }
        return stream
    }

    override fun onPostExecute(result: InputStream?) {
        super.onPostExecute(result)
        val reason = errorMessage
        if (reason == null && result != null) {

            presenter.onStreamOpened(result)
            return
        }
        if (reason != null) {
            presenter.onStreamError(reason)
        } else {
            presenter.onStreamError("Network error.")
        }
    }
}