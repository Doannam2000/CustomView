package com.ddwan.customview.clock

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ddwan.customview.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    var hour = 0
    var minute = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTime.setOnClickListener {
            val timePicker = TimePickerDialog(
                this,
                TimePickerDialog.THEME_HOLO_DARK, { _, i, i2 ->
                    val calendar = Calendar.getInstance()
                    hour = i - calendar.get(Calendar.HOUR_OF_DAY)
                    minute = i2 - calendar.get(Calendar.MINUTE)
                    customClock.setTime(hour, minute)
                }, 12, 0, true
            )
            timePicker.updateTime(hour, minute)
            timePicker.show()
        }
    }
}