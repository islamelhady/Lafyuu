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

val Tshirt: ImageVector
    get() {
        if (_Tshirt != null) {
            return _Tshirt!!
        }
        _Tshirt = ImageVector.Builder(
            name = "Tshirt",
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
                moveTo(8.667f, 3f)
                curveTo(8.667f, 3f, 9.778f, 5.25f, 12f, 5.25f)
                curveTo(14.222f, 5.25f, 15.333f, 3f, 15.333f, 3f)
                lineTo(22f, 8.625f)
                lineTo(19.778f, 12f)
                lineTo(17.556f, 10.875f)
                verticalLineTo(21f)
                horizontalLineTo(6.444f)
                verticalLineTo(10.875f)
                lineTo(4.222f, 12f)
                lineTo(2f, 8.625f)
                lineTo(8.667f, 3f)
                close()
            }
        }.build()

        return _Tshirt!!
    }

@Suppress("ObjectPropertyName")
private var _Tshirt: ImageVector? = null

@Preview
@Composable
fun TshirtPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Tshirt,
            contentDescription = null
        )
    }
}