package org.hyperskill.stopwatch.handlers

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import org.hyperskill.stopwatch.R

class ButtonStartReset(private val context: Context,
                       private val startButton: Button,
                       private val resetButton: Button,
                       private val textView: TextView,
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

    init {
        startButton.text = context.getString(R.string.start)
        resetButton.text = context.getString(R.string.reset)
        textView.text = context.getString(R.string.default_time)

        startButton.setOnClickListener {
            startFunction()
        }

        resetButton.setOnClickListener {
            resetFunction()
        }
    }

    private fun startFunction() {
        handler.post(runnable)
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