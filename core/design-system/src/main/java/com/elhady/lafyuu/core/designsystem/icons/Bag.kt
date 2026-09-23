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

val Bag: ImageVector
    get() {
        if (_Bag != null) {
            return _Bag!!
        }
        _Bag = ImageVector.Builder(
            name = "Bag",
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
                moveTo(21f, 6.177f)
                horizontalLineTo(3f)
                verticalLineTo(21f)
                horizontalLineTo(21f)
                verticalLineTo(6.177f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(21f, 6.177f)
                horizontalLineTo(3f)
                lineTo(5.117f, 3f)
                horizontalLineTo(18.883f)
                lineTo(21f, 6.177f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(14.383f, 10.677f)
                curveTo(14.383f, 11.992f, 13.316f, 13.059f, 12.001f, 13.059f)
                curveTo(10.686f, 13.059f, 9.62f, 11.992f, 9.62f, 10.677f)
            }
        }.build()

        return _Bag!!
    }

@Suppress("ObjectPropertyName")
private var _Bag: ImageVector? = null

@Preview
@Composable
fun BagPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Bag,
            contentDescription = null
        )
    }
}