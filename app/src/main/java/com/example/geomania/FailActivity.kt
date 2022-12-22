package com.example.geomania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class FailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fail)

        //Show the username
        findViewById<TextView>(R.id.usernameFailTV).text = User.username

        findViewById<Button>(R.id.returnToQBBtn).setOnClickListener {
            returnToQuestionBrowser()
        }
    }

    private fun returnToQuestionBrowser(){
        val intent = Intent(this, QuestionBrowserActivity::class.java)
        startActivity(intent)
        finish()
    }
}