package com.example.codingchallange.roomdb.entity

data class Question(
    val answers: Answers,
    val correctAnswer: String,
    val question: String,
    val questionImageUrl: String,
    val score: Int,
    val type: String
)