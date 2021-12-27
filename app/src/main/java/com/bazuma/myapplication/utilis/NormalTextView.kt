package com.bazuma.myapplication.utilis

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class NormalTextView(context: Context, attributeSet: AttributeSet)
    : AppCompatTextView(context,attributeSet){
    /**
     * The init block runs every time the class is instantiated.
     */
    init {
        applyFont()
    }
    /**
     * Applies a font to a TextView.
     *
     * @param textView TextView when the font should apply
     */
    private fun applyFont() {
        /**
            This is used to get the file from the assets folder and set it to the title textView.
        */
        val normalTypeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        typeface=normalTypeface
    }
}