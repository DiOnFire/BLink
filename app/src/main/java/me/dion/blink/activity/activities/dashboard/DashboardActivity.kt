package me.dion.blink.activity.activities.dashboard

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.dion.blink.R
import me.dion.blink.traits.Account
import me.dion.blink.traits.RequestThread
import me.dion.blink.traits.SerializableResponse
import okhttp3.Headers
import okhttp3.Request

@SuppressLint("HandlerLeak")
class DashboardActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var loginText: TextView
    private lateinit var emailText: TextView
    private lateinit var account: Account
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val headerView = navView.inflateHeaderView(R.layout.nav_header)
        loginText = headerView.findViewById(R.id.user_name_text)
        emailText = headerView.findViewById(R.id.email_text)

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open,
            R.string.close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        loadAccountData()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun parseAccount(metadata: JsonObject): Account {
        return Account(
            metadata.get("id").asInt,
            metadata.get("login").asString,
            metadata.get("email").asString,
            metadata.get("email_verified").asBoolean,
            metadata.get("subscription").asBoolean,
            metadata.get("discord_linked").asBoolean,
            metadata.get("discord_oauth").asString,
            metadata.get("register_date").asString
        )
    }

    private fun loadAccountData() {
        val handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                val bundle = msg.data
                val response: SerializableResponse = bundle.get("response") as SerializableResponse
                val data = response.response.body.string()
                println(data)
                account = parseAccount(JsonParser.parseString(data).asJsonObject)
                loginText.text = account.username
                emailText.text = account.email
            }
        }

        val headers = Headers.Builder()
            .add("Authorization", "Bearer " + intent.getStringExtra("access_token"))
            .add("credentials", "include")
            .build()

        val dataRequest = Request.Builder()
            .url(resources.getString(R.string.api_url_get_me))
            .headers(headers)
            .build()

        val thread = RequestThread(handler, dataRequest)
        thread.start()
    }

    override fun onBackPressed() {}
}