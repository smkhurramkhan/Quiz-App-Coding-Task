package com.example.codingchallange.roomdb

import androidx.room.TypeConverter
import com.example.codingchallange.roomdb.entity.Answers
import com.example.codingchallange.roomdb.entity.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {


    @TypeConverter
    fun fromString(value: String?): List<Question> {
        val listType = object : TypeToken<List<Question>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(questions: List<Question>?): String {
        val gson = Gson()
        return gson.toJson(questions)
    }

    @TypeConverter
    fun setAnswers(value: Answers?) = Gson().toJson(value)

    @TypeConverter
    fun getAnswers(string: String?): Answers? {
        return Gson().fromJson(string, Answers::class.java)
    }


}