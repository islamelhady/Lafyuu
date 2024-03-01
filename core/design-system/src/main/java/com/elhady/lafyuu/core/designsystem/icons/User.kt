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

val User: ImageVector
    get() {
        if (_User != null) {
            return _User!!
        }
        _User = ImageVector.Builder(
            name = "User",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF9098B1)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(12f, 4.001f)
                curveTo(10.397f, 4.001f, 9.098f, 5.3f, 9.098f, 6.903f)
                curveTo(9.098f, 8.506f, 10.397f, 9.805f, 12f, 9.805f)
                curveTo(13.603f, 9.805f, 14.902f, 8.506f, 14.902f, 6.903f)
                curveTo(14.902f, 5.3f, 13.603f, 4.001f, 12f, 4.001f)
                close()
                moveTo(7.098f, 6.903f)
                curveTo(7.098f, 4.195f, 9.293f, 2.001f, 12f, 2.001f)
                curveTo(14.708f, 2.001f, 16.902f, 4.195f, 16.902f, 6.903f)
                curveTo(16.902f, 9.61f, 14.708f, 11.805f, 12f, 11.805f)
                curveTo(9.293f, 11.805f, 7.098f, 9.61f, 7.098f, 6.903f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF9098B1)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(9.518f, 15.39f)
                curveTo(6.47f, 15.39f, 4f, 17.861f, 4f, 20.908f)
                verticalLineTo(20.999f)
                curveTo(4f, 21.552f, 3.552f, 21.999f, 3f, 21.999f)
                curveTo(2.448f, 21.999f, 2f, 21.552f, 2f, 20.999f)
                verticalLineTo(20.908f)
                curveTo(2f, 16.756f, 5.366f, 13.39f, 9.518f, 13.39f)
                horizontalLineTo(14.482f)
                curveTo(18.634f, 13.39f, 22f, 16.756f, 22f, 20.908f)
                verticalLineTo(20.999f)
                curveTo(22f, 21.552f, 21.552f, 21.999f, 21f, 21.999f)
                curveTo(20.448f, 21.999f, 20f, 21.552f, 20f, 20.999f)
                verticalLineTo(20.908f)
                curveTo(20f, 17.861f, 17.53f, 15.39f, 14.482f, 15.39f)
                horizontalLineTo(9.518f)
                close()
            }
        }.build()

        return _User!!
    }

@Suppress("ObjectPropertyName")
private var _User: ImageVector? = null

@Preview
@Composable
fun UserPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = User,
            contentDescription = null
        )
    }
}