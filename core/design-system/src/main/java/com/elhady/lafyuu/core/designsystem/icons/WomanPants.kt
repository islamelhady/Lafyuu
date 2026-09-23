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

val WomanPants: ImageVector
    get() {
        if (_WomanPants != null) {
            return _WomanPants!!
        }
        _WomanPants = ImageVector.Builder(
            name = "WomanPants",
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
                moveTo(6.909f, 2f)
                horizontalLineTo(17.091f)
                curveTo(17.091f, 2f, 18.788f, 5.105f, 18.788f, 7.21f)
                curveTo(18.788f, 8.592f, 17.657f, 10.42f, 17.657f, 13.579f)
                curveTo(17.657f, 16.737f, 19f, 22f, 19f, 22f)
                horizontalLineTo(15.084f)
                lineTo(13.542f, 14.73f)
                lineTo(12.001f, 7.46f)
                lineTo(8.916f, 22f)
                horizontalLineTo(5f)
                curveTo(5f, 22f, 6.343f, 16.736f, 6.343f, 13.579f)
                curveTo(6.343f, 10.421f, 5.212f, 8.566f, 5.212f, 7.21f)
                curveTo(5.212f, 5.105f, 6.909f, 2f, 6.909f, 2f)
                close()
            }
        }.build()

        return _WomanPants!!
    }

@Suppress("ObjectPropertyName")
private var _WomanPants: ImageVector? = null

@Preview
@Composable
fun WomanPantsPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = WomanPants,
            contentDescription = null
        )
    }
}