package me.dion.blink.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.dion.blink.R
import me.dion.blink.activity.activities.dashboard.DashboardActivity
import me.dion.blink.activity.activities.pre.MainActivity
import me.dion.blink.traits.Account
import me.dion.blink.traits.RequestThread
import me.dion.blink.traits.SerializableResponse
import okhttp3.Headers
import okhttp3.Request

@SuppressLint("HandlerLeak")
class NavigationDrawerBuilder(val activity: AppCompatActivity) {
    private lateinit var loginText: TextView
    private lateinit var emailText: TextView
    private lateinit var account: Account

    fun getAccount(): Account {
        return account
    }

    fun build() {
        val drawerLayout = activity.findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = activity.findViewById<NavigationView>(R.id.nav_view)

        val headerView = navView.inflateHeaderView(R.layout.nav_header)
        loginText = headerView.findViewById(R.id.user_name_text)
        emailText = headerView.findViewById(R.id.email_text)

        val toggle = ActionBarDrawerToggle(
            activity,
            drawerLayout,
            R.string.open,
            R.string.close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        loadAccountData()

        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> navHomeClick()
                R.id.nav_cloud -> navCloudClick()
                R.id.nav_devices -> navDevicesClick()
                R.id.nav_account -> navAccountClick()
                R.id.nav_configs -> navConfigsClick()
                R.id.nav_remote -> navRemoteClick()
                R.id.nav_logout -> navLogoutClick()
                R.id.nav_support -> navSupportClick()
                R.id.nav_share -> navShareClick()
                R.id.nav_settings -> navSettingsClick()
            }
            true
        }
    }

    private fun navHomeClick() {
        val intent = Intent(
            activity,
            DashboardActivity::class.java
        )
        activity.startActivity(intent)
    }

    private fun navCloudClick() {

    }

    private fun navAccountClick() {

    }

    private fun navConfigsClick() {

    }

    private fun navDevicesClick() {

    }

    private fun navLogoutClick() {
        val dialog: AlertDialog = activity.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.sure)
                .setMessage(R.string.logout_dialog_msg)

            builder.apply {
                setPositiveButton(R.string.logout
                ) { _, _ ->
                    activity.intent.removeExtra("access_token")
                    FileUtil.saveData("none", activity.applicationContext)
                    val intent = Intent(
                        activity,
                        MainActivity::class.java
                    )
                    activity.startActivity(intent)
                }
                setNegativeButton(R.string.cancel
                ) { dialog, _ ->
                    dialog.dismiss()
                }
            }
            builder.create()
        }
        dialog.show()
    }

    private fun navRemoteClick() {

    }

    private fun navSettingsClick() {

    }

    private fun navShareClick() {

    }

    private fun navSupportClick() {

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
                account = parseAccount(JsonParser.parseString(data).asJsonObject)
                loginText.text = account.username
                emailText.text = account.email
            }
        }

        val headers = Headers.Builder()
            .add("Authorization", "Bearer " + activity.intent.getStringExtra("access_token"))
            .add("credentials", "include")
            .build()

        val dataRequest = Request.Builder()
            .url(activity.resources.getString(R.string.api_url_get_me))
            .headers(headers)
            .build()

        val thread = RequestThread(handler, dataRequest)
        thread.start()
    }
}