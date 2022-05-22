package com.example.taskie.ui.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.taskie.DiaryFragment
import com.example.taskie.R
import com.example.taskie.TaskFragment


private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager, private var date: String) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putString("date", date)
        return if (position == 0) {
            val taskFragment = TaskFragment()
            taskFragment.arguments = bundle
            taskFragment
        } else {
            val diaryFragment = DiaryFragment()
            diaryFragment.arguments = bundle
            diaryFragment
        }
    }

    override fun getItemPosition(obj: Any): Int {
        return POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }

    fun setDate(date: String) {
        this.date = date
    }
}