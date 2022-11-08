package com.example.mealtracker.fragments

import android.R
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mealtracker.databinding.FragmentInputBinding
import com.example.mealtracker.foodDetails.FoodDetails
import com.example.mealtracker.foodDetails.FoodX
import com.example.mealtracker.foodDetails.NutrientsX
import com.example.mealtracker.foodDetails.Parsed
import com.example.mealtracker.interfaces.ApiInterface
import com.example.mealtracker.userProfie.FoodNutrients
import com.example.mealtracker.userProfie.Time
import com.example.mealtracker.userProfie.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class InputFragment : Fragment() {

    val REQUEST_IMAGE_CAPTURE = 1
    private val calendar = Calendar.getInstance()
    private var currentDate: String = ""
    private lateinit var binding: FragmentInputBinding
    private var URL: String = "https://api.edamam.com/"

    //    private var storageRef = Firebase.storage.reference;
    private lateinit var authenticaion: FirebaseAuth
    lateinit var imageBitMap: Bitmap
    private lateinit var imageUri: Uri

//    private lateinit var databaseReference: DatabaseReference

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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
//        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Showing date picker in the input window
        binding.datePicker.setOnClickListener {
            datePicker()
        }

//        Showing Time picker in the input window
        binding.timePicker.setOnClickListener {
            timePicker()
        }

//        Show drop down dialogue to show suggestions based on user input
        binding.findSuggestions.setOnClickListener {
            showDropDown()
        }

//        Save all input details for the food after getting the nutrition data from EDEMAN api
        binding.saveToFirebase.setOnClickListener {
            getFoodDetails(binding.searchBox.text.toString())

        }

        binding.imageView.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun showDropDown() {
        when {
            TextUtils.isEmpty(binding.searchBox.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    requireActivity(), "Please enter a food name", Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                val query = binding.searchBox.text.trim().toString()
                suggestions = getSuggestions(query)
                adapter = ArrayAdapter<String>(
                    requireActivity(), R.layout.simple_list_item_1, suggestions
                )
                var autocomplete = binding.searchBox
                autocomplete.setAdapter(adapter)
                adapter?.notifyDataSetChanged()
            }
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
        val retroFitBuilder =
            Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(URL)
                .build().create(ApiInterface::class.java)
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
            }

            override fun onFailure(call: Call<List<String>?>, t: Throwable) {
                Toast.makeText(
                    requireActivity(), "error fetching from API" + t.message, Toast.LENGTH_SHORT
                ).show()
                Log.d("Main Activity ", "On failure " + t.message)
            }
        })
        return myArrayList
    }

    private fun getFoodDetails(input: String): FoodNutrients? {
        var foodNutrients: FoodNutrients? = null

//        Calling the EDEMAM api to get the nutrition data based on the user input
        val retroFitBuilder =
            Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(URL)
                .build().create(ApiInterface::class.java)
        val retoFitData = retroFitBuilder.getFoodDetails(input)
        val storageReference =

            retoFitData.enqueue(object : Callback<FoodDetails?> {
                override fun onResponse(
                    call: Call<FoodDetails?>,
                    response: Response<FoodDetails?>
                ) {
                    val responseBody = response.body()!!
                    val parsed: List<Parsed> = responseBody.parsed
                    val food: FoodX = parsed[0].food
                    val nutrients: NutrientsX = food.nutrients
                    Log.d("Inside the fragment nutrients data", nutrients.toString())
                    foodNutrients = FoodNutrients(
                        nutrients.CHOCDF.toDouble(),
                        nutrients.FAT.toDouble(),
                        nutrients.FIBTG.toDouble(),
                        nutrients.PROCNT.toDouble(),
                        nutrients.ENERC_KCAL
                    )

//                Writing all the details user entered with nutrition data into firebase
                    writeDateToFirebase(
                        foodNutrients!!,
//                    "11-12-32",
                        binding.datePicker.text.toString(),
//                    "12-33",
                        binding.timePicker.text.toString(),
//                    "23",
                        binding.quantity.text.toString(),
                        input
                    )

                }

                override fun onFailure(call: Call<FoodDetails?>, t: Throwable) {
                    val activity: Activity? = activity
                    if (activity != null && isAdded) {
                        Toast.makeText(
                            requireActivity(),
                            "error fetching from API" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Main Activity ", "On failure " + t.message)
                    }
                }
            })

        Log.d("Outside the fragment", foodNutrients.toString())
        return foodNutrients
    }

    //Function to write data to firebase
    private fun writeDateToFirebase(
        nutrientsX: FoodNutrients,
        date: String,
        time: String,
        quantity: String,
        mealName: String
    ) {
        authenticaion = FirebaseAuth.getInstance()
//        val uid = authenticaion.currentUser?.uid
        val uid = "OLbgV02I7aQzrxooENPCm2ptGUG1"
        val database = FirebaseDatabase.getInstance().reference.child("Users")
        val timeT = Time(nutrientsX, "Image Url", "BreakFast", quantity, time)
        val dateD = com.example.mealtracker.userProfie.Date(date, listOf(timeT))
        val user = UserData(listOf(dateD), "First User")
        val refStorage = FirebaseStorage.getInstance().getReference("Images")
        val baos = ByteArrayOutputStream()
        imageBitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val img = baos.toByteArray()
        refStorage.putBytes(img)
        database.child(uid).child(date).child(time).setValue(timeT).addOnSuccessListener {
            val activity: Activity? = activity
            if (activity != null && isAdded) {
                Toast.makeText(
                    requireActivity(),
                    "Saved Data Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                imageBitMap = data?.extras?.get("data") as Bitmap
//                imageUri = data?.data!!

                binding.imageView.setImageBitmap(imageBitMap)
//                binding.imageView.setImageURI(imageUri)
            }
        }
    }
}




