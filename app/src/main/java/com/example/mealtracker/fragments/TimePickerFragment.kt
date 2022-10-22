package com.example.mealtracker.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.text.SimpleDateFormat
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private val calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = calendar.get(Calendar.HOUR)
        val min = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(requireActivity(), this, hour, min, false)
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, min: Int) {
        calendar.set(Calendar.HOUR, hour)
        calendar.set(Calendar.MINUTE, min)
        val selectedTime = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(calendar.time)
        val selcetdDateBundle = Bundle()
        selcetdDateBundle.putString("SELECTED_TIME", selectedTime)
        setFragmentResult("REQUEST_KEY", selcetdDateBundle)
    }


}