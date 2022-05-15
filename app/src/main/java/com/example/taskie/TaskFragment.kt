package com.example.taskie

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TaskFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_task, container, false)
        val fab: FloatingActionButton = view.findViewById(R.id.fab_tasks)

        fab.setOnClickListener { view ->
            val intent = Intent(activity, EditTaskActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}