package com.example.osuinfo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.example.osuinfo.api.osu.Beatmap
import com.example.osuinfo.api.osu.OsuClient
import com.example.osuinfo.api.osu.Score
import com.example.osuinfo.api.osu.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(context: Context) : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _scores = MutableLiveData<List<Score>>()
    val scores: LiveData<List<Score>> = _scores

    private val _beatmaps = MutableLiveData<List<Beatmap>>()
    val beatmaps: LiveData<List<Beatmap>> = _beatmaps

    // Called when this object is initialised (aka on fragment load).
    init {
        CoroutineScope(Dispatchers.Default).launch {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

            val key = sharedPreferences.getString("key", "")!!
            val username = sharedPreferences.getString("username", "")!!

            // Fetch the user from the API and update the live data.
            val user = OsuClient.getUser(key, username)
            _user.postValue(user)

            // Fetch the user's scores from the API.
            val scores = OsuClient.getUserBest(key, username)
            val beatmaps = mutableListOf<Beatmap>()

            // Assuming there are scores, fetch its associated beatmap and add it to the beatmap list.
            scores?.forEach { score ->
                val beatmap = OsuClient.getBeatmaps(key, score.beatmapId!!, score.mods!!)!![0]
                beatmaps.add(beatmap)
            }

            // Update the score and beatmap list live data.
            _scores.postValue(scores)
            _beatmaps.postValue(beatmaps)
        }
    }
}