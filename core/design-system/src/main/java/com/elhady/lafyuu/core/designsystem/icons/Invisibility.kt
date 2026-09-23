package com.elhady.lafyuu.core.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Invisibility: ImageVector
    get() {
        if (_Invisibility != null) {
            return _Invisibility!!
        }
        _Invisibility = ImageVector.Builder(
            name = "Invisibility",
            defaultWidth = 22.dp,
            defaultHeight = 20.dp,
            viewportWidth = 22f,
            viewportHeight = 20f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF656665)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(2.796f, 5.973f)
                curveTo(1.872f, 7.064f, 1.168f, 8.346f, 0.75f, 9.751f)
                curveTo(2.041f, 14.088f, 6.059f, 17.25f, 10.815f, 17.25f)
                curveTo(11.807f, 17.25f, 12.768f, 17.112f, 13.678f, 16.855f)
                moveTo(5.043f, 3.978f)
                curveTo(6.7f, 2.886f, 8.683f, 2.25f, 10.816f, 2.25f)
                curveTo(15.572f, 2.25f, 19.589f, 5.412f, 20.88f, 9.749f)
                curveTo(20.169f, 12.142f, 18.626f, 14.178f, 16.588f, 15.522f)
                moveTo(5.043f, 3.978f)
                lineTo(1.816f, 0.75f)
                moveTo(5.043f, 3.978f)
                lineTo(8.694f, 7.629f)
                moveTo(16.588f, 15.522f)
                lineTo(19.816f, 18.75f)
                moveTo(8.694f, 7.629f)
                lineTo(12.937f, 11.871f)
                lineTo(16.588f, 15.522f)
                moveTo(12.937f, 11.871f)
                curveTo(13.48f, 11.328f, 13.816f, 10.578f, 13.816f, 9.75f)
                curveTo(13.816f, 8.093f, 12.472f, 6.75f, 10.816f, 6.75f)
                curveTo(9.987f, 6.75f, 9.237f, 7.086f, 8.694f, 7.629f)
            }
        }.build()

        return _Invisibility!!
    }

@Suppress("ObjectPropertyName")
private var _Invisibility: ImageVector? = null
