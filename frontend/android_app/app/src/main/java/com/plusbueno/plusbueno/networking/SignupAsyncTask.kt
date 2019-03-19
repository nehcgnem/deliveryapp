package com.plusbueno.plusbueno.networking

import android.os.AsyncTask
import android.util.Log
import com.plusbueno.plusbueno.parser.LoginParser
import com.plusbueno.plusbueno.parser.UniversalParser
import com.plusbueno.plusbueno.parser.exception.BadRequestException
import com.plusbueno.plusbueno.parser.exception.NetworkErrorException
import com.plusbueno.plusbueno.parser.util.DoubleSHA1PasswordHasher
import com.plusbueno.plusbueno.parser.util.HttpUtil
import com.plusbueno.plusbueno.parser.util.PasswordHasher
import com.plusbueno.plusbueno.presenter.SignUpPresenter

class SignupAsyncTask (val presenter: SignUpPresenter) : AsyncTask<String, String, String>() {

    override fun doInBackground(vararg credentials: String): String {
        val username = credentials[0]
        var password = credentials[1]
        var password2 = credentials[2]
        val hasher : PasswordHasher = DoubleSHA1PasswordHasher()
        password = hasher.hash(password)
        password2 = hasher.hash(password2)

        val userMap = mapOf<String, String>(Pair("username", username), Pair("password1", password),
                Pair("password2", password2) )

        var returnMessage : String = "Internal error."
        try {
            var result = HttpUtil.post(UniversalParser.BASE_URL_RESTFUL + "/rest-auth/registration/", userMap, userMap.javaClass)
            LoginParser.doLogin(credentials[0], credentials[1])  // get a token
            returnMessage = "Success."
        } catch (e : NetworkErrorException) {
            returnMessage = "Network error"
        } catch (e : BadRequestException) {
            if (e.message != null) {
                returnMessage = e.message.toString()
            }
        }
        return returnMessage

    }

    override fun onPostExecute(result: String?) {
        if (result == "Success.") {
            presenter.onSuccess()
        } else {
            presenter.onFail(result)
        }

    }
}