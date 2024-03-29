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
        findViewById<TextView>(R.id.scoreTV).text = getString(R.string.result_score, intent.getStringExtra("score"))

        findViewById<Button>(R.id.returnBtn).setOnClickListener {
            returnToQuestionBrowser()
        }
    }

    private fun returnToQuestionBrowser(){
        val intent = Intent(this, QuestionBrowserActivity::class.java)
        startActivity(intent)
        finish()
    }
}