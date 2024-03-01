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

val Password: ImageVector
    get() {
        if (_Password != null) {
            return _Password!!
        }
        _Password = ImageVector.Builder(
            name = "Password",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF9098B1)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(2f, 9.75f)
                curveTo(2f, 9.198f, 2.448f, 8.75f, 3f, 8.75f)
                horizontalLineTo(21f)
                curveTo(21.552f, 8.75f, 22f, 9.198f, 22f, 9.75f)
                verticalLineTo(21f)
                curveTo(22f, 21.552f, 21.552f, 22f, 21f, 22f)
                horizontalLineTo(3f)
                curveTo(2.448f, 22f, 2f, 21.552f, 2f, 21f)
                verticalLineTo(9.75f)
                close()
                moveTo(4f, 10.75f)
                verticalLineTo(20f)
                horizontalLineTo(20f)
                verticalLineTo(10.75f)
                horizontalLineTo(4f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF9098B1)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(12f, 4f)
                curveTo(9.481f, 4f, 7.375f, 6.132f, 7.375f, 8.841f)
                curveTo(7.375f, 9.393f, 6.927f, 9.841f, 6.375f, 9.841f)
                curveTo(5.823f, 9.841f, 5.375f, 9.393f, 5.375f, 8.841f)
                curveTo(5.375f, 5.099f, 8.306f, 2f, 12f, 2f)
                curveTo(15.694f, 2f, 18.625f, 5.099f, 18.625f, 8.841f)
                curveTo(18.625f, 9.393f, 18.177f, 9.841f, 17.625f, 9.841f)
                curveTo(17.073f, 9.841f, 16.625f, 9.393f, 16.625f, 8.841f)
                curveTo(16.625f, 6.132f, 14.519f, 4f, 12f, 4f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF9098B1)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(12f, 13.726f)
                curveTo(12.552f, 13.726f, 13f, 14.173f, 13f, 14.726f)
                verticalLineTo(15.851f)
                curveTo(13f, 16.403f, 12.552f, 16.851f, 12f, 16.851f)
                curveTo(11.448f, 16.851f, 11f, 16.403f, 11f, 15.851f)
                verticalLineTo(14.726f)
                curveTo(11f, 14.173f, 11.448f, 13.726f, 12f, 13.726f)
                close()
            }
        }.build()

        return _Password!!
    }

@Suppress("ObjectPropertyName")
private var _Password: ImageVector? = null

@Preview
@Composable
fun PasswordPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Password,
            contentDescription = null
        )
    }
}