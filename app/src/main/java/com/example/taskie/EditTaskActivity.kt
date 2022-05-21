package com.example.taskie

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.taskie.database.Task
import com.example.taskie.database.TaskieDatabase
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * An activity for creating and editing the task.
 */
class EditTaskActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    private val themeKey = "currentTheme"
    private val dao = TaskieDatabase.getInstance(this).taskDao()
    private var task: Task? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskId = intent.getLongExtra("taskId", -1)

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
        val taskText: TextInputEditText = findViewById(R.id.editTask)
        var taskType = false

        lifecycleScope. launch {
            if (taskId > -1) {
                task = dao.getTaskById(taskId)
                if (task != null) {
                    taskText.setText(task?.taskName)
                    if (task!!.taskType) {
                        radioButtonRelax.isChecked = true
                        radioButtonWork.isChecked = false
                    } else {
                        radioButtonRelax.isChecked = false
                        radioButtonWork.isChecked = true
                    }
                }
            }
        }

        buttonSave.setOnClickListener {
            if (task != null) {
                task?.taskName = taskText.text.toString()
                task?.taskType = taskType
                lifecycleScope. launch {
                    dao.update(task!!)
                }
            } else {
                val date = intent.getStringExtra("date")
                val formatter: DateTimeFormatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
                val localDate: LocalDate? = LocalDate.parse(date, formatter)
                val timeInMilliseconds: Long =
                    (localDate!!.atStartOfDay(ZoneOffset.UTC).toEpochSecond() + 1) * 1000
                val newTask = Task(taskText.text.toString(), timeInMilliseconds, taskType)
                lifecycleScope.launch {
                    dao.insert(newTask)
                }
            }
            finish()
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
            dao.delete(task!!)
        }

        finish()

        return true
    }
}