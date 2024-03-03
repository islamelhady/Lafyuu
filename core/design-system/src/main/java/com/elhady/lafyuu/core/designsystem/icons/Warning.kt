package com.elhady.lafyuu.core.designsystem.icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val Warning: ImageVector
    get() {
        if (_Warning != null) {
            return _Warning!!
        }
        _Warning = ImageVector.Builder(
            name = "Warning",
            defaultWidth = 6.dp,
            defaultHeight = 33.dp,
            viewportWidth = 6f,
            viewportHeight = 33f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 6f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(3f, 3f)
                verticalLineTo(20f)
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 6f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(3f, 29f)
                verticalLineTo(30f)
            }
        }.build()

        return _Warning!!
    }

@Suppress("ObjectPropertyName")
private var _Warning: ImageVector? = null

@Preview
@Composable
fun WarningPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Warning,
            contentDescription = null
        )
    }
}