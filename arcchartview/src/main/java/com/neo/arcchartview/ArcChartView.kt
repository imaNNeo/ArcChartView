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
class ArcChartView @JvmOverloads constructor(mContext : Context, val attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        View(mContext,attrs, defStyleAttr) {


    private var drawLinePaint: Paint
    private var bgPaint : Paint
    private var clearPaint : Paint



    var linesCount : Int = 0
        set(value) {
            field = value
            initRefreshCountRelateds()
            invalidate()
        }

    var linesWidth: Float = 0f
        set(value) {
            field = value
            refreshLinesWidthRelateds()
            invalidate()
        }

    var linesSpace : Float = 0f
        set(value) {
            field = value
            invalidate()
        }



    var sectionsCount : Int = 0
        set(value) {
            field = value
            initRefreshCountRelateds()
            invalidate()
        }

    var sectionsSpace: Float = 0f
        set(value) {
            field = value
            refreshSectionsSpaceRelateds()
            invalidate()
        }

    var midStartExtraOffset: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    var iconSize: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    var bgColor : Int = 0
        set(value) {
            field = value
            invalidate()
        }




    private var sectionDegree : Float = 0.0f
    private var mWidth : Int = 0
    private var mHeight : Int = 0

    private var sectionIcons : MutableList<Bitmap?> = mutableListOf()
    private var sectionsValue : MutableList<Int> = mutableListOf()
    private var filledColors: MutableList<Int> = mutableListOf()
    private var unfilledColors: MutableList<Int> = mutableListOf()


    private var tempRectf : RectF = RectF(0f,0f,0f,0f)
    private var tmpSrcRect : Rect = Rect(0,0,0,0)
    private var tmpDstRect : Rect = Rect(0,0,0,0)

    init {
        linesCount = 10
        linesSpace = DpHandler.dpToPx(mContext,4).toFloat()
        linesWidth = DpHandler.dpToPx(mContext,6).toFloat()

        sectionsCount = 8
        sectionsSpace = DpHandler.dpToPx(mContext,4).toFloat()

        midStartExtraOffset = DpHandler.dpToPx(mContext,16).toFloat()

        iconSize  = DpHandler.dpToPx(mContext,32).toFloat()

        bgColor = Color.WHITE

        if(attrs!=null){
            val a = mContext.obtainStyledAttributes(attrs,R.styleable.ArcChartView)

            linesCount = a.getInt(R.styleable.ArcChartView_acv_lines_count,linesCount)
            linesSpace = a.getDimension(R.styleable.ArcChartView_acv_lines_space,linesSpace)
            linesWidth = a.getDimension(R.styleable.ArcChartView_acv_lines_width, linesWidth)

            sectionsCount = a.getInt(R.styleable.ArcChartView_acv_sections_count,sectionsCount)
            sectionsSpace = a.getDimension(R.styleable.ArcChartView_acv_sections_space,sectionsSpace)

            midStartExtraOffset = a.getDimension(R.styleable.ArcChartView_acv_mid_start_extra_offset, midStartExtraOffset)

            iconSize = a.getDimension(R.styleable.ArcChartView_acv_icon_size,iconSize)

            bgColor = a.getColor(R.styleable.ArcChartView_acv_bg_color,Color.WHITE)

            a.recycle()
        }

        initRefreshCountRelateds()

        drawLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
        }
        refreshLinesWidthRelateds()


        bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = bgColor
        }


        clearPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = bgColor
            style = Paint.Style.FILL_AND_STROKE
        }
        refreshSectionsSpaceRelateds()
    }

    private fun initRefreshCountRelateds() {
        if(sectionsCount<1)sectionsCount=1
        sectionDegree = (360/sectionsCount).toFloat()

        var value = 0
        sectionsValue.clear()
        for(i in 0 until sectionsCount) {
            if(value>=linesCount)value=0
            sectionsValue.add(i, ++value)
        }

        filledColors.clear()
        for(i in 0 until sectionsCount) {
            val color = when(i%8){
                0 -> color(R.color.filled_section_1)
                1 -> color(R.color.filled_section_2)
                2 -> color(R.color.filled_section_3)
                3 -> color(R.color.filled_section_4)
                4 -> color(R.color.filled_section_5)
                5 -> color(R.color.filled_section_6)
                6 -> color(R.color.filled_section_7)
                7 -> color(R.color.filled_section_8)
                else -> Color.BLACK
            }
            filledColors.add(i,color)
        }


        unfilledColors.clear()
        for(i in 0 until sectionsCount) {
            val color = when(i%8){
                0 -> color(R.color.unfilled_section_1)
                1 -> color(R.color.unfilled_section_2)
                2 -> color(R.color.unfilled_section_3)
                3 -> color(R.color.unfilled_section_4)
                4 -> color(R.color.unfilled_section_5)
                5 -> color(R.color.unfilled_section_6)
                6 -> color(R.color.unfilled_section_7)
                7 -> color(R.color.unfilled_section_8)
                else -> Color.BLACK
            }
            unfilledColors.add(i,color)
        }



        sectionIcons.clear()
        for(i in 0 until sectionsCount) {
            sectionIcons.add(i,BitmapFactory.decodeResource(context.resources,R.drawable.ic_star))
        }
    }
    private fun refreshLinesWidthRelateds() {
        drawLinePaint?.let {
            drawLinePaint.strokeWidth = linesWidth
        }
    }
    private fun refreshSectionsSpaceRelateds() {
        clearPaint?.let {
            clearPaint.strokeWidth = sectionsSpace
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
        canvas?.drawRect(0f,0f, mWidth.toFloat(), mHeight.toFloat(),bgPaint)


        //Draw unfilled arc lines
        for(i in 1..linesCount){
            val left = centerX - (((linesWidth + linesSpace) *i)+ midStartExtraOffset)
            val top = centerY - (((linesWidth + linesSpace) *i)+ midStartExtraOffset)
            val right = centerX + (((linesWidth + linesSpace) *i)+ midStartExtraOffset)
            val bot = centerY + (((linesWidth + linesSpace) *i)+ midStartExtraOffset)

            for(j in 0..(sectionsCount-1)){
                drawLinePaint.color = filledColors[j]
                val startDegree = (j*sectionDegree)
                val endDegree = sectionDegree

                tempRectf.set(left,top, right,bot)
                canvas?.drawArc(tempRectf, startDegree,endDegree,false, drawLinePaint)
            }
        }


        //Draw filled arc lines
        for(i in 1..linesCount){
            val left = centerX - (((linesWidth + linesSpace) *i)+ midStartExtraOffset)
            val top = centerY - (((linesWidth + linesSpace) *i)+ midStartExtraOffset)
            val right = centerX + (((linesWidth + linesSpace) *i)+ midStartExtraOffset)
            val bot = centerY + (((linesWidth + linesSpace) *i)+ midStartExtraOffset)

            for(j in 0..(sectionsCount-1)){
                if(sectionsValue[j]>i-1)continue
                drawLinePaint.color = unfilledColors!![j]
                val startDegree = (j*sectionDegree)
                val endDegree = sectionDegree

                tempRectf.set(left,top, right,bot)
                canvas?.drawArc(tempRectf, startDegree,endDegree,false, drawLinePaint)
            }
        }



        //Draw Sections space
        var radius = ((linesSpace + linesWidth)*(linesCount))*2
        for(j in 0..(sectionsCount-1)){
            var degree = (j*sectionDegree).toDouble()

            var endX = Math.cos(Math.toRadians(degree)).toFloat()
            var endY = Math.sin(Math.toRadians(degree)).toFloat()

            canvas?.drawLine(centerX,centerY,centerX + (endX*radius),centerY + (endY*radius),clearPaint)
        }



        //Draw icons
        radius = ((linesSpace + linesWidth)*(linesCount+1)) + midStartExtraOffset + (iconSize / 2)
        for(j in 0..(sectionsCount-1)){
            val bmp = sectionIcons[j] ?: continue

            var degree = (j*(sectionDegree)).toDouble()
            degree += (sectionDegree/2)

            var endX = Math.cos(Math.toRadians(degree)).toFloat()
            endX *= radius
            endX += centerX

            var endY = Math.sin(Math.toRadians(degree)).toFloat()
            endY *= radius
            endY += centerY


            val iconSizeHalf = (iconSize/2).toInt()
            tmpSrcRect.set(0,0, bmp.width, bmp.height)
            tmpDstRect.set((endX-iconSizeHalf).toInt(), (endY-iconSizeHalf).toInt(),
                    (endX+iconSizeHalf).toInt(), (endY+iconSizeHalf).toInt())
            canvas?.drawBitmap(bmp,tmpSrcRect,tmpDstRect,bgPaint)
        }

    }

    fun getSectionValue(section: Int) : Int = sectionsValue[section]
    fun setSectionValue(section: Int,value: Int){
        if(section<0 || section>(sectionsCount-1))return
        if(value<0 || value>linesCount)return

        sectionsValue[section] = value
        invalidate()
    }

    fun getUnFilledColor(section: Int) : Int{
        if(section<0 || section>(sectionsCount-1))return 0
        return unfilledColors[section]
    }
    fun setUnFilldeColor(section: Int,color : Int){
        if(section<0 || section>(sectionsCount-1))return
        unfilledColors[section] = color
        invalidate()
    }

    fun getFilledColor(section: Int) : Int{
        if(section<0 || section>(sectionsCount-1))return 0
        return filledColors[section]
    }
    fun setFilldeColor(section: Int,color : Int){
        if(section<0 || section>(sectionsCount-1))return
        filledColors[section] = color
        invalidate()
    }

    fun getIcon(section: Int) : Bitmap?{
        if(section<0 || section>(sectionsCount-1))return null
        return sectionIcons[section]
    }
    fun setIcon(section: Int, icn: Bitmap?){
        if(section<0 || section>(sectionsCount-1))return
        sectionIcons[section] = icn
        invalidate()
    }
}