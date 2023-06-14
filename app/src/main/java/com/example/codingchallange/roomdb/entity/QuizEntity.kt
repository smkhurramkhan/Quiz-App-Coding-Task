package com.example.codingchallange.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuizEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val questions: MutableList<Question>
)