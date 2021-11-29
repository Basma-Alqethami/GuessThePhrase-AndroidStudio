package com.example.guessthephrase

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var myRV: RecyclerView
    private lateinit var guessButton: Button
    private lateinit var textFieldMessage: EditText
    private lateinit var textViewGuess: TextView
    private lateinit var textViewLetters: TextView
    private lateinit var clMain: ConstraintLayout

    private lateinit var textViewScore: TextView

    private val answers = ArrayList<String>()
    private val answer = "i am a trainee at saudi digital academy"
    private var hidTexe = ""
    private var count = 0
    private var guessedLetters = ArrayList<Char>()
    private var SwitchGuess = true

    private lateinit var sharedPreferences: SharedPreferences
    private var score = 0
    private var highScore = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        highScore = sharedPreferences.getInt("HighScore", 0)

        textViewScore = findViewById(R.id.highScore)
        textViewScore.text = "Your score is $highScore"


        myRV = findViewById(R.id.rvMain)
        guessButton = findViewById(R.id.submitButton)
        textFieldMessage = findViewById(R.id.messageText)
        textViewGuess = findViewById(R.id.textGuess)
        textViewLetters = findViewById(R.id.guessedLetters)

        clMain = findViewById(R.id.clMain)
        myRV.adapter = RecyclerViewAdapter(answers)
        myRV.layoutManager = LinearLayoutManager(this)

        for (i in answer.indices) {
            if (answer[i] == ' ') {
                hidTexe += ' '
            } else {
                hidTexe += '*'
            }
        }
        updateText()
        guessButton.setOnClickListener { addGuess() }
    }

    private fun updateScore(){
        score = 10 - count
        if(score >= highScore){
            highScore = score
            with(sharedPreferences.edit()) {
                putInt("HighScore", highScore)
                apply()
            }
            Snackbar.make(clMain, "NEW HIGH SCORE!", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun addGuess() {
        var userGuess = textFieldMessage.text.toString()
        userGuess = userGuess.lowercase()
        if (count != 10 ) {
            if (SwitchGuess) {
                if (userGuess == answer) {
                    disableEntry()
                    updateScore()
                    showAlertDialog("You win!\n\nPlay again?")
                    answers.add("-----------------------------------------")
                    answers.add("You win!\nThe correct answer was: [ $answer ]\n\nGame Over")

                } else {
                    SwitchGuess = false
                    updateText()
                    answers.add("Wrong guess: $userGuess")
                }
            } else {
                if (userGuess.isNotEmpty() && userGuess.length == 1) {
                    SwitchGuess = true
                    checkLetters(userGuess[0])
                    count++
                    answers.add("${10 - count} guesses remaining")
                } else {
                    Snackbar.make(clMain, "Please enter one letter only", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        } else {
            disableEntry()
            showAlertDialog("You lose!\n\nPlay again?")
            answers.add("-----------------------------------------")
            answers.add("You lose\nThe correct answer was: [ $answer ] \n\nGame Over")
        }
        myRV.scrollToPosition(answers.size - 1)
        textFieldMessage.text.clear()
        myRV.adapter?.notifyDataSetChanged()
    }

    private fun updateText(){
        textViewGuess.text = hidTexe.toUpperCase()
        textViewLetters.text = "Guessed Letters:  " + guessedLetters.joinToString()
        if(SwitchGuess){
            textFieldMessage.hint = "Guess the full phrase"
        }else{
            textFieldMessage.hint = "Guess a letter"
        }
    }

    private fun checkLetters(guessedLetter: Char){
        var allStars = hidTexe.toCharArray()
        var listOfIndex = ArrayList<Int>()
        var temText = ""
        // find all index
        for (i in answer.indices) {
            if (answer[i] == guessedLetter) {
                listOfIndex.add(i)
            }
        }
        if (listOfIndex.isNotEmpty()) {
            for (item in listOfIndex) {
                allStars[item]= guessedLetter
            }

            for (i in allStars) {
                if (i == ' ') {
                    temText += ' '
                } else if (i == '*'){
                    temText += '*'
                } else {
                    temText += i
                }
            }
            hidTexe = temText
            guessedLetters.add(guessedLetter)
            answers.add("Found ${listOfIndex.size} ${guessedLetter.toUpperCase()}(s)")
        } else {
            answers.add("No ${guessedLetter.toUpperCase()}s found")
        }
        updateText()
    }

    private fun disableEntry() {
        guessButton.isEnabled = false
        guessButton.isClickable = false
        textFieldMessage.isEnabled = false
        textFieldMessage.isClickable = false
    }

    private fun showAlertDialog(title: String) {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage(title)
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                this.recreate()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Game Over")
        alert.show()
    }
}