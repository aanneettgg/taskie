package com.example.taskie

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

/**
 * An activity providing notifications.
 */
class NotificationsActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    private val themeKey = "currentTheme"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(
            "ThemePref",
            Context.MODE_PRIVATE
        )

        when (sharedPreferences.getString(themeKey, "peach")) {
            "mint" ->  theme.applyStyle(R.style.OverlayThemeMint, true)
            "peach" ->  theme.applyStyle(R.style.OverlayThemePeach, true)
        }

        setContentView(R.layout.activity_notifications)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_briefcase)
            .setContentTitle("Message from Taskie")
            .setContentText("Your tasks are not done.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

}