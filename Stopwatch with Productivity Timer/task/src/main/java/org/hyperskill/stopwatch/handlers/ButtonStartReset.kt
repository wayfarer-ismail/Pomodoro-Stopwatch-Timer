package org.hyperskill.stopwatch.handlers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.hyperskill.stopwatch.R
import org.hyperskill.stopwatch.UIElements
import kotlin.concurrent.thread

class ButtonStartReset(private val context: Context, private val ui: UIElements) {

    private var seconds = 0
    private var upperLimit = 0

    private val handler = Handler(Looper.getMainLooper())
    private val updateTime = object : Runnable {
        override fun run() {
            seconds++
            ui.textView.text = formatTime(seconds)
            handler.postAtTime(this, SystemClock.uptimeMillis() + 1000)
        }
    }
    private val onUpperLimitReached = object  : Runnable {
        override fun run() {
            if (upperLimit != 0 && seconds > upperLimit) {
                ui.textView.setTextColor(Color.RED)
                showNotification()
                handler.removeCallbacks(this)
            }
            handler.postDelayed(this, 1000)
        }
    }
    private val colorChangeRunnable = object : Runnable {
        val colors = intArrayOf(Color.parseColor("#ADD8E6"), Color.CYAN) // Light Blue and Cyan
        var colorIndex = 0

        @SuppressLint("NewApi")
        override fun run() {
            val colorStateList = ColorStateList.valueOf(colors[colorIndex])
            ui.progressBar.indeterminateTintList = colorStateList
            colorIndex = (colorIndex + 1) % colors.size
            handler.postDelayed(this, 1000)
        }
    }

    init {
        ui.startButton.text = context.getString(R.string.start)
        ui.resetButton.text = context.getString(R.string.reset)
        ui.textView.text = context.getString(R.string.default_time)

        ui.progressBar.visibility = ProgressBar.INVISIBLE

        setListeners()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("org.hyperskill", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setListeners() {
        ui.startButton.setOnClickListener {
            startFunction()
        }

        ui.resetButton.setOnClickListener {
            resetFunction()
        }

        ui.settingsButton.setOnClickListener {
            timeLimitAlertDialog()?.show()
        }
    }

    private fun startFunction() {
        ui.progressBar.visibility = ProgressBar.VISIBLE
        ui.settingsButton.isEnabled = false

        if (seconds == 0) {
            thread {
                handler.postDelayed(updateTime, 1000)
                handler.postDelayed(colorChangeRunnable, 1000)
                handler.postDelayed(onUpperLimitReached, 1000)
            }
        }
    }

    private fun resetFunction() {
        seconds = 0
        ui.textView.text = formatTime(seconds)
        ui.textView.setTextColor(Color.BLACK)
        ui.progressBar.visibility = ProgressBar.INVISIBLE
        ui.settingsButton.isEnabled = true

        handler.removeCallbacks(colorChangeRunnable)
        handler.removeCallbacks(updateTime)
        handler.post(onUpperLimitReached)
    }

    private fun timeLimitAlertDialog(): AlertDialog? {
        val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null, false)
        val editText = contentView.findViewById<EditText>(R.id.upperLimitEditText)

        return AlertDialog.Builder(context)
            .setTitle("Set upper limit")
            .setView(contentView)
            .setPositiveButton("OK") { dialog, _ ->
                val input = editText.text.toString()
                upperLimit = if (input.isEmpty() or input.startsWith("-")) 0 else input.toInt()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    private fun showNotification() {
        val builder = NotificationCompat.Builder(context, "org.hyperskill")
            .setSmallIcon(R.drawable.notif_icon)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        val notification = builder.build()
        notification.flags = notification.flags or NotificationCompat.FLAG_INSISTENT

        with(NotificationManagerCompat.from(context)) {
            notify(393939, notification)
        }
    }

    private fun formatTime(sec: Int): String {
        val minutes = (sec % 3600) / 60
        val seconds = sec % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}