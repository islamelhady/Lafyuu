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

val ManPants: ImageVector
    get() {
        if (_ManPants != null) {
            return _ManPants!!
        }
        _ManPants = ImageVector.Builder(
            name = "ManPants",
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
                moveTo(5.875f, 2f)
                horizontalLineTo(17.125f)
                lineTo(19f, 22f)
                horizontalLineTo(15.25f)
                lineTo(11.5f, 9.5f)
                lineTo(7.75f, 22f)
                horizontalLineTo(4f)
                lineTo(5.875f, 2f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 6f)
                lineTo(17f, 6f)
            }
        }.build()

        return _ManPants!!
    }

@Suppress("ObjectPropertyName")
private var _ManPants: ImageVector? = null

@Preview
@Composable
fun ManPantsPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = ManPants,
            contentDescription = null
        )
    }
}