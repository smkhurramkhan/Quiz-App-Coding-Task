package com.example.codingchallange.ui.start

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.codingchallange.R
import com.example.codingchallange.databinding.ActivityStartBinding
import com.example.codingchallange.ui.home.HomeActivity
import com.example.codingchallange.utils.SharedPrefs

class StartActivity : AppCompatActivity() {
    private lateinit var startBinding: ActivityStartBinding
    private var sharedPrefs: SharedPrefs? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startBinding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(startBinding.root)

        sharedPrefs = SharedPrefs(this)

        startBinding.btnStart.setOnClickListener {
            if (startBinding.etName.text?.isEmpty() == true) {
                Toast.makeText(
                    this, getString(R.string.please_enter_your_name),
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                sharedPrefs?.setUserName(startBinding.etName.text.toString())
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}