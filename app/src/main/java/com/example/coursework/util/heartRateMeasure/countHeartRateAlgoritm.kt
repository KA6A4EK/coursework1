package com.example.coursework.util.heartRateMeasure

data class DataPoint(val x: Float, var y: Float)


fun heartRateAnalysis(list: List<DataPoint>): Int {
    var heartRate = 0
    var minTimeUp = 0f
    var maxTimeUp = 0f
    var minTimeDown = 0f
    var maxTimeDown = 0f
    var increases = false
    for (i in 1..list.size - 2) {
        if (increases && list[i - 1].y > list[i].y && list[i].y > list[i + 1].y) {
            increases = false
            heartRate++
            if (minTimeUp==0f){
                minTimeUp= list[i-1].x
            }
            maxTimeUp = list[i-1].x
        }
        if (!increases && list[i - 1].y < list[i].y && list[i].y < list[i + 1].y) {
            increases = true
            heartRate++
            if (minTimeDown==0f){
                minTimeDown= list[i-1].x
            }
            maxTimeDown = list[i-1].x
        }
    }
    return heartRate * (60000 / ((maxTimeDown+maxTimeUp-minTimeDown-minTimeUp)/2)).toInt() / 2
}
