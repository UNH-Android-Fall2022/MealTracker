package com.example.mealtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.mealtracker.databinding.ActivityRegistrationBinding
import com.google.firebase.ktx.Firebase


class Registration : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.registerbtn.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.username.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@Registration,
                        "Please enter username/email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@Registration,
                        "Please enter password",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.confirmPassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@Registration,
                        "Please confirm password",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                !TextUtils.equals(
                    binding.confirmPassword.text.toString(),
                    binding.password.text.toString()
                ) -> {
                    Toast.makeText(
                        this@Registration,
                        "password don't match",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {

                    val emailId: String = binding.username.text.toString().trim() { it <= ' ' }
                    val password: String = binding.password.text.toString().trim() { it <= ' ' }

                }
            }


        }
    }
}