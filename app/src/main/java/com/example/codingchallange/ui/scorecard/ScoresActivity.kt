package com.example.codingchallange.ui.scorecard

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codingchallange.R
import com.example.codingchallange.databinding.ActivityScoresBinding
import com.example.codingchallange.roomdb.entity.HighScoreEntity
import com.example.codingchallange.ui.scorecard.adapter.ScoreCardAdapter
import com.example.codingchallange.ui.main.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScoresActivity : AppCompatActivity() {
    private lateinit var scoresBinding: ActivityScoresBinding

    private var scoreCardAdapter: ScoreCardAdapter? = null
    private var scoreList = mutableListOf<HighScoreEntity>()
    private val viewModel: QuizViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scoresBinding = ActivityScoresBinding.inflate(layoutInflater)
        setContentView(scoresBinding.root)

        initToolbar()

        setUpRecycler()

        viewModel.highScore.observe(this) {
            scoreList.addAll(it)

            scoreCardAdapter?.setList(scoreList)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setUpRecycler() {
        scoreCardAdapter = ScoreCardAdapter(this, scoreList)
        scoresBinding.rvScores.layoutManager = LinearLayoutManager(this)
        scoresBinding.rvScores.adapter = scoreCardAdapter
    }

    private fun initToolbar() {
        val title = getString(R.string.high_score)
        scoresBinding.toolbar.title = title
        setSupportActionBar(scoresBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
}