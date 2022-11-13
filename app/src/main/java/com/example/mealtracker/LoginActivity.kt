package com.example.mealtracker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mealtracker.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val db = FirebaseAuth.getInstance()
    private val TAG: String = "In Login Activity"
    lateinit var sharedPreferences: SharedPreferences
    var PREFS_KEY = "prefs"
    var EMAIL_KEY = "email"
    var PWD_KEY = "pwd"
    var email = ""
    var pwd = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

        email = sharedPreferences.getString(EMAIL_KEY, "").toString()

        pwd = sharedPreferences.getString(PWD_KEY, "").toString()
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

//      https://firebase.google.com/docs/auth/android/start#kotlin+ktx_3

        binding.loginbtn.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.username.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter username/email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val emailId: String = binding.username.text.toString().trim { it <= ' ' }
                    val password: String = binding.password.text.toString().trim { it <= ' ' }
                    db.signInWithEmailAndPassword(emailId, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // on below line we are adding our email and
                                // pwd to shared prefs to save them.
                                editor.putString(EMAIL_KEY, emailId)
                                editor.putString(PWD_KEY, password)

                                // on below line we are applying
                                // changes to our shared prefs.
                                editor.apply()
                                val user = db.currentUser
                                if (user != null) {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Login Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

//                                Logging into the main activity
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("USER_ID", user?.uid)
                                startActivity(intent)
                                finish()

                            } else {
                                Log.d(TAG, "Sign in failed", task.exception)
                                Toast.makeText(
                                    this@LoginActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }

// Showing registration activity
        binding.signUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, Registration::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if (!email.equals("") && !pwd.equals("")) {
            // if email and pwd is not empty we
            // are opening our main 2 activity on below line.
            val i = Intent(this@LoginActivity, MainActivity::class.java)

            // on below line we are calling start
            // activity method to start our activity.
            startActivity(i)

            // on below line we are calling
            // finish to finish our main activity.
            finish()
        }
    }
}