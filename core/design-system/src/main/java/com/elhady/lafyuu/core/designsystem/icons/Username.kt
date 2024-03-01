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

val Username: ImageVector
    get() {
        if (_Username != null) {
            return _Username!!
        }
        _Username = ImageVector.Builder(
            name = "Username",
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
                moveTo(12f, 21f)
                curveTo(7.03f, 21f, 3f, 16.97f, 3f, 12f)
                curveTo(3f, 7.03f, 7.03f, 3f, 12f, 3f)
                curveTo(16.97f, 3f, 21f, 7.03f, 21f, 12f)
                curveTo(21f, 15.375f, 18.75f, 17.625f, 16.5f, 17.625f)
                curveTo(12f, 17.625f, 12.896f, 10.397f, 15.375f, 8.625f)
            }
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(13.484f, 12f)
                curveTo(13.484f, 12f, 12.692f, 15.375f, 10.442f, 15.375f)
                curveTo(6.797f, 15.375f, 8.775f, 8.625f, 11.605f, 8.625f)
                curveTo(13.713f, 8.625f, 13.484f, 12f, 13.484f, 12f)
                close()
            }
        }.build()

        return _Username!!
    }

@Suppress("ObjectPropertyName")
private var _Username: ImageVector? = null

@Preview
@Composable
fun UsernamePreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Username,
            contentDescription = null
        )
    }
}