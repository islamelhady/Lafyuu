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

val Mic: ImageVector
    get() {
        if (_Mic != null) {
            return _Mic!!
        }
        _Mic = ImageVector.Builder(
            name = "Mic",
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
                moveTo(11.998f, 12.296f)
                curveTo(10.429f, 12.296f, 9.157f, 11.024f, 9.157f, 9.454f)
                verticalLineTo(5.842f)
                curveTo(9.157f, 4.272f, 10.429f, 3f, 11.998f, 3f)
                curveTo(13.568f, 3f, 14.84f, 4.272f, 14.84f, 5.842f)
                verticalLineTo(9.454f)
                curveTo(14.84f, 11.024f, 13.568f, 12.296f, 11.998f, 12.296f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 17.625f)
                verticalLineTo(21f)
            }
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5.25f, 9.75f)
                curveTo(5.25f, 13.478f, 8.272f, 16.5f, 12f, 16.5f)
                curveTo(15.728f, 16.5f, 18.75f, 13.478f, 18.75f, 9.75f)
            }
        }.build()

        return _Mic!!
    }

@Suppress("ObjectPropertyName")
private var _Mic: ImageVector? = null

@Preview
@Composable
fun MicPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Mic,
            contentDescription = null
        )
    }
}