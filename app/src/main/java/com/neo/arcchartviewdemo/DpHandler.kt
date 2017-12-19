package com.neo.arcchartview

import android.content.Context
import android.util.DisplayMetrics

/**
 * Created by iman.
 * iman.neofight@gmail.com
 */
class DpHandler {
    companion object {

        fun dpToPx(ctx: Context, dp: Int): Int {
            val displayMetrics = ctx.resources.displayMetrics
            return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
        }

        fun pxToDp(ctx: Context, px: Int): Int {
            val displayMetrics = ctx.resources.displayMetrics
            return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
        }

    }
}