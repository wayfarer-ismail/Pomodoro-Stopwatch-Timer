package org.hyperskill.stopwatch.handlers

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.ProgressBar
import org.hyperskill.stopwatch.R
import org.hyperskill.stopwatch.UIElements

class ButtonStartReset(private val context: Context, private val ui: UIElements) {

    private var seconds = 0
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            ui.textView.text = formatTime(seconds)
            seconds++
            handler.postAtTime(this, SystemClock.uptimeMillis() + 1000)
        }
    }
    private val colorChangeHandler = Handler(Looper.getMainLooper())
    private val colorChangeRunnable = object : Runnable {
        val colors = intArrayOf(Color.parseColor("#ADD8E6"), Color.CYAN) // Light Blue and Cyan
        var colorIndex = 0

        @SuppressLint("NewApi")
        override fun run() {
            val colorStateList = ColorStateList.valueOf(colors[colorIndex])
            ui.progressBar.indeterminateTintList = colorStateList
            colorIndex = (colorIndex + 1) % colors.size
            colorChangeHandler.postDelayed(this, 1000)
        }
    }

    init {
        ui.startButton.text = context.getString(R.string.start)
        ui.resetButton.text = context.getString(R.string.reset)
        ui.textView.text = context.getString(R.string.default_time)

        ui.progressBar.progress = 0
        ui.progressBar.isIndeterminate = true
        ui.progressBar.visibility = ProgressBar.INVISIBLE

        setListeners()
    }

    private fun setListeners() {
        ui.startButton.setOnClickListener {
            startFunction()
            ui.progressBar.visibility = ProgressBar.VISIBLE
            colorChangeHandler.post(colorChangeRunnable)
            ui.settingsButton.isEnabled = false
        }

        ui.resetButton.setOnClickListener {
            resetFunction()
            ui.progressBar.visibility = ProgressBar.INVISIBLE
            colorChangeHandler.removeCallbacks(colorChangeRunnable)
            ui.settingsButton.isEnabled = true
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
        ui.textView.text = formatTime(seconds)
    }

    private fun formatTime(sec: Int): String {
        val minutes = (sec % 3600) / 60
        val seconds = sec % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}