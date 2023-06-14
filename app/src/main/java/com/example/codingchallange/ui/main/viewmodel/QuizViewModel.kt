package com.example.codingchallange.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.example.codingchallange.roomdb.dao.HighScoreDao
import com.example.codingchallange.roomdb.dao.QuizDao
import com.example.codingchallange.roomdb.entity.HighScoreEntity
import com.example.codingchallange.ui.main.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    repository: QuizRepository,
    quizDao: QuizDao,
    private val highScoreDao: HighScoreDao
) : ViewModel() {

    val quiz = repository.getQuiz()

    val localQuiz = quizDao.getQuizQuestions()

    val highScore = highScoreDao.getScores()


    suspend fun insertScore(highScoreEntity: HighScoreEntity) {
        highScoreDao.insertScore(
            highScoreEntity
        )
    }
}