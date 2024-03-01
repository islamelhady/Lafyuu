package com.elhady.lafyuu.core.designsystem.icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val Message: ImageVector
    get() {
        if (_Message != null) {
            return _Message!!
        }
        _Message = ImageVector.Builder(
            name = "Message",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF9098B1)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(2f, 5.25f)
                curveTo(2f, 4.698f, 2.448f, 4.25f, 3f, 4.25f)
                horizontalLineTo(21f)
                curveTo(21.552f, 4.25f, 22f, 4.698f, 22f, 5.25f)
                verticalLineTo(18.75f)
                curveTo(22f, 19.302f, 21.552f, 19.75f, 21f, 19.75f)
                horizontalLineTo(3f)
                curveTo(2.448f, 19.75f, 2f, 19.302f, 2f, 18.75f)
                verticalLineTo(5.25f)
                close()
                moveTo(4f, 6.25f)
                verticalLineTo(17.75f)
                horizontalLineTo(20f)
                verticalLineTo(6.25f)
                horizontalLineTo(4f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF9098B1)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(2.241f, 4.599f)
                curveTo(2.6f, 4.18f, 3.231f, 4.131f, 3.651f, 4.491f)
                lineTo(12f, 11.647f)
                lineTo(20.349f, 4.491f)
                curveTo(20.769f, 4.131f, 21.4f, 4.18f, 21.759f, 4.599f)
                curveTo(22.119f, 5.019f, 22.07f, 5.65f, 21.651f, 6.009f)
                lineTo(12.651f, 13.723f)
                curveTo(12.276f, 14.044f, 11.724f, 14.044f, 11.349f, 13.723f)
                lineTo(2.349f, 6.009f)
                curveTo(1.93f, 5.65f, 1.881f, 5.019f, 2.241f, 4.599f)
                close()
            }
        }.build()

        return _Message!!
    }

@Suppress("ObjectPropertyName")
private var _Message: ImageVector? = null

@Preview
@Composable
fun MessagePreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Message,
            contentDescription = null
        )
    }
}