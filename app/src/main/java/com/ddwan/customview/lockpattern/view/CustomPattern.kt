package com.ddwan.customview.lockpattern.view

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class CustomPattern @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var mess = arrayOf("Mật khẩu không đúng",
        "Thành công rồi nè !",
        "Nhập mật khẩu mới !",
        "Tạo thành công !",
        "Yêu cầu 4 kí tự trở lên",
        "Nhập mật khẩu !")
    private var point = ArrayList<Point>()
    private var password = ArrayList<Int>()
    private var fontSize = 0
    private var paint: Paint = Paint()
    private var mPadding = 0f
    private var startPoint: Point? = null
    private var endPoint: Point? = null
    private var isInit = false
    private var isDrawing = false
    private var isCorrect = false
    private var isCheck = false
    private var isFirstTime = true
    private var listPass = ArrayList<Int>()
    private var shared = context.getSharedPreferences("PASSWORD", MODE_PRIVATE)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasure = MeasureSpec.getSize(widthMeasureSpec)
        var heightMeasure = MeasureSpec.getSize(widthMeasureSpec)
        if(widthMeasure<350.dp)
            widthMeasure = 350.dp
        if(heightMeasure<400.dp)
            heightMeasure = 400.dp
        setMeasuredDimension(widthMeasure, heightMeasure)
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            init()
            paint.textSize = fontSize.toFloat()
            paint.color = Color.BLUE
            paint.textAlign = Paint.Align.CENTER
            if (isFirstTime) {
                canvas.drawText(mess[2], width / 2f, (mPadding + 100) / 2, paint)
            } else {
                canvas.drawText(mess[5], width / 2f, (mPadding + 100) / 2, paint)
            }
        }
        drawPoint(canvas)
        drawPoints(canvas)
        if (isFirstTime) {
            paint.textSize = fontSize.toFloat()
            paint.textAlign = Paint.Align.CENTER
            if (isCheck) {
                if (isCorrect) {
                    canvas.drawText(mess[3], width / 2f, (mPadding + 100) / 2, paint)
                    isFirstTime = false
                    shared.edit().putBoolean("isFirstTime", false).apply()
                } else {
                    canvas.drawText(mess[4], width / 2f, (mPadding + 100) / 2, paint)
                }
            }
            isCheck = false
        } else {
            if (isCheck) {
                paint.textSize = fontSize.toFloat()
                if (isCorrect) {
                    canvas.drawText(mess[1], width / 2f, (mPadding + 100) / 2, paint)
                } else {
                    canvas.drawText(mess[0], width / 2f, (mPadding + 100) / 2, paint)
                }
                isCheck = false
            }
        }
    }

    private fun init() {
        mPadding = width.coerceAtMost(height) / 6f
        fontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20f,
            resources.displayMetrics).toInt()

        point.add(Point(mPadding.toInt(), mPadding.toInt() + 100))
        point.add(Point(width / 2, mPadding.toInt() + 100))
        point.add(Point(width - mPadding.toInt(), mPadding.toInt() + 100))

        point.add(Point(mPadding.toInt(), height / 2 + 50))
        point.add(Point(width / 2, height / 2 + 50))
        point.add(Point(width - mPadding.toInt(), height / 2 + 50))

        point.add(Point(mPadding.toInt(), height - mPadding.toInt()))
        point.add(Point(width / 2, height - mPadding.toInt()))
        point.add(Point(width - mPadding.toInt(), height - mPadding.toInt()))

        isFirstTime = shared.getBoolean("isFirstTime", true)

        val array = ArrayList<Int>()
        val size = shared.getInt("size", 0)

        for (i in 0 until size)
            array.add(shared.getInt("key$i", 0))
        Log.d("size", array.size.toString())
        listPass = array
        isInit = true
    }

    private fun drawPoints(canvas: Canvas) {
        if (isDrawing && endPoint != null) {
            paint.color = Color.BLUE
            paint.strokeWidth = 5f
            paint.style = Paint.Style.STROKE
            for (i in 0..password.size) {
                if (i < password.size - 1) {
                    canvas.drawLine(point[password[i]].x.toFloat(),
                        point[password[i]].y.toFloat(),
                        point[password[i + 1]].x.toFloat(),
                        point[password[i + 1]].y.toFloat(),
                        paint)
                }
            }
            canvas.drawLine(startPoint!!.x.toFloat(),
                startPoint!!.y.toFloat(), endPoint!!.x.toFloat(), endPoint!!.y.toFloat(), paint)
            startPoint = endPoint
            endPoint = null
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                for (i in point) {
                    val x = Math.pow((event.x - i.x).toDouble(), 2.0)
                    val y = Math.pow((event.y - i.y).toDouble(), 2.0)
                    if (x + y < Math.pow(85.0, 2.0) && !password.contains(point.indexOf(i))) {
                        password.add(point.indexOf(i))
                        startPoint = i
                        isDrawing = true
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (startPoint != null) {
                    for (i in point) {
                        val x = Math.pow((event.x - i.x).toDouble(), 2.0)
                        val y = Math.pow((event.y - i.y).toDouble(), 2.0)
                        if (x + y < Math.pow(70.0, 2.0) && !password.contains(point.indexOf(i))) {
                            endPoint = i
                            password.add(point.indexOf(i))
                            invalidate()
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                startPoint = null
                endPoint = null
                isDrawing = false
                isCheck = true
                if (!isFirstTime)
                    checkPassword()
                else
                    savePassword()
                invalidate()
            }
        }
        return true
    }

    private fun savePassword() {
        isCorrect = if (password.size < 4) {
            password.clear()
            false
        } else {
            listPass.clear()
            listPass.addAll(password)
            shared.edit().putInt("size", listPass.size).apply()
            for (i in listPass.indices) {
                shared.edit().putInt("key$i", listPass[i]).apply()
            }
            password.clear()
            true
        }
    }

    private fun checkPassword() {
        if (password.size < 4 || listPass.size != password.size) {
            isCorrect = false
            password.clear()
            return
        }
        for (i in listPass.indices) {
            if (password[i] != listPass[i]) {
                isCorrect = false
                password.clear()
                return
            }
        }
        password.clear()
        isCorrect = true
    }


    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

    private fun drawPoint(canvas: Canvas) {
        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL

        point.forEach { canvas.drawCircle(it.x.toFloat(), it.y.toFloat(), 13f, paint) }

//        // draw 3 point top
//        canvas.drawCircle(mPadding, mPadding + 100, 13f, paint)
//        canvas.drawCircle(width / 2f, mPadding + 100, 13f, paint)
//        canvas.drawCircle(width - mPadding, mPadding + 100, 13f, paint)
//        // draw 3 point middle
//        canvas.drawCircle(mPadding, height / 2f + 50, 13f, paint)
//        canvas.drawCircle(width / 2f, height / 2f + 50, 13f, paint)
//        canvas.drawCircle(width - mPadding, height / 2f + 50, 13f, paint)
//        // draw 3 point bottom
//        canvas.drawCircle(mPadding, height - mPadding, 13f, paint)
//        canvas.drawCircle(width / 2f, height - mPadding, 13f, paint)
//        canvas.drawCircle(width - mPadding, height - mPadding, 13f, paint)

    }
}