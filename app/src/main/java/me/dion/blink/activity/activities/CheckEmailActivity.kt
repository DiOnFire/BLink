package me.dion.blink.activity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import me.dion.blink.R

class CheckEmailActivity : AppCompatActivity() {
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_email)

        backBtn = findViewById(R.id.back_btn)

        backBtn.setOnClickListener {
            val intent = Intent(
                this,
                MainActivity::class.java
            )
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(
            this,
            MainActivity::class.java
        )
        startActivity(intent)
    }
}