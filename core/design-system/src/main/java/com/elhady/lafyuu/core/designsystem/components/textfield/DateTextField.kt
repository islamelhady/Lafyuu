package com.elhady.lafyuu.core.designsystem.components.textfield


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.Date
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme


@Composable
fun DateTextField(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "24/09/2020"
) {
    BaseTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        placeholder = placeholder,
        state = LafyuuFieldState.Default,
        trailingContent = {
            Icon(
                imageVector = Date,
                contentDescription = null,
                tint = Theme.color.neutralGrey
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .height(Theme.size.inputFieldHeight)
            .clickable { onClick() }
    )
}


@Preview(showBackground = true)
@Composable
private fun AllRemainingFormsPreview() {
    LafyuuTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SectionTitle("Date")
            var date by remember { mutableStateOf("") }
            DateTextField(
                value = date,
                onClick = { }
            )
            var dateFilled by remember { mutableStateOf("24/09/2020") }
            DateTextField(
                value = dateFilled,
                onClick = {}
            )
        }
    }
}
