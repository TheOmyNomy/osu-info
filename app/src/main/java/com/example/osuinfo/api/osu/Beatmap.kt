package com.example.osuinfo.api.osu

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Beatmap(
    @Json(name = "artist") var artist: String?,
    @Json(name = "beatmapset_id") var beatmapSetId: Int?,
    @Json(name = "creator") var creator: String?,
    @Json(name = "difficultyrating") var starRating: Double?,
    @Json(name = "title") var title: String?,
    @Json(name = "version") var version: String?,
    @Json(name = "max_combo") var combo: Int?
) : Parcelable