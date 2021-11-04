package com.ddwan.customview.customeditext

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.ddwan.customview.R
import kotlinx.android.synthetic.main.custom_dialog.view.*
import java.lang.Exception


@SuppressLint("ClickableViewAccessibility")
class CustomEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private var editText: EditText
    private var displayColor: View
    var text = ""

    init {
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomEditText)
        var color = typedArray.getString(R.styleable.CustomEditText_customEditText_Color).toString()
        typedArray.recycle()
        if (color == "null") {
            color = "#000000"
        }

        val inflater =
            getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.custom_editext, this, true)
        editText = findViewById(R.id.editText)
        displayColor = findViewById(R.id.textColor)

        displayColor.setBackgroundColor(Color.parseColor(color))
        editText.setTextColor(Color.parseColor(color))

        displayColor.setOnClickListener {
            val view = View.inflate(getContext(), R.layout.custom_dialog, null)
            val builder = AlertDialog.Builder(getContext())
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            view.display_color.setBackgroundColor(Color.parseColor(color))
            view.image_color.isDrawingCacheEnabled = true
            view.image_color.buildDrawingCache(true)
            view.text_color.setText(color.substring(1))
            view.image_color.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN ||
                    motionEvent.action == MotionEvent.ACTION_MOVE
                ) {
                    val bitmap = view.image_color.drawingCache
                    try {
                        val pixel = bitmap.getPixel(motionEvent.x.toInt(), motionEvent.y.toInt())
                        val r = Color.red(pixel)
                        val g = Color.green(pixel)
                        val b = Color.blue(pixel)
                        val col = Color.rgb(r, g, b)
                        view.text_color.setText(String.format("%02x%02x%02x", r, g, b))
                        view.display_color.setBackgroundColor(col)
                    } catch (e: Exception) {
                    }
                }
                true
            }
            view.text_color.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (view.text_color.text.length == 6) {
                        try {
                            view.display_color.setBackgroundColor(Color.parseColor("#${view.text_color.text}"))
                            view.text_color.setTextColor(Color.BLACK)
                            view.oke.isEnabled = true
                        }catch (e:Exception){
                            view.text_color.setTextColor(Color.RED)
                            view.oke.isEnabled = false
                        }
                    } else {
                        view.text_color.setTextColor(Color.RED)
                        view.oke.isEnabled = false
                    }
                }
            })
            view.oke.setOnClickListener {
                color = "#${view.text_color.text}"
                displayColor.setBackgroundColor(Color.parseColor(color))
                editText.setTextColor(Color.parseColor(color))
                dialog.dismiss()
            }
            view.exit.setOnClickListener {
                dialog.dismiss()
            }
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                text = p0.toString()
            }
        })
    }
}


