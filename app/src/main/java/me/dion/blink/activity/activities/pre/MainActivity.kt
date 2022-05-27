package me.dion.blink.activity.activities.pre

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.dion.blink.R
import me.dion.blink.activity.activities.dashboard.DashboardActivity
import me.dion.blink.activity.alerts.AbstractAlert
import me.dion.blink.activity.alerts.LoadingDialog
import me.dion.blink.traits.RequestThread
import me.dion.blink.util.FileUtil
import me.dion.blink.traits.SerializableResponse
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.lang.Exception

@SuppressLint("HandlerLeak")
class MainActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var loginTextEdit: EditText
    private lateinit var passwordTextEdit: EditText
    private lateinit var forgotPwdText: TextView
    private lateinit var registerText: TextView
    private lateinit var loadingDialog: LoadingDialog
    private val gson = Gson()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById(R.id.login_btn)
        loginTextEdit = findViewById(R.id.login_text_edit)
        passwordTextEdit = findViewById(R.id.password_text_edit)
        forgotPwdText = findViewById(R.id.forgot_pwd_text)
        registerText = findViewById(R.id.create_acc_text)
        loadingDialog = LoadingDialog(this@MainActivity)

        load()

        loginButton.setOnClickListener {
            executeLogin()
        }

        registerText.setOnClickListener {
            executeRegister()
        }
    }

    private fun load() {
        val metadata = FileUtil.loadData(applicationContext)
        if (metadata.has("access_token") && metadata.get("access_token").asString != "none") {
            loadingDialog.startLoadingDialog()
            token = metadata.get("access_token").asString
            checkEmail()
        }
    }

    private fun save(token: String) {
        val json = JsonObject()
        json.add("access_token", JsonParser.parseString(token))
        FileUtil.saveData(gson.toJson(json), applicationContext)
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

            loadingDialog.startLoadingDialog()

            val handler = object : Handler() {
                override fun handleMessage(msg: Message) {
                    val bundle = msg.data
                    val response: SerializableResponse = bundle.get("response") as SerializableResponse
                    if (response.response != null && response.response.isSuccessful) {
                        auth()
                    } else {
                        AbstractAlert("Connection error", "Something went wrong. Try again later.").show(supportFragmentManager, "noConnectionAlert")
                        loginButton.isEnabled = true
                    }
                }
            }


            val thread = RequestThread(handler, testRequest)
            thread.start()
        }
    }

    private fun auth() {
        val obj = JsonObject()
        obj.addProperty("username", loginTextEdit.text.toString())
        obj.addProperty("password", passwordTextEdit.text.toString())

        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), Gson().toJson(obj))

        val authRequest = Request.Builder()
            .url(resources.getString(R.string.api_url_auth))
            .post(requestBody)
            .build()

        val handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                val bundle = msg.data
                val response: SerializableResponse = bundle.get("response") as SerializableResponse
                val accessToken = parseToken(response.response)
                if (accessToken == "null") {
                    AbstractAlert("Invalid credentials", "Login or password is not correct. Try again.").show(supportFragmentManager, "invalidCredentialsAlert")
                } else {
                    token = accessToken
                    save(accessToken)
                    checkEmail()
                }
            }
        }

        val thread = RequestThread(handler, authRequest)
        thread.start()
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

    private fun parseEmail(response: Response): Boolean {
        return try {
            val json = JsonParser.parseString(response.body.string()).asJsonObject
            json.get("email_verified").asBoolean
        } catch (e: Exception) {
            false
        }
    }

    private fun executeEmail() {
        val intent = Intent(
            this,
            DashboardActivity::class.java
        )
        intent.putExtra("access_token", token)
        startActivity(intent)
    }

    private fun checkEmail() {
        // эта херь работает блядь ахахахахха

        // ебашим хандлер шоб выловить говно с треда
        val handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                loadingDialog.dismissDialog()
                val bundle = msg.data
                // сериализабле респонз = мега говно чтобы наебать андроид через сериализацию (адаптер с okhttp на байты)
                val response: SerializableResponse = bundle.get("response") as SerializableResponse
                val body = response.response.body.string()
                println(body)
                if (JsonParser.parseString(body).asJsonObject.has("email_verified") && !parseEmail(response.response)) {
                    executeEmail()
                } else {
                    intent.removeExtra("access_token")
                    save("none")
                    AbstractAlert("Session expired", "Your session expired. Please login again.").show(supportFragmentManager, "sessionExpiredAlert")
                }
            }
        }

        // аццесс через токен
        val headers = Headers.Builder()
            .add("Authorization", "Bearer $token")
            .add("credentials", "include")
            .build()

        // ебашим реквест
        val request = Request.Builder()
            .url(resources.getString(R.string.api_url_get_me))
            .headers(headers)
            .build()

        // запускаем поток с говном
        val thread = RequestThread(handler, request)

        thread.start()
    }
}