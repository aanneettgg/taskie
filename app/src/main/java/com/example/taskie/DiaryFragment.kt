package com.example.taskie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.taskie.database.Diary
import com.example.taskie.database.TaskieDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

/**
 * A fragment of MainActivity for diary.
 */
class DiaryFragment: Fragment() {
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
        val view: View = inflater.inflate(R.layout.fragment_diary, container, false)
        val fab: FloatingActionButton = view.findViewById(R.id.fab_diary)
        val diaryText = view.findViewById(R.id.editMultilineTextDiary) as EditText
        val dayText = view.findViewById(R.id.textViewDate) as TextView
        dayText.text = date

        val dao = TaskieDatabase.getInstance(requireContext()).diaryDao()
        var diary: Diary? = null

        lifecycleScope.launch {
            diary = dao.getDiaryByDate(date)
            diaryText.setText(diary?.diaryInfo)
        }

        fab.setOnClickListener {
            if (diary != null) {
                lifecycleScope.launch {
                    diary?.diaryInfo = diaryText.text.toString()
                    dao.update(diary!!)
                }
            } else {
                lifecycleScope.launch {
                    dao.insert(Diary(diaryText.text.toString(), System.currentTimeMillis()))
                }
            }
        }

        return view
    }


}