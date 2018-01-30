package com.neo.arcchartviewdemo

import android.animation.ValueAnimator
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.*
import com.neo.arcchartview.ArcChartView
import com.neo.arcchartview.DpHandler


class MainActivity : AppCompatActivity() ,SeekBar.OnSeekBarChangeListener{
    lateinit var myArcChartView : ArcChartView
    lateinit var sbAttrsValue: SeekBar
    lateinit var spActions : Spinner
    lateinit var tvValue : TextView
    lateinit var tvSectionsValue : TextView

    lateinit var spSection : Spinner
    lateinit var sbSectionsValue: SeekBar

    lateinit var swAnimate : Switch


    var actionsList : MutableList<String> = mutableListOf()
    var sectionsList : MutableList<String> = mutableListOf()

    var isAnimating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myArcChartView = findViewById(R.id.arc_chart_view)
        sbAttrsValue = findViewById(R.id.sb_attrsValue)
        spActions = findViewById(R.id.sp_actions)
        tvValue = findViewById(R.id.tv_value)
        tvSectionsValue = findViewById(R.id.tv_sectionsValue)

        spSection = findViewById(R.id.sp_sections)
        sbSectionsValue = findViewById(R.id.sb_sectionsValue)

        swAnimate = findViewById(R.id.sw_anim)
        swAnimate.setOnCheckedChangeListener { buttonView, isChecked ->
            isAnimating = isChecked
        }


        initSpinner()
        refreshSpinnerSections()


        //Change filled/unFilled colors
//        myArcChartView.setFilldeColor(0, Color.BLACK)
//        myArcChartView.setUnFilldeColor(0,Color.LTGRAY)
//        myArcChartView.setFilldeColor(1, Color.BLACK)
//        myArcChartView.setUnFilldeColor(1,Color.LTGRAY)


