package com.elhady.lafyuu.feature.explore.presentation

import com.elhady.lafyuu.feature.explore.domain.model.Category

enum class ExploreCategoryGroup(
    val title: String,
    val categoryNames: Set<String>
) {
    FINE_JEWELRY(
        title = "Fine Jewelry",
        categoryNames = setOf(
            "jewelry",
            "jewelry-sets",
            "necklaces",
            "pendants",
            "earrings",
            "rings"
        )
    ),

    WRIST_AND_ANKLE_WEAR(
        title = "Wrist & Ankle Wear",
        categoryNames = setOf(
            "bracelets",
            "bangles",
            "anklets",
            "charms"
        )
    ),

    FASHION_AND_LIFESTYLE(
        title = "Fashion & Lifestyle",
        categoryNames = setOf(
            "clothes",
            "bags",
            "sneakers"
        )
    ),

    ACCESSORIES_AND_WATCHES(
        title = "Accessories & Watches",
        categoryNames = setOf(
            "watches",
            "glasses",
            "brooches"
        )
    )
}

data class CategoryGroupUiModel(
    val group: ExploreCategoryGroup,
    val categories: List<Category>
)

fun groupByExploreCategory(
    categories: List<Category>
): List<CategoryGroupUiModel> =
    ExploreCategoryGroup.entries.mapNotNull { group ->
        categories
            .filter { category ->
                category.name.trim().lowercase() in group.categoryNames
            }
            .takeIf { it.isNotEmpty() }
            ?.let { groupedCategories ->
                CategoryGroupUiModel(
                    group = group,
                    categories = groupedCategories
                )
            }
    }
