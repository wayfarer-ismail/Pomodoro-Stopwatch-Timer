package org.hyperskill.stopwatch

import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UIElements(activity: AppCompatActivity) {
    val startButton: Button = activity.findViewById(R.id.startButton)
    val resetButton: Button = activity.findViewById(R.id.resetButton)
    val textView: TextView = activity.findViewById(R.id.textView)
    val progressBar: ProgressBar = activity.findViewById(R.id.progressBar)
    val settingsButton: Button = activity.findViewById(R.id.settingsButton)
}