package ru.stersh.hronos.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

class TimerView : AppCompatTextView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var baseTime: Long = 0L
        set(value) {
            field = value
            updateText()
        }
    var startTime = 0L
        set(value) {
            field = value
            updateText()
        }

    private var isStarted = false
    private var isRuning = false
    private var isVisible = true

    private val tickRunnable: Runnable = object : Runnable {
        override fun run() {
            if (isRuning) {
                updateText()
                postDelayed(this, 1000)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isVisible = false
        updateRunning()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        isVisible = visibility == View.VISIBLE
        updateRunning()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        updateRunning()
    }

    fun start() {
        isStarted = true
        updateRunning()
    }

    fun stop() {
        isStarted = false
        updateRunning()
    }

    private fun updateRunning() {
        val running = isVisible && isStarted && isShown
        if (running != isRuning) {
            if (running) {
                updateText()
                postDelayed(tickRunnable, 1000)
            } else {
                removeCallbacks(tickRunnable)
            }
            isRuning = running
        }
    }

    private fun updateText() {
        val now = System.currentTimeMillis()
        val currentSeconds = if (startTime == 0L) {
            baseTime / 1000
        } else {
            (now - startTime + baseTime) / 1000
        }
        val hours = currentSeconds / 3600
        val minutes = (currentSeconds - hours * 3600) / 60
        val seconds = (currentSeconds - hours * 3600) - minutes * 60
        val h = if (hours < 10) {
            "0$hours"
        } else {
            hours.toString()
        }
        val m = if (minutes < 10) {
            "0$minutes"
        } else {
            minutes.toString()
        }
        val s = if (seconds < 10) {
            "0$seconds"
        } else {
            seconds.toString()
        }
        this.text = h + ":" + m + ":" + s
    }
}