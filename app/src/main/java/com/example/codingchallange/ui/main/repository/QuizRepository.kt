package com.example.codingchallange.ui.main.repository

import com.example.codingchallange.roomdb.dao.QuizDao
import com.example.codingchallange.ui.main.remote.QuizRemoteDataSource
import com.example.codingchallange.utils.performGetOperation
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val remoteDataSource: QuizRemoteDataSource,
    private val quizDao: QuizDao
) {


    fun getQuiz() = performGetOperation(
        databaseQuery = { quizDao.getQuizQuestions() },
        networkCall = { remoteDataSource.getQuizQuestions() },
        saveCallResult = {
            quizDao.insert(it)
        }
    )

}