package org.hyperskill.stopwatch

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.hyperskill.stopwatch.handlers.ButtonStartReset

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.startButton)
        val resetButton = findViewById<Button>(R.id.resetButton)
        val textView = findViewById<TextView>(R.id.textView)

        ButtonStartReset(this, startButton, resetButton, textView)
    }
}