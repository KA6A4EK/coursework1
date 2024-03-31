package com.example.coursework.util.heartRateMeasure

data class DataPoint(val x: Float, var y: Float)


fun heartRateAnalysis(list: List<DataPoint>): Int {
    var heartRate = 0
    var increases = false
    for (i in 1..list.size - 2) {
        if (increases && list[i - 1].y > list[i].y && list[i].y > list[i + 1].y) {
            increases = false
            heartRate++
        }
        if (!increases && list[i - 1].y < list[i].y && list[i].y < list[i + 1].y) {
            increases = true
            heartRate++
        }
    }
    return heartRate * (60000 / (list.last().x - list.first().x)).toInt() / 2
}
//TODO