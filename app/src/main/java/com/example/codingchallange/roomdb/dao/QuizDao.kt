package com.example.codingchallange.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.codingchallange.roomdb.entity.QuizEntity

@Dao
interface QuizDao {

    @Query("SELECT * FROM quizentity")
    fun getQuizQuestions(): LiveData<List<QuizEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<QuizEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: QuizEntity)

}