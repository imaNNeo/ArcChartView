package com.neo.arcchartview

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.neo.arcchartviewdemo.R

/**
 * Created by iman.
 * iman.neofight@gmail.com
 */
class ArcChartView : View {
    private lateinit var mContext : Context
    private lateinit var drawLinePaint: Paint
    private lateinit var bgPaint : Paint
    private lateinit var clearPaint : Paint

    var linesSpace = 0f
    var lineStrokewidth = 0f
    var linesCount : Int = 0
    var sectionDegree = 0f
    var selectionsSpace = 0f
    var bgColor = Color.WHITE
    var sectionsCount : Int = 0
    var midStartSize = 0f

    var fills : MutableList<Int> = mutableListOf()


    var mWidth : Int = 0
    var mHeight : Int = 0

    var filledColors: List<Int>? = null
    var unfilledColors: List<Int>? = null

    var iconBmp : Bitmap? = null
    var iconSize: Float = 0f

    constructor(ctx : Context) : super(ctx) {init(ctx)}
    constructor(ctx : Context,attrs: AttributeSet) : super(ctx,attrs){init(ctx)}
    constructor(ctx : Context,attrs: AttributeSet,defStyleAttr : Int) : super(ctx,attrs,defStyleAttr) {init(ctx)}
    constructor(ctx : Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(ctx,attrs, defStyleAttr,defStyleRes) {init(ctx)}



    private fun init(ctx: Context){
        mContext = ctx


        linesCount = 10
        linesSpace = DpHandler.dpToPx(ctx,4).toFloat()
        lineStrokewidth = DpHandler.dpToPx(ctx,6).toFloat()

        sectionsCount = 8
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

        sectionDegree = (360/sectionsCount).toFloat()
        selectionsSpace = DpHandler.dpToPx(ctx,4).toFloat()


        drawLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        drawLinePaint.style = Paint.Style.STROKE
        drawLinePaint.strokeWidth = lineStrokewidth


        bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bgPaint.color = bgColor


        clearPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        clearPaint.strokeWidth = selectionsSpace
        clearPaint.color = bgColor
        clearPaint.style = Paint.Style.FILL_AND_STROKE


        midStartSize = DpHandler.dpToPx(ctx,16).toFloat()


        iconBmp = BitmapFactory.decodeResource(context.resources,R.drawable.ic_star)
        iconSize = DpHandler.dpToPx(ctx,16).toFloat()
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
            val left = centerX - (((lineStrokewidth + linesSpace) *i)+midStartSize)
            val top = centerY - (((lineStrokewidth + linesSpace) *i)+midStartSize)
            val right = centerX + (((lineStrokewidth + linesSpace) *i)+midStartSize)
            val bot = centerY + (((lineStrokewidth + linesSpace) *i)+midStartSize)
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
            val left = centerX - (((lineStrokewidth + linesSpace) *i)+midStartSize)
            val top = centerY - (((lineStrokewidth + linesSpace) *i)+midStartSize)
            val right = centerX + (((lineStrokewidth + linesSpace) *i)+midStartSize)
            val bot = centerY + (((lineStrokewidth + linesSpace) *i)+midStartSize)
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
        var radius = ((linesSpace + lineStrokewidth)*(linesCount))*2
        for(j in 0..(sectionsCount-1)){
            var degree = (j*sectionDegree).toDouble()

            var endX = Math.cos(Math.toRadians(degree)).toFloat()
            var endY = Math.sin(Math.toRadians(degree)).toFloat()


            canvas?.drawLine(centerX,centerY,centerX + (endX*radius),centerY + (endY*radius),clearPaint)
        }



        //Draw icons
        radius = ((linesSpace + lineStrokewidth)*(linesCount)) + midStartSize + (iconSize * 2)
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