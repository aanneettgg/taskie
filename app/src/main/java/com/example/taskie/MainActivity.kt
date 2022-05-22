package com.example.taskie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.taskie.databinding.ActivityMainBinding
import com.example.taskie.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Main activity of Taskie application.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var sharedPreferences: SharedPreferences
    private val themeKey = "currentTheme"
    private lateinit var date: String

    @RequiresApi(Build.VERSION_CODES.O)
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

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0F

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        date = current.format(formatter)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, date)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1) {
            val intent = intent
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            finish()
            startActivity(intent)
        } else if (requestCode == 2) {
            val returnedResult: String = data?.getStringExtra("date")!!
            date = returnedResult
            val adapter = binding.viewPager.adapter as SectionsPagerAdapter
            adapter.setDate(returnedResult)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.calendar_item -> {
                val i = Intent(this, CalendarActivity::class.java)
                i.putExtra("date", date)
                this.startActivityForResult(i, 2)
                true
            }
            R.id.notifications_item -> {
                val i = Intent(this, NotificationsActivity::class.java)
                this.startActivity(i)
                true
            }
            R.id.theme_item -> {
                val i = Intent(this, ThemeActivity::class.java)
                this.startActivityForResult(i, 1)
                true
            }
            else -> false
        }
    }

    fun dateChanged(newDate: String) {
        date = newDate
        val adapter = binding.viewPager.adapter as SectionsPagerAdapter
        adapter.setDate(date)
        adapter.notifyDataSetChanged()
    }
}