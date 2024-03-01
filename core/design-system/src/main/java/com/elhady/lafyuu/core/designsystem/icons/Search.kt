package com.elhady.lafyuu.core.designsystem.icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val Search: ImageVector
    get() {
        if (_Search != null) {
            return _Search!!
        }
        _Search = ImageVector.Builder(
            name = "Search",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f
            ) {
                moveTo(10.875f, 18.75f)
                curveTo(15.224f, 18.75f, 18.75f, 15.224f, 18.75f, 10.875f)
                curveTo(18.75f, 6.526f, 15.224f, 3f, 10.875f, 3f)
                curveTo(6.526f, 3f, 3f, 6.526f, 3f, 10.875f)
                curveTo(3f, 15.224f, 6.526f, 18.75f, 10.875f, 18.75f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(16.5f, 16.5f)
                lineTo(21f, 21f)
            }
        }.build()

        return _Search!!
    }

@Suppress("ObjectPropertyName")
private var _Search: ImageVector? = null

@Preview
@Composable
fun SearchPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Search,
            contentDescription = null
        )
    }
}