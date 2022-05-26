package me.dion.blink.activity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.dion.blink.R
import me.dion.blink.activity.alerts.AbstractAlert
import me.dion.blink.task.RequestTask
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull

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
            AbstractAlert("Login too short", "Your login must contain 3 or more symbols").show(supportFragmentManager, "tooShortLoginAlert")
            loginButton.isEnabled = true
        } else if (passwordTextEdit.text.length < 8) {
            AbstractAlert("Password too short", "Your password must contain 8 or more symbols.").show(supportFragmentManager, "tooShortPasswordAlert")
            loginButton.isEnabled = true
        } else {
            val testRequest = Request.Builder()
                .url(resources.getString(R.string.api_url_status))
                .get()
                .build()

            val response = RequestTask().execute(testRequest).get()

            if (response != null && response.isSuccessful) {
                val accessToken = auth()
                if (accessToken == "null") {
                    AbstractAlert("Invalid credentials", "Login or password is not correct. Try again.").show(supportFragmentManager, "invalidCredentialsAlert")
                }
            } else {
                AbstractAlert("Connection error", "Something went wrong. Try again later.").show(supportFragmentManager, "noConnectionAlert")
                loginButton.isEnabled = true
            }
        }
    }

    private fun auth(): String {
        val obj = JsonObject()
        obj.addProperty("username", loginTextEdit.text.toString())
        obj.addProperty("password", passwordTextEdit.text.toString())

        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), Gson().toJson(obj))

        val authRequest = Request.Builder()
            .url(resources.getString(R.string.api_url_auth))
            .post(requestBody)
            .build()

        val response = RequestTask().execute(authRequest).get()
        return parseToken(response)
    }

    private fun parseToken(response: Response): String {
        val json = JsonParser.parseString(response.body.string()).asJsonObject
        if (json.get("access_token") == null) return "null"
        return json.get("access_token").asString
    }

    private fun executeRegister() {
        val intent = Intent(
            this,
            RegisterAccountActivity::class.java
        )
        startActivity(intent)
    }
}