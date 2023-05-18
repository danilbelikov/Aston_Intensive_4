package com.example.aston4_lesson

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class ClockView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val attributeColor =
        context.obtainStyledAttributes(attrs, R.styleable.ClockView, defStyleAttr, 0)
    private val attributeSize =
        context.obtainStyledAttributes(attrs, R.styleable.ClockView, defStyleAttr, 0)
    private val sizeHand =
        attributeSize.getDimension(R.styleable.ClockView_clockHandSize, pixelsToDp(400f))
    private val colorHandColor = attributeColor.getColor(
        R.styleable.ClockView_clockHandColor_ClockView,
        context.getColor(R.color.white)
    )
    private var height = 0
    private var width = 0
    private var padding = 0
    private var fontSize = 0
    private val numeralSpacing = 0
    private var handTruncation = 0
    private var hourHandTruncation = 0
    private var radius = 0
    private var paint: Paint? = null
    private var isInit = false
    private val numbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val rect = Rect()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isInit) {
            initializeClock()
        }
        canvas.drawColor(Color.BLACK)
        drawCircle(canvas)
        drawNumeral(canvas)
        drawHands(canvas)
        drawRectangle(canvas)
        drawCenter(canvas)
        postInvalidateDelayed(500)
        invalidate()
    }
    private fun initializeClock() {
        height = getHeight()
        width = getWidth()
        padding = numeralSpacing + 50
        fontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 13f,
            resources.displayMetrics
        ).toInt()
        val min = height.coerceAtMost(width)
        radius = min / 2 - padding
        handTruncation = min / 20
        hourHandTruncation = min / 7
        paint = Paint()
        isInit = true
    }
    private fun drawRectangle(canvas: Canvas) {
        val myRect: Rect = Rect()
        myRect.set(0, 0, width, height)
        paint?.apply {
            reset()
            color = Color.RED
            strokeWidth = pixelsToDp(70f)
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        canvas.drawRect(
            myRect,
            paint!!
        )
    }
    private fun drawHand(canvas: Canvas, loc: Double, isHour: Boolean) {
        val angle = Math.PI * loc / 30 - Math.PI / 2

        val handRadius =
            if (isHour) sizeHand.toInt() else radius - handTruncation
        canvas.drawLine(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 2 + cos(angle) * handRadius).toFloat(),
            (height / 2 + sin(angle) * handRadius).toFloat(),

            paint!!.apply {
                isAntiAlias = true
                color = colorHandColor
            }
        )
    }
    private fun drawHands(canvas: Canvas) {
        val c = Calendar.getInstance()
        var hour = c[Calendar.HOUR_OF_DAY].toFloat()
        hour = if (hour > 12) hour - 12 else hour
        drawHand(canvas, ((hour + c[Calendar.MINUTE] / 60) * 5f).toDouble(), true)
        drawHand(canvas, c[Calendar.MINUTE].toDouble(), false)
        drawHand(canvas, c[Calendar.SECOND].toDouble(), false)
    }
    private fun drawNumeral(canvas: Canvas) {
        paint?.apply {
            textSize = fontSize.toFloat()
        }

        for (number in numbers) {
            paint!!.color = Color.WHITE
            val tmp = number.toString()
            paint!!.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (width / 2 + cos(angle) * radius - rect.width() / 2).toInt()
            val y = (height / 2 + sin(angle) * radius + rect.height() / 2).toInt()
            canvas.drawText(tmp, x.toFloat(), y.toFloat(), paint!!)
        }
    }
    private fun drawCircle(canvas: Canvas) {
        paint?.apply {
            reset()
            color = Color.RED
            strokeWidth = pixelsToDp(20f)
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            pixelsToDp((radius + padding - 10).toFloat()),
            paint!!
        )
        paint!!.style = Paint.Style.STROKE
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            pixelsToDp((radius + padding - 10).toFloat() + 30f),
            paint!!
        )
        paint!!.style = Paint.Style.FILL
    }
    private fun drawCenter(canvas: Canvas) {
        paint?.apply {
            style = Paint.Style.FILL
            color = Color.WHITE
            canvas.drawCircle(
                (width / 2).toFloat(),
                (height / 2).toFloat(),
                pixelsToDp(30f),
                paint!!
            )
        }
    }
    private fun pixelsToDp(pixels: Float): Float {
        val screenPixelDensity = context.resources.displayMetrics.density
        return pixels / screenPixelDensity
    }
}