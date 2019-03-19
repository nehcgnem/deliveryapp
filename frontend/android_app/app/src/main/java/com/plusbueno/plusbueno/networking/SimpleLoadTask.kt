package com.plusbueno.plusbueno.networking

import android.os.AsyncTask
import com.plusbueno.plusbueno.parser.UniversalParser
import com.plusbueno.plusbueno.parser.exception.AuthorizationException
import com.plusbueno.plusbueno.parser.exception.BadRequestException
import com.plusbueno.plusbueno.parser.exception.NetworkErrorException
import com.plusbueno.plusbueno.presenter.SimpleLoadPresenter

/**
 * Created by LZMA on 2018/11/10.
 */
class SimpleLoadTask<T>(val presenter : SimpleLoadPresenter<T>, val type : Class<T>) : AsyncTask<String, String, T>() {
    private var errorMessage : String?  = null

    override fun doInBackground(vararg url: String?): T? {
        val result : T
        try {
            result = UniversalParser.get(url[0], type)
            return result
        } catch (e : BadRequestException) {
            if (e.message == null) {
                errorMessage = "Server internal error."
            } else {
                errorMessage = e.localizedMessage
            }
        } catch (e : AuthorizationException) {
            errorMessage = "Authorization error. Try to login again,"
        } catch (e : NetworkErrorException) {
            errorMessage = "Our server is busy. Try again later."
        }
        return null
    }

    override fun onPostExecute(result: T?) {
        val error = errorMessage
        super.onPostExecute(result)
        if (result!= null) {
            presenter.onSimpleLoadSuccess(result)
        } else if (error != null) {
            presenter.onSimpleLoadFail(error)
        }
    }
}