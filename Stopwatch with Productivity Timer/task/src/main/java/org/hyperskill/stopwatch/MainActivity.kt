package org.hyperskill.stopwatch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.hyperskill.stopwatch.R.*
import org.hyperskill.stopwatch.handlers.ButtonStartReset

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        val uiElements = UIElements(this)
        ButtonStartReset(this, uiElements)
    }
}