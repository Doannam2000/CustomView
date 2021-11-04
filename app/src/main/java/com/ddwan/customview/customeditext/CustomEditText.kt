package com.ddwan.customview.customeditext

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.ddwan.customview.R


class CustomEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private var editText:EditText
    private var textView:TextView
    init {
        var inflater = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.custom_editext, this, true)
        editText = findViewById(R.id.editText)
        textView=  findViewById(R.id.textColor)
        textView.setOnClickListener {
            editText.setTextColor(Color.RED)
        }
    }
}


