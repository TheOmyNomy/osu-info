package com.example.osuinfo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.osuinfo.api.osu.OsuClient
import com.example.osuinfo.startup.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val key = sharedPreferences.getString("key", "")!!
        val username = sharedPreferences.getString("username", "")!!

        // Check if the key and username are valid, and if so, continue to the home fragment.
        // Otherwise, reset whatever is stored in the shared preferences and enter the setup screen.
        validate(key, username) { isValid ->
            if (isValid) {
                runOnUiThread {
                    val mainBottomNavigationView =
                        findViewById<BottomNavigationView>(R.id.mainBottomNavigationView)

                    val navigationController = findNavController(R.id.mainNavigationHostFragment)

                    val applicationBarConfiguration =
                        AppBarConfiguration(setOf(R.id.homeFragment, R.id.settingsFragment))

                    setupActionBarWithNavController(
                        navigationController,
                        applicationBarConfiguration
                    )

                    mainBottomNavigationView.setupWithNavController(navigationController)
                }
            } else next()
        }
    }

    // Given an API key and a username, call the API to verify that both are valid.
    // Then call the callback specified.
    private fun validate(key: String, username: String, callback: (Boolean) -> Unit) {
        var isValid = false

        CoroutineScope(Dispatchers.Default).launch {
            isValid = OsuClient.getUser(key, username) != null
        }.invokeOnCompletion {
            callback(isValid)
        }
    }

    // Reset the stored key and username and enter the setup screen.
    private fun next() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        sharedPreferences.edit()
            .putString("key", "")
            .putString("username", "")
            .apply()

        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }
}