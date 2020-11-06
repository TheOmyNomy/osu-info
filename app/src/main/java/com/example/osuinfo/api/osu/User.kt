package com.example.osuinfo.api.osu

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "user_id") var userId: Int?,
    @Json(name = "username") var username: String?,
    @Json(name = "pp_rank") var globalRank: Int?,
    @Json(name = "level") var level: Float?,
    @Json(name = "pp_raw") var performancePoints: Float?,
    @Json(name = "accuracy") var accuracy: Float?,
    @Json(name = "country") var countryCode: String?,
    @Json(name = "pp_country_rank") var countryRank: Int?
) : Parcelable