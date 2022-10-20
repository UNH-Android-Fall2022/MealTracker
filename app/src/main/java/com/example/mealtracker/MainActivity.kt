package com.example.mealtracker

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.mealtracker.databinding.FragmentHomeBinding
import com.example.mealtracker.fragments.HomeFragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart

import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF

class MainActivity : AppCompatActivity() {


    private lateinit var binding: FragmentHomeBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

//    Sample Data
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.nav_menu, menu)

        return super.onCreateOptionsMenu(menu)

    }

}