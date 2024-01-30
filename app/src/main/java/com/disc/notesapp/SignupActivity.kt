package com.disc.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var login : TextView
    private lateinit var signup : TextView
    private lateinit var email : EditText
    private lateinit var name : EditText
    private lateinit var password : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val color = android.graphics.Color.parseColor("#252525")
        window.statusBarColor = color

        login = findViewById(R.id.login)
        email = findViewById(R.id.email)
        name = findViewById(R.id.name)
        password = findViewById(R.id.password)
        password = findViewById(R.id.repassword)
        signup = findViewById(R.id.signup)
        val auth = FirebaseAuth.getInstance()

        signup.setOnClickListener {
            if (name.text.isEmpty() || email.text.isEmpty() || password.text.isEmpty()){
                showToast("Fill all blank edittext")
            }else{
                val userEmail = email.text.toString().trim()
                val userPassword = password.text.toString().trim()
                auth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        } else {
                            // If sign-up fails, display a message to the user.
                            showToast("Sign-up failed: ${task.exception?.message}")
                        }
                    }
            }
        }

        login.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

    }

    fun showToast(message:String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}