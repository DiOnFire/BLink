package me.dion.blink.activity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.JsonObject
import me.dion.blink.R
import me.dion.blink.task.RequestTask
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody

class RegisterAccountActivity : AppCompatActivity() {
    private lateinit var loginEdit: EditText
    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var backText: TextView
    private lateinit var signUpBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_account)

        loginEdit = findViewById(R.id.login_text_edit)
        emailEdit = findViewById(R.id.email_text_edit)
        passwordEdit = findViewById(R.id.password_text_edit)
        backText = findViewById(R.id.back_text)
        signUpBtn = findViewById(R.id.register_btn)
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

        val response = RequestTask().execute(regRequest).get()


    }
}