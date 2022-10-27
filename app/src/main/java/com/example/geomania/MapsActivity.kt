package com.example.geomania

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.geomania.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlin.math.min
import kotlin.math.max

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    private var score = 0
    private var checkedQuestion = false

    private var currentQuestionIndex = 0
    private var selectedAnswerIndex = -1
    private lateinit var questions: List<Question>

    private lateinit var questionTV: TextView
    private val answersTVs = mutableListOf<TextView>()
    private lateinit var submitBtn: Button
    private lateinit var blankIV: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupUI()

        questions = SQLCommunication.getQuestions(intent.getStringExtra("directory")!!)
    }

    //Quiz functionality
    private fun submitClicked(){
        if(!checkedQuestion) {
            checkQuestion()
            checkedQuestion = true
        } else {
            if(currentQuestionIndex == questions.size - 1){
                //The player finished the quiz
                User.rewardPlayer(score, questions.size)
                showEndScreen()

                return
            } else {
                //Show the next question
                nextQuestion()
                showQuestion()
            }

            checkedQuestion = false
        }
    }

    private fun checkQuestion() {
        when(questions[currentQuestionIndex]){
            is MChoiceQuestion -> checkMultipleChoiceQuestion()
            is SpellingQuestion -> checkSpellingQuestion()
            else -> TODO()
        }

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
        changeTextViewBackground(view, R.drawable.selected_option_border)
    }

    private fun resetMChoiceTextView(view: TextView){
        changeTextViewBackground(view, R.drawable.default_option_border)
    }

    private fun changeTextViewBackground(view: TextView, background: Int){
        view.background = ContextCompat.getDrawable(this, background)
    }

    private fun checkMultipleChoiceQuestion(){
        val currentQuestion = questions[currentQuestionIndex] as MChoiceQuestion
        if (selectedAnswerIndex != -1) {
            if (selectedAnswerIndex != currentQuestion.correctAnswer) {
                answersTVs[selectedAnswerIndex].background =
                    ContextCompat.getDrawable(this, R.drawable.wrong_option_border)
            } else {
                score++
            }
        }

        answersTVs[currentQuestion.correctAnswer].background =
            ContextCompat.getDrawable(this, R.drawable.correct_option_border)
    }

    //Spelling question functionality
    private fun checkSpellingQuestion(){
        val currentQuestion = questions[currentQuestionIndex] as SpellingQuestion
        val answerET = findViewById<EditText>(R.id.answerET)
        answerET.isActivated = false
        answerET.isEnabled = false

        if(compareResults(currentQuestion.correctAnswer, answerET.text.toString()) > 1){
            answerET.setBackgroundColor(resources.getColor(R.color.Red, resources.newTheme()))
            showRightAnswer(currentQuestion.correctAnswer)
        } else {
            answerET.setBackgroundColor(resources.getColor(R.color.Green, resources.newTheme()))
            score++
        }
    }

    private fun compareResults(correctAnswer: String, answer: String) : Int {
        val caArr = correctAnswer.lowercase().toCharArray()
        val aArr = answer.lowercase().replace(" ", "").toCharArray()

        val iterations = min(caArr.size, aArr.size)

        var occurrences = 0

        for(i in 0 until iterations){
            if(caArr[i] == aArr[i]){
                occurrences++
            }
        }

        return max(caArr.size, aArr.size) - occurrences
    }

    private fun showRightAnswer(answer: String){
        val correctAnswerTV = findViewById<TextView>(R.id.correctAnswerTV)

        findViewById<TextView>(R.id.cattv).visibility = View.VISIBLE
        correctAnswerTV.visibility = View.VISIBLE

        correctAnswerTV.text = answer
    }

    //UI functionality
    private fun setupUI() {
        blankIV = findViewById(R.id.BlankIV)
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
            it.background = ContextCompat.getDrawable(this, R.drawable.default_option_border)
        }
    }

    private fun resetSpellingUI(){
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