package org.hyperskill.stopwatch.handlers

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import org.hyperskill.stopwatch.R

class ButtonStartReset(private val context: Context,
                       private val startButton: Button,
                       private val resetButton: Button,
                       private val textView: TextView,
                       private val progressBar: ProgressBar
    ) {

    private var seconds = 0
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            textView.text = formatTime(seconds)
            seconds++
            handler.postAtTime(this, SystemClock.uptimeMillis() + 1000)
        }
    }
    private val colorChangeHandler = Handler(Looper.getMainLooper())
    private val colorChangeRunnable = object : Runnable {
        val colors = intArrayOf(Color.parseColor("#ADD8E6"), Color.CYAN) // Light Blue and Cyan
        var colorIndex = 0

        override fun run() {
            val colorFilter = PorterDuffColorFilter(colors[colorIndex], PorterDuff.Mode.SRC_IN)
            progressBar.indeterminateDrawable.colorFilter = colorFilter
            colorIndex = (colorIndex + 1) % colors.size
            colorChangeHandler.postDelayed(this, 1000)

            // Log the color of the progress bar
            val color = progressBar.indeterminateDrawable.colorFilter
            Log.d("ProgressBarColor", "Color: $color")
        }
    }

    init {
        startButton.text = context.getString(R.string.start)
        resetButton.text = context.getString(R.string.reset)
        textView.text = context.getString(R.string.default_time)

        progressBar.progress = 0
        progressBar.isIndeterminate = true
        progressBar.visibility = ProgressBar.INVISIBLE

        startButton.setOnClickListener {
            startFunction()
            progressBar.visibility = ProgressBar.VISIBLE
            colorChangeHandler.post(colorChangeRunnable)
        }

        resetButton.setOnClickListener {
            resetFunction()
            progressBar.visibility = ProgressBar.INVISIBLE
            colorChangeHandler.removeCallbacks(colorChangeRunnable)
        }
    }

    private fun startFunction() {
        if (seconds == 0) {
            handler.post(runnable)
        }
    }

    private fun resetFunction() {
        handler.removeCallbacks(runnable)
        seconds = 0
        textView.text = formatTime(seconds)
    }

    private fun formatTime(sec: Int): String {
        val minutes = (sec % 3600) / 60
        val seconds = sec % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}