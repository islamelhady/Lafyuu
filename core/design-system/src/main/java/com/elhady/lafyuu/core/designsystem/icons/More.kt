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

val More: ImageVector
    get() {
        if (_More != null) {
            return _More!!
        }
        _More = ImageVector.Builder(
            name = "More",
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
                moveTo(13f, 12f)
                curveTo(13f, 11.448f, 12.552f, 11f, 12f, 11f)
                curveTo(11.448f, 11f, 11f, 11.448f, 11f, 12f)
                curveTo(11f, 12.552f, 11.448f, 13f, 12f, 13f)
                curveTo(12.552f, 13f, 13f, 12.552f, 13f, 12f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(13f, 5f)
                curveTo(13f, 4.448f, 12.552f, 4f, 12f, 4f)
                curveTo(11.448f, 4f, 11f, 4.448f, 11f, 5f)
                curveTo(11f, 5.552f, 11.448f, 6f, 12f, 6f)
                curveTo(12.552f, 6f, 13f, 5.552f, 13f, 5f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(13f, 19f)
                curveTo(13f, 18.448f, 12.552f, 18f, 12f, 18f)
                curveTo(11.448f, 18f, 11f, 18.448f, 11f, 19f)
                curveTo(11f, 19.552f, 11.448f, 20f, 12f, 20f)
                curveTo(12.552f, 20f, 13f, 19.552f, 13f, 19f)
                close()
            }
        }.build()

        return _More!!
    }

@Suppress("ObjectPropertyName")
private var _More: ImageVector? = null

@Preview
@Composable
fun MorePreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = More,
            contentDescription = null
        )
    }
}