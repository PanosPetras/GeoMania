package com.example.geomania

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

class QuestionBrowserActivity : AppCompatActivity() {
    private var dir : String? = null
    private var contents: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_browser)

        dir = intent.getStringExtra("directory")
        getDirectoryContents()
        createContentButtons()
    }

    private fun createContentButtons(){
        val parentLayout = findViewById<ConstraintLayout>(R.id.questionBrowserConstraintLayout)
        var newLayout: ConstraintLayout? = null
        var btn: Button

        contents.forEach{
            if(contents.indexOf(it) % 3 == 0){
                newLayout = ConstraintLayout(this)
                parentLayout.addView(newLayout)
            }

            btn = Button(this)
            btn.text = it
            btn.gravity = 1

            newLayout!!.addView(btn)
        }
    }

    private fun getDirectoryContents(){
        var reader: BufferedReader? = null

        val fName = if(dir == null) "content.csv" else "$dir//content.csv"

        try {
            reader = BufferedReader(
                InputStreamReader(assets.open(fName))
            )

            val mLine: String? = reader.readLine()

            mLine?.let {
                val contents = mLine.split(",")

                contents.forEach{
                    this.contents.add(it)
                }
            }
        } catch (e: IOException) {
            //Handle it
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    //log the exception
                }
            }
        }
    }
}