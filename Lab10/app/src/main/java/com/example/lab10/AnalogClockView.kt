package com.example.lab10

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.Calendar
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class AnalogClockView : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {

            }

    private var mHeight: Int = 0
    private var mWidth: Int = 0
    private var mRadius: Int = 0
    private var mAngle: Double = 0.0
    private var mCentreX: Int = 0
    private var mCentreY: Int = 0
    private var mPadding: Int = 0
    private var mIsInit: Boolean = false
    private var mPaint: Paint = Paint()
    private var mRect: Rect = Rect()
    private var mNumbers: ArrayList<Int> = arrayListOf()
    private var mMinimum: Int = 0
    private var mHour: Float = 0f
    private var mMinute: Float = 0f
    private var mSecond: Float = 0f
    private var mHourHandSize: Int = 0
    private var mHandSize: Int = 0

    private fun init() {
        mHeight = height
        mWidth = width
        mPadding = 50

        mCentreX = mWidth / 2
        mCentreY = mHeight / 2

        mMinimum = min(mHeight, mWidth)
        mRadius = mMinimum / 2 - mPadding

        mAngle = (PI / 30 - PI/2)

        mHourHandSize = mRadius / 2
        mHandSize = 3 * mRadius / 4

        mNumbers = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

        mIsInit = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!mIsInit) {
            init()
        }

        drawCircle(canvas)
        drawHands(canvas)
        drawNumerals(canvas)

        postInvalidateDelayed(1000)
    }

    private fun drawCircle(canvas: Canvas) {
        mPaint.reset()
        setPaintAttributes(Color.BLUE, Paint.Style.STROKE, 8)
        canvas.drawCircle(mCentreX.toFloat(), mCentreY.toFloat(), mRadius.toFloat(), mPaint)

    }

    private fun setPaintAttributes(color: Int, stroke: Paint.Style, strokeWidth: Int) {
        mPaint.reset()
        mPaint.color = color
        mPaint.style = stroke
        mPaint.strokeWidth = strokeWidth.toFloat()
        mPaint.isAntiAlias = true
    }

    private fun drawHands(canvas: Canvas) {
        var calendar = Calendar.getInstance()
        mHour = calendar.get(Calendar.HOUR_OF_DAY).toFloat()
        mHour = if (mHour > 12) (mHour - 12) else mHour

        mMinute = calendar.get(Calendar.MINUTE).toFloat()
        mSecond = calendar.get(Calendar.SECOND).toFloat()

        drawHourHand(canvas, (mHour + mMinute / 60.0) * 5f)
        drawMinuteHand(canvas, mMinute)
        drawSecondsHand(canvas, mSecond)
    }

    private fun drawMinuteHand(canvas: Canvas, location: Float) {
        mPaint.reset();
        setPaintAttributes(Color.WHITE, Paint.Style.STROKE,8);
        mAngle = Math.PI * location / 30 - Math.PI / 2;
        canvas.drawLine(
            mCentreX.toFloat(),
            mCentreY.toFloat(),
            (mCentreX + cos( mAngle) * mHandSize).toFloat(),
            (mCentreY+ sin(mAngle) * mHourHandSize).toFloat(),
            mPaint);
    }

    private fun drawHourHand(canvas: Canvas, location: Double) {
        mPaint.reset()
        setPaintAttributes(Color.WHITE, Paint.Style.STROKE, 10)

        mAngle = PI * location / 30 - PI / 2

        canvas.drawLine(
            mCentreX.toFloat(),
            mCentreY.toFloat(),
            (mCentreX + cos(mAngle) * mHourHandSize).toFloat(),
            (mCentreY + sin(mAngle) * mHourHandSize).toFloat(),
            mPaint
        )
    }

    private fun drawSecondsHand(canvas: Canvas, location: Float) {
        mPaint.reset()
        setPaintAttributes(Color.RED, Paint.Style.STROKE, 8)

        mAngle = PI * location / 30 - PI / 2

        canvas.drawLine(
            mCentreX.toFloat(),
            mCentreY.toFloat(),
            (mCentreX + cos(mAngle) * mHandSize).toFloat(),
            (mCentreY + sin(mAngle) * mHandSize).toFloat(),
            mPaint
        )
    }

    private fun drawNumerals(canvas: Canvas) {
        mPaint.textSize = 34f
        mPaint.color = Color.WHITE
        mPaint.strokeWidth = 4f

        var radius = mRadius * 0.85

        for (number in mNumbers) {
            var num = number.toString()
            mPaint.getTextBounds(num, 0, num.length, mRect)
            var angle = PI / 6 * (number - 3)
            var x = (mCentreX + cos(angle) * radius - mRect.width() / 2).toFloat()
            var y = (mCentreY + sin(angle) * radius + mRect.height() / 2).toFloat()
            canvas.drawText(num, x, y, mPaint)
        }
    }
}