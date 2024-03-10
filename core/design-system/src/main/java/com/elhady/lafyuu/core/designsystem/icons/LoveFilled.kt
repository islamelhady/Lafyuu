package com.elhady.lafyuu.core.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val LoveFilled: ImageVector
    get() {
        if (_LoveFilled != null) {
            return _LoveFilled!!
        }
        _LoveFilled = ImageVector.Builder(
            name = "LoveFilled",
            defaultWidth = 21.dp,
            defaultHeight = 19.dp,
            viewportWidth = 21f,
            viewportHeight = 19f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFB7181)),
                stroke = SolidColor(Color(0xFFFB7181)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(2.586f, 9.839f)
                lineTo(10.042f, 17.126f)
                lineTo(17.497f, 9.839f)
                lineTo(17.537f, 9.799f)
                curveTo(19.598f, 7.785f, 19.598f, 4.525f, 17.537f, 2.512f)
                curveTo(15.476f, 0.498f, 12.142f, 0.497f, 10.082f, 2.512f)
                lineTo(10.042f, 2.551f)
                lineTo(10f, 2.511f)
                curveTo(7.94f, 0.496f, 4.605f, 0.496f, 2.545f, 2.511f)
                curveTo(0.485f, 4.525f, 0.484f, 7.785f, 2.545f, 9.799f)
                lineTo(2.586f, 9.839f)
                close()
            }
        }.build()

        return _LoveFilled!!
    }

@Suppress("ObjectPropertyName")
private var _LoveFilled: ImageVector? = null