        //Change Icons
        myArcChartView.setSectionIcons(mutableListOf(
                BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.ic1),
                BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.ic2),
                BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.ic3),
                BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.ic4),
                BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.ic5),
                BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.ic6),
                BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.ic7),
                BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.ic8)
        ))

        //IconsClickListener
        myArcChartView.listener = object : ArcChartView.AcvListener {
            override fun onSectionsIconClicked(sectionPos: Int) {
                Toast.makeText(applicationContext, sectionPos.toString(),Toast.LENGTH_SHORT).show()
            }

            override fun onStartSettingSectionValue(sectionPos: Int, sectionValue: Int) {
                tvSectionsValue.setText("onStartSettingSectionValue $sectionPos $sectionValue")
            }

            override fun onContinueSettingSectionValue(sectionPos: Int, sectionValue: Int) {
                tvSectionsValue.setText("onContinueSettingSectionValue $sectionPos $sectionValue")
            }

            override fun onFinishedSettingSectionValue(sectionPos: Int, sectionValue: Int) {
                tvSectionsValue.setText("onFinishedSettingSectionValue $sectionPos $sectionValue")
            }
        }


        //Rotate Animation using startDegreeOffset
        val anim = ValueAnimator.ofFloat(0f,360f).apply {
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            interpolator = OvershootInterpolator()
            duration = 3000
        }
        anim.addUpdateListener {
            if(isAnimating)
                myArcChartView.startDegreeOffset =
                        it.animatedValue as Float
        }
        anim.start()


    }

    private fun initSpinner() {
        actionsList.add("acv_lines_count")
        actionsList.add("acv_lines_space")
        actionsList.add("acv_lines_width")
        actionsList.add("acv_sections_count")
        actionsList.add("acv_sections_space")
        actionsList.add("acv_mid_start_extra_offset")
        actionsList.add("acv_icon_size")
        actionsList.add("acv_icon_margin")

        val spinnerArrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                actionsList)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item)
        spActions.adapter = spinnerArrayAdapter


        spActions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                refreshSelected(position)
            }

        }

        spActions.setSelection(0)
        refreshValueText(myArcChartView.linesCount)
    }

    private fun refreshSelected(position : Int) {
        sbAttrsValue.setOnSeekBarChangeListener(null)
        when(actionsList[position]){
            "acv_lines_count" -> {
                sbAttrsValue.max = 20
                sbAttrsValue.progress = myArcChartView.linesCount
                refreshValueText(sbAttrsValue.progress)
            }
            "acv_lines_space" -> {
                sbAttrsValue.max = 20
                sbAttrsValue.progress = DpHandler.pxToDp(this@MainActivity, myArcChartView.linesSpace.toInt())
                refreshValueText(sbAttrsValue.progress)
            }
            "acv_lines_width" -> {
                sbAttrsValue.max = 80
                sbAttrsValue.progress = DpHandler.pxToDp(this@MainActivity, myArcChartView.linesWidth.toInt())
                refreshValueText(sbAttrsValue.progress)
            }
            "acv_sections_count" -> {
                sbAttrsValue.max = 25
                sbAttrsValue.progress = myArcChartView.sectionsCount
                refreshValueText(sbAttrsValue.progress)
            }
            "acv_sections_space" -> {
                sbAttrsValue.max = 40
                sbAttrsValue.progress = DpHandler.pxToDp(this@MainActivity, myArcChartView.sectionsSpace.toInt())
                refreshValueText(sbAttrsValue.progress)
            }
            "acv_mid_start_extra_offset" -> {
                sbAttrsValue.max = 100
                sbAttrsValue.progress = DpHandler.pxToDp(this@MainActivity, myArcChartView.midStartExtraOffset.toInt())
                refreshValueText(sbAttrsValue.progress)
            }
            "acv_icon_size" -> {
                sbAttrsValue.max = 64
                sbAttrsValue.progress = DpHandler.pxToDp(this@MainActivity, myArcChartView.iconSize.toInt())
                refreshValueText(sbAttrsValue.progress)
            }
            "acv_icon_margin" -> {
                sbAttrsValue.max = 64
                sbAttrsValue.progress = DpHandler.pxToDp(this@MainActivity, myArcChartView.iconMargin.toInt())
                refreshValueText(sbAttrsValue.progress)
            }
        }
        sbAttrsValue.setOnSeekBarChangeListener(this)
    }

    private fun refreshSpinnerSections() {
        sectionsList.clear()
        for(i in 1..myArcChartView.sectionsCount)
            sectionsList.add("Section $i")

        val spinnerArrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                sectionsList)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item)
        spSection.adapter = spinnerArrayAdapter


        spSection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                refreshSeekbarValueSections()
            }

        }

        spSection.setSelection(0)
    }

    private fun refreshSeekbarValueSections() {
        sbSectionsValue.max = myArcChartView.linesCount
        sbSectionsValue.progress = myArcChartView.getSectionValue(spSection.selectedItemPosition)
        sbSectionsValue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                myArcChartView.setSectionValue(spSection.selectedItemPosition,progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        refreshValueText(progress)
        refreshProgress(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    private fun refreshValueText(progress: Int) {
        when(actionsList[spActions.selectedItemPosition]){
            "acv_lines_count" -> {
                tvValue.text = "$progress"
            }
            "acv_lines_space" -> {
                tvValue.text = "$progress dp"
            }
            "acv_lines_width" -> {
                tvValue.text = "$progress dp"
            }
            "acv_sections_count" -> {
                tvValue.text = "$progress"
            }
            "acv_sections_space" -> {
                tvValue.text = "$progress dp"
            }
            "acv_mid_start_extra_offset" -> {
                tvValue.text = "$progress dp"
            }
            "acv_icon_size" -> {
                tvValue.text = "$progress dp"
            }
            "acv_icon_margin" -> {
                tvValue.text = "$progress dp"
            }
        }
    }

    private fun refreshProgress(progress: Int) {
        when(actionsList[spActions.selectedItemPosition]){
            "acv_lines_count" -> {
                myArcChartView.linesCount = progress
                refreshSeekbarValueSections()
            }
            "acv_lines_space" -> {
                myArcChartView.linesSpace = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
            "acv_lines_width" -> {
                myArcChartView.linesWidth = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
            "acv_sections_count" -> {
                myArcChartView.sectionsCount = progress
                refreshSpinnerSections()
            }
            "acv_sections_space" -> {
                myArcChartView.sectionsSpace = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
            "acv_mid_start_extra_offset" -> {
                myArcChartView.midStartExtraOffset = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
            "acv_icon_size" -> {
                myArcChartView.iconSize = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
            "acv_icon_margin" -> {
                myArcChartView.iconMargin = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
        }
    }
}
