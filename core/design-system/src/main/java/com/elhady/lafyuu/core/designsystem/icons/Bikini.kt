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

val Bikini: ImageVector
    get() {
        if (_Bikini != null) {
            return _Bikini!!
        }
        _Bikini = ImageVector.Builder(
            name = "Bikini",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5f, 16f)
                horizontalLineTo(19f)
                verticalLineTo(18.8f)
                curveTo(16.2f, 18.8f, 13.4f, 20.2f, 13.4f, 23f)
                horizontalLineTo(10.6f)
                curveTo(10.6f, 20.2f, 7.8f, 18.8f, 5f, 18.8f)
                verticalLineTo(16f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5f, 1f)
                verticalLineTo(6.331f)
                curveTo(5f, 8.91f, 7.048f, 11f, 9.575f, 11f)
                horizontalLineTo(14.423f)
                curveTo(16.952f, 11f, 19f, 8.91f, 19f, 6.331f)
                verticalLineTo(1f)
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5f, 5f)
                curveTo(10.6f, 5f, 12f, 11f, 12f, 11f)
                curveTo(12f, 11f, 12f, 5f, 19f, 5f)
            }
        }.build()

        return _Bikini!!
    }

@Suppress("ObjectPropertyName")
private var _Bikini: ImageVector? = null

@Preview
@Composable
fun BikiniPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Bikini,
            contentDescription = null
        )
    }
}