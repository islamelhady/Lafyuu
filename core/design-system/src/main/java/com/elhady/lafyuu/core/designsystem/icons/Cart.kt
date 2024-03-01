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

val Cart: ImageVector
    get() {
        if (_Cart != null) {
            return _Cart!!
        }
        _Cart = ImageVector.Builder(
            name = "Cart",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF9098B1)),
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(9.188f, 21f)
                curveTo(9.498f, 21f, 9.75f, 20.748f, 9.75f, 20.438f)
                curveTo(9.75f, 20.127f, 9.498f, 19.875f, 9.188f, 19.875f)
                curveTo(8.877f, 19.875f, 8.625f, 20.127f, 8.625f, 20.438f)
                curveTo(8.625f, 20.748f, 8.877f, 21f, 9.188f, 21f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF9098B1)),
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(17.063f, 21f)
                curveTo(17.373f, 21f, 17.625f, 20.748f, 17.625f, 20.438f)
                curveTo(17.625f, 20.127f, 17.373f, 19.875f, 17.063f, 19.875f)
                curveTo(16.752f, 19.875f, 16.5f, 20.127f, 16.5f, 20.438f)
                curveTo(16.5f, 20.748f, 16.752f, 21f, 17.063f, 21f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3f, 3f)
                horizontalLineTo(5.25f)
                lineTo(7.5f, 16.5f)
                horizontalLineTo(18.75f)
                lineTo(21f, 6.375f)
                horizontalLineTo(6.375f)
            }
        }.build()

        return _Cart!!
    }

@Suppress("ObjectPropertyName")
private var _Cart: ImageVector? = null


@Preview
@Composable
fun CartPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Cart,
            contentDescription = null
        )
    }
}