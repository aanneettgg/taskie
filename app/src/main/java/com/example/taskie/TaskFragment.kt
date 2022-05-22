package com.example.taskie

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskie.database.*
import com.example.taskie.ui.main.TaskAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * A fragment of MainActivity for task management.
 */
class TaskFragment: Fragment() {
    private lateinit var date: String
    private lateinit var tasks: List<Task>
    private lateinit var rvTasks: RecyclerView
    private lateinit var daoTasks: TaskDao
    private lateinit var text: String
    private lateinit var dao: GratefulnessDao
    private var gratefulness: Gratefulness? = null
    private lateinit var gratefulText: EditText

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        val args = arguments
        date = args!!.getString("date").toString()
        text = state?.getString("text").toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_task, container, false)
        val fab: FloatingActionButton = view.findViewById(R.id.fab_tasks)
        gratefulText = view.findViewById(R.id.editTextGratefulness) as EditText
        val dayText = view.findViewById(R.id.textViewDate) as TextView
        val arrowLeft: ImageView = view.findViewById(R.id.imageViewDateLeft)
        val arrowRight: ImageView = view.findViewById(R.id.imageViewDateRight)
        rvTasks = view.findViewById(R.id.recyclerView) as RecyclerView
        dayText.text = date.replace("-", ".")

        dao = TaskieDatabase.getInstance(requireContext()).gratefulnessDao()
        daoTasks = TaskieDatabase.getInstance(requireContext()).taskDao()

        loadTaskList()

        loadGratefulness()

        // add new task
        fab.setOnClickListener {
            val intent = Intent(activity, EditTaskActivity::class.java)
            intent.putExtra("date", date)
            intent.putExtra("maxRelax", isMaxCountOfRelaxTasks())
            startActivityForResult(intent, 3)
        }

        // save gratefulness
        gratefulText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (gratefulness != null) {
                    lifecycleScope.launch {
                        gratefulness?.gratefulnessInfo = gratefulText.text.toString()
                        dao.update(gratefulness!!)
                    }
                } else {
                    lifecycleScope.launch {
                        dao.insert(Gratefulness(gratefulText.text.toString(), System.currentTimeMillis()))
                    }
                }
            }
        }

        // set date to yesterday
        arrowLeft.setOnClickListener {
            switchDate(true, dayText)
        }

        // set date to tomorrow
        arrowRight.setOnClickListener {
            switchDate(false, dayText)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 3) {
            loadTaskList()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("text", text)
    }

    /**
     * Starts the EditTaskActivity to edit existing task.
     *
     * @param item task for editing
     */
    fun editItem(item: Task) {
        val intent = Intent(activity, EditTaskActivity::class.java)

        intent.putExtra("taskId", item.taskId)
        intent.putExtra("maxRelax", isMaxCountOfRelaxTasks())
        startActivityForResult(intent, 3)
    }

    /**
     * Set item state to completed or uncompleted.
     *
     * @param item task for setting a state
     */
    fun setItemState(item: Task) {
        item.isCompleted = !item.isCompleted
        lifecycleScope.launch {
            daoTasks.update(item)
        }
    }

    private fun isMaxCountOfRelaxTasks(): Boolean {
        lifecycleScope.launch {
            tasks = daoTasks.getTasksByDate(date)
        }
        var count = 0
        for (task in tasks) {
            if (task.taskType) {
                count += 1
            }
        }
        return count >= 5
    }

    private fun loadTaskList() {
        lifecycleScope.launch {
            tasks = daoTasks.getTasksByDate(date)
            val adapter = TaskAdapter(tasks,this@TaskFragment , requireContext())
            rvTasks.adapter = adapter
            rvTasks.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadGratefulness() {
        lifecycleScope.launch {
            gratefulness = dao.getGratefulnessByDate(date)
            gratefulText.setText(gratefulness?.gratefulnessInfo)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun switchDate(isLeft: Boolean, day: TextView) {
        val formatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
        var localDate: LocalDate? = LocalDate.parse(date, formatter)

        localDate = if (isLeft) {
            localDate!!.minusDays(1)
        } else {
            localDate!!.plusDays(1)
        }
        date = localDate.format(formatter)

        loadTaskList()
        loadGratefulness()

        day.text = date.replace("-", ".")
    }
}