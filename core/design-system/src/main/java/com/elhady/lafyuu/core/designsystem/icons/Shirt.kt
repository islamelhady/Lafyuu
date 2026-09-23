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

val Shirt: ImageVector
    get() {
        if (_Shirt != null) {
            return _Shirt!!
        }
        _Shirt = ImageVector.Builder(
            name = "Shirt",
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
                moveTo(8.625f, 3f)
                horizontalLineTo(15.375f)
                lineTo(12f, 8.625f)
                lineTo(8.625f, 3f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15.375f, 3f)
                lineTo(17.625f, 5.25f)
                lineTo(15.375f, 12f)
                lineTo(12f, 8.625f)
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6.375f, 5.25f)
                lineTo(3f, 6.375f)
                verticalLineTo(21f)
                horizontalLineTo(21f)
                verticalLineTo(6.375f)
                lineTo(17.625f, 5.25f)
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 21f)
                verticalLineTo(8.625f)
                lineTo(8.625f, 12f)
                lineTo(6.375f, 5.25f)
                lineTo(8.625f, 3f)
            }
        }.build()

        return _Shirt!!
    }

@Suppress("ObjectPropertyName")
private var _Shirt: ImageVector? = null

@Preview
@Composable
fun ShirtPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Shirt,
            contentDescription = null
        )
    }
}