package com.example.taskie

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
import com.example.taskie.database.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * A fragment of MainActivity for diary.
 */
class DiaryFragment: Fragment() {
    private lateinit var date: String
    private lateinit var text: String
    private var diary: Diary? = null
    private lateinit var dao: DiaryDao
    private lateinit var diaryText: EditText

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
        val view: View = inflater.inflate(R.layout.fragment_diary, container, false)
        val fab: FloatingActionButton = view.findViewById(R.id.fab_diary)
        diaryText = view.findViewById(R.id.editMultilineTextDiary) as EditText
        val dayText = view.findViewById(R.id.textViewDate) as TextView
        val arrowLeft: ImageView = view.findViewById(R.id.imageViewDateLeft)
        val arrowRight: ImageView = view.findViewById(R.id.imageViewDateRight)
        dayText.text = date.replace("-", ".")

        dao = TaskieDatabase.getInstance(requireContext()).diaryDao()

        loadDiary()

        // save diary
        fab.setOnClickListener {
            if (diary != null) {
                lifecycleScope.launch {
                    diary?.diaryInfo = diaryText.text.toString()
                    dao.update(diary!!)
                }
            } else {
                val formatter: DateTimeFormatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
                val localDate: LocalDate? = LocalDate.parse(date, formatter)
                val timeInMilliseconds: Long =
                    (localDate!!.atStartOfDay(ZoneOffset.UTC).toEpochSecond() + 1) * 1000
                val newDiary = Diary(diaryText.text.toString(), timeInMilliseconds)
                lifecycleScope.launch {
                    dao.insert(newDiary)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("text", text)
    }

    private fun loadDiary() {
        lifecycleScope.launch {
            diary = dao.getDiaryByDate(date)
            diaryText.setText(diary?.diaryInfo)
            text = diaryText.text.toString()
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

        loadDiary()

        day.text = date.replace("-", ".")
        (activity as MainActivity?)?.dateChanged(date)
    }
}