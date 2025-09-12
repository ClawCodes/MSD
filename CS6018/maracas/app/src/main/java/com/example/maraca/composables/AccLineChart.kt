package com.example.maraca.composables

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.example.maraca.data.AccReading

@Composable
fun AccLineChart(
    readings: List<AccReading>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color.Cyan
) {
    if (readings.size < 2) return

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val startTime = readings.first().timestamp.toDouble()
        val times = readings.map { (it.timestamp - startTime) }
        val mags = readings.map { it.magnitude().toDouble() }

        val timeRange = (times.maxOrNull() ?: 0.0).takeIf { it > 0.0 } ?: 1.0
        val minMag = mags.minOrNull() ?: 0.0
        val maxMag = mags.maxOrNull() ?: 1.0
        val magRange = (maxMag - minMag).takeIf { it > 0.0 } ?: 1.0

        val path = Path()
        readings.forEachIndexed { i, r ->
            val x = ((times[i]) / timeRange * width).toFloat()
            val y = (height - ((mags[i] - minMag) / magRange * height)).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = lineColor,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
        )
    }
}