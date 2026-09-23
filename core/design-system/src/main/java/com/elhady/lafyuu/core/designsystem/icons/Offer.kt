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

val Offer: ImageVector
    get() {
        if (_Offer != null) {
            return _Offer!!
        }
        _Offer = ImageVector.Builder(
            name = "Offer",
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
                moveTo(8.063f, 8.625f)
                curveTo(8.373f, 8.625f, 8.625f, 8.373f, 8.625f, 8.063f)
                curveTo(8.625f, 7.752f, 8.373f, 7.5f, 8.063f, 7.5f)
                curveTo(7.752f, 7.5f, 7.5f, 7.752f, 7.5f, 8.063f)
                curveTo(7.5f, 8.373f, 7.752f, 8.625f, 8.063f, 8.625f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(21f, 13f)
                lineTo(13f, 21f)
                lineTo(3f, 11f)
                verticalLineTo(3f)
                horizontalLineTo(11f)
                lineTo(21f, 13f)
                close()
            }
        }.build()

        return _Offer!!
    }

@Suppress("ObjectPropertyName")
private var _Offer: ImageVector? = null

@Preview
@Composable
fun OfferPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Offer,
            contentDescription = null
        )
    }
}