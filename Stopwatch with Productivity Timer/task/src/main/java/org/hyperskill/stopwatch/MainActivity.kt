package org.hyperskill.stopwatch

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.hyperskill.stopwatch.R.*
import org.hyperskill.stopwatch.handlers.ButtonStartReset

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        val startButton = findViewById<Button>(id.startButton)
        val resetButton = findViewById<Button>(id.resetButton)
        val textView = findViewById<TextView>(id.textView)
        val progressBar = findViewById<ProgressBar>(id.progressBar)

        ButtonStartReset(this, startButton, resetButton, textView, progressBar)
        val uiElements = UIElements(this)

    }
}