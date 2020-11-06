package com.example.osuinfo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.osuinfo.api.osu.Beatmap
import com.example.osuinfo.api.osu.Score
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(viewModelStore, HomeViewModelFactory(requireContext())).get(
                HomeViewModel::class.java
            )

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val homeSwipeToRefresh = root.findViewById<SwipeRefreshLayout>(R.id.homeSwipeToRefresh)

        // Setup the swipe to refresh listener that navigates to the current
        // fragment which will recreate this fragment and refresh the content.
        homeSwipeToRefresh?.setOnRefreshListener {
            parentFragment?.findNavController()?.navigate(R.id.homeFragment)
        }

        val homeProgressBar = root.findViewById<ProgressBar>(R.id.homeProgressBar)

        val homeAvatarImageView = root.findViewById<ImageView>(R.id.homeAvatarImageView)
        val homeUsernameTextView = root.findViewById<TextView>(R.id.homeUsernameTextView)
        val homePerformanceTextView = root.findViewById<TextView>(R.id.homePerformanceTextView)
        val homeRankTextView = root.findViewById<TextView>(R.id.homeRankTextView)
        val homeAccuracyTextView = root.findViewById<TextView>(R.id.homeAccuracyTextView)
        val homeLevelTextView = root.findViewById<TextView>(R.id.homeLevelTextView)

        var hasFailed = false

        // The user live data is updated once the user object has been fetched from
        // the API. Once it has, we update the views to the new information, unhide them,
        // and hide the progress bar.
        homeViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                Picasso.get().load("https://a.ppy.sh/" + user.userId)
                    .placeholder(R.drawable.ic_baseline_image_24).into(homeAvatarImageView)

                homeUsernameTextView.text = user.username
                homePerformanceTextView.text = "Performance: %.2fpp".format(user.performancePoints)

                homeRankTextView.text =
                    "Rank: #%d (%s%d)".format(user.globalRank, user.countryCode, user.countryRank)

                homeAccuracyTextView.text = "Accuracy: %.2f".format(user.accuracy) + "%"
                homeLevelTextView.text = "Level: %.0f".format(user.level)

                homeAvatarImageView.visibility = View.VISIBLE
                homeUsernameTextView.visibility = View.VISIBLE
                homePerformanceTextView.visibility = View.VISIBLE
                homeRankTextView.visibility = View.VISIBLE
                homeAccuracyTextView.visibility = View.VISIBLE
                homeLevelTextView.visibility = View.VISIBLE

                homeProgressBar.visibility = View.INVISIBLE
            } else {
                displayErrorSnackBar()
                hasFailed = true
            }
        })

        val homeScoresProgressBar = root.findViewById<ProgressBar>(R.id.homeScoresProgressBar)

        // Don't bother trying to handle the rest of this method if the user API request failed.
        if (hasFailed) {
            homeProgressBar.visibility = View.INVISIBLE
            homeScoresProgressBar.visibility = View.INVISIBLE

            return root
        }

        val homeHorizontalBarView = root.findViewById<View>(R.id.homeHorizontalBarView)
        val homeBestPerformanceTextView =
            root.findViewById<TextView>(R.id.homeBestPerformanceTextView)

        val homeScoresRecyclerView = root.findViewById<RecyclerView>(R.id.homeScoresRecyclerView)
        homeScoresRecyclerView.layoutManager = LinearLayoutManager(context)

        homeScoresRecyclerView.adapter =
            ScoreAdapter(homeViewModel.scores, homeViewModel.beatmaps) { score, beatmap ->
                showScore(score, beatmap)
            }

        // Displays a line divider between items in the score recycler view.
        homeScoresRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        // The beatmaps live data is updated once the beatmap list has been fetched from
        // the API. Once it has, we update the views to the new information, unhide them,
        // and hide the progress bar.
        homeViewModel.beatmaps.observe(viewLifecycleOwner, Observer { beatmaps ->
            if (beatmaps != null) {
                (homeScoresRecyclerView.adapter as ScoreAdapter).notifyDataSetChanged()

                homeHorizontalBarView.visibility = View.VISIBLE
                homeBestPerformanceTextView.visibility = View.VISIBLE
                homeScoresRecyclerView.visibility = View.VISIBLE

                homeScoresProgressBar.visibility = View.INVISIBLE
            } else {
                displayErrorSnackBar()
                hasFailed = true
            }
        })

        // Don't bother trying to handle the rest of this method if the user API request failed.
        if (hasFailed) {
            homeProgressBar.visibility = View.INVISIBLE
            homeScoresProgressBar.visibility = View.INVISIBLE
        }

        return root
    }

    // Called when an item in the score recycler view is clicked.
    // Creates an intent with the relevant data and starts it.
    private fun showScore(score: Score, beatmap: Beatmap) {
        val intent = Intent(requireContext(), ScoreActivity::class.java)

        intent.putExtra("score", score)
        intent.putExtra("beatmap", beatmap)
        intent.putExtra("user", homeViewModel.user.value)

        startActivity(intent)
    }

    private fun displayErrorSnackBar() {
        // Failed to fetch information from the api.
        // This cannot be due to an invalid API key and is likely
        // due to a connection issue. Display a snack bar with a retry button for the
        // user to try again.

        // We use an indefinite snack bar because there's nothing else the user can do.
        Snackbar.make(
            requireView(),
            getString(R.string.api_fetch_failed),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.retry)) {
                parentFragment?.findNavController()?.navigate(R.id.homeFragment)
            }.show()
    }
}