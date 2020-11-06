package com.example.osuinfo.startup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.example.osuinfo.R
import com.example.osuinfo.api.osu.OsuClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsernameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val key = sharedPreferences.getString("key", "")!!
        var username = sharedPreferences.getString("username", "")!!

        // Check if the username is already stored and valid, and if so, continue to the next activity.
        validate(key, username) { isValid ->
            if (isValid) next()
        }

        findViewById<FloatingActionButton>(R.id.usernameFloatingActionButton).setOnClickListener {
            username = findViewById<EditText>(R.id.usernameEditText).text.toString()

            // If the entered username is valid, continue to the next activity, otherwise display a
            // toast message letting the user know it was invalid.
            validate(key, username) { isValid ->
                if (!isValid) {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Invalid username! Please try again.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else next()
            }
        }
    }

    // Given an API key and a username, call the API to verify that the username is valid.
    // Then call the callback specified.
    private fun validate(key: String, username: String, callback: (Boolean) -> Unit) {
        var isValid = false

        CoroutineScope(Dispatchers.Default).launch {
            isValid = OsuClient.getUser(key, username) != null
        }.invokeOnCompletion {
            callback(isValid)
        }
    }

    // Save the entered username into the default shared preferences and continue to the next activity.
    private fun next() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // We use commit() instead of apply() here because we want to ensure that the username is stored
        // before entering the next activity as it is required.
        sharedPreferences.edit()
            .putString("username", findViewById<EditText>(R.id.usernameEditText).text.toString())
            .commit()

        val intent = Intent(this, CompleteActivity::class.java)
        startActivity(intent)
    }
}