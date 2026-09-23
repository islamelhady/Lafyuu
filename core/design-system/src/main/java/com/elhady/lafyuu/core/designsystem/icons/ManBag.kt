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

val ManBag: ImageVector
    get() {
        if (_ManBag != null) {
            return _ManBag!!
        }
        _ManBag = ImageVector.Builder(
            name = "ManBag",
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
                moveTo(21f, 7.5f)
                horizontalLineTo(3f)
                verticalLineTo(21f)
                horizontalLineTo(21f)
                verticalLineTo(7.5f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3f, 12.984f)
                lineTo(12f, 15.375f)
                lineTo(21f, 13.125f)
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(8.295f, 6.468f)
                verticalLineTo(3f)
                horizontalLineTo(16.498f)
                verticalLineTo(6.375f)
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(11.996f, 11.625f)
                horizontalLineTo(12.007f)
            }
        }.build()

        return _ManBag!!
    }

@Suppress("ObjectPropertyName")
private var _ManBag: ImageVector? = null

@Preview
@Composable
fun ManBagPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = ManBag,
            contentDescription = null
        )
    }
}