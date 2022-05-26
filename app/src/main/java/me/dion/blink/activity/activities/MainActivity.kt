package me.dion.blink.activity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import me.dion.blink.R
import me.dion.blink.activity.alerts.NoConnectionAlert
import me.dion.blink.activity.alerts.login.TooShortLoginAlert
import me.dion.blink.activity.alerts.login.TooShortPasswordAlert
import me.dion.blink.task.RequestTask
import okhttp3.Request

class MainActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var loginTextEdit: EditText
    private lateinit var passwordTextEdit: EditText
    private lateinit var forgotPwdText: TextView
    private lateinit var registerText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById(R.id.login_btn)
        loginTextEdit = findViewById(R.id.login_text_edit)
        passwordTextEdit = findViewById(R.id.password_text_edit)
        forgotPwdText = findViewById(R.id.forgot_pwd_text)
        registerText = findViewById(R.id.create_acc_text)

        loginButton.setOnClickListener {
            executeLogin()
        }

        registerText.setOnClickListener {
            executeRegister()
        }
    }

    private fun executeLogin() {
        loginButton.isEnabled = false

        if (loginTextEdit.text.length < 3) {
            TooShortLoginAlert().show(supportFragmentManager, "tooShortLoginAlert")
            loginButton.isEnabled = true
        } else if (passwordTextEdit.text.length < 8) {
            TooShortPasswordAlert().show(supportFragmentManager, "tooShortPasswordAlert")
            loginButton.isEnabled = true
        } else {
            val testRequest = Request.Builder()
                .url(resources.getString(R.string.api_url_status))
                .get()
                .build()

            val response = RequestTask().execute(testRequest).get()

            if (response != null && response.isSuccessful) {
                println()
            } else {
                NoConnectionAlert().show(supportFragmentManager, "noConnectionAlert")
                loginButton.isEnabled = true
            }
        }
    }

    private fun executeRegister() {

    }
}