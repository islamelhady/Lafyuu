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

val MinusMini: ImageVector
    get() {
        if (_MinusMini != null) {
            return _MinusMini!!
        }
        _MinusMini = ImageVector.Builder(
            name = "MinusMini",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3.333f, 8f)
                horizontalLineTo(12.667f)
            }
        }.build()

        return _MinusMini!!
    }

@Suppress("ObjectPropertyName")
private var _MinusMini: ImageVector? = null

@Preview
@Composable
fun MinusMiniPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = MinusMini,
            contentDescription = null
        )
    }
}