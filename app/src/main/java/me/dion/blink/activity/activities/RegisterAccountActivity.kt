package me.dion.blink.activity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import me.dion.blink.R

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

    }
}