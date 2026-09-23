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

val StarFilled: ImageVector
    get() {
        if (_StarFilled != null) {
            return _StarFilled!!
        }
        _StarFilled = ImageVector.Builder(
            name = "StarFilled",
            defaultWidth = 16.dp,
            defaultHeight = 15.dp,
            viewportWidth = 16f,
            viewportHeight = 15f
        ).apply {
            path(fill = SolidColor(Color(0xFFFFC833))) {
                moveTo(7.608f, 0f)
                lineTo(10.27f, 4.337f)
                lineTo(15.217f, 5.528f)
                lineTo(11.915f, 9.399f)
                lineTo(12.311f, 14.472f)
                lineTo(7.608f, 12.528f)
                lineTo(2.906f, 14.472f)
                lineTo(3.302f, 9.399f)
                lineTo(-0f, 5.528f)
                lineTo(4.947f, 4.337f)
                lineTo(7.608f, 0f)
                close()
            }
        }.build()

        return _StarFilled!!
    }

@Suppress("ObjectPropertyName")
private var _StarFilled: ImageVector? = null

@Preview
@Composable
fun StarFilledPreview() {
    Box(Modifier.padding(16.dp)) {
        Image(
            imageVector = StarFilled,
            contentDescription = null
        )
    }
}