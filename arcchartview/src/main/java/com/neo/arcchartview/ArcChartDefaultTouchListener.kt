package com.neo.arcchartview

import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * Created by FoxXav on 16/01/18.
 * mail@ FoxXav@gmx.com
 */
class ArcChartDefaultTouchListener @JvmOverloads constructor(chartSelector : ArcChartView) : View.OnTouchListener {
    private var originalStartX:Float=0f
    private var originalStartY:Float=0f
    private var chartSelector:ArcChartView
    private var angle:Double=0.toDouble()
    private var cadranCible: Int = 0
    private var valActuelle: Int = 0

    init {
        this.chartSelector=chartSelector
    }

    override fun onTouch(v: View?, motionEvent: MotionEvent?): Boolean {
        if (motionEvent?.action == MotionEvent.ACTION_DOWN) {
            //On surcharge ici le point de depart du mouvement avec position du curseur de la seekbar
            originalStartX = motionEvent.x
            originalStartY = motionEvent.y

            //Calcul du "cadran" en fonction de la position initiale du "click"/"touch"(ie action_down)
            //Mise à l'echelle trigo => Heigth et width /2 val 1
            val trigoY = (originalStartY - chartSelector.getMeasuredHeight() / 2).toDouble()
            val trigoX = (originalStartX - chartSelector.getMeasuredWidth() / 2).toDouble()
            //angle =  Math.atan2(trigoY, trigoX);//On ajoute PI pour se remettre dans l'intervalle 0, 2PI au lieu de -PI-PI
            angle = Math.atan(trigoX / trigoY) + Math.PI / 2

            if (originalStartY > chartSelector.getMeasuredHeight() / 2) {
                ///!\ Ici angle <0 donc on fait bien + pour faire - ;-)
                angle += Math.PI
            }
            //Reverse angle to be similar to graphic and obtain the right section below. => Not trigonometric but clockwise angle mesure
            cadranCible = Math.floor((Math.PI * 2 - angle) / (2 * Math.PI / chartSelector.sectionsCount)).toInt()
            valActuelle = chartSelector.getSectionValue(cadranCible)
        } else {
            //Calcul du deplacement
            val dist = Math.sqrt(Math.pow((originalStartX - motionEvent?.x!!.toFloat()).toDouble(), 2.0) + Math.pow((originalStartY - motionEvent?.y!!.toFloat()).toDouble(), 2.0))
            val nbMove = Math.floor(dist / (chartSelector.linesWidth + chartSelector.linesSpace / 2)).toInt()

            var left = false
            var right = false
            var up = false
            var down = false

            //Determination du sens du mouvement.
            if (originalStartX > motionEvent.x) {
                //Mvt vers la gauche
                left = true
            } else {
                if (originalStartX < motionEvent.x) {
                    right = true
                }
            }

            if (originalStartY > motionEvent.y) {
                //Mvt vers le haut
                up = true
            } else {
                if (originalStartY < motionEvent.y) {
                    down = true
                }
            }

            var add = false
            if (Math.toDegrees(angle) > 0 && Math.toDegrees(angle) < 90) {//Cadran quart haut/droit
                if (up || right) {
                    add = true
                }
            } else {
                if (Math.toDegrees(angle) > 90 && Math.toDegrees(angle) < 180) {//Cadran quart haut/gauche
                    //On est dans la partie gauche supérieur du cadran
                    if (up || left) {
                        add = true
                    }
                } else {
                    //On est dans la partie droite du cadran
                    if (Math.toDegrees(angle) > 180 && Math.toDegrees(angle) < 270) {//Cadran quart bas/gauche
                        //On est dans la partie gauche inférieur du cadran
                        if (down || left) {
                            add = true
                        }
                    } else { //Cadran quart bas/droit
                        if (right || down) {
                            add = true
                        }
                    }
                }
            }

            var newVal: Int
            if (add) {
                newVal = valActuelle + nbMove
            } else {
                newVal = valActuelle - nbMove
            }
            if (newVal < 0) {
                newVal = 0
            } else {
                if (newVal > chartSelector.linesCount) {
                    newVal = chartSelector.linesCount
                }
            }
            chartSelector.setSectionValue(cadranCible, newVal)
        }

        return true
    }

}