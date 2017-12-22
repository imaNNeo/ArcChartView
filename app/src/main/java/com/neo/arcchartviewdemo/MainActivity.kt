package com.neo.arcchartviewdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.neo.arcchartview.ArcChartView
import com.neo.arcchartview.DpHandler


class MainActivity : AppCompatActivity() ,SeekBar.OnSeekBarChangeListener{
    lateinit var myArcChartView : ArcChartView
    lateinit var mySeekBar : SeekBar
    lateinit var spActions : Spinner
    lateinit var tvValue : TextView


    var actionsList : MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myArcChartView = findViewById(R.id.arc_chart_view)
        mySeekBar = findViewById(R.id.seek_bar)
        spActions = findViewById(R.id.sp_actions)
        tvValue = findViewById(R.id.tv_value)

        initSpinner()
    }

    private fun initSpinner() {
        actionsList.add("acv_lines_count")
        actionsList.add("acv_lines_space")
        actionsList.add("acv_lines_width")
        actionsList.add("acv_sections_count")
        actionsList.add("acv_sections_space")
        actionsList.add("acv_mid_start_extra_offset")
        actionsList.add("acv_icon_size")

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
        mySeekBar.setOnSeekBarChangeListener(null)
        when(actionsList[position]){
            "acv_lines_count" -> {
                mySeekBar.max = 20
                mySeekBar.progress = myArcChartView.linesCount
                refreshValueText(mySeekBar.progress)
            }
            "acv_lines_space" -> {
                mySeekBar.max = 20
                mySeekBar.progress = DpHandler.pxToDp(this@MainActivity, myArcChartView.linesSpace.toInt())
                refreshValueText(mySeekBar.progress)
            }
            "acv_lines_width" -> {
                mySeekBar.max = 80
                mySeekBar.progress = DpHandler.pxToDp(this@MainActivity, myArcChartView.linesWidth.toInt())
                refreshValueText(mySeekBar.progress)
            }
            "acv_sections_count" -> {
                mySeekBar.max = 25
                mySeekBar.progress = myArcChartView.sectionsCount
                refreshValueText(mySeekBar.progress)
            }
            "acv_sections_space" -> {
                mySeekBar.max = 40
                mySeekBar.progress = DpHandler.pxToDp(this@MainActivity, myArcChartView.sectionsSpace.toInt())
                refreshValueText(mySeekBar.progress)
            }
            "acv_mid_start_extra_offset" -> {
                mySeekBar.max = 100
                mySeekBar.progress = DpHandler.pxToDp(this@MainActivity, myArcChartView.midStartExtraOffset.toInt())
                refreshValueText(mySeekBar.progress)
            }
            "acv_icon_size" -> {
                mySeekBar.max = 64
                mySeekBar.progress = DpHandler.pxToDp(this@MainActivity, myArcChartView.iconSize.toInt())
                refreshValueText(mySeekBar.progress)
            }
        }
        mySeekBar.setOnSeekBarChangeListener(this)
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
        }
    }

    private fun refreshProgress(progress: Int) {
        when(actionsList[spActions.selectedItemPosition]){
            "acv_lines_count" -> {
                myArcChartView.linesCount = progress
            }
            "acv_lines_space" -> {
                myArcChartView.linesSpace = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
            "acv_lines_width" -> {
                myArcChartView.linesWidth = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
            "acv_sections_count" -> {
                myArcChartView.sectionsCount = progress
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
        }
    }
}
