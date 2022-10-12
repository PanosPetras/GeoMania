package com.example.geomania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        findViewById<TextView>(R.id.congratsTV).text = User.username
        findViewById<TextView>(R.id.scoreTV).text = "Το σκορ σου ήταν ${intent.getStringExtra("score")}"

        findViewById<Button>(R.id.returnBtn).setOnClickListener {
            returnToMainMenu()
        }
    }

    private fun returnToMainMenu(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}