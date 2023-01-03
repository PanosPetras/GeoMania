package com.example.geomania

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.geomania.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.min
import kotlin.math.max

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    private var score = 0
    private var mistakes = 0
    private var checkedQuestion = false

    private var currentQuestionIndex = 0
    private var selectedAnswerIndex = -1
    private lateinit var questions: List<Question>

    private lateinit var questionTV: TextView
    private val answersTVs = mutableListOf<TextView>()
    private lateinit var submitBtn: Button
    private var milestone: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupUI()

        //Get the directory of the questions for this quiz
        val dir = intent.getStringExtra("directory")!!

        //Fetch the questions
        questions = AssetsReader.getQuestions(dir)
        milestone = AssetsReader.getMilestone(dir)

        User.onCoinsChanged = {
            showCoinBalanceChange(it)
        }
    }

    //Quiz functionality
    private fun submitClicked(){
        if(!checkedQuestion) {
            checkQuestion()
            checkedQuestion = true
        } else {
            if(currentQuestionIndex == questions.size - 1){
                //The player finished the quiz
                User.rewardPlayer(score, questions.size, milestone)
                showEndScreen()

                return
            } else {
                //Show the next question
                nextQuestion()
                showQuestion()

                checkedQuestion = false
            }
        }
    }

    private fun checkQuestion() {
        when(questions[currentQuestionIndex]){
            is MChoiceQuestion -> checkMultipleChoiceQuestion()
            is SpellingQuestion -> checkSpellingQuestion()
            else -> TODO()
        }

        //If the player has made 3 mistakes in a row, end the quiz
        if(mistakes >= 3){
            showFailScreen()
        }

        //Update the confirm button's text
        if (currentQuestionIndex + 1 < questions.count()) {
            submitBtn.text = getString(R.string.next_question)
        } else {
            submitBtn.text = getString(R.string.finish_quiz)
        }
    }

    private fun showQuestion(){
        //Get the current question
        val currentQuestion = questions[currentQuestionIndex]

        //Reset the UI of the activity
        resetUI()

        //Show the current question body
        questionTV.text = currentQuestion.body

        when(currentQuestion){
            is MChoiceQuestion -> {
                showMChoiceOptions(currentQuestion)
                findViewById<ConstraintLayout>(R.id.mChoiceLayout).visibility = View.VISIBLE
            }
            is SpellingQuestion -> {
                findViewById<ConstraintLayout>(R.id.spellingLayout).visibility = View.VISIBLE
            }
            else -> TODO()
        }

        //Handle the map
        MapsFunctionality.zoomToLocation(mMap, currentQuestion.location, currentQuestion.zoom)
        currentQuestion.marker?.let {
            MapsFunctionality.addMarker(mMap, currentQuestion.marker)
        }
    }

    private fun nextQuestion(){
        selectedAnswerIndex = -1
        currentQuestionIndex++
    }

    //Multiple choice question functionality
    private fun showMChoiceOptions(currentQuestion: MChoiceQuestion) {
        for (i in 0..3) {
            answersTVs[i].text = currentQuestion.answers[i]
        }
    }

    private fun onAnswerSelected(selectedTV: TextView){
        //Reset the previously selected answer
        if(selectedAnswerIndex > -1){
            resetMChoiceTextView(answersTVs[selectedAnswerIndex])
        }

        //Highlight the currently selected answer
        highlightMChoiceTextView(selectedTV)

        //Get the index of the currently selected answer
        selectedAnswerIndex = answersTVs.indexOf(selectedTV)
    }

    private fun highlightMChoiceTextView(view: TextView){
        changeTextViewBackground(view, R.drawable.borders_selected_option_border)
    }

    private fun resetMChoiceTextView(view: TextView){
        changeTextViewBackground(view, R.drawable.borders_default_option_border)
    }

    private fun changeTextViewBackground(view: TextView, background: Int){
        view.background = ContextCompat.getDrawable(this, background)
    }

    private fun checkMultipleChoiceQuestion(){
        val currentQuestion = questions[currentQuestionIndex] as MChoiceQuestion

        if (selectedAnswerIndex != currentQuestion.correctAnswer) {
            //If the player has selected an answer, highlight it as wrong
            if(selectedAnswerIndex != -1) {
                answersTVs[selectedAnswerIndex].background =
                    ContextCompat.getDrawable(this, R.drawable.borders_wrong_option_border)
            }

            mistakes++
            User.wrongAnswer()
        } else {
            score++
            mistakes = 0
            User.correctAnswer()
        }

        //Highlight the correct answer
        answersTVs[currentQuestion.correctAnswer].background =
            ContextCompat.getDrawable(this, R.drawable.borders_correct_option_border)
    }

    //Spelling question functionality
    private fun checkSpellingQuestion(){
        val currentQuestion = questions[currentQuestionIndex] as SpellingQuestion
        val answerET = findViewById<EditText>(R.id.answerET)

        answerET.isActivated = false
        answerET.isEnabled = false

        //Check if the given answer and the correct answer have more than 1 different character
        if(compareResults(currentQuestion.correctAnswer, answerET.text.toString()) > 1){
            //Highlight the given answer as wrong
            answerET.setBackgroundColor(resources.getColor(R.color.Red, resources.newTheme()))
            //Show the correct answer
            showRightAnswer(currentQuestion.correctAnswer)

            mistakes++
            User.wrongAnswer()
        } else {
            //Highlight the given answer as correct
            answerET.setBackgroundColor(resources.getColor(R.color.Green, resources.newTheme()))

            score++
            mistakes = 0
            User.correctAnswer()
        }
    }

    private fun compareResults(correctAnswer: String, answer: String) : Int {
        //Convert both string to lowercase character arrays
        val caArr = correctAnswer.lowercase().toCharArray()
        val aArr = answer.lowercase().replace(" ", "").toCharArray()

        val iterations = min(caArr.size, aArr.size)

        var occurrences = 0

        //Check how similar the two strings are
        for(i in 0 until iterations){
            if(caArr[i] == aArr[i]){
                occurrences++
            }
        }

        //Return the number of characters that are different between the two string
        return max(caArr.size, aArr.size) - occurrences
    }

    private fun showRightAnswer(answer: String){
        val correctAnswerTV = findViewById<TextView>(R.id.correctAnswerTV)

        findViewById<TextView>(R.id.cattv).visibility = View.VISIBLE
        correctAnswerTV.visibility = View.VISIBLE

        correctAnswerTV.text = answer
    }

    //UI functionality
    private fun showCoinBalanceChange(dC: Int) {
        findViewById<TextView>(R.id.dcTV).text = dC.toString()
        val ll = findViewById<LinearLayout>(R.id.dCLL)
        ll.visibility = View.VISIBLE

        Executors.newSingleThreadScheduledExecutor().schedule({
            for(i in 10 downTo 1){
                ll.alpha = i / 10f;
                Thread.sleep(75);
            }

            ll.visibility = View.INVISIBLE
            ll.alpha = 1f;
        }, 1, TimeUnit.SECONDS)
    }

    private fun setupUI() {
        questionTV = findViewById(R.id.QuestionTV)
        answersTVs.add(findViewById(R.id.optionOneTV))
        answersTVs.add(findViewById(R.id.optionTwoTV))
        answersTVs.add(findViewById(R.id.optionThreeTV))
        answersTVs.add(findViewById(R.id.optionFourTV))
        submitBtn = findViewById(R.id.submitBtn)

        answersTVs.forEach { tv ->
            tv.setOnClickListener {
                onAnswerSelected(tv)
            }
        }

        submitBtn.setOnClickListener {
            submitClicked()
        }
    }

    private fun resetUI(){
        submitBtn.text = getString(R.string.submit)

        resetMChoiceUI()
        resetSpellingUI()

        MapsFunctionality.setMapStyle(mMap, this, R.raw.style_without_labels)

        MapsFunctionality.removeMarker()
    }

    private fun resetMChoiceUI(){
        answersTVs.forEach {
            it.background = ContextCompat.getDrawable(this, R.drawable.borders_default_option_border)
        }
    }

    private fun resetSpellingUI(){
        //Just UI code, oof boring stuff
        val answerET = findViewById<EditText>(R.id.answerET)
        answerET.text.clear()
        answerET.isActivated = true
        answerET.isEnabled = true
        answerET.setBackgroundColor(Color.TRANSPARENT)
        findViewById<TextView>(R.id.correctAnswerTV).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.cattv).visibility = View.INVISIBLE
    }

    private fun showEndScreen(){
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("score", "$score/${questions.size}")

        startActivity(intent)

        finish()
    }

    private fun showFailScreen(){
        val intent = Intent(this, FailActivity::class.java)

        startActivity(intent)

        finish()
    }

    //Called when the map is ready
    //Most of the map functionality exists in MapFunctionality
    override fun onMapReady(googleMap: GoogleMap) {
        //Cache the map reference
        mMap = googleMap

        //Do not allow the player to manipulate the map
        mMap.uiSettings.isScrollGesturesEnabled = false
        mMap.uiSettings.isZoomGesturesEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = false

        MapsFunctionality.setMapStyle(mMap, this, R.raw.style_without_labels)

        showQuestion()
    }
}