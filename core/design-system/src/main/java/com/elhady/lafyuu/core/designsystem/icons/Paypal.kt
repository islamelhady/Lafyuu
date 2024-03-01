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

val Paypal: ImageVector
    get() {
        if (_Paypal != null) {
            return _Paypal!!
        }
        _Paypal = ImageVector.Builder(
            name = "Paypal",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF1565C0))) {
                moveTo(9.349f, 6.884f)
                lineTo(9.352f, 6.885f)
                curveTo(9.404f, 6.663f, 9.593f, 6.5f, 9.829f, 6.5f)
                horizontalLineTo(16.566f)
                curveTo(16.574f, 6.5f, 16.583f, 6.497f, 16.591f, 6.497f)
                curveTo(16.448f, 4.108f, 14.443f, 3f, 12.675f, 3f)
                horizontalLineTo(5.938f)
                curveTo(5.701f, 3f, 5.512f, 3.168f, 5.461f, 3.388f)
                lineTo(5.458f, 3.388f)
                lineTo(2.514f, 16.906f)
                lineTo(2.52f, 16.907f)
                curveTo(2.513f, 16.939f, 2.501f, 16.969f, 2.501f, 17.004f)
                curveTo(2.501f, 17.281f, 2.724f, 17.5f, 3.001f, 17.5f)
                horizontalLineTo(7.036f)
                lineTo(9.349f, 6.884f)
                close()
            }
            path(fill = SolidColor(Color(0xFF039BE5))) {
                moveTo(16.592f, 6.497f)
                curveTo(16.619f, 6.935f, 16.59f, 7.411f, 16.478f, 7.938f)
                curveTo(15.837f, 10.935f, 13.522f, 12.495f, 10.66f, 12.495f)
                curveTo(10.66f, 12.495f, 8.925f, 12.495f, 8.504f, 12.495f)
                curveTo(8.243f, 12.495f, 8.12f, 12.648f, 8.064f, 12.765f)
                lineTo(7.194f, 16.79f)
                lineTo(7.041f, 17.504f)
                horizontalLineTo(7.038f)
                lineTo(6.407f, 20.402f)
                lineTo(6.413f, 20.403f)
                curveTo(6.406f, 20.435f, 6.394f, 20.465f, 6.394f, 20.5f)
                curveTo(6.394f, 20.776f, 6.617f, 21f, 6.894f, 21f)
                horizontalLineTo(10.56f)
                lineTo(10.567f, 20.995f)
                curveTo(10.803f, 20.991f, 10.99f, 20.823f, 11.039f, 20.601f)
                lineTo(11.048f, 20.593f)
                lineTo(11.954f, 16.385f)
                curveTo(11.954f, 16.385f, 12.017f, 15.984f, 12.439f, 15.984f)
                curveTo(12.861f, 15.984f, 14.528f, 15.984f, 14.528f, 15.984f)
                curveTo(17.39f, 15.984f, 19.729f, 14.431f, 20.37f, 11.433f)
                curveTo(21.091f, 8.053f, 18.68f, 6.509f, 16.592f, 6.497f)
                close()
            }
            path(fill = SolidColor(Color(0xFF283593))) {
                moveTo(9.83f, 6.5f)
                curveTo(9.593f, 6.5f, 9.404f, 6.663f, 9.352f, 6.884f)
                lineTo(9.35f, 6.883f)
                lineTo(8.063f, 12.766f)
                curveTo(8.119f, 12.649f, 8.242f, 12.496f, 8.502f, 12.496f)
                curveTo(8.925f, 12.496f, 10.62f, 12.496f, 10.62f, 12.496f)
                curveTo(13.481f, 12.496f, 15.836f, 10.936f, 16.476f, 7.938f)
                curveTo(16.589f, 7.412f, 16.618f, 6.935f, 16.591f, 6.497f)
                curveTo(16.583f, 6.496f, 16.574f, 6.5f, 16.566f, 6.5f)
                horizontalLineTo(9.83f)
                close()
            }
        }.build()

        return _Paypal!!
    }

@Suppress("ObjectPropertyName")
private var _Paypal: ImageVector? = null

@Preview
@Composable
fun PaypalPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Paypal,
            contentDescription = null
        )
    }
}