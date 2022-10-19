package com.example.mealtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import com.example.mealtracker.databinding.ActivityLoginBinding
import com.example.mealtracker.databinding.ActivityRegistrationBinding
import com.example.mealtracker.fragments.HomeFragment
import com.github.mikephil.charting.data.PieEntry

class MainActivity : AppCompatActivity() {

    lateinit var pieList:ArrayList<PieEntry>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentManage= supportFragmentManager
        val fragmenttrans=fragmentManage.beginTransaction()
        fragmenttrans.replace(R.id.fragment_container,HomeFragment.newInstance("hello","hello2"))
        setContentView(R.layout.activity_main)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.nav_menu, menu)

        return super.onCreateOptionsMenu(menu)

    }

}