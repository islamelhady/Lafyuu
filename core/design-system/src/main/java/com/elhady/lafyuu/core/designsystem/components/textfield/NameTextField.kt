package com.elhady.lafyuu.core.designsystem.components.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.User
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme

@Composable
fun NameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "First Name",
    state: LafyuuFieldState = LafyuuFieldState.Default,
    errorMessage: String? = null
) {
    BaseTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        state = state,
        errorMessage = errorMessage,
        leadingIcon = User,
        modifier = modifier
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

            SectionTitle("Name")
            var name by remember { mutableStateOf("") }
            NameTextField(
                value = name,
                onValueChange = { name = it },
                state = LafyuuFieldState.Default
            )
            var nameFilled by remember { mutableStateOf("Islam") }
            NameTextField(
                value = nameFilled,
                onValueChange = { nameFilled = it },
                state = LafyuuFieldState.Default
            )
            var nameActive by remember { mutableStateOf("Elhady") }
            NameTextField(
                value = nameActive,
                onValueChange = { nameActive = it },
                state = LafyuuFieldState.Active
            )
        }
    }
}