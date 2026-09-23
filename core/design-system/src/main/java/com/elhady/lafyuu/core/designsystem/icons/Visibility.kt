package com.elhady.lafyuu.core.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Visibility: ImageVector
    get() {
        if (_Visibility != null) {
            return _Visibility!!
        }
        _Visibility = ImageVector.Builder(
            name = "Visibility",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF656665)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(2.035f, 12.322f)
                curveTo(1.966f, 12.115f, 1.966f, 11.891f, 2.035f, 11.683f)
                curveTo(3.423f, 7.51f, 7.36f, 4.5f, 12f, 4.5f)
                curveTo(16.638f, 4.5f, 20.574f, 7.507f, 21.964f, 11.678f)
                curveTo(22.033f, 11.885f, 22.033f, 12.109f, 21.964f, 12.317f)
                curveTo(20.576f, 16.49f, 16.639f, 19.5f, 11.999f, 19.5f)
                curveTo(7.361f, 19.5f, 3.425f, 16.493f, 2.035f, 12.322f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF656665)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15f, 12f)
                curveTo(15f, 13.657f, 13.657f, 15f, 12f, 15f)
                curveTo(10.343f, 15f, 9f, 13.657f, 9f, 12f)
                curveTo(9f, 10.343f, 10.343f, 9f, 12f, 9f)
                curveTo(13.657f, 9f, 15f, 10.343f, 15f, 12f)
                close()
            }
        }.build()

        return _Visibility!!
    }

@Suppress("ObjectPropertyName")
private var _Visibility: ImageVector? = null
