package com.plusbueno.plusbueno.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.Snackbar
import android.os.Bundle
import android.os.Message
import android.support.design.widget.Snackbar.LENGTH_SHORT
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.plusbueno.plusbueno.R
import com.plusbueno.plusbueno.data.UserType
import com.plusbueno.plusbueno.parser.LoginManager
import com.plusbueno.plusbueno.presenter.LogInPresenter
import kotlinx.android.synthetic.main.activity_log_in.*

//const val EXTRA_MESSAGE = "com.plusbueno.plusbueno.view.MESSAGE"

class LogIn : AppCompatActivity(), LogInView {

    private val presenter = LogInPresenter(this)
    private lateinit var noInternetSnackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        button.visibility = View.GONE

        noInternetSnackbar = Snackbar.make(loginCoordinatorLayout, "No internet connection. Can't log in.", LENGTH_SHORT)
    }

    fun signUp(view: View) {
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
    }

    override fun customerHome() {
        val intent = when (LoginManager.getUserType()) {

            UserType.BUSINESS -> Intent(this, BusHome::class.java)
            UserType.CUSTOMER, null -> Intent(this, CustHome::class.java)
        }

        startActivity(intent)
    }

    override fun networkError() {
        noInternetSnackbar.show()
    }

    override fun loginFailed() {
        Snackbar.make(loginCoordinatorLayout, "login failed", LENGTH_SHORT).show()
    }

    override fun loginFailedCustom(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun logIn(view: View) {
        val emailField = findViewById<EditText>(R.id.emailLogin)
        val email = emailField.text.toString()

        val passField = findViewById<EditText>(R.id.passwordLogin)
        val pass = passField.text.toString()

        presenter.tryLogIn(email, pass)
    }

    fun testActivity(view: View) {
        val intent = Intent(this, PaymentActivity::class.java)
        startActivity(intent)
    }
}
