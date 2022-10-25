package com.example.geomania

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.io.BufferedReader
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

        findViewById<Button>(R.id.AQBBackBtn).setOnClickListener {
            goBack()
        }
    }

    private fun createContentButtons(){
        val parentLayout = findViewById<ConstraintLayout>(R.id.questionBrowserConstraintLayout)
        var newLayout: LinearLayout? = null
        var btn: Button

        contents.forEach{
            if(contents.indexOf(it) % 3 == 0){
                newLayout = parentLayout.findViewWithTag("QBLL" + (1 + contents.indexOf(it) / 3))
            }

            btn = createMenuButton()
            btn.text = it.split("/")[0]
            btn.tag = if(dir == null) it else "$dir/$it"

            newLayout!!.addView(btn)
        }
    }

    private fun getDirectoryContents(){
        var reader: BufferedReader? = null

        val fName = if(dir == null) "Content/content.csv" else "Content/$dir/content.csv"

        try {
            reader = BufferedReader(
                InputStreamReader(assets.open(fName))
            )

            val mLine: String = reader.readText()

            mLine.let {
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

    private fun categoryButtonClicked(directory: String){
        if(!directory.contains(".")){
            openNewFolder(directory)
        } else {
            openGame(directory)
        }
    }

    private fun openNewFolder(directory: String){
        openActivity(QuestionBrowserActivity::class.java, directory)
    }

    private fun openGame(directory: String){
        openActivity(MapsActivity::class.java, "Content/$directory")
    }

    private fun goBack(){
        if(dir != null) {
            val rb = dir!!.split("/").toMutableList()
            rb.remove(rb.last())

            val newDir =    if (rb.size != 0) rb.joinToString("/")
                            else null

            openActivity(QuestionBrowserActivity::class.java, newDir)
        } else {
            openActivity(MainActivity::class.java, "")
        }
    }

    private fun openActivity(activity: Class<*>, directory: String?){
        val intent = Intent(this, activity)
        intent.putExtra("directory", directory)

        startActivity(intent)

        finish()
    }

    //UI Functionality
    private fun createMenuButton(): Button{
        val dm = resources.displayMetrics
        val dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90f, dm).toInt()

        val btn = Button(this)
        btn.gravity = 1
        btn.width = dimension
        btn.height = dimension

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, dm).toInt(), 0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, dm).toInt(), 0)
        btn.layoutParams = params

        btn.setOnClickListener {
            categoryButtonClicked((it as Button).tag.toString())
        }

        btn.setBackgroundColor(this.resources.getColor(R.color.purple_500, this.resources.newTheme()))

        return btn
    }
}