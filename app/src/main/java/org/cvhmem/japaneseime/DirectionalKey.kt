package org.cvhmem.japaneseime

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Dimension

enum class InputDirection
{
    NONE,
    UP,
    DOWN,
    LEFT,
    RIGHT,
}


/**
 * TODO: document your custom view class.
 */
class DirectionalKey(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private lateinit var textPaint: TextPaint
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f

    private val directionLabels: Array<String?> = arrayOfNulls(5)
    var exampleDrawable: Drawable? = null

    init {
        init(attrs, 0)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.DirectionalKey, defStyle, 0
        )

        directionLabels[InputDirection.NONE.ordinal] = a.getString(R.styleable.DirectionalKey_labelCenter)
        directionLabels[InputDirection.UP.ordinal] = a.getString(R.styleable.DirectionalKey_labelUp)
        directionLabels[InputDirection.DOWN.ordinal] = a.getString(R.styleable.DirectionalKey_labelDown)
        directionLabels[InputDirection.LEFT.ordinal] = a.getString(R.styleable.DirectionalKey_labelLeft)
        directionLabels[InputDirection.RIGHT.ordinal] = a.getString(R.styleable.DirectionalKey_labelRight)

        if (a.hasValue(R.styleable.DirectionalKey_exampleDrawable)) {
            exampleDrawable = a.getDrawable(
                R.styleable.DirectionalKey_exampleDrawable
            )
            exampleDrawable?.callback = this
        }

        a.recycle()

        // Set up a default TextPaint object
        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {
        textPaint.let {
            it.textSize = 100.0F
            it.color = Color.RED
            textWidth = it.measureText("a")
            textHeight = it.fontMetrics.bottom
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
                paddingLeft + (contentWidth - textWidth) / 2,
                paddingTop + (contentHeight + textHeight) / 2,
                textPaint
            )
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
}