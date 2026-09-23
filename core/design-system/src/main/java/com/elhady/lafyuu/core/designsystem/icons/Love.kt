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

val Love: ImageVector
    get() {
        if (_Love != null) {
            return _Love!!
        }
        _Love = ImageVector.Builder(
            name = "Love",
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
                moveTo(4.545f, 12.775f)
                lineTo(12f, 20.063f)
                lineTo(19.455f, 12.775f)
                lineTo(19.496f, 12.736f)
                curveTo(21.556f, 10.722f, 21.556f, 7.462f, 19.496f, 5.448f)
                curveTo(17.435f, 3.435f, 14.1f, 3.434f, 12.04f, 5.448f)
                lineTo(12f, 5.488f)
                lineTo(11.959f, 5.447f)
                curveTo(9.898f, 3.433f, 6.564f, 3.433f, 4.504f, 5.447f)
                curveTo(2.444f, 7.462f, 2.443f, 10.722f, 4.504f, 12.735f)
                lineTo(4.545f, 12.775f)
                close()
            }
        }.build()

        return _Love!!
    }

@Suppress("ObjectPropertyName")
private var _Love: ImageVector? = null

@Preview
@Composable
fun LovePreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Love,
            contentDescription = null
        )
    }
}