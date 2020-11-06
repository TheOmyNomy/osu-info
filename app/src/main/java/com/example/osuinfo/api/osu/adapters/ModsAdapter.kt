package com.example.osuinfo.api.osu.adapters

import com.example.osuinfo.api.osu.Mods
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson

// Used to convert a string into a Mods object when reading in a JSON file.
class ModsAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): List<Mods>? {
        val value = reader.nextString().toInt()

        val result = Mods.values().filter { mod ->
            (mod.value and value) > 0
        } as MutableList<Mods>

        if (result.contains(Mods.NIGHT_CORE)) result.remove(Mods.DOUBLE_TIME)
        if (result.contains(Mods.PERFECT)) result.remove(Mods.SUDDEN_DEATH)
        if (result.contains(Mods.CINEMA)) result.remove(Mods.AUTO)

        return result
    }

    @ToJson
    fun toJson(mods: List<Mods>): String {
        var value = 0

        mods.forEach { mod ->
            value += mod.value
        }

        return value.toString()
    }
}