package com.example.geomania

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class QuestionBrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_browser)

        Toast.makeText(this, assets.list("Spelling")?.joinToString(","), Toast.LENGTH_LONG).show()
    }
}