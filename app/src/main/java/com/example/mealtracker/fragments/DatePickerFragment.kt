package com.example.mealtracker.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private val calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DATE)
        return DatePickerDialog(requireActivity(), this, year, month, date)
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, date: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DATE, date)
        val slectedDate = SimpleDateFormat("dd-MM-YY", Locale.ENGLISH).format(calendar.time)
        val selcetdDateBundle = Bundle()
        selcetdDateBundle.putString("SELECTED_DATE", slectedDate)
        setFragmentResult("REQUEST_KEY", selcetdDateBundle)
    }
}