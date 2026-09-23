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
import com.elhady.lafyuu.core.designsystem.icons.Cart

val Left: ImageVector
    get() {
        if (_Left != null) {
            return _Left!!
        }
        _Left = ImageVector.Builder(
            name = "Left",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF9098B1)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15f, 18f)
                lineTo(9f, 12f)
                lineTo(15f, 6f)
            }
        }.build()

        return _Left!!
    }

@Suppress("ObjectPropertyName")
private var _Left: ImageVector? = null

@Preview
@Composable
fun LeftPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = Left,
            contentDescription = null
        )
    }
}