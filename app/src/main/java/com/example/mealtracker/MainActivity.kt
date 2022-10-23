package com.example.mealtracker

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.mealtracker.databinding.ActivityMainBinding
import com.example.mealtracker.fragments.HomeFragment
import com.example.mealtracker.fragments.InputFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    private lateinit var USER_ID: String
    private lateinit var bottomnavbar: BottomNavigationView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main)
        USER_ID = intent.getStringExtra("USER_ID").toString()
        Log.d("onCreate: ", USER_ID)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val homeFragment = HomeFragment()
        val inputFragment = InputFragment()
//        bottomnavbar = binding.bottomNav
        bottomnavbar = findViewById<BottomNavigationView>(R.id.bottomNav)

        setTheFragment(homeFragment)

        bottomnavbar.setOnItemSelectedListener {
            Log.d("Inside the Today", "Main Activity*********************************************")
            when (it.itemId) {
                R.id.today -> {
                    setTheFragment(homeFragment)
                }
                R.id.weekly -> {
//                    setTheFragment(inputFragment)
                }
                R.id.monthly -> {
//                    setTheFragment(inputFragment)
                }
                R.id.add -> {
                    setTheFragment(inputFragment)
                }
            }
            true
        }
    }

    private fun setTheFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment).commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

}