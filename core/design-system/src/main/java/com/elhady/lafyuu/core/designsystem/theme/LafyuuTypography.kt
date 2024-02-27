package com.elhady.lafyuu.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import com.elhady.lafyuu.core.designsystem.R

val Raleway = FontFamily(
    Font(R.font.raleway_bold, FontWeight.Bold),
    Font(R.font.raleway_semibold, FontWeight.SemiBold),
    Font(R.font.raleway_regular, FontWeight.Normal),
)

val NunitoSans = FontFamily(
    Font(R.font.nunito_sans_light, FontWeight.Light),
    Font(R.font.nunito_sans_regular, FontWeight.Normal),
    Font(R.font.nunito_sans_medium, FontWeight.Medium),
    Font(R.font.nunito_sans_semibold, FontWeight.SemiBold),
    Font(R.font.nunito_sans_bold, FontWeight.Bold)
)

object LafyuuTypography {

    val HeroTitle = TextStyle(
        fontFamily = Raleway,
        fontSize = 50.sp,
        lineHeight = 54.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.5).sp
    )

    val Display = TextStyle(
        fontFamily = Raleway,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.Bold
    )

    val ScreenTitle = TextStyle(
        fontFamily = Raleway,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Bold
    )
    val Title = ScreenTitle
    val SectionTitle = ScreenTitle

    val SubtitleCustom = TextStyle(
        fontFamily = NunitoSans,
        fontSize = 19.sp,
        lineHeight = 27.sp,
        fontWeight = FontWeight.Light
    )

    val BodyLarge = TextStyle(
        fontFamily = NunitoSans,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal
    )

    val BodyMedium = TextStyle(
        fontFamily = NunitoSans,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal
    )
    val Body = BodyMedium
    val Caption = BodyMedium.copy(fontSize = 12.sp, lineHeight = 16.sp)

    val ButtonText = TextStyle(
        fontFamily = NunitoSans,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.SemiBold
    )
    val LabelLarge = ButtonText
    val Label = BodyMedium

    val PriceCurrent = TextStyle(
        fontFamily = NunitoSans,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold
    )
    val Price = PriceCurrent

    val PriceOriginal = TextStyle(
        fontFamily = NunitoSans,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Normal,
        textDecoration = TextDecoration.LineThrough
    )
    val PriceOld = PriceOriginal

    val Material = Typography(
        displayLarge = HeroTitle,
        headlineLarge = Display,
        headlineMedium = ScreenTitle,
        bodyLarge = BodyLarge,
        bodyMedium = BodyMedium,
        labelLarge = ButtonText
    )
}
