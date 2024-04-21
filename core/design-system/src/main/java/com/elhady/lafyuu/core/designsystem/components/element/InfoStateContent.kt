package com.elhady.lafyuu.core.designsystem.components.element

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.element.AlertIcon
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun InfoStateContent(
    errorMessage: String,
    errorType: AlertType = AlertType.Error,
    caption: String = "Try Again",
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Theme.space.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Theme.space.large)
    ) {
        AlertIcon(
            type = errorType,
            modifier = Modifier
        )
        LafyuuText(
            text = errorMessage,
            style = Theme.typography.heading2,
            color = Theme.color.neutralDark,
            textAlign = TextAlign.Center
        )
        LafyuuText(
            text = "thank you for shopping using lafyuu",
            style = Theme.typography.normalTextRegular,
            color = Theme.color.neutralGrey,
            textAlign = TextAlign.Center
        )
        DefaultButton(
            caption = caption,
            onClick = onRetryClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun InfoStateContentPreview() {
    InfoStateContent(
        errorMessage = "Something went wrong",
        errorType = AlertType.Error,
        caption = "Try Again",
        onRetryClick = {}
    )
}
