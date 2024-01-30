package com.disc.notesapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var login : TextView
    private lateinit var signup : TextView
    private lateinit var email : EditText
    private lateinit var password : EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val color = android.graphics.Color.parseColor("#252525")
        window.statusBarColor = color

        login = findViewById(R.id.login)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        signup = findViewById(R.id.signup)

        val auth = FirebaseAuth.getInstance()

        login.setOnClickListener {
            if (email.text.isEmpty() || password.text.isEmpty()){
                showToast("Fill all blank edittext")
            }else{
                val userEmail = email.text.toString().trim()
                val userPassword = password.text.toString().trim()
                auth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            showToast("Login successful")
                            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                            finish()
                        } else {
                            showToast("Login failed: ${task.exception?.message}")
                        }
                    }

            }
        }

        signup.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
            finish()
        }

    }

    fun showToast(message:String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}