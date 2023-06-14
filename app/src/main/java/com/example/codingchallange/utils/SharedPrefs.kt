package com.example.codingchallange.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context?) {
    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    init {
        if (context != null) {
            prefs = context.getSharedPreferences("quizAppPrefs", Context.MODE_PRIVATE)
            editor = prefs.edit()
            editor.apply()
        }
    }


    fun getUserName(): String? {
        return prefs.getString("username", "Khurram")
    }

    fun setUserName(username: String) {
        editor.putString("username", username)
        editor.commit()
    }


}