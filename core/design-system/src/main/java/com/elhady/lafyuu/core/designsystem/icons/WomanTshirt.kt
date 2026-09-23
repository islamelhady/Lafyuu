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

val WomanTshirt: ImageVector
    get() {
        if (_WomanTshirt != null) {
            return _WomanTshirt!!
        }
        _WomanTshirt = ImageVector.Builder(
            name = "WomanTshirt",
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
                moveTo(8.625f, 3f)
                curveTo(8.625f, 3f, 9.75f, 5.25f, 12f, 5.25f)
                curveTo(14.25f, 5.25f, 15.375f, 3f, 15.375f, 3f)
                lineTo(21f, 4.125f)
                lineTo(19.875f, 8.625f)
                horizontalLineTo(17.625f)
                curveTo(17.625f, 8.625f, 18.75f, 10.875f, 18.75f, 12f)
                curveTo(18.75f, 12.989f, 17.833f, 14.731f, 17.625f, 16.5f)
                curveTo(17.298f, 19.284f, 17.625f, 21f, 17.625f, 21f)
                horizontalLineTo(6.375f)
                curveTo(6.375f, 21f, 6.698f, 19.235f, 6.375f, 16.5f)
                curveTo(6.156f, 14.644f, 5.25f, 12.91f, 5.25f, 12f)
                curveTo(5.25f, 10.875f, 6.375f, 8.625f, 6.375f, 8.625f)
                horizontalLineTo(4.125f)
                lineTo(3f, 4.125f)
                lineTo(8.625f, 3f)
                close()
            }
        }.build()

        return _WomanTshirt!!
    }

@Suppress("ObjectPropertyName")
private var _WomanTshirt: ImageVector? = null

@Preview
@Composable
fun WomanTshirtPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = WomanTshirt,
            contentDescription = null
        )
    }
}