package com.example.codingchallange.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.codingchallange.roomdb.entity.HighScoreEntity

@Dao
interface HighScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(highScoreEntity: HighScoreEntity)

    @Query("SELECT * FROM highscoreentity ORDER BY time DESC")
    fun getScores(): LiveData<List<HighScoreEntity>>

}