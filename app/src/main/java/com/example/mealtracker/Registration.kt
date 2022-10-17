package com.example.mealtracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.mealtracker.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Registration : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private val db = FirebaseAuth.getInstance()


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

//                    https://firebase.google.com/docs/auth/android/start#kotlin+ktx_3

                    db.createUserWithEmailAndPassword(emailId, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    this@Registration,
                                    "Created User succesfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(this@Registration, LoginActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("userId", firebaseUser.uid)
                                intent.putExtra("email", emailId)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@Registration,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                }
            }


        }
    }
}