package com.example.codingchallange.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.codingchallange.ui.main.MainActivity
import com.example.codingchallange.databinding.ActivityHomeBinding
import com.example.codingchallange.ui.scorecard.ScoresActivity
import com.example.codingchallange.ui.main.viewmodel.QuizViewModel
import com.example.codingchallange.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var homeBinding: ActivityHomeBinding
    private val viewModel: QuizViewModel by viewModels()
    private var isWeHaveData = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        onClickListeners()

        viewModel.localQuiz.observe(this)
        {
            if (it.isNotEmpty()) {
                isWeHaveData = true
            }
        }

        fetchDataFromServer()

    }

    private fun fetchDataFromServer() {
        viewModel.quiz.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    isWeHaveData = true
                    Timber.d("sucess")
                }

                Status.LOADING -> {
                    Timber.d("loading")
                }

                Status.ERROR -> {
                    Timber.d("error")
                }
            }
        }
    }

    private fun onClickListeners() {
        homeBinding.btnPlay.setOnClickListener {
            if (isWeHaveData) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, "Fetching data", Toast.LENGTH_SHORT).show()
                fetchDataFromServer()
            }
        }

        homeBinding.btnhighScore.setOnClickListener {
            startActivity(Intent(this, ScoresActivity::class.java))
        }
    }
}