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

val Location: ImageVector
    get() {
        if (_Location != null) {
            return _Location!!
        }
        _Location = ImageVector.Builder(
            name = "Location",
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
                moveTo(19f, 9.778f)
                curveTo(19f, 14.074f, 11.5f, 22f, 11.5f, 22f)
                curveTo(11.5f, 22f, 4f, 14.074f, 4f, 9.778f)
                curveTo(4f, 5.481f, 7.358f, 2f, 11.5f, 2f)
                curveTo(15.642f, 2f, 19f, 5.483f, 19f, 9.778f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF9098B1)),
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 1f
            ) {
                moveTo(11.5f, 7.5f)
                curveTo(12.605f, 7.5f, 13.5f, 8.395f, 13.5f, 9.5f)
                curveTo(13.5f, 10.605f, 12.605f, 11.5f, 11.5f, 11.5f)
                curveTo(10.395f, 11.5f, 9.5f, 10.605f, 9.5f, 9.5f)
                curveTo(9.5f, 8.395f, 10.395f, 7.5f, 11.5f, 7.5f)
                close()
            }
        }.build()

        return _Location!!
    }

@Suppress("ObjectPropertyName")
private var _Location: ImageVector? = null

@Preview
@Composable
fun LocationPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Location,
            contentDescription = null
        )
    }
}