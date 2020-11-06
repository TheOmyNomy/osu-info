package com.example.osuinfo

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.osuinfo.api.osu.Beatmap
import com.example.osuinfo.api.osu.Score
import com.example.osuinfo.api.osu.User
import com.squareup.picasso.Picasso
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ScoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        val score = intent.getParcelableExtra<Score>("score")!!
        val beatmap = intent.getParcelableExtra<Beatmap>("beatmap")!!
        val user = intent.getParcelableExtra<User>("user")!!

        val scoreAvatarImageView = findViewById<ImageView>(R.id.scoreAvatarImageView)

        Picasso.get().load("https://a.ppy.sh/" + user.userId)
            .placeholder(R.drawable.ic_baseline_image_24).into(scoreAvatarImageView)

        findViewById<TextView>(R.id.scoreUsernameTextView).text = user.username

        val scoreGradeImageView = findViewById<ImageView>(R.id.scoreGradeImageView)

        // Get the associated bitmap resource from the score's grade and set the image view to it.
        scoreGradeImageView.setImageBitmap(
            when (score.grade) {
                "XH" -> BitmapFactory.decodeResource(
                    scoreGradeImageView.context.resources,
                    R.drawable.grade_xh
                )
                "X" -> BitmapFactory.decodeResource(
                    scoreGradeImageView.context.resources,
                    R.drawable.grade_x
                )
                "SH" -> BitmapFactory.decodeResource(
                    scoreGradeImageView.context.resources,
                    R.drawable.grade_sh
                )
                "S" -> BitmapFactory.decodeResource(
                    scoreGradeImageView.context.resources,
                    R.drawable.grade_s
                )
                "A" -> BitmapFactory.decodeResource(
                    scoreGradeImageView.context.resources,
                    R.drawable.grade_a
                )
                "B" -> BitmapFactory.decodeResource(
                    scoreGradeImageView.context.resources,
                    R.drawable.grade_b
                )
                "C" -> BitmapFactory.decodeResource(
                    scoreGradeImageView.context.resources,
                    R.drawable.grade_c
                )
                "D" -> BitmapFactory.decodeResource(
                    scoreGradeImageView.context.resources,
                    R.drawable.grade_d
                )
                else -> BitmapFactory.decodeResource(
                    scoreGradeImageView.context.resources,
                    R.drawable.ic_baseline_image_24
                )
            }
        )

        findViewById<TextView>(R.id.scoreScoreTextView).text = score.score.toString()

        // Construct the mods string from the score's mods.
        var mods = buildString {
            if (score.mods!!.isNotEmpty()) {
                append("+")

                score.mods!!.forEach { mod ->
                    append(mod.acronym)
                    append(',')
                }
            }
        }

        if (mods.isNotEmpty() && mods.last() == ',') mods = mods.dropLast(1)

        findViewById<TextView>(R.id.scoreModsTextView).text = mods

        findViewById<TextView>(R.id.scoreAccuracyTextView).text =
            "%.2f".format(score.accuracy) + "%"

        findViewById<TextView>(R.id.scoreComboTextView).text = "${score.combo}x / ${beatmap.combo}x"

        findViewById<TextView>(R.id.scorePerformanceTextView).text =
            "%.2fpp".format(score.performancePoints)

        findViewById<TextView>(R.id.score300TextView).text = score.count300.toString()
        findViewById<TextView>(R.id.score100TextView).text = score.count100.toString()
        findViewById<TextView>(R.id.score50TextView).text = score.count50.toString()
        findViewById<TextView>(R.id.scoreMissesTextView).text = score.countMiss.toString()

        findViewById<TextView>(R.id.scoreDateTextView).text = "Played on " + score.date!!.format(
            DateTimeFormatter.ofLocalizedDate(
                FormatStyle.LONG
            )
        )

        val scoreCoverImageView = findViewById<ImageView>(R.id.scoreCoverImageView)

        Picasso.get().load("https://b.ppy.sh/thumb/${beatmap.beatmapSetId}l.jpg")
            .placeholder(R.drawable.ic_baseline_image_24).into(scoreCoverImageView)

        findViewById<TextView>(R.id.scoreBeatmapTextView).text =
            "${beatmap.artist} - ${beatmap.title} [${beatmap.version}]"

        findViewById<TextView>(R.id.scoreCreatorTextView).text = "Mapped by " + beatmap.creator

        findViewById<TextView>(R.id.scoreDifficultyTextView).text =
            "%.2f".format(beatmap.starRating) + "★"
    }
}