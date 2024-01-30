package com.disc.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val color = android.graphics.Color.parseColor("#252525")
        window.statusBarColor = color

        val auth = FirebaseAuth.getInstance()

        Handler().postDelayed({
            if (auth.currentUser!=null){
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
            finish()
        }, 2000)
    }
}