package com.example.geomania

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

import com.example.geomania.databinding.ActivityMapsBinding

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

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

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        blankIV = findViewById(R.id.BlankIV)
        questionTV = findViewById(R.id.QuestionTV)
        answersTVs.add(findViewById(R.id.optionOneTV))
        answersTVs.add(findViewById(R.id.optionTwoTV))
        answersTVs.add(findViewById(R.id.optionThreeTV))
        answersTVs.add(findViewById(R.id.optionFourTV))
        submitBtn = findViewById(R.id.submitBtn)

        answersTVs.forEach { tv ->
            tv.setOnClickListener{
                onAnswerSelected(tv)
            }
        }

        submitBtn.setOnClickListener{
            submitClicked()
        }

        questions = SQLCommunication.getQuestions()
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
        if (selectedAnswerIndex != -1) {
            if (selectedAnswerIndex != questions[currentQuestionIndex].correctAnswer) {
                answersTVs[selectedAnswerIndex].background =
                    ContextCompat.getDrawable(this, R.drawable.wrong_option_border)
            } else {
                score++
            }
        }

        answersTVs[questions[currentQuestionIndex].correctAnswer].background =
            ContextCompat.getDrawable(this, R.drawable.correct_option_border)

        if (questions[currentQuestionIndex].type == QuestionType.MapShowsAfterQuestion) {
            blankIV.isVisible = false
            questionTV.setTextColor(ContextCompat.getColor(this, R.color.black))

            MapsFunctionality.setMapStyle(mMap, this, R.raw.style_with_labels)
        }

        if (currentQuestionIndex + 1 < questions.count()) {
            submitBtn.text = getString(R.string.next_question)
        } else {
            submitBtn.text = getString(R.string.finish_quiz)
        }
    }

    private fun showQuestion(){
        resetUI()

        //Get the current question
        val currentQuestion = questions[currentQuestionIndex]

        //Hide the map if needed
        if(currentQuestion.type == QuestionType.MapShowsAfterQuestion){
            blankIV.isVisible = true
            questionTV.setTextColor(ContextCompat.getColor(this, R.color.white))
        }

        //Show the current question and the possible answers
        questionTV.text = currentQuestion.body
        for(i in 0..3){
            answersTVs[i].text = currentQuestion.answers[i]
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
    private fun onAnswerSelected(selectedTV: TextView){
        //Reset the previously selected answer
        if(selectedAnswerIndex > -1){
            answersTVs[selectedAnswerIndex].background = ContextCompat.getDrawable(this, R.drawable.default_option_border)
        }

        //Highlight the currently selected answer
        selectedTV.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border)

        //Get the index of the currently selected answer
        selectedAnswerIndex = answersTVs.indexOf(selectedTV)
    }

    /*private fun checkMultipleChoiceQuestion(){

    }*/

    //Spelling question functionality

    //UI functionality
    private fun resetUI(){
        submitBtn.text = getString(R.string.submit)

        answersTVs.forEach {
            it.background = ContextCompat.getDrawable(this, R.drawable.default_option_border)
        }

        MapsFunctionality.setMapStyle(mMap, this, R.raw.style_without_labels)

        MapsFunctionality.removeMarker()
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

        mMap.uiSettings.isScrollGesturesEnabled = false

        MapsFunctionality.setMapStyle(mMap, this, R.raw.style_without_labels)

        showQuestion()
    }
}