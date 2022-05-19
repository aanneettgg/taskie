package com.example.taskie

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.taskie.database.Task
import com.example.taskie.database.TaskieDatabase
import kotlinx.coroutines.launch

/**
 * An activity for creating and editing the task.
 */
class EditTaskActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    private val themeKey = "currentTheme"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = TaskieDatabase.getInstance(this).taskDao()
        var task: Task? = null
        val taskText = findViewById<EditText>(R.id.editTask)
        var taskType = false

        lifecycleScope. launch {
           task = dao.getTaskById(1)
           taskText.setText(task?.taskName)
        }

        sharedPreferences = getSharedPreferences(
            "ThemePref",
            Context.MODE_PRIVATE
        )

        when (sharedPreferences.getString(themeKey, "peach")) {
            "mint" ->  theme.applyStyle(R.style.OverlayThemeMint, true)
            "peach" ->  theme.applyStyle(R.style.OverlayThemePeach, true)
        }

        setContentView(R.layout.activity_edit_task)

        val buttonSave: Button = findViewById(R.id.buttonSave)
        val buttonBack: Button = findViewById(R.id.buttonBack)
        val radioButtonWork = findViewById<View>(R.id.radioButtonWork) as RadioButton
        val radioButtonRelax = findViewById<View>(R.id.radioButtonRelax) as RadioButton


        buttonSave.setOnClickListener {
            val taskNew = Task(taskText.text.toString(), System.currentTimeMillis(), taskType)
            if (task != null) {
                lifecycleScope. launch {
                    dao.update(taskNew)
                }
            } else {
                lifecycleScope.launch {
                    dao.insert(taskNew)
                }
            }
        }

        buttonBack.setOnClickListener {
            finish()
        }

        radioButtonWork.setOnClickListener {
            taskType = false
        }

        radioButtonRelax.setOnClickListener {
            taskType = true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        lifecycleScope. launch {
            // dao.delete(task)
        }

        finish()

        return true
    }
}