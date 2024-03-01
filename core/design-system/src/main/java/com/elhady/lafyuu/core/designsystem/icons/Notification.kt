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

val Notification: ImageVector
    get() {
        if (_Notification != null) {
            return _Notification!!
        }
        _Notification = ImageVector.Builder(
            name = "Notification",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF9098B1)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(12f, 4f)
                curveTo(8.669f, 4f, 5.969f, 6.7f, 5.969f, 10.031f)
                verticalLineTo(15.282f)
                curveTo(5.969f, 15.397f, 5.949f, 15.512f, 5.91f, 15.62f)
                lineTo(5.548f, 16.625f)
                horizontalLineTo(18.452f)
                lineTo(18.09f, 15.62f)
                curveTo(18.051f, 15.512f, 18.031f, 15.397f, 18.031f, 15.282f)
                verticalLineTo(10.031f)
                curveTo(18.031f, 6.7f, 15.331f, 4f, 12f, 4f)
                close()
                moveTo(3.969f, 10.031f)
                curveTo(3.969f, 5.595f, 7.564f, 2f, 12f, 2f)
                curveTo(16.436f, 2f, 20.031f, 5.595f, 20.031f, 10.031f)
                verticalLineTo(15.107f)
                lineTo(20.816f, 17.286f)
                curveTo(20.926f, 17.593f, 20.88f, 17.934f, 20.693f, 18.2f)
                curveTo(20.506f, 18.466f, 20.201f, 18.625f, 19.875f, 18.625f)
                horizontalLineTo(4.125f)
                curveTo(3.799f, 18.625f, 3.494f, 18.466f, 3.307f, 18.2f)
                curveTo(3.12f, 17.934f, 3.074f, 17.593f, 3.184f, 17.286f)
                lineTo(3.969f, 15.107f)
                verticalLineTo(10.031f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF9098B1)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(8.625f, 16.625f)
                curveTo(9.177f, 16.625f, 9.625f, 17.073f, 9.625f, 17.625f)
                curveTo(9.625f, 18.937f, 10.688f, 20f, 12f, 20f)
                curveTo(13.312f, 20f, 14.375f, 18.937f, 14.375f, 17.625f)
                curveTo(14.375f, 17.073f, 14.823f, 16.625f, 15.375f, 16.625f)
                curveTo(15.927f, 16.625f, 16.375f, 17.073f, 16.375f, 17.625f)
                curveTo(16.375f, 20.041f, 14.416f, 22f, 12f, 22f)
                curveTo(9.584f, 22f, 7.625f, 20.041f, 7.625f, 17.625f)
                curveTo(7.625f, 17.073f, 8.073f, 16.625f, 8.625f, 16.625f)
                close()
            }
        }.build()

        return _Notification!!
    }

@Suppress("ObjectPropertyName")
private var _Notification: ImageVector? = null

@Preview
@Composable
fun NotificationPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Notification,
            contentDescription = null
        )
    }
}