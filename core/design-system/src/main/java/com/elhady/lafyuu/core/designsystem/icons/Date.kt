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

val Date: ImageVector
    get() {
        if (_Date != null) {
            return _Date!!
        }
        _Date = ImageVector.Builder(
            name = "Date",
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
                moveTo(3f, 10.875f)
                horizontalLineTo(21f)
                moveTo(16.5f, 7.5f)
                verticalLineTo(3f)
                moveTo(7.5f, 7.5f)
                verticalLineTo(3f)
                moveTo(3f, 5.25f)
                horizontalLineTo(21f)
                verticalLineTo(21f)
                horizontalLineTo(3f)
                verticalLineTo(5.25f)
                close()
            }
        }.build()

        return _Date!!
    }

@Suppress("ObjectPropertyName")
private var _Date: ImageVector? = null

@Preview
@Composable
fun DatePreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Date,
            contentDescription = null
        )
    }
}