package com.example.taskie

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskie.database.Gratefulness
import com.example.taskie.database.Task
import com.example.taskie.database.TaskDao
import com.example.taskie.database.TaskieDatabase
import com.example.taskie.ui.main.TaskAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

/**
 * A fragment of MainActivity for task management.
 */
class TaskFragment: Fragment() {
    private lateinit var date: String
    private lateinit var tasks: List<Task>
    private lateinit var rvTasks: RecyclerView
    private lateinit var daoTasks: TaskDao

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        val args = arguments
        date = args!!.getString("date").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_task, container, false)
        val fab: FloatingActionButton = view.findViewById(R.id.fab_tasks)
        val gratefulText = view.findViewById(R.id.editTextGratefulness) as EditText
        val dayText = view.findViewById(R.id.textViewDate) as TextView
        val arrowLeft: ImageView = view.findViewById(R.id.imageViewDateLeft)
        val arrowRight: ImageView = view.findViewById(R.id.imageViewDateRight)
        rvTasks = view.findViewById(R.id.recyclerView) as RecyclerView
        dayText.text = date

        val dao = TaskieDatabase.getInstance(requireContext()).gratefulnessDao()
        daoTasks = TaskieDatabase.getInstance(requireContext()).taskDao()

        var gratefulness: Gratefulness? = null

        loadTaskList()

        lifecycleScope.launch {
            gratefulness = dao.getGratefulnessByDate(date)
            gratefulText.setText(gratefulness?.gratefulnessInfo)
        }
        // add new task
        fab.setOnClickListener {
            val intent = Intent(activity, EditTaskActivity::class.java)
            intent.putExtra("date", date)
            startActivityForResult(intent, 3)
        }

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

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 3) {
            loadTaskList()
        }
    }

    /**
     * Starts the EditTaskActivity to edit existing task.
     *
     * @param item task for editing
     */
    fun editItem(item: Task) {
        val intent = Intent(activity, EditTaskActivity::class.java)

        intent.putExtra("taskId", item.taskId)
        startActivityForResult(intent, 3)
    }

    fun setItemState(item: Task) {
        item.isCompleted = !item.isCompleted
        lifecycleScope.launch {
            daoTasks.update(item)
        }
    }

    private fun loadTaskList() {
        lifecycleScope.launch {
            tasks = daoTasks.getTasksByDate(date)
            val adapter = TaskAdapter(tasks,this@TaskFragment , requireContext())
            rvTasks.adapter = adapter
            rvTasks.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}