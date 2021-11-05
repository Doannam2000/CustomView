package com.ddwan.customview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ddwan.customview.clock.MainActivity
import com.ddwan.customview.customeditext.EditTextActivity
import com.ddwan.customview.demoanimation.DemoAnimationActivity
import com.ddwan.customview.lockpattern.view.LockPattern
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()
        btnClock.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        btnAnimation.setOnClickListener {
            startActivity(Intent(this,DemoAnimationActivity::class.java))
        }
        btnLockPattern.setOnClickListener {
            startActivity(Intent(this, LockPattern::class.java))
        }
        btnEditText.setOnClickListener {
            startActivity(Intent(this,EditTextActivity::class.java))
        }
    }
}