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

val ManUnderwear: ImageVector
    get() {
        if (_ManUnderwear != null) {
            return _ManUnderwear!!
        }
        _ManUnderwear = ImageVector.Builder(
            name = "ManUnderwear",
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
                moveTo(19.875f, 5.25f)
                horizontalLineTo(4.125f)
                verticalLineTo(8.444f)
                lineTo(3f, 18.75f)
                horizontalLineTo(8.625f)
                lineTo(12f, 13.125f)
                lineTo(15.375f, 18.75f)
                horizontalLineTo(21f)
                lineTo(19.875f, 8.625f)
                verticalLineTo(5.25f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4.125f, 8.625f)
                horizontalLineTo(19.875f)
            }
        }.build()

        return _ManUnderwear!!
    }

@Suppress("ObjectPropertyName")
private var _ManUnderwear: ImageVector? = null

@Preview
@Composable
fun ManUnderwearPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = ManUnderwear,
            contentDescription = null
        )
    }
}