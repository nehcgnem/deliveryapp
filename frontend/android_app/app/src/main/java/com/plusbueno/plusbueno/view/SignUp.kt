package com.plusbueno.plusbueno.view

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.plusbueno.plusbueno.R
import com.plusbueno.plusbueno.presenter.SignUpPresenter
import com.plusbueno.plusbueno.parser.LoginParser
import com.plusbueno.plusbueno.parser.UniversalParser
import com.plusbueno.plusbueno.parser.exception.BadRequestException
import com.plusbueno.plusbueno.parser.exception.NetworkErrorException
import com.plusbueno.plusbueno.parser.util.DoubleSHA1PasswordHasher
import com.plusbueno.plusbueno.parser.util.HttpUtil
import com.plusbueno.plusbueno.parser.util.PasswordHasher
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity(), SignUpView {
    private val presenter = SignUpPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    fun trySignup(view: View) {
        if (!TOSAgreement.isChecked) {
            Toast.makeText(this, getText(R.string.TOSfail), Toast.LENGTH_LONG).show()
        } else {
            val username = signUpEmail.text.toString()
            val password = signUpPassword.text.toString()
            val password2 = confirmPassword.text.toString()

            presenter.trySignup(username, password, password2)
        }
    }

    override fun showError(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun goHome() {
        val intent = Intent(this, CustHome::class.java)
        startActivity(intent)
    }
}
