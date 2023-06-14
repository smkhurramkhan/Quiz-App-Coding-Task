package com.example.codingchallange.ui.main.remote

import com.example.codingchallange.api.ApiService
import javax.inject.Inject

class QuizRemoteDataSource @Inject constructor(
    private val apiService: ApiService
): BaseDataSource() {

    suspend fun getQuizQuestions() = getResult { apiService.getQuizQuestions() }
}