package com.example.osuinfo

import android.os.Bundle
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.osuinfo.api.osu.OsuClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.xml_preference, rootKey)

        val keyPreference = findPreference<EditTextPreference>("key")!!
        val usernamePreference = findPreference<EditTextPreference>("username")!!

        // Since we're using a coroutine scope, the onPreferenceChange() event will finish
        // before the result of the validate() method. To get around this issue, we store the last
        // key and username, and if the validation process fails, set the preference values to their
        // last correct value.
        var lastKey = keyPreference.text
        var lastUsername = usernamePreference.text

        keyPreference.setOnPreferenceChangeListener { preference, value ->
            validate(value as String, usernamePreference.text) { isValid ->
                // We have to run these on the UI thread because we're creating a UI element
                // (either a toast or a snack bar).
                requireActivity().runOnUiThread {
                    // If it's valid, display a success toast message.
                    if (isValid) {
                        Toast.makeText(
                            requireContext(),
                            requireContext().getString(R.string.success),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // If it's not valid, display a snack bar with a retry button.
                        keyPreference.text = lastKey

                        Snackbar.make(
                            requireView(),
                            requireContext().getString(R.string.invalid_key),
                            Snackbar.LENGTH_LONG
                        ).setAction(requireContext().getString(R.string.retry)) {
                            preference.performClick()
                        }.show()
                    }
                }
            }

            requireActivity().runOnUiThread {
                lastKey = keyPreference.text
            }

            true
        }

        usernamePreference.setOnPreferenceChangeListener { preference, value ->
            validate(keyPreference.text, value as String) { isValid ->
                // We have to run these on the UI thread because we're creating a UI element
                // (either a toast or a snack bar).
                requireActivity().runOnUiThread {
                    // If it's valid, display a success toast message.
                    if (isValid) {
                        Toast.makeText(
                            requireContext(),
                            requireContext().getString(R.string.success),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // If it's not valid, display a snack bar with a retry button.
                        usernamePreference.text = lastUsername

                        Snackbar.make(
                            requireView(),
                            requireContext().getString(R.string.invalid_username),
                            Snackbar.LENGTH_LONG
                        ).setAction(requireContext().getString(R.string.retry)) {
                            preference.performClick()
                        }.show()
                    }
                }
            }

            requireActivity().runOnUiThread {
                lastUsername = usernamePreference.text
            }

            true
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
}