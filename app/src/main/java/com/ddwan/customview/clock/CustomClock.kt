package com.ddwan.customview.clock

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.*

class CustomClock @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var mPadding = 0
    private var numeralSpacing = 0
    private var handTruncation = 0
    private var hourHandTruncation = 0
    private var radius = 0
    private var fontSize = 0
    private var paint: Paint = Paint()
    private val numbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private var rect = Rect()
    private var mHour = 0
    private var mMinute = 0
    private var isInit = false

    override fun onDraw(canvas: Canvas) {
        if(!isInit)
            init()
        canvas.drawColor(Color.GRAY)
        drawCircle(canvas)
        drawCenter(canvas)
        drawNumeral(canvas)
        drawHands(canvas)
        postInvalidateDelayed(500)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasure = MeasureSpec.getSize(widthMeasureSpec)
        var heightMeasure = MeasureSpec.getSize(heightMeasureSpec)
        if(widthMeasure<100.dp)
            widthMeasure = 100.dp
        if(heightMeasure < 100.dp)
            heightMeasure = 100.dp
        setMeasuredDimension(widthMeasure,heightMeasure)
    }

    private fun init(){
        mPadding = numeralSpacing + 50
        fontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13f,
            resources.displayMetrics).toInt()
        val min = height.coerceAtMost(width)
        radius = min / 2 - mPadding
        handTruncation = min / 20
        hourHandTruncation = min / 7
        isInit = true
    }

    private fun drawHand(canvas: Canvas, loc: Float, isHour: Boolean, isSecond: Boolean) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        if (isSecond)
            paint.color = Color.YELLOW
        val handRadius =
            if (isHour) radius - handTruncation - hourHandTruncation else radius - handTruncation

        canvas.drawLine((width / 2).toFloat(), (height / 2).toFloat(),
            (width / 2 + Math.cos(angle) * handRadius).toFloat(),
            (height / 2 + Math.sin(angle) * handRadius).toFloat(),
            paint)
    }

    private fun drawHands(canvas: Canvas) {
        var c: Calendar = Calendar.getInstance()
        var hour: Float = c.get(Calendar.HOUR_OF_DAY).toFloat()
        var minute = c.get(Calendar.MINUTE).toFloat()
        minute += mMinute
        if(minute > 60) {
            minute -=60
            hour++
        }
        hour += mHour
        hour = if (hour > 12) hour - 12 else hour
        drawHand(canvas, (hour + c.get(Calendar.MINUTE) / 60) *5f, isHour = true, isSecond = false)
        drawHand(canvas, minute, isHour = false, isSecond = false)
        drawHand(canvas, c.get(Calendar.SECOND).toFloat(), isHour = false, isSecond = true)
    }

    private fun drawNumeral(canvas: Canvas) {
        paint.textSize = fontSize.toFloat()
        for (number in numbers) {
            val tmp = number.toString()
            paint.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (width / 2 + Math.cos(angle) * radius - rect.width() / 2).toInt()
            val y = (height / 2 + Math.sin(angle) * radius + rect.height() / 2).toInt()
            canvas.drawText(tmp, x.toFloat(), y.toFloat(), paint)
        }
    }

    private fun drawCenter(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), 12F, paint)
    }

    private fun drawCircle(canvas: Canvas) {
        paint.reset()
        paint.color = Color.WHITE
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        canvas.drawCircle((width / 2).toFloat(),
            (height / 2).toFloat(), (radius + mPadding - 10).toFloat(), paint)
    }

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

    fun setTime(hour:Int,minute:Int){
        mHour = hour
        mMinute = minute
    }

}