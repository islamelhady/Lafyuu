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

val SearchMini: ImageVector
    get() {
        if (_SearchMini != null) {
            return _SearchMini!!
        }
        _SearchMini = ImageVector.Builder(
            name = "SearchMini",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF40BFFF)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(7.25f, 3f)
                curveTo(4.903f, 3f, 3f, 4.903f, 3f, 7.25f)
                curveTo(3f, 9.597f, 4.903f, 11.5f, 7.25f, 11.5f)
                curveTo(9.597f, 11.5f, 11.5f, 9.597f, 11.5f, 7.25f)
                curveTo(11.5f, 4.903f, 9.597f, 3f, 7.25f, 3f)
                close()
                moveTo(1f, 7.25f)
                curveTo(1f, 3.798f, 3.798f, 1f, 7.25f, 1f)
                curveTo(10.702f, 1f, 13.5f, 3.798f, 13.5f, 7.25f)
                curveTo(13.5f, 10.702f, 10.702f, 13.5f, 7.25f, 13.5f)
                curveTo(3.798f, 13.5f, 1f, 10.702f, 1f, 7.25f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF40BFFF)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(10.293f, 10.293f)
                curveTo(10.683f, 9.902f, 11.317f, 9.902f, 11.707f, 10.293f)
                lineTo(14.707f, 13.293f)
                curveTo(15.098f, 13.683f, 15.098f, 14.317f, 14.707f, 14.707f)
                curveTo(14.317f, 15.098f, 13.683f, 15.098f, 13.293f, 14.707f)
                lineTo(10.293f, 11.707f)
                curveTo(9.902f, 11.317f, 9.902f, 10.683f, 10.293f, 10.293f)
                close()
            }
        }.build()

        return _SearchMini!!
    }

@Suppress("ObjectPropertyName")
private var _SearchMini: ImageVector? = null

@Preview
@Composable
fun SearchMiniPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = SearchMini,
            contentDescription = null
        )
    }
}