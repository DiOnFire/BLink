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
import me.dion.blink.activity.alerts.AbstractAlert
import me.dion.blink.activity.alerts.LoadingDialog
import me.dion.blink.util.RequestThread
import me.dion.blink.util.SerializableResponse
import me.dion.blink.util.Validator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody

@SuppressLint("HandlerLeak")
class RegisterAccountActivity : AppCompatActivity() {
    private lateinit var loginEdit: EditText
    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var backText: TextView
    private lateinit var signUpBtn: Button
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_account)

        loginEdit = findViewById(R.id.login_text_edit)
        emailEdit = findViewById(R.id.email_text_edit)
        passwordEdit = findViewById(R.id.password_text_edit)
        backText = findViewById(R.id.back_text)
        signUpBtn = findViewById(R.id.back_btn)

        loadingDialog = LoadingDialog(this@RegisterAccountActivity)

        backText.setOnClickListener {
            finish()
        }
        signUpBtn.setOnClickListener {
            if (validateRegister()) {
                loadingDialog.startLoadingDialog()
                register()
            }
        }
    }

    private fun validateRegister(): Boolean {
        if (loginEdit.text.length < 3) {
            AbstractAlert("Login too short", "Login or password is not correct. Try again.").show(supportFragmentManager, "tooShortLoginAlert")
        } else if (passwordEdit.text.length < 8) {
            AbstractAlert("Password too short", "Your password must contain 8 or more symbols.").show(supportFragmentManager, "tooShortPasswordAlert")
        } else if (!Validator.isEmail(emailEdit.text.toString())) {
            AbstractAlert("Invalid email", "Please check your email and try again.").show(supportFragmentManager, "invalidEmailAlert")
        } else {
            return true
        }
        return false
    }

    private fun register() {
        val obj = JsonObject()
        obj.addProperty("login", loginEdit.text.toString())
        obj.addProperty("password", passwordEdit.text.toString())
        obj.addProperty("email", emailEdit.text.toString())

        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), Gson().toJson(obj))

        val regRequest = Request.Builder()
            .url(resources.getString(R.string.api_url_register))
            .post(requestBody)
            .build()

        val handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                val bundle = msg.data
                val response: SerializableResponse = bundle.get("response") as SerializableResponse
                val data = response.response.body.string()
                val json = JsonParser.parseString(data).asJsonObject
                loadingDialog.dismissDialog()
                if (json.get("error") != null) {
                    handleError(json.get("error").asString)
                } else {
                    continueReg()
                }
            }
        }

        val thread = RequestThread(handler, regRequest)
        thread.start()
    }

    private fun continueReg() {
        val intent = Intent(
            this,
            CheckEmailActivity::class.java
        )
        startActivity(intent)
    }

    private fun handleError(error: String) {
        if (error.contains("email")) {
            AbstractAlert("Error", "An user with this email has already registered. Choose another email address.").show(supportFragmentManager, "sameEmailExistError")
        } else if (error.contains("nickname")) {
            AbstractAlert("Error", "An user with this nickname has already registered. Choose another nickname.").show(supportFragmentManager, "sameNicknameExistError")
        }
    }
}