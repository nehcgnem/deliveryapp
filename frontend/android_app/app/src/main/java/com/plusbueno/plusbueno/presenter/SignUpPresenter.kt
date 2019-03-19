package com.plusbueno.plusbueno.presenter

import com.plusbueno.plusbueno.networking.SignupAsyncTask
import com.plusbueno.plusbueno.view.SignUpView

class SignUpPresenter (val view:SignUpView) {
    fun trySignup(username: String, password: String, password2 : String) {
        val task = SignupAsyncTask(this)
        task.execute(username, password, password2)
    }

    fun onSuccess() {
        view.goHome()
    }

    fun onFail(errorType : String?) {
        view.showError(errorType)
    }
}