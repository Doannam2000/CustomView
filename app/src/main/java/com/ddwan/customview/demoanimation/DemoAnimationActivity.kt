package com.ddwan.customview.demoanimation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.ddwan.customview.R
import kotlinx.android.synthetic.main.activity_demo_animation.*

class DemoAnimationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_animation)
        supportActionBar?.hide()
        btnBlink.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this,R.anim.blink_animation)
            imageView.startAnimation(animation)
        }
        btnFade.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this,R.anim.fade_animation)
            imageView.startAnimation(animation)
        }
        btnMove.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this,R.anim.move_animation)
            imageView.startAnimation(animation)
        }
        btnRotate.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this,R.anim.rotate_animation)
            imageView.startAnimation(animation)
        }
        btnSlide.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this,R.anim.slide_animation)
            imageView.startAnimation(animation)
        }
        btnZoom.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this,R.anim.zoom_animation)
            imageView.startAnimation(animation)
        }
        btnStop.setOnClickListener {
            imageView.clearAnimation()
        }
    }
}