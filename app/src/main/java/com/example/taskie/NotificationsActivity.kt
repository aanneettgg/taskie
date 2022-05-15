package com.example.taskie

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class NotificationsActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    val themeKey = "currentTheme"

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
    }

}