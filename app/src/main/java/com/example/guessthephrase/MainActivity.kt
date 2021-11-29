package com.example.guessthephrase

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var myRV: RecyclerView
    private lateinit var guessButton: Button
    private lateinit var messageText: TextView
    private lateinit var textGuess: TextView
    private lateinit var textLetters: TextView
    private lateinit var clMain: ConstraintLayout

    private val messages = ArrayList<String>()
    private val answer = "i am a trainee at saudi digital academy"
    private val myAnswerDictionary = mutableMapOf<Int, Char>()
    private var myAnswer = ""
    private var guessedLetters = ""
    private var count = 0
    private var guessPhrase = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myRV = findViewById(R.id.rvMain)
        guessButton = findViewById(R.id.submitButton)
        messageText = findViewById(R.id.messageText)
        textGuess = findViewById(R.id.textGuess)
        textLetters = findViewById(R.id.guessedLetters)

        clMain = findViewById(R.id.clMain)
        myRV.adapter = RecyclerViewAdapter(messages)
        myRV.layoutManager = LinearLayoutManager(this)

        for (i in answer.indices) {
            if (answer[i] == ' ') {
                myAnswer += ' '
                myAnswerDictionary[i] = ' '
            } else {
                myAnswer += '*'
                myAnswerDictionary[i] = '*'
            }
        }
        updateTextField()
        guessButton.setOnClickListener { addGuess() }
    }

    private fun addGuess() {
        var userGuess = messageText.text.toString()

        if (guessPhrase) {
            if (userGuess == answer) {
                disableEntry()
                showAlertDialog("You win!\n\nPlay again?")
            } else {
                updateTextField()
                guessPhrase = false
                messages.add("Wrong guess: $userGuess")
            }
        }
        else {
            if (userGuess.isNotEmpty() && userGuess.length == 1) {
                myAnswer = ""
                guessPhrase = true
                checkLetters(userGuess[0])
            } else {
                Snackbar.make(clMain, "Please enter one letter only", Snackbar.LENGTH_LONG).show()
            }
        }
        messageText.text = ""
        myRV.adapter?.notifyDataSetChanged()
    }

    private fun updateTextField(){
        textGuess.text = myAnswer.toUpperCase()
        textLetters.text = "Guessed Letters:  " + guessedLetters
        if(guessPhrase){
            messageText.hint = "Guess the full phrase"
        }else{
            messageText.hint = "Guess a letter"
        }
    }

    private fun checkLetters(guessedLetter: Char){
        var found = 0
        for(i in answer.indices){
            if(answer[i] == guessedLetter){
                myAnswerDictionary[i] = guessedLetter
                found++
            }
        }
        for(i in myAnswerDictionary){myAnswer += myAnswerDictionary[i.key]}
        if(myAnswer==answer){
            disableEntry()
            showAlertDialog("You win!\n\nPlay again?")
        }
        if(guessedLetters.isEmpty()){guessedLetters+=guessedLetter}else{guessedLetters+=", "+guessedLetter}
        if(found>0){
            messages.add("Found $found ${guessedLetter.toUpperCase()}(s)")
        }else{
            messages.add("No ${guessedLetter.toUpperCase()}s found")
        }
        count++
        val guessesLeft = 10 - count
        if(count<10){messages.add("$guessesLeft guesses remaining")}
        updateTextField()
        myRV.scrollToPosition(messages.size - 1)
    }

    private fun disableEntry() {
        guessButton.isEnabled = false
        guessButton.isClickable = false
        messageText.isEnabled = false
        messageText.isClickable = false
    }

    private fun showAlertDialog(title: String) {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage(title)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                this.recreate()
            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Game Over")
        // show alert dialog
        alert.show()
    }
}