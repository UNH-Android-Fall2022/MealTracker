package com.example.mealtracker.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealtracker.R
import com.example.mealtracker.adapter.MyAdapter
import com.example.mealtracker.databinding.FragmentHomeBinding
import com.example.mealtracker.interfaces.MyViewModelFactory
import com.example.mealtracker.interfaces.TimeViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private lateinit var viewModel: TimeViewModel
private lateinit var timeRecyclerView: RecyclerView
lateinit var adapter: MyAdapter

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var param1: String? = null
    private var param2: String? = null
    lateinit var pieChart: PieChart
    private var currentDate: String = ""
    private lateinit var binding: FragmentHomeBinding
    private val calendar = Calendar.getInstance()
    private lateinit var db: DatabaseReference
    private lateinit var authenticaion: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        binding = FragmentHomeBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
        setPieChart()

        return binding.root
    }

//    https://www.youtube.com/watch?v=OEhSuv-_a9w&ab_channel=geekofia

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.leftArrow.setOnClickListener {
            this.calendar.add(Calendar.DAY_OF_MONTH, -1)
            currentDate = SimpleDateFormat("dd-MM-YYYY", Locale.ENGLISH).format(calendar.time)
            binding.datePicker.text = currentDate
//            Todo get the data from firebase
        }

        binding.rightArrow.setOnClickListener {
            this.calendar.add(Calendar.DAY_OF_MONTH, 1)
            currentDate = SimpleDateFormat("dd-MM-YYYY", Locale.ENGLISH).format(calendar.time)
            binding.datePicker.text = currentDate
            //            Todo get the data from firebase

        }

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
                    if (slectedDate == date) {
                        binding.datePicker.text = "Today"
                    } else {
                        binding.datePicker.text = date

                    }
                }
            }
            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
        }

        timeRecyclerView = view.findViewById(R.id.recyclerViewHome)
        timeRecyclerView.layoutManager = LinearLayoutManager(context)
        timeRecyclerView.setHasFixedSize(true)
        adapter = MyAdapter()
        timeRecyclerView.adapter = adapter
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory("10-11-2022", "OLbgV02I7aQzrxooENPCm2ptGUG1")
        ).get(TimeViewModel::class.java)

        viewModel.allTimes.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.updateTimeList(it)
        })


    }


    //    https://www.geeksforgeeks.org/android-create-a-pie-chart-with-kotlin/

    private fun setPieChart() {
        pieChart = binding.pieChart
        getDataFromFireBase("hello")


        // on below line we are setting user percent value,
        // setting description as enabled and offset for pie chart
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        pieChart.dragDecelerationFrictionCoef = 0.95f

        // on below line we are setting hole
        // and hole color for pie chart
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)

        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f

        // on below line we are setting center text
        pieChart.setDrawCenterText(true)

        // on below line we are setting
        // rotation for our pie chart
        pieChart.rotationAngle = 0f

        // enable rotation of the pieChart by touch
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        // on below line we are setting animation for our pie chart
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)

        // on below line we are creating array list and
        // adding data to it to display in pie chart
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(90f))
        entries.add(PieEntry(8f))
        entries.add(PieEntry(20f))

        // on below line we are setting pie data set
        val dataSet = PieDataSet(entries, "Mobile OS")

        // on below line we are setting icons.
        dataSet.setDrawIcons(false)

        // on below line we are setting slice for pie
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors to list
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.green_c))
        colors.add(resources.getColor(R.color.yellow))
        colors.add(resources.getColor(R.color.red))

        // on below line we are setting colors.
        dataSet.colors = colors

        // on below line we are setting pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.data = data

        // undo all highlights
        pieChart.highlightValues(null)

        // loading chart
        pieChart.invalidate()
    }


    private fun getDataFromFireBase(date: String) {
        db = Firebase.database.reference
        val dummyDate = "14-10-2022"
        val uid = "OLbgV02I7aQzrxooENPCm2ptGUG1"
        /*db.child("Users").child(uid).get()
            .addOnSuccessListener { result ->
                Log.d("TAG getting details from firebase", "${result.child("25-10-2022").value}")
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
            }*/
        val myRef = db.child("Users").child(uid)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var fiber = 0
                var protein = 0
                var fat = 0
                var calories = 0
                var cholestrol = 0

                for (dates in snapshot.children) {

                    if (dates.key == dummyDate) {
                        for (times in dates.children) {
                            Log.d("Inside TIMES", "${times.value}")
                            val nutrients = times.child("foodNutrients")
                            Log.d("Inside Nutrients", "${nutrients.value}")
                            cholestrol += nutrients.child("chocdf").getValue<Int>()!!
                            calories += nutrients.child("enerc_KCAL").getValue<Int>()!!
                            fat += nutrients.child("fat").getValue<Int>()!!
                            fiber += nutrients.child("fibtg").getValue<Int>()!!
                            protein += nutrients.child("procnt").getValue<Int>()!!
                        }
                    }
                    Log.d(
                        "TOTAL NUTRIENTS",
                        "onDataChange: $calories $fiber $protein $fat $cholestrol"
                    )
                }
                if (isAdded)// This {@link androidx.fragment.app.Fragment} class method is responsible to check if the your view is attached to the Activity or not
                {
                    val entries: ArrayList<PieEntry> = ArrayList()
                    entries.add(PieEntry(fiber.toFloat()))
                    entries.add(PieEntry(protein.toFloat()))
                    entries.add(PieEntry(fat.toFloat()))
                    entries.add(PieEntry(calories.toFloat()))
                    entries.add(PieEntry(cholestrol.toFloat()))

                    val dataSet = PieDataSet(entries, "DAILY Intake")
                    dataSet.setDrawIcons(false)

                    // on below line we are setting slice for pie
                    dataSet.sliceSpace = 6f
                    dataSet.iconsOffset = MPPointF(0f, 40f)
                    dataSet.selectionShift = 10f

                    // add a lot of colors to list
                    val colors: ArrayList<Int> = ArrayList()
                    colors.add(resources.getColor(R.color.teal_200))
                    colors.add(resources.getColor(R.color.yellow))
                    colors.add(resources.getColor(R.color.red))
                    colors.add(resources.getColor(R.color.material_dynamic_primary70))
                    colors.add(resources.getColor(R.color.green_c))


                    // on below line we are setting colors.
                    dataSet.colors = colors

                    // on below line we are setting pie data set
                    val data = PieData(dataSet)
                    data.setValueFormatter(PercentFormatter())
                    data.setValueTextSize(15f)
                    data.setValueTypeface(Typeface.DEFAULT_BOLD)
                    data.setValueTextColor(Color.WHITE)
                    pieChart.data = data

                    // undo all highlights
                    pieChart.highlightValues(null)

                    // loading chart
                    pieChart.invalidate()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}