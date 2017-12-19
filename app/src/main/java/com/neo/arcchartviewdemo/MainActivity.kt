package com.neo.arcchartviewdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import com.neo.arcchartview.ArcChartView

class MainActivity : AppCompatActivity() ,SeekBar.OnSeekBarChangeListener{
    lateinit var myArcChartView : ArcChartView
    lateinit var mySeekBar : SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myArcChartView = findViewById(R.id.arc_chart_view)
        mySeekBar = findViewById(R.id.seek_bar)
        mySeekBar.max = 10
        mySeekBar.progress = 2
        mySeekBar.setOnSeekBarChangeListener(this)
    }



    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        myArcChartView.changeFills(4,progress+1)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}
