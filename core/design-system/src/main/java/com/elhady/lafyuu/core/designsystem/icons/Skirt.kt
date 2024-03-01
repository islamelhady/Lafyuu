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

val Skirt: ImageVector
    get() {
        if (_Skirt != null) {
            return _Skirt!!
        }
        _Skirt = ImageVector.Builder(
            name = "Skirt",
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
                moveTo(19.778f, 4f)
                horizontalLineTo(4.222f)
                verticalLineTo(7.273f)
                lineTo(2f, 17.837f)
                curveTo(2f, 17.837f, 4.5f, 20f, 12f, 20f)
                curveTo(19.5f, 20f, 22f, 17.837f, 22f, 17.837f)
                lineTo(19.778f, 7.459f)
                verticalLineTo(4f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 7f)
                horizontalLineTo(20f)
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(16f, 14f)
                lineTo(15f, 8f)
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(8f, 14f)
                lineTo(9f, 8f)
            }
        }.build()

        return _Skirt!!
    }

@Suppress("ObjectPropertyName")
private var _Skirt: ImageVector? = null

@Preview
@Composable
fun SkirtPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Skirt,
            contentDescription = null
        )
    }
}