package com.example.osuinfo

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.osuinfo.api.osu.Beatmap
import com.example.osuinfo.api.osu.Score
import java.time.LocalDate
import java.time.Period

class ScoreAdapter(
    private val scores: LiveData<List<Score>>,
    private val beatmaps: LiveData<List<Beatmap>>,
    private val callback: (Score, Beatmap) -> Unit
) : RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.layout_score, parent, false) as View
        return ViewHolder(view)
    }

    // There's a chance that the scores live data has not been initialised yet.
    // In that case, there won't be any items in the list, so we can safely set the item count to 0.
    override fun getItemCount() = scores.value?.size ?: 0

    override fun onBindViewHolder(holder: ScoreAdapter.ViewHolder, position: Int) {
        val score = scores.value!![position]
        val beatmap = beatmaps.value!![position]

        holder.bind(score, beatmap)
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val scoreMiniGradeImageView =
            view.findViewById<ImageView>(R.id.scoreMiniGradeImageView)
        private val scoreMiniBeatmapTextView =
            view.findViewById<TextView>(R.id.scoreMiniBeatmapTextView)

        private val scoreMiniInformationTextView =
            view.findViewById<TextView>(R.id.scoreMiniInformationTextView)

        private val scoreMiniDifficultyTextView =
            view.findViewById<TextView>(R.id.scoreMiniDifficultyTextView)

        private val scoreMiniDateTextView = view.findViewById<TextView>(R.id.scoreMiniDateTextView)

        fun bind(score: Score, beatmap: Beatmap) {
            // Get the associated bitmap resource from the score's grade and set the image view to it.
            scoreMiniGradeImageView.setImageBitmap(
                when (score.grade) {
                    "XH" -> BitmapFactory.decodeResource(
                        scoreMiniGradeImageView.context.resources,
                        R.drawable.grade_xh
                    )
                    "X" -> BitmapFactory.decodeResource(
                        scoreMiniGradeImageView.context.resources,
                        R.drawable.grade_x
                    )
                    "SH" -> BitmapFactory.decodeResource(
                        scoreMiniGradeImageView.context.resources,
                        R.drawable.grade_sh
                    )
                    "S" -> BitmapFactory.decodeResource(
                        scoreMiniGradeImageView.context.resources,
                        R.drawable.grade_s
                    )
                    "A" -> BitmapFactory.decodeResource(
                        scoreMiniGradeImageView.context.resources,
                        R.drawable.grade_a
                    )
                    "B" -> BitmapFactory.decodeResource(
                        scoreMiniGradeImageView.context.resources,
                        R.drawable.grade_b
                    )
                    "C" -> BitmapFactory.decodeResource(
                        scoreMiniGradeImageView.context.resources,
                        R.drawable.grade_c
                    )
                    "D" -> BitmapFactory.decodeResource(
                        scoreMiniGradeImageView.context.resources,
                        R.drawable.grade_d
                    )
                    else -> BitmapFactory.decodeResource(
                        scoreMiniGradeImageView.context.resources,
                        R.drawable.ic_baseline_image_24
                    )
                }
            )

            scoreMiniBeatmapTextView.text =
                "${beatmap.artist} - ${beatmap.title} [${beatmap.version}]"

            // Construct a string containing the performance points, accuracy, and mods.
            scoreMiniInformationTextView.text = buildString {
                append("%.2fpp".format(score.performancePoints))

                append(" | ")

                append("%.2f".format(score.accuracy) + "%")

                if (score.mods!!.isNotEmpty()) {
                    append(" | +")

                    score.mods!!.forEach { mod ->
                        append(mod.acronym)
                        append(',')
                    }
                }
            }

            if (scoreMiniInformationTextView.text.last() == ',')
                scoreMiniInformationTextView.text = scoreMiniInformationTextView.text.dropLast(1)

            scoreMiniDifficultyTextView.text = "%.2f".format(beatmap.starRating) + "★"

            val dateDifference = Period.between(score.date!!, LocalDate.now())

            // Get the time since the score was set.
            var dateText =
                "${dateDifference.years} ${if (dateDifference.years > 1) "years" else "year"} ago"

            if (dateDifference.years == 0) {
                if (dateDifference.months == 0) {
                    if (dateDifference.days == 0) {
                        dateText = "less than 24 hours ago"
                    } else {
                        dateText =
                            "${dateDifference.days} ${if (dateDifference.days > 1) "days" else "day"} ago"
                    }
                } else {
                    dateText =
                        "${dateDifference.months} ${if (dateDifference.months > 1) "months" else "month"} ago"
                }
            }

            scoreMiniDateTextView.text = dateText

            view.setOnClickListener {
                callback(score, beatmap)
            }
        }
    }
}