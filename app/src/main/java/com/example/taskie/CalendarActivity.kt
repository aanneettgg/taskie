package com.example.taskie

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity

/**
 * An activity for accessing various days with tasks and diary.
 */
class CalendarActivity : AppCompatActivity() {

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

        setContentView(R.layout.activity_calendar)

        val calendar: CalendarView = findViewById(R.id.calendarView)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->

            var monthFix = month
            monthFix += 1
            var dayOfMonthStr = dayOfMonth.toString()
            var monthStr = monthFix.toString()
            if (dayOfMonth < 10) {
                dayOfMonthStr = "0$dayOfMonth"
            }
            if (month < 10) {
                monthStr = "0$monthFix"
            }
            val intent = intent
            val text = "$dayOfMonthStr-$monthStr-$year"
            intent.putExtra("date", text)
            setResult(2, intent);
            finish();
        }
    }

}