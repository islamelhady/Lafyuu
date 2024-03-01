package com.elhady.lafyuu.core.designsystem.icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val Google: ImageVector
    get() {
        if (_Google != null) {
            return _Google!!
        }
        _Google = ImageVector.Builder(
            name = "Google",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFFFFC107))) {
                moveTo(19.844f, 10.433f)
                horizontalLineTo(19.2f)
                verticalLineTo(10.4f)
                horizontalLineTo(12f)
                verticalLineTo(13.6f)
                horizontalLineTo(16.521f)
                curveTo(15.862f, 15.463f, 14.089f, 16.8f, 12f, 16.8f)
                curveTo(9.349f, 16.8f, 7.2f, 14.651f, 7.2f, 12f)
                curveTo(7.2f, 9.349f, 9.349f, 7.2f, 12f, 7.2f)
                curveTo(13.224f, 7.2f, 14.337f, 7.662f, 15.184f, 8.416f)
                lineTo(17.447f, 6.153f)
                curveTo(16.018f, 4.821f, 14.107f, 4f, 12f, 4f)
                curveTo(7.582f, 4f, 4f, 7.582f, 4f, 12f)
                curveTo(4f, 16.418f, 7.582f, 20f, 12f, 20f)
                curveTo(16.418f, 20f, 20f, 16.418f, 20f, 12f)
                curveTo(20f, 11.464f, 19.945f, 10.94f, 19.844f, 10.433f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFF3D00))) {
                moveTo(4.922f, 8.276f)
                lineTo(7.55f, 10.204f)
                curveTo(8.261f, 8.443f, 9.984f, 7.2f, 12f, 7.2f)
                curveTo(13.223f, 7.2f, 14.336f, 7.662f, 15.184f, 8.416f)
                lineTo(17.447f, 6.153f)
                curveTo(16.018f, 4.821f, 14.107f, 4f, 12f, 4f)
                curveTo(8.927f, 4f, 6.262f, 5.735f, 4.922f, 8.276f)
                close()
            }
            path(fill = SolidColor(Color(0xFF4CAF50))) {
                moveTo(12f, 20f)
                curveTo(14.066f, 20f, 15.944f, 19.209f, 17.364f, 17.924f)
                lineTo(14.888f, 15.828f)
                curveTo(14.084f, 16.437f, 13.086f, 16.8f, 12f, 16.8f)
                curveTo(9.919f, 16.8f, 8.152f, 15.474f, 7.487f, 13.622f)
                lineTo(4.878f, 15.632f)
                curveTo(6.202f, 18.223f, 8.891f, 20f, 12f, 20f)
                close()
            }
            path(fill = SolidColor(Color(0xFF1976D2))) {
                moveTo(19.844f, 10.433f)
                horizontalLineTo(19.2f)
                verticalLineTo(10.4f)
                horizontalLineTo(12f)
                verticalLineTo(13.6f)
                horizontalLineTo(16.521f)
                curveTo(16.204f, 14.495f, 15.629f, 15.266f, 14.886f, 15.828f)
                curveTo(14.887f, 15.828f, 14.887f, 15.828f, 14.888f, 15.828f)
                lineTo(17.364f, 17.923f)
                curveTo(17.188f, 18.082f, 20f, 16f, 20f, 12f)
                curveTo(20f, 11.464f, 19.945f, 10.94f, 19.844f, 10.433f)
                close()
            }
        }.build()

        return _Google!!
    }

@Suppress("ObjectPropertyName")
private var _Google: ImageVector? = null

@Preview
@Composable
fun GooglePreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Google,
            contentDescription = null
        )
    }
}