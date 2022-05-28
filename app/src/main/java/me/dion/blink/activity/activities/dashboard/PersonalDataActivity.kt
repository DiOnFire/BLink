package me.dion.blink.activity.activities.dashboard

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonParser
import me.dion.blink.R
import me.dion.blink.traits.Account
import me.dion.blink.traits.RequestThread
import me.dion.blink.traits.SerializableResponse
import okhttp3.Headers
import okhttp3.Request

@SuppressLint("HandlerLeak")
class PersonalDataActivity : AppCompatActivity() {
    private lateinit var usernameMenu: MenuItem
    private lateinit var dateMenu: MenuItem
    private lateinit var uidMenu: MenuItem
    private lateinit var emailMenu: MenuItem
    private lateinit var discordMenu: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_data)

        val menu: Menu = findViewById<NavigationView?>(R.id.menu_view).menu

        usernameMenu = menu.findItem(R.id.personal_data_nickname)
        dateMenu = menu.findItem(R.id.personal_data_create_date)
        uidMenu = menu.findItem(R.id.personal_data_uid)
        emailMenu = menu.findItem(R.id.personal_data_email)
        discordMenu = menu.findItem(R.id.personal_data_discord)

        fetchData()
    }

    private fun fetchData() {
        val account = intent.getSerializableExtra("account") as Account
        usernameMenu.title = "Username: " + account.username
        dateMenu.title = "Creation date: " + account.register_date
        uidMenu.title = "UID: " + account.id
        emailMenu.title = "Email: " + account.email
        if (account.discord_linked) {
            fetchDiscord(account)
        } else {
            discordMenu.title = "Discord not linked!"
        }
    }

    private fun fetchDiscord(account: Account) {
        val handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                val bundle = msg.data
                val response: SerializableResponse = bundle.get("response") as SerializableResponse
                val body = response.response.body.string()
                val json = JsonParser.parseString(body).asJsonObject
                discordMenu.title = json.get("username").asString + "#" + json.get("discriminator").asString
            }
        }

        val headers = Headers.Builder()
            .add("Authorization", account.discord_oauth)
            .build()

        val request = Request.Builder()
            .url("https://discord.com/api/users/@me")
            .headers(headers)
            .build()

        val thread = RequestThread(handler, request)
        thread.start()
    }
}