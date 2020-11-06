package com.example.osuinfo.startup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.osuinfo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        findViewById<FloatingActionButton>(R.id.welcomeFloatingActionButton).setOnClickListener {
            val intent = Intent(this, KeyActivity::class.java)
            startActivity(intent)
        }
    }
}