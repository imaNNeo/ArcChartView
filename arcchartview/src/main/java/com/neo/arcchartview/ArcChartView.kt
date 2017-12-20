package com.neo.arcchartview

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

/**
 * Created by iman.
 * iman.neofight@gmail.com
 */
class ArcChartView @JvmOverloads constructor(mContext : Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        View(mContext,attrs, defStyleAttr) {
    private var drawLinePaint: Paint
    private var bgPaint : Paint
    private var clearPaint : Paint

    var linesSpace = DpHandler.dpToPx(mContext,4).toFloat()
    var lineStrokeWidth = DpHandler.dpToPx(mContext,6).toFloat()
    var linesCount : Int = 10

    var sectionsCount : Int = 8
    var sectionDegree = (360/sectionsCount).toFloat()
    var selectionsSpace = DpHandler.dpToPx(mContext,4).toFloat()
    var bgColor = Color.WHITE
    var midStartSize = DpHandler.dpToPx(mContext,16).toFloat()

    var fills : MutableList<Int> = mutableListOf()


    var mWidth : Int = 0
    var mHeight : Int = 0

    var filledColors: List<Int>? = null
    var unfilledColors: List<Int>? = null

    var iconBmp = BitmapFactory.decodeResource(context.resources,R.drawable.ic_star)
    var iconSize: Float = DpHandler.dpToPx(mContext,16).toFloat()

    init {
        fills.add(0,8)
        fills.add(1,7)
        fills.add(2,8)
        fills.add(3,8)
        fills.add(4,3)
        fills.add(5,9)
        fills.add(6,7)
        fills.add(7,7)

        filledColors = listOf(
                color(R.color.unfilled_section_1),color(R.color.unfilled_section_2),
                color(R.color.unfilled_section_3),color(R.color.unfilled_section_4),
                color(R.color.unfilled_section_5),color(R.color.unfilled_section_6),
                color(R.color.unfilled_section_7),color(R.color.unfilled_section_8))

        unfilledColors = listOf(
                color(R.color.filled_section_1),color(R.color.filled_section_2),
                color(R.color.filled_section_3),color(R.color.filled_section_4),
                color(R.color.filled_section_5),color(R.color.filled_section_6),
                color(R.color.filled_section_7),color(R.color.filled_section_8))


        drawLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = lineStrokeWidth
        }


        bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = bgColor
        }


        clearPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = selectionsSpace
            color = bgColor
            style = Paint.Style.FILL_AND_STROKE
        }
    }


    fun color(resId : Int) = ContextCompat.getColor(context,resId)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth
        mHeight = measuredHeight
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val centerX = (mWidth/2).toFloat()
        val centerY = (mHeight/2).toFloat()

        //Draw Background
        canvas?.drawRect(Rect(0,0,mWidth,mHeight),bgPaint)


        //Draw unfilled arc lines
        for(i in 1..linesCount){
            val left = centerX - (((lineStrokeWidth + linesSpace) *i)+midStartSize)
            val top = centerY - (((lineStrokeWidth + linesSpace) *i)+midStartSize)
            val right = centerX + (((lineStrokeWidth + linesSpace) *i)+midStartSize)
            val bot = centerY + (((lineStrokeWidth + linesSpace) *i)+midStartSize)
            var oval = RectF(left,top, right,bot)

            for(j in 0..(sectionsCount-1)){
                drawLinePaint.color = unfilledColors!![j]
                val startDegree = (j*sectionDegree)
                val endDegree = sectionDegree
                canvas?.drawArc(oval, startDegree,endDegree,false, drawLinePaint)
            }
        }


        //Draw filled arc lines
        for(i in 1..linesCount){
            val left = centerX - (((lineStrokeWidth + linesSpace) *i)+midStartSize)
            val top = centerY - (((lineStrokeWidth + linesSpace) *i)+midStartSize)
            val right = centerX + (((lineStrokeWidth + linesSpace) *i)+midStartSize)
            val bot = centerY + (((lineStrokeWidth + linesSpace) *i)+midStartSize)
            var oval = RectF(left,top, right,bot)

            for(j in 0..(sectionsCount-1)){
                if(fills[j]>i)continue
                drawLinePaint.color = filledColors!![j]

                val startDegree = (j*sectionDegree)
                val endDegree = sectionDegree
                canvas?.drawArc(oval, startDegree,endDegree,false, drawLinePaint)
            }
        }



        //Draw Sections space
        var radius = ((linesSpace + lineStrokeWidth)*(linesCount))*2
        for(j in 0..(sectionsCount-1)){
            var degree = (j*sectionDegree).toDouble()

            var endX = Math.cos(Math.toRadians(degree)).toFloat()
            var endY = Math.sin(Math.toRadians(degree)).toFloat()


            canvas?.drawLine(centerX,centerY,centerX + (endX*radius),centerY + (endY*radius),clearPaint)
        }



        //Draw icons
        radius = ((linesSpace + lineStrokeWidth)*(linesCount)) + midStartSize + (iconSize * 2)
        for(j in 0..(sectionsCount-1)){
            var degree = (j*(sectionDegree)).toDouble()
            degree += (sectionDegree/2)

            var endX = Math.cos(Math.toRadians(degree)).toFloat()
            endX *= radius
            endX += centerX

            var endY = Math.sin(Math.toRadians(degree)).toFloat()
            endY *= radius
            endY += centerY


            var src = Rect(0,0,iconBmp!!.width,iconBmp!!.height)
            var dsdt = Rect((endX- iconSize).toInt(), (endY- iconSize).toInt(), (endX+ iconSize).toInt(), (endY+ iconSize).toInt())
            canvas?.drawBitmap(iconBmp,src,dsdt,Paint())
        }

    }

    fun changeFills(section : Int,fillLines : Int){
        fills.set(section,fillLines)
        invalidate()
    }
}