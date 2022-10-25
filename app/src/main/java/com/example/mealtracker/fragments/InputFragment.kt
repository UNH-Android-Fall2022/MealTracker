package com.example.mealtracker.fragments

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.mealtracker.databinding.FragmentInputBinding
import com.example.mealtracker.interfaces.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class InputFragment : Fragment() {

    private val calendar = Calendar.getInstance()
    private var currentDate: String = ""
    private lateinit var binding: FragmentInputBinding
    private var URL: String =
        "https://api.edamam.com/"

    var suggestions: List<String> = ArrayList<String>()
    var adapter: ArrayAdapter<String>? = null
//    var autocomplete  =null


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
            datePicker()
        }

        binding.timePicker.setOnClickListener {
            timePicker()
        }

        binding.findSuggestions.setOnClickListener {

            val query = binding.searchBox.text.trim().toString()
            suggestions = getSuggestions(query)
            //this will call your method every time the user stops typing, if you want to call it for each letter, call it in onTextChanged
            adapter =
                ArrayAdapter<String>(requireActivity(), R.layout.simple_list_item_1, suggestions)

            var autocomplete = binding.searchBox

            autocomplete.setAdapter(adapter)
            adapter?.notifyDataSetChanged()
        }


    }

    private fun timePicker() {
        val timePickerFragment = TimePickerFragment()
        val supportFragmentManager = requireActivity().supportFragmentManager
        Log.d("In time picker ", "button clicked")
        supportFragmentManager.setFragmentResultListener(
            "REQUEST_KEY", viewLifecycleOwner
        ) { resultKey, bundle ->
            if (resultKey == "REQUEST_KEY") {
                val time = bundle.getString("SELECTED_TIME")
                Log.d("Selected time", "$time")
                //                    currentDate = time.toString()
                //                    calendar.set(Calendar.MINUTE, currentDate.split("-")[1].toInt())
                //                    calendar.set(Calendar.HOUR, currentDate.split("-")[0].toInt())
                binding.timePicker.setText(time)
            }
        }
        timePickerFragment.show(supportFragmentManager, "DatePickerFragment")
    }


    // To get the date from the user
    private fun datePicker() {
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


    // Making the api call to the server for getting suggestions bases on users input
    private fun getSuggestions(input: String): ArrayList<String> {
        val myArrayList = ArrayList<String>()
        val retroFitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(URL).build().create(ApiInterface::class.java)
        val retoFitData = retroFitBuilder.getData(input)
        retoFitData.enqueue(object : Callback<List<String>?> {
            override fun onResponse(call: Call<List<String>?>, response: Response<List<String>?>) {
                val responseBody = response.body()!!
                val myStringBuilder = StringBuilder()
                for (myData in responseBody) {
                    myArrayList.add(myData)
                    myStringBuilder.append(myData)
                    myStringBuilder.append("\n")
                }
                Log.d("Inside InputFragment", myStringBuilder.toString())
//                suggestions = myArrayList
//                adapter = ArrayAdapter<String>(
//                    requireActivity(),
//                    R.layout.simple_list_item_1, suggestions
//                )
//                adapter?.notifyDataSetChanged()

            }

            override fun onFailure(call: Call<List<String>?>, t: Throwable) {
                Log.d("Main Activity ", "On failure " + t.message)
            }
        })

        return myArrayList


    }

}


