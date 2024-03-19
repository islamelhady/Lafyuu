package com.elhady.lafyuu.core.designsystem.components.card

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.button.SmallButton
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.Trash
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun AddressCard(
    name: String,
    address: String,
    phone: String,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(Theme.corner.small)
            .border(
                width = if (isSelected) Theme.size.border else Theme.size.border,
                color = if (isSelected) Theme.color.blue else Theme.color.neutralLight,
                shape = Theme.corner.small
            )
            .padding(Theme.space.large),
        verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
    ) {
        LafyuuText(
            text = name,
            color = Theme.color.neutralDark,
            style = Theme.typography.heading5
        )
        LafyuuText(
            text = address,
            style = Theme.typography.normalTextRegular,
            color = Theme.color.neutralGrey
        )
        LafyuuText(
            text = phone,
            style = Theme.typography.normalTextRegular,
            color = Theme.color.neutralGrey
        )
        Row(
            modifier = Modifier.padding(top = Theme.space.large),
            horizontalArrangement = Arrangement.spacedBy(Theme.space.extraLarge),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SmallButton(
                caption = "Edit",
                onClick = onEditClick,
                modifier = Modifier.width(Theme.size.huge),

                )
            Icon(
                imageVector = Trash,
                contentDescription = "Trash",
                tint = Theme.color.neutralGrey,
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 380, heightDp = 400)
@Composable
private fun AllCardAndListComponentsPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                var selected by remember { mutableStateOf(false) }

                SectionTitle("Address Card")
                AddressCard(
                    name = "Islam Elhady",
                    address = "3711 Spring Hill Rd undefined Tallahassee, Nevada 52874 United States",
                    phone = "+20 114 114 8538",
                    onEditClick = {
                        selected = !selected
                    },
                    isSelected = selected
                )
            }
        }
    }
}