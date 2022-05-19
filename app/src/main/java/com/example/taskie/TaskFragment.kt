package com.example.taskie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.example.taskie.database.Gratefulness
import com.example.taskie.database.Task
import com.example.taskie.database.TaskieDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

/**
 * A fragment of MainActivity for task management.
 */
class TaskFragment: Fragment() {
    private lateinit var date: String
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

        val dao = TaskieDatabase.getInstance(requireContext()).gratefulnessDao()
        var gratefulness: Gratefulness? = null
        val daoTasks = TaskieDatabase.getInstance(requireContext()).taskDao()
        var tasks: LiveData<List<Task>>?

        lifecycleScope.launch {
            gratefulness = dao.getGratefulnessByDate(date)
            gratefulText.setText(gratefulness?.gratefulnessInfo)
        }

        lifecycleScope.launch {
            tasks = daoTasks.getTasksByDate(date)
            // for pre zoznam taskov
        }

        fab.setOnClickListener {
            val intent = Intent(activity, EditTaskActivity::class.java)
            startActivity(intent)
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
}