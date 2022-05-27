package me.dion.blink.activity.activities.dashboard

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import me.dion.blink.R
import me.dion.blink.util.NavigationDrawerBuilder

@SuppressLint("HandlerLeak")
class DashboardActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var loginText: TextView
    private lateinit var emailText: TextView
    private lateinit var builder: NavigationDrawerBuilder
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        builder = NavigationDrawerBuilder(this)
        builder.build()
    }

    override fun onBackPressed() {}
}