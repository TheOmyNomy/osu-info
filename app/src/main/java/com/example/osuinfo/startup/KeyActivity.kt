package com.example.osuinfo.startup

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.osuinfo.R
import com.example.osuinfo.api.osu.OsuClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KeyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var key = sharedPreferences.getString("key", "")!!

        // Check if the API key is already stored and valid, and if so, continue to the next activity.
        validate(key) { isValid ->
            if (isValid) next()
        }

        // Load the API page when the website button is clicked.
        findViewById<Button>(R.id.osuWebsiteButton).setOnClickListener {
            findViewById<WebView>(R.id.osuWebsiteWebView).loadUrl("https://osu.ppy.sh/p/api")
        }

        findViewById<FloatingActionButton>(R.id.keyFloatingActionButton).setOnClickListener {
            key = findViewById<EditText>(R.id.keyEditText).text.toString()

            // If the entered key is valid, continue to the next activity, otherwise display a
            // toast message letting the user know it was invalid.
            validate(key) { isValid ->
                if (!isValid) {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Invalid API key! Please try again.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else next()
            }
        }
    }

    // Given an API key, call the API with a test user to verify that it is valid.
    // Then call the callback specified.
    private fun validate(key: String, callback: (Boolean) -> Unit) {
        var isValid = false

        CoroutineScope(Dispatchers.Default).launch {
            isValid = OsuClient.getUser(key, "peppy") != null
        }.invokeOnCompletion {
            callback(isValid)
        }
    }

    // Save the entered key into the default shared preferences and continue to the next activity.
    private fun next() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // We use commit() instead of apply() here because we want to ensure that the key is stored
        // before entering the next activity as it is required.
        sharedPreferences.edit()
            .putString("key", findViewById<EditText>(R.id.keyEditText).text.toString())
            .commit()

        val intent = Intent(this, UsernameActivity::class.java)
        startActivity(intent)
    }
}