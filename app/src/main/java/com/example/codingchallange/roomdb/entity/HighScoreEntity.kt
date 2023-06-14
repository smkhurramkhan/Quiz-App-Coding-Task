package com.example.codingchallange.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HighScoreEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var username: String,
    var score: Int,
    var time: Long
)