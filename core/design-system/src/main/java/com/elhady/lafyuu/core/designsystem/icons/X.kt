package com.elhady.lafyuu.core.designsystem.icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val X: ImageVector
    get() {
        if (_X != null) {
            return _X!!
        }
        _X = ImageVector.Builder(
            name = "X",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(16.95f, 7.05f)
                lineTo(7.05f, 16.95f)
            }
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(7.05f, 7.05f)
                lineTo(16.95f, 16.95f)
            }
        }.build()

        return _X!!
    }

@Suppress("ObjectPropertyName")
private var _X: ImageVector? = null

@Preview
@Composable
fun XPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = X,
            contentDescription = null
        )
    }
}