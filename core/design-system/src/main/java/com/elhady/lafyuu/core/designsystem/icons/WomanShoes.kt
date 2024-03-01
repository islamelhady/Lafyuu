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

val WomanShoes: ImageVector
    get() {
        if (_WomanShoes != null) {
            return _WomanShoes!!
        }
        _WomanShoes = ImageVector.Builder(
            name = "WomanShoes",
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
                moveTo(18f, 4.08f)
                curveTo(18f, 3.901f, 22f, 4.08f, 22f, 4.08f)
                lineTo(23f, 9.698f)
                lineTo(21.375f, 20f)
                horizontalLineTo(20.25f)
                lineTo(18.875f, 10.713f)
                lineTo(13f, 20f)
                horizontalLineTo(2.929f)
                curveTo(1.863f, 20.001f, 1f, 19.167f, 1f, 18.14f)
                curveTo(1f, 17.544f, 1.296f, 16.984f, 1.795f, 16.634f)
                lineTo(6.603f, 13.263f)
                curveTo(12.206f, 13.263f, 18f, 8.015f, 18f, 4.08f)
                close()
            }
        }.build()

        return _WomanShoes!!
    }

@Suppress("ObjectPropertyName")
private var _WomanShoes: ImageVector? = null

@Preview
@Composable
fun WomanShoesPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = WomanShoes,
            contentDescription = null
        )
    }
}