package com.elhady.lafyuu.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.elhady.lafyuu.core.designsystem.components.other.ProductCategory
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.Bikini
import com.elhady.lafyuu.core.designsystem.icons.Dress
import com.elhady.lafyuu.core.designsystem.icons.ManPants
import com.elhady.lafyuu.core.designsystem.icons.ManShoes
import com.elhady.lafyuu.core.designsystem.icons.ManUnderwear
import com.elhady.lafyuu.core.designsystem.icons.Shirt
import com.elhady.lafyuu.core.designsystem.icons.Skirt
import com.elhady.lafyuu.core.designsystem.icons.Tshirt
import com.elhady.lafyuu.core.designsystem.icons.WomanBag
import com.elhady.lafyuu.core.designsystem.icons.WomanPants
import com.elhady.lafyuu.core.designsystem.icons.WomanShoes
import com.elhady.lafyuu.core.designsystem.icons.WomanTshirt
import com.elhady.lafyuu.core.designsystem.icons.Short as ShortIcon
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.home.domain.model.Category

@Composable
fun CategoryListSection(
    categories: List<Category>,
    onCategoryClick: (categoryId: String, categoryName: String) -> Unit,
    onSeeMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (categories.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth()) {
        SectionTitle(
            title = "Category",
            actionText = "More Category",
            onActionClick = onSeeMoreClick
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = Theme.space.large),
            horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
        ) {
            items(
                items = categories,
                key = { it.id }
            ) { category ->
                ProductCategory(
                    label = category.name,
                    icon = getCategoryIcon(category.name),
                    onClick = { onCategoryClick(category.id, category.name) }
                )
            }
        }
    }
}

fun getCategoryIcon(categoryName: String): ImageVector {
    val name = categoryName.lowercase()
    return when {
        name.contains("charms") -> Tshirt
        name.contains("anklets") -> Shirt
        name.contains("pendants") -> Dress
        name.contains("earrings") -> Bikini
        name.contains("clothes") -> WomanBag
        name.contains("bags") -> WomanBag
        name.contains("jewelry") -> ManShoes
        name.contains("necklaces") -> ManUnderwear
        name.contains("bracelets") -> Bikini
        name.contains("rings") -> Skirt
        name.contains("glasses") -> WomanPants
        name.contains("sneakers") -> WomanShoes
        name.contains("watches") -> WomanTshirt
        name.contains("brooches") -> WomanTshirt
        name.contains("bangles") -> WomanTshirt
        name.contains("jewelry-sets") -> WomanTshirt
        else -> Shirt
    }
}
