package com.example.osuinfo.api.osu

import android.util.Log
import com.example.osuinfo.api.osu.adapters.LocalDateAdapter
import com.example.osuinfo.api.osu.adapters.ModsAdapter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request

object OsuClient {
    private const val baseUrl = "https://osu.ppy.sh/api/"

    // The instance of moshi used to convert JSON data into a usable object.
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(LocalDateAdapter())
        .add(ModsAdapter())
        .build()

    // API callback that fetches a list of beatmaps given a beatmap ID and a list of mods.
    fun getBeatmaps(key: String, beatmapId: Int, mods: List<Mods>): List<Beatmap>? {
        var value = 0

        // Converts the list of mods into its flag form.
        mods.forEach { mod ->
            if (mod == Mods.EASY || mod == Mods.HARD_ROCK || mod == Mods.DOUBLE_TIME || mod == Mods.HALF_TIME) {
                value += mod.value
            } else if (mod == Mods.NIGHT_CORE) {
                value += Mods.DOUBLE_TIME.value
            }
        }

        // Fetch the JSON data from the API.
        val data = run("${baseUrl}get_beatmaps?k=${key}&b=${beatmapId}&mods=${value}")

        // Create the adapters for creating the object from the JSON data.
        val resultType = Types.newParameterizedType(List::class.java, Beatmap::class.java)
        val adapter: JsonAdapter<List<Beatmap>> = moshi.adapter(resultType)

        val result: List<Beatmap>?

        // Convert the JSON data to an object.
        try {
            result = adapter.fromJson(data)
        } catch (e: JsonDataException) {
            return null
        }

        // Return the list of beatmaps if it's not null or empty, otherwise return null.
        return if (result != null && result.isNotEmpty()) result else null
    }

    // API callback that fetches a list of user's top 10 best scores given a username.
    fun getUserBest(key: String, username: String): List<Score>? {
        // Fetch the JSON data from the API.
        val data = run("${baseUrl}get_user_best?k=${key}&u=${username}")

        // Create the adapters for creating the object from the JSON data.
        val resultType = Types.newParameterizedType(List::class.java, Score::class.java)
        val adapter: JsonAdapter<List<Score>> = moshi.adapter(resultType)

        val result: List<Score>?

        // Convert the JSON data to an object.
        try {
            result = adapter.fromJson(data)
        } catch (e: JsonDataException) {
            return null
        }

        // Return the list of scores if it's not null or empty, otherwise return null.
        return if (result != null && result.isNotEmpty()) result else null
    }

    // API callback that fetches a user given a username.
    fun getUser(key: String, username: String): User? {
        // Fetch the JSON data from the API.
        val data = run("${baseUrl}get_user?k=${key}&u=${username}")

        // Create the adapters for creating the object from the JSON data.
        val resultType = Types.newParameterizedType(List::class.java, User::class.java)
        val adapter: JsonAdapter<List<User>> = moshi.adapter(resultType)

        val result: List<User>?

        // Convert the JSON data to an object.
        try {
            result = adapter.fromJson(data)
        } catch (e: JsonDataException) {
            return null
        }

        // Return the user object if it's not null or empty, otherwise return null.
        return if (result != null && result.isNotEmpty()) result[0] else null
    }

    // Given a URL, call the API and return the body's response (JSON data).
    private fun run(url: String): String {
        val request = Request.Builder().url(url).build()

        OkHttpClient().newCall(request).execute().use { response ->
            return response.body!!.string()
        }
    }
}