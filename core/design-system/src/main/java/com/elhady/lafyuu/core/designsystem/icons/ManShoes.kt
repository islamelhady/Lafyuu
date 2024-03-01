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

val ManShoes: ImageVector
    get() {
        if (_ManShoes != null) {
            return _ManShoes!!
        }
        _ManShoes = ImageVector.Builder(
            name = "ManShoes",
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
                moveTo(2.7f, 17f)
                horizontalLineTo(23f)
                verticalLineTo(7f)
                curveTo(23f, 7f, 21.625f, 8.429f, 18.875f, 8.429f)
                curveTo(16.125f, 8.429f, 14.75f, 7f, 14.75f, 7f)
                lineTo(1.939f, 13.654f)
                curveTo(1.364f, 13.954f, 1f, 14.566f, 1f, 15.234f)
                curveTo(1f, 16.21f, 1.76f, 17f, 2.7f, 17f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 9f)
                lineTo(14f, 12f)
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(8f, 11f)
                lineTo(9.196f, 12.773f)
            }
        }.build()

        return _ManShoes!!
    }

@Suppress("ObjectPropertyName")
private var _ManShoes: ImageVector? = null

@Preview
@Composable
fun ManShoesPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = ManShoes,
            contentDescription = null
        )
    }
}