package com.elhady.lafyuu.core.designsystem.components.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.Invisibility
import com.elhady.lafyuu.core.designsystem.icons.Password
import com.elhady.lafyuu.core.designsystem.icons.Visibility
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme


@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Password",
    state: LafyuuFieldState = LafyuuFieldState.Default,
    errorMessage: String? = "Oops! Your Password Is Not Correct"
) {
    var isVisible by remember { mutableStateOf(false) }

    BaseTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        state = state,
        errorMessage = errorMessage,
        leadingIcon = Password,
        keyboardType = KeyboardType.Password,
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingContent = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(
                    imageVector = if (isVisible) Invisibility else Visibility,
                    contentDescription = if (isVisible) "Hide password" else "Show password",
                )
            }
        },
        modifier = modifier
    )
}



@Preview(showBackground = true, widthDp = 380, heightDp = 800)
@Composable
private fun AllFormFieldsPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                SectionTitle("Password")
                var passwordPlaceholder by remember { mutableStateOf("") }
                PasswordTextField(
                    value = passwordPlaceholder,
                    onValueChange = { passwordPlaceholder = it },
                    state = LafyuuFieldState.Default
                )
                var passwordFilled by remember { mutableStateOf("password123") }
                PasswordTextField(
                    value = passwordFilled,
                    onValueChange = { passwordFilled = it },
                    state = LafyuuFieldState.Default
                )
                var passwordActive by remember { mutableStateOf("password123") }
                PasswordTextField(
                    value = passwordActive,
                    onValueChange = { passwordActive = it },
                    state = LafyuuFieldState.Active
                )
                var passwordError by remember { mutableStateOf("password123") }
                PasswordTextField(
                    value = passwordError,
                    onValueChange = { passwordError = it },
                    state = LafyuuFieldState.Error
                )
            }
        }
    }
}