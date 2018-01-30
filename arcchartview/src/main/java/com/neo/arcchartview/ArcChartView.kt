package com.neo.arcchartview
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by iman.
 * iman.neofight@gmail.com
 */
class ArcChartView @JvmOverloads constructor(mContext : Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        View(mContext,attrs, defStyleAttr) {


    private var tmpBitmap : Bitmap? = null
    private var isResized = false
    private var drawingCanvas : Canvas? = null

    private var drawLinePaint: Paint
    private var clearPaint : Paint



    var linesCount : Int = 0
        set(value) {
            field = value
            initRefreshCountRelateds()
            requestLayout()
        }

    var linesWidth: Float = 0f
        set(value) {
            field = value
            refreshLinesWidthRelateds()
            requestLayout()
        }

    var linesSpace : Float = 0f
        set(value) {
            field = value
            requestLayout()
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
            requestLayout()
        }

    var iconSize: Float = 0f
        set(value) {
            field = value
            requestLayout()
        }

    var iconMargin: Float = 0f
        set(value) {
            field = value
            requestLayout()
        }

    var startDegreeOffset: Float = 20f
        set(value) {
            field = value
            invalidate()
        }

    var allowSettingValueByTouch = true

    var listener: AcvListener? = null





    private var sectionDegree : Float = 0.0f

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

        midStartExtraOffset = DpHandler.dpToPx(mContext,28).toFloat()

        iconSize  = DpHandler.dpToPx(mContext,32).toFloat()
        iconMargin = DpHandler.dpToPx(mContext,6).toFloat()

        startDegreeOffset = 0f

        allowSettingValueByTouch = true;

        if(attrs!=null){
            val a = mContext.obtainStyledAttributes(attrs,R.styleable.ArcChartView)

            linesCount = a.getInt(R.styleable.ArcChartView_acv_lines_count,linesCount)
            linesSpace = a.getDimension(R.styleable.ArcChartView_acv_lines_space,linesSpace)
            linesWidth = a.getDimension(R.styleable.ArcChartView_acv_lines_width, linesWidth)

            sectionsCount = a.getInt(R.styleable.ArcChartView_acv_sections_count,sectionsCount)
            sectionsSpace = a.getDimension(R.styleable.ArcChartView_acv_sections_space,sectionsSpace)

            midStartExtraOffset = a.getDimension(R.styleable.ArcChartView_acv_mid_start_extra_offset, midStartExtraOffset)

            iconSize = a.getDimension(R.styleable.ArcChartView_acv_icon_size,iconSize)
            iconMargin = a.getDimension(R.styleable.ArcChartView_acv_icon_margin,iconMargin)

            startDegreeOffset = a.getFloat(R.styleable.ArcChartView_acv_start_degree_offset,startDegreeOffset)

            allowSettingValueByTouch = a.getBoolean(R.styleable.ArcChartView_acv_allow_setting_value_by_touch,allowSettingValueByTouch)

            a.recycle()
        }

        initRefreshCountRelateds()

        drawLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
        }
        refreshLinesWidthRelateds()


        clearPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL_AND_STROKE
            color = Color.TRANSPARENT
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        }
        refreshSectionsSpaceRelateds()




        sectionIcons.clear()
        sectionIcons.add(0,BitmapFactory.decodeResource(context.resources,R.drawable.ic_star))
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

        val widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec)



        val mWidth = when(widthMeasureMode){
            MeasureSpec.EXACTLY->widthMeasureSize
            MeasureSpec.AT_MOST->Math.min(widthMeasureSize,calculateDesireWidth())
            MeasureSpec.UNSPECIFIED->calculateDesireWidth()
            else -> calculateDesireWidth()
        }


        val mHeight = when(heightMeasureMode){
            MeasureSpec.EXACTLY->heightMeasureSize
            MeasureSpec.AT_MOST->Math.min(heightMeasureSize,calculateDesireHeight())
            MeasureSpec.UNSPECIFIED->calculateDesireHeight()
            else -> calculateDesireHeight()
        }



        setMeasuredDimension(mWidth,mHeight)
    }

    override fun requestLayout() {
        isResized = true
        super.requestLayout()
    }

    private fun calculateDesireWidth() : Int {
        //Whole Chart Space (linesWidth + linesSpace + midExtraSize)
        val chartSpace = ((((linesWidth + linesSpace) * linesCount) * 2) + (linesWidth * 2)) + midStartExtraOffset

        //Icons Space (width + margin)
        val iconsSpace = ((iconMargin * 2) + iconSize * 2)

        //Padding Space (left + right)
        val padding = paddingLeft + paddingRight

        return (chartSpace + iconsSpace + padding).toInt()
    }

    private fun calculateDesireHeight() : Int {
        //Whole Chart Space (linesWidth + linesSpace + midExtraSize)
        val chartSpace = ((((linesWidth + linesSpace) * linesCount) * 2) + (linesWidth * 2)) + midStartExtraOffset

        //Icons Space (width + margin)
        val iconsSpace = ((iconMargin * 2) + iconSize * 2)

        //Padding Space (top + bottom)
        val padding = paddingTop + paddingBottom

        return (chartSpace + iconsSpace + padding).toInt()
    }


    override fun onDraw(c: Canvas?) {
        super.onDraw(c)

        if(tmpBitmap==null || isResized) {
            tmpBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            tmpBitmap?.eraseColor(Color.TRANSPARENT)
            isResized=false
            drawingCanvas = Canvas(tmpBitmap)
        }


        drawingCanvas?.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR)

        val centerX = (width/2).toFloat()
        val centerY = (height/2).toFloat()

        //Draw unfilled arc lines
        for(i in 1..linesCount){
            val left = centerX - ((((linesWidth + linesSpace) * (i-1))+(linesWidth/2))+ (midStartExtraOffset/2))
            val top = centerY - ((((linesWidth + linesSpace) * (i-1))+(linesWidth/2))+ (midStartExtraOffset/2))
            val right = centerX + ((((linesWidth + linesSpace) * (i-1))+(linesWidth/2))+ (midStartExtraOffset/2))
            val bot = centerY + ((((linesWidth + linesSpace) * (i-1))+(linesWidth/2))+ (midStartExtraOffset/2))

            for(j in 0..(sectionsCount-1)){
                drawLinePaint.color = filledColors[j]
                val startDegree = startDegreeOffset + (j*sectionDegree)
                val sweepAngle = sectionDegree

                tempRectf.set(left,top, right,bot)
                drawingCanvas?.drawArc(tempRectf, startDegree,sweepAngle,false, drawLinePaint)
            }

        }


        //Draw filled arc lines
        for(i in 1..linesCount){
            val left = centerX - ((((linesWidth + linesSpace) * (i-1))+(linesWidth/2))+ (midStartExtraOffset/2))
            val top = centerY - ((((linesWidth + linesSpace) * (i-1))+(linesWidth/2))+ (midStartExtraOffset/2))
            val right = centerX + ((((linesWidth + linesSpace) * (i-1))+(linesWidth/2))+ (midStartExtraOffset/2))
            val bot = centerY + ((((linesWidth + linesSpace) * (i-1))+(linesWidth/2))+ (midStartExtraOffset/2))

            for(j in 0..(sectionsCount-1)){
                if(sectionsValue[j]>i-1)continue
                drawLinePaint.color = unfilledColors!![j]
                val startDegree = startDegreeOffset + (j*sectionDegree)
                val sweepAngle = sectionDegree

                tempRectf.set(left,top, right,bot)
                drawingCanvas?.drawArc(tempRectf, startDegree,sweepAngle,false, drawLinePaint)
            }
        }



        //Draw Sections space (Clear)
        var radius = Math.sqrt(Math.pow(width.toDouble(), 2.0) + Math.pow(height.toDouble(), 2.0)).toFloat()
        for(j in 0..(sectionsCount-1)){
            var degree = (startDegreeOffset + (j*sectionDegree)).toDouble()

            var endX = Math.cos(Math.toRadians(degree)).toFloat()
            var endY = Math.sin(Math.toRadians(degree)).toFloat()

            drawingCanvas?.drawLine(centerX,centerY,centerX + (endX*radius),centerY + (endY*radius),clearPaint)
        }



        //Draw icons
        val iconsRect = getDrawingIconsRect()
        for(j in 0..(iconsRect.size-1)){
            val bmp = (if(sectionIcons.size>0)
                sectionIcons[j%sectionIcons.size]
            else continue) ?: continue

            tmpSrcRect.set(0,0, bmp!!.width, bmp!!.height)
            tmpDstRect.set(iconsRect[j])
            drawingCanvas?.drawBitmap(bmp,tmpSrcRect,tmpDstRect,null)
        }



        c?.drawBitmap(tmpBitmap,0f,0f,null)

    }

    private fun getDrawingIconsRect() : Array<Rect?>{
        val rects = arrayOfNulls<Rect?>(sectionsCount)

        val centerX = width/2
        val centerY = height/2

        val radius = (((linesSpace + linesWidth)*(linesCount) + (linesWidth))) + (midStartExtraOffset/2) + iconMargin + (iconSize / 2)
        for(j in 0..(sectionsCount-1)){
            var degree = (startDegreeOffset + j*(sectionDegree)).toDouble()
            degree += (sectionDegree/2)

            var endX = Math.cos(Math.toRadians(degree)).toFloat()
            endX *= radius
            endX += centerX

            var endY = Math.sin(Math.toRadians(degree)).toFloat()
            endY *= radius
            endY += centerY



            val iconSizeHalf = (iconSize/2).toInt()
            rects[j] = Rect((endX-iconSizeHalf).toInt(), (endY-iconSizeHalf).toInt(),
                    (endX+iconSizeHalf).toInt(), (endY+iconSizeHalf).toInt())
        }

        return  rects
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

    fun setSectionIcons(sectionIcons : MutableList<Bitmap?>){
        this.sectionIcons = sectionIcons
        invalidate()
    }


    var downX = 0f
    var downY = 0f
    var touchingSection : Int = -1
    var touchingSectionValue : Int = -1
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {

                if(allowSettingValueByTouch) {
                    downX = event.x
                    downY = event.y

                    val secLine = handleTouchSectionLine(downX, downY)
                    touchingSection = secLine.first
                    touchingSectionValue = secLine.second

                    setSectionValue(secLine.first, secLine.second)
                    listener?.onStartSettingSectionValue(secLine.first, secLine.second)
                }
            }
            MotionEvent.ACTION_UP -> {
                if(downX==event.x && downY==event.y){
                    //Click happened
                    handleOnClick(event)
                }

                if(touchingSection!=-1 && touchingSectionValue!=-1) {
                    listener?.onFinishedSettingSectionValue(touchingSection,touchingSectionValue)
                    touchingSection = -1
                    touchingSectionValue = -1
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val secLine = handleTouchSectionLine(event.x,event.y)

                if(touchingSection == secLine.first && touchingSection!=-1) {
                    if(touchingSectionValue != secLine.second) {
                        setSectionValue(secLine.first, secLine.second)
                        listener?.onContinueSettingSectionValue(secLine.first, secLine.second)
                    }

                    touchingSectionValue = secLine.second
                }

            }
        }
        return true
    }

    private fun handleTouchSectionLine(touchX: Float, touchY: Float) : Pair<Int,Int> {
        //To move center of screen as 0, 0 point
        var dX = (touchX - (width/2)).toDouble()
        var dY = (touchY - (height/2)).toDouble()

        var r = Math.sqrt(dX*dX + dY*dY)
        var theta = Math.toDegrees(Math.atan2(dY, dX))

        if(theta<0)
            theta = 180 + (180 - Math.abs(theta))


        var foundSection : Int = -1
        for(i in 0..(sectionsCount-1)){
            //StartDegree of current checking section
            val startDegree = startDegreeOffset + (i*sectionDegree)
            if(theta >= startDegree && theta <= startDegree + sectionDegree){
                //Section is Found!!
                foundSection = i
                break
            }
        }


        var foundLine : Int = -1
        for(i in 1..linesCount){
            //Current checking line distance
            val distance = midStartExtraOffset + ((i * linesWidth)+((i-1) * linesSpace))

            if(r <= midStartExtraOffset + linesSpace){
                foundLine = 0
                break
            }

            if(r <= distance){
                //Line is Found!!
                foundLine = i
                break
            }
        }

        if(foundLine==-1)foundLine = linesCount

        return Pair(foundSection,foundLine)
    }

    private fun handleOnClick(event: MotionEvent) {
        val iconsRect = getDrawingIconsRect()

        for(j in 0..(iconsRect.size-1)){
            val r = iconsRect[j] ?: break

            if(r.contains(event.x.toInt(), event.y.toInt())){
                //icon in Section j clicked
                listener?.onSectionsIconClicked(j)
            }

        }
    }

    interface AcvListener {
        fun onSectionsIconClicked(sectionPos : Int){}
        fun onStartSettingSectionValue(sectionPos : Int, sectionValue : Int){}
        fun onContinueSettingSectionValue(sectionPos : Int, sectionValue : Int){}
        fun onFinishedSettingSectionValue(sectionPos : Int, sectionValue : Int){}
    }
}