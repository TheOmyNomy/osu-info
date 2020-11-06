package com.example.osuinfo.startup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.osuinfo.MainActivity
import com.example.osuinfo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete)

        findViewById<FloatingActionButton>(R.id.completeFloatingActionButton).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}