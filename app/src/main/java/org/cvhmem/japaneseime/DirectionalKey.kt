/*
 * Copyright 2024 Nelson Penn
 *
 * This file is part of Japanese Android IME.
 *
 * Japanese Android IME is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Japanese Android IME is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Japanese Android IME. If not, see <https://www.gnu.org/licenses/>.
 */
package org.cvhmem.japaneseime

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import kotlin.math.absoluteValue
import kotlin.math.pow

enum class InputDirection
{
    NONE,
    UP,
    DOWN,
    LEFT,
    RIGHT,
}


private const val ACTIVE_ZONE_MULTIPLIER = 1.5;

/**
 * A custom view implementing the core directional, swipe-activated key characteristic of 12-key
 * keyboard layouts.
 */
class DirectionalKey(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private lateinit var centerTextPaint: TextPaint
    private lateinit var directionalTextPaint: TextPaint
    private var textWidth: Float = 0f
    private var centerTextHeight: Float = 0f
    private var directionalTextHeight: Float = 0f
    private var centerTextColor: Int = Color.MAGENTA
    private var directionalTextColor: Int = Color.MAGENTA

    private val directionLabels: Array<String?> = arrayOfNulls(5)
    private val directionVectors: Array<Pair<Int, Int>> = arrayOf(
        Pair(0, 0),
        Pair(0, -1),
        Pair(0, 1),
        Pair(-1, 0),
        Pair(1, 0),
    )
    private var centerTextSize: Float = 100F
    private var directionalTextSize: Float = 50F
    var exampleDrawable: Drawable? = null
    var onInput: (String) -> Unit = {}
    var unconfirmedInputDirection: InputDirection = InputDirection.NONE;


    init {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.DirectionalKey, 0, 0
        )

        directionLabels[InputDirection.NONE.ordinal] = a.getString(R.styleable.DirectionalKey_labelCenter)
        directionLabels[InputDirection.UP.ordinal] = a.getString(R.styleable.DirectionalKey_labelUp)
        directionLabels[InputDirection.DOWN.ordinal] = a.getString(R.styleable.DirectionalKey_labelDown)
        directionLabels[InputDirection.LEFT.ordinal] = a.getString(R.styleable.DirectionalKey_labelLeft)
        directionLabels[InputDirection.RIGHT.ordinal] = a.getString(R.styleable.DirectionalKey_labelRight)

        centerTextSize = a.getDimension(R.styleable.DirectionalKey_centerTextSize, centerTextSize)
        directionalTextSize = a.getDimension(R.styleable.DirectionalKey_directionalTextSize, centerTextSize)
        centerTextColor = a.getColor(R.styleable.DirectionalKey_centerTextColor, centerTextColor)
        directionalTextColor = a.getColor(R.styleable.DirectionalKey_directionalTextColor, directionalTextColor)

        a.recycle()

        // Set up a default TextPaint object
        centerTextPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.CENTER
            color = centerTextColor
        }
        directionalTextPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.CENTER
            color = directionalTextColor
        }

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {
        centerTextPaint.let {
            it.textSize = centerTextSize
            textWidth = it.measureText(directionLabels[0] ?: "")
            centerTextHeight = it.fontMetrics.bottom
        }
        directionalTextPaint.let {
            it.textSize = directionalTextSize
            textWidth = it.measureText(directionLabels[0] ?: "")
            directionalTextHeight = it.fontMetrics.bottom
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        directionLabels[0]?.let {
            // Draw the text.
            canvas.drawText(
                it,
                paddingLeft + (contentWidth) / 2f,
                paddingTop + (contentHeight + centerTextHeight) / 2f,
                centerTextPaint
            )
        }
        for (label in 1..4)
        {
            directionLabels[label]?.let {
                canvas.drawText(
                    it,
                    paddingLeft + (contentWidth) / 2f + directionVectors[label].first * contentWidth * .22F,
                    paddingTop + (contentHeight + directionalTextHeight) / 2f + directionVectors[label].second * contentWidth * .22F,
                    directionalTextPaint
                )
            }

        }

        // Draw the example drawable on top of the text.
        exampleDrawable?.let {
            it.setBounds(
                paddingLeft, paddingTop,
                paddingLeft + contentWidth, paddingTop + contentHeight
            )
            it.draw(canvas)
        }
    }

    private fun getInputDirection(event: MotionEvent): InputDirection
    {
        val cx = width / 2f
        val cy = height / 2f

        val dx = event.x - cx
        val dy = event.y - cy

        val withinActiveZone = (dx.pow(2) + dy.pow(2)) < (centerTextSize * ACTIVE_ZONE_MULTIPLIER).pow(2)

        if (withinActiveZone)
        {
            return InputDirection.NONE
        }
        if (dy.absoluteValue > dx.absoluteValue)
        {
            if (dy < 0)
            {
                return InputDirection.UP
            }
            return InputDirection.DOWN
        }

        if (dx < 0)
        {
            return InputDirection.LEFT
        }
        return InputDirection.RIGHT
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action)
        {
            MotionEvent.ACTION_DOWN -> {
                // motion coordinates are relative to top left corner of view
                val cx = width / 2f
                val cy = height / 2f

                val dx = event.x - cx
                val dy = event.y - cy
                val willHandlePress = (dx.pow(2) + dy.pow(2)) < (centerTextSize * ACTIVE_ZONE_MULTIPLIER).pow(2)
                if (willHandlePress){
                    unconfirmedInputDirection = InputDirection.NONE
                    performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                }
                return willHandlePress;
            }
            MotionEvent.ACTION_UP -> {
                // motion coordinates are relative to top left corner of view
                val inputDirection = getInputDirection(event)
                val label = directionLabels[inputDirection.ordinal]
                if (label != null)
                {
                    /*
                     * In testing, haptic feedback on release has been more annoying than helpful
                     * AOSP keyboard doesn't seem to do it either.
                     */
                    //performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY_RELEASE);
                    onInput(label)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val inputDirection = getInputDirection(event)
                if (unconfirmedInputDirection != InputDirection.NONE && inputDirection == InputDirection.NONE){
                    performHapticFeedback(HapticFeedbackConstants.GESTURE_THRESHOLD_DEACTIVATE)
                }
                else if(inputDirection != unconfirmedInputDirection)
                {
                    performHapticFeedback(HapticFeedbackConstants.GESTURE_THRESHOLD_ACTIVATE)
                }
                unconfirmedInputDirection = inputDirection
            }
        }
        // TODO: return true if in actionable zone
        return true
    }
}