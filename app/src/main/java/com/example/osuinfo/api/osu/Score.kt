package com.example.osuinfo.api.osu

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Parcelize
@JsonClass(generateAdapter = true)
data class Score(
    @Json(name = "beatmap_id") var beatmapId: Int?,
    @Json(name = "score") var score: Int?,
    @Json(name = "maxcombo") var combo: Int?,
    @Json(name = "count50") var count50: Int?,
    @Json(name = "count100") var count100: Int?,
    @Json(name = "count300") var count300: Int?,
    @Json(name = "countmiss") var countMiss: Int?,
    @Json(name = "enabled_mods") var mods: List<Mods>?,
    @Json(name = "date") var date: LocalDate?,
    @Json(name = "rank") var grade: String?,
    @Json(name = "pp") var performancePoints: Float?,
    var accuracy: Float?
) : Parcelable {
    init {
        accuracy =
            (50.0f * count50!! + 100.0f * count100!! + 300.0f * count300!!) / (300.0f * (countMiss!! + count50!! + count100!! + count300!!)) * 100.0f
    }
}