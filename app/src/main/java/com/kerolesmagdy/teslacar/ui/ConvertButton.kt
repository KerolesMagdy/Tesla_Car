package com.kerolesmagdy.teslacar.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.kerolesmagdy.teslacar.R


class ConvertButton(context: Context?, attrs: AttributeSet?) :
    AppCompatImageView(context, attrs) {

    private var currentDrawable: Drawable? = null
    private var enableDrawable: Drawable? = null
    private var disableDrawable: Drawable? = null
    private var actionEnable = false

    init {
        val attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ConvertButton)
        when {
            attributes.getDrawable(R.styleable.ConvertButton_disable_drawable) != null -> {
                disableDrawable = attributes.getDrawable(R.styleable.ConvertButton_disable_drawable)
                currentDrawable = disableDrawable
                setImageDrawable(disableDrawable)
            }
            attributes.getDrawable(R.styleable.ConvertButton_enable_drawable) != null -> {
                enableDrawable = attributes.getDrawable(R.styleable.ConvertButton_enable_drawable)
            }
        }
        attributes.recycle()

//        setImageDrawable(ContextCompat.getDrawable(getContext(), sDrawable))
    }

    fun changeState() {
        when (currentDrawable) {
            enableDrawable -> {
                setImageDrawable(disableDrawable)
                currentDrawable = disableDrawable
                actionEnable = false
            }
            disableDrawable -> {
                setImageDrawable(enableDrawable)
                currentDrawable = enableDrawable
                actionEnable = true
            }
        }
    }

    fun isActionEnable(): Boolean {
        return actionEnable
    }


}