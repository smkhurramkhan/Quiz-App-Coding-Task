package com.example.codingchallange.api

import com.example.codingchallange.roomdb.entity.QuizEntity
import retrofit2.Response
import retrofit2.http.GET


interface ApiService {

    @GET(Constants.GET_QUIZ)
    suspend fun getQuizQuestions(): Response<QuizEntity>


}