package com.example.mealtracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mealtracker.databinding.FragmentInputBinding
import java.text.SimpleDateFormat
import java.util.*

class InputFragment : Fragment() {

    private val calendar = Calendar.getInstance()
    private var currentDate: String = ""
    private lateinit var binding: FragmentInputBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        binding = FragmentInputBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return binding.root
//        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.datePicker.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager
            Log.d("In date picker ", "button clicked")
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY", viewLifecycleOwner
            ) { resultKey, bundle ->
                if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    Log.d("Selected date", "$date")
                    val slectedDate =
                        SimpleDateFormat("dd-MM-YYYY", Locale.ENGLISH).format(calendar.time)
                    currentDate = date.toString()
                    calendar.set(Calendar.YEAR, currentDate.split("-")[2].toInt())
                    calendar.set(Calendar.MONTH, currentDate.split("-")[1].toInt())
                    calendar.set(Calendar.DATE, currentDate.split("-")[0].toInt())
                    binding.datePicker.setText(date)
                }
            }
            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")

        }
    }


}
