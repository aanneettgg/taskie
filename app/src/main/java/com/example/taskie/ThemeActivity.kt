package com.example.taskie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity


class ThemeActivity : AppCompatActivity() {

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

        setContentView(R.layout.activity_theme)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val radioButtonMint = findViewById<View>(R.id.radioButtonMint) as RadioButton
        radioButtonMint.setOnClickListener{
            sharedPreferences.edit().putString(themeKey, "mint").apply()

            val intent = intent // from getIntent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            finish()
            startActivity(intent)
        }

        val radioButtonPeach = findViewById<View>(R.id.radioButtonPeach) as RadioButton
        radioButtonPeach.setOnClickListener{
            sharedPreferences.edit().putString(themeKey, "peach").apply()

            val intent = intent
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            finish()
            startActivity(intent)
        }

        if(sharedPreferences.getString(themeKey, "peach") == "peach") {
            radioButtonPeach.isChecked = true
            radioButtonMint.isChecked = false
        } else {
            radioButtonPeach.isChecked = false
            radioButtonMint.isChecked = true
        }
    }
}