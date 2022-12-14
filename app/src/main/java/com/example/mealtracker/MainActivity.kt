package com.example.mealtracker

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.mealtracker.databinding.ActivityMainBinding
import com.example.mealtracker.fragments.HomeFragment
import com.example.mealtracker.fragments.InputFragment
import com.example.mealtracker.fragments.MonthFragment
import com.example.mealtracker.fragments.WeekFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class MainActivity : AppCompatActivity() {


    private lateinit var USER_ID: String
    private lateinit var bottomnavbar: BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    private lateinit var authenticaion: FirebaseAuth
    var PREFS_KEY = "prefs"
    private var alarmMgr: AlarmManager? = null
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authenticaion = FirebaseAuth.getInstance()
        USER_ID = authenticaion.currentUser?.uid.toString()

        createNotificationChannel()
        scheduleNotification()

//        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main)
        USER_ID = intent.getStringExtra("USER_ID").toString()
        Log.d("onCreate: ", USER_ID)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val homeFragment = HomeFragment()
        val inputFragment = InputFragment()
        val weekFragment = WeekFragment()
        val monthFragment = MonthFragment()
//        bottomnavbar = binding.bottomNav
        bottomnavbar = findViewById<BottomNavigationView>(R.id.bottomNav)
        setTheFragment(homeFragment)
//Navigation between fragments from bottom navigation bar
        bottomnavbar.setOnItemSelectedListener {
            Log.d("Inside the Today", "Main Activity*********************************************")
            when (it.itemId) {
                R.id.today -> {
                    setTheFragment(homeFragment)
                }
                R.id.weekly -> {
                    setTheFragment(weekFragment)
                }
                R.id.monthly -> {
                    setTheFragment(monthFragment)
                }
                R.id.add -> {
                    setTheFragment(inputFragment)
                }
            }
            true
        }

    }

    private fun setTheFragment(fragment: Fragment) {

        val mBundle = Bundle()
        mBundle.putString("UserId", USER_ID)
        fragment.arguments = mBundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment).commit()
        }
    }

    //    Layout inflator for creating bottom navigation bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.logout, menu)
        return true
//        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()
                val i = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(i)

                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun scheduleNotification() {
        val intent = Intent(applicationContext, NotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmMgr = getSystemService(Context.ALARM_SERVICE) as? AlarmManager?
        val alarmFor: Calendar = Calendar.getInstance()
        alarmFor.set(Calendar.HOUR_OF_DAY, 12)
        alarmFor.set(Calendar.MINUTE, 0)
        alarmFor.set(Calendar.SECOND, 0)

        alarmMgr?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmFor.timeInMillis,
            AlarmManager.INTERVAL_HOUR * 6,
            pendingIntent
        )
    }


    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

