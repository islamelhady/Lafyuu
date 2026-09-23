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

val Gender: ImageVector
    get() {
        if (_Gender != null) {
            return _Gender!!
        }
        _Gender = ImageVector.Builder(
            name = "Gender",
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
                moveTo(12f, 14f)
                curveTo(15.314f, 14f, 18f, 11.314f, 18f, 8f)
                curveTo(18f, 4.686f, 15.314f, 2f, 12f, 2f)
                curveTo(8.686f, 2f, 6f, 4.686f, 6f, 8f)
                curveTo(6f, 11.314f, 8.686f, 14f, 12f, 14f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 14f)
                verticalLineTo(22f)
            }
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(9f, 18f)
                horizontalLineTo(15f)
            }
        }.build()

        return _Gender!!
    }

@Suppress("ObjectPropertyName")
private var _Gender: ImageVector? = null

@Preview
@Composable
fun GenderPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Gender,
            contentDescription = null
        )
    }
}