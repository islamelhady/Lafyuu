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

val Dress: ImageVector
    get() {
        if (_Dress != null) {
            return _Dress!!
        }
        _Dress = ImageVector.Builder(
            name = "Dress",
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
                moveTo(14.25f, 6.375f)
                lineTo(15.375f, 9.75f)
                lineTo(14.25f, 13.125f)
                lineTo(21f, 19.875f)
                curveTo(21f, 19.875f, 18.75f, 21f, 12f, 21f)
                curveTo(5.25f, 21f, 3f, 19.875f, 3f, 19.875f)
                lineTo(9.75f, 13.125f)
                lineTo(8.625f, 9.75f)
                lineTo(9.75f, 6.375f)
                horizontalLineTo(14.25f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(13.125f, 6.375f)
                horizontalLineTo(16.5f)
                lineTo(17.625f, 3f)
                horizontalLineTo(14.25f)
                lineTo(13.125f, 6.375f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6.375f, 3f)
                horizontalLineTo(9.75f)
                lineTo(10.875f, 6.375f)
                horizontalLineTo(7.5f)
                lineTo(6.375f, 3f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(9.75f, 13.125f)
                horizontalLineTo(14.25f)
            }
        }.build()

        return _Dress!!
    }

@Suppress("ObjectPropertyName")
private var _Dress: ImageVector? = null

@Preview
@Composable
fun DressPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Dress,
            contentDescription = null
        )
    }
}