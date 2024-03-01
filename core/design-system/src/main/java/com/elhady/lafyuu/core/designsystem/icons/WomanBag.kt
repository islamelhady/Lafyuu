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

val WomanBag: ImageVector
    get() {
        if (_WomanBag != null) {
            return _WomanBag!!
        }
        _WomanBag = ImageVector.Builder(
            name = "WomanBag",
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
                moveTo(21f, 21f)
                horizontalLineTo(3f)
                lineTo(4.832f, 10.007f)
                curveTo(5.073f, 8.56f, 6.325f, 7.5f, 7.791f, 7.5f)
                horizontalLineTo(16.209f)
                curveTo(17.675f, 7.5f, 18.927f, 8.56f, 19.168f, 10.007f)
                lineTo(21f, 21f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5.25f, 11.859f)
                lineTo(18.75f, 12f)
            }
            path(
                stroke = SolidColor(Color(0xFF40BFFF)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(8f, 6.468f)
                verticalLineTo(6.375f)
                curveTo(8f, 4.511f, 9.511f, 3f, 11.375f, 3f)
                horizontalLineTo(12.828f)
                curveTo(14.5f, 3f, 16f, 4.636f, 16f, 6.5f)
            }
        }.build()

        return _WomanBag!!
    }

@Suppress("ObjectPropertyName")
private var _WomanBag: ImageVector? = null

@Preview
@Composable
fun WomanBagPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = WomanBag,
            contentDescription = null
        )
    }
}