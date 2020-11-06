package com.example.osuinfo.api.osu.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

// Used to convert a string into a LocalDate object when reading in a JSON file.
class LocalDateAdapter {
    private val pattern = "yyyy-MM-dd HH:mm:ss"

    @FromJson
    fun fromJson(reader: JsonReader): LocalDate? {
        val value = reader.nextString()
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return LocalDate.parse(value, formatter)
    }

    @ToJson
    fun toJson(date: LocalDate?): String {
        return SimpleDateFormat(pattern, Locale.ENGLISH).format(date)
    }
}