package me.dion.blink.activity.activities.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import me.dion.blink.R
import me.dion.blink.traits.Account
import me.dion.blink.util.NavigationDrawerBuilder

class AccountActivity : AppCompatActivity() {
    private lateinit var shieldImg: ImageView
    private lateinit var securityStatus: TextView
    private lateinit var securityDesc: TextView
    private lateinit var accountText: TextView
    private lateinit var account: Account
    private lateinit var builder: NavigationDrawerBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        shieldImg = findViewById(R.id.shield_img)
        securityStatus = findViewById(R.id.security_status)
        securityDesc = findViewById(R.id.security_desc)
        accountText = findViewById(R.id.account_name)

        builder = NavigationDrawerBuilder(this)
        builder.build()

        account = intent.getSerializableExtra("account") as Account

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val menuView = findViewById<NavigationView>(R.id.menu_account)

        menuView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_personal -> executePersonalMenu()
            }
            true
        }
        updateSecurityInfo()
    }

    private fun prepareMenu() {

    }

    private fun executePersonalMenu() {
        val intent = Intent(
            this,
            PersonalDataActivity::class.java
        )
        intent.putExtra("account", account)
        startActivity(intent)
    }

    private fun updateSecurityInfo() {
        accountText.text = account.username
        if (account.discord_linked || account.email_verified) {
            securityStatus.text = resources.getString(R.string.account_secured)
            if (account.discord_linked && account.email_verified) {
                securityDesc.text = resources.getString(R.string.fa_dis_email)
            } else if (account.discord_linked && !account.email_verified) {
                securityDesc.text = resources.getString(R.string.fa_discord)
            } else if (!account.discord_linked && account.email_verified) {
                securityDesc.text = resources.getString(R.string.fa_email)
            }
        } else {
            securityStatus.text = resources.getString(R.string.account_not_secured)
            securityDesc.text = resources.getString(R.string.fa_no)
        }
    }
}