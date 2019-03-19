package com.plusbueno.plusbueno.networking

import android.os.AsyncTask
import com.plusbueno.plusbueno.parser.LoginParser
import com.plusbueno.plusbueno.parser.exception.BadRequestException
import com.plusbueno.plusbueno.parser.exception.LoginFailedException
import com.plusbueno.plusbueno.parser.exception.NetworkErrorException
import com.plusbueno.plusbueno.presenter.LogInPresenter
import java.net.URL

class DoLogInTask (val presenter: LogInPresenter) : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg credentials: String): String? {
        val userName = credentials[0]
        val password = credentials[1]

        try {
            val authKey = LoginParser.doLogin(userName, password)
            return authKey
        } catch (e: NetworkErrorException) {
            return "Error: Network error"
        } catch (e: LoginFailedException) {
            return "Error: Login failed"
        } catch (e : BadRequestException) {
            return "Error: " + e.message
        }
    }

    override fun onPostExecute(result: String?) {
        presenter.endLogIn(result)
    }

}