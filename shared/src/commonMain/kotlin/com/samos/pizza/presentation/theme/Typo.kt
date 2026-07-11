package com.samos.pizza.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import pizza.shared.generated.resources.Res
import pizza.shared.generated.resources.figtree_extrabold
import pizza.shared.generated.resources.figtree_regular
import pizza.shared.generated.resources.figtree_semibold

@Composable
fun figtreeFontFamily(): FontFamily {
    val figtreeExtrabold = Font(
        resource = Res.font.figtree_extrabold,
        weight = FontWeight.ExtraBold,
        style = FontStyle.Normal,
    )
    val figtreeSemibold = Font(
        resource = Res.font.figtree_semibold,
        weight = FontWeight.SemiBold,
        style = FontStyle.Normal,
    )
    val figtreeRegular = Font(
        resource = Res.font.figtree_regular,
        weight = FontWeight.Normal,
        style = FontStyle.Normal,
    )
    return remember {
        FontFamily(
            figtreeExtrabold,
            figtreeSemibold,
            figtreeRegular,
        )
    }
}

@Composable
fun typography(): Typography {
    val customFont = figtreeFontFamily()

    return MaterialTheme.typography.copy(
        headlineSmall = MaterialTheme.typography.headlineSmall
            .copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 24.sp,
                fontFamily = customFont,
            ),
        titleMedium = MaterialTheme.typography.titleMedium
            .copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 24.sp,
                fontFamily = customFont,
            ),
        titleSmall = MaterialTheme.typography.titleSmall
            .copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 24.sp,
                fontFamily = customFont,
            ),
        bodyMedium = MaterialTheme.typography.bodyMedium
            .copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 24.sp,
                fontFamily = customFont,
            ),
        bodySmall = MaterialTheme.typography.bodySmall
            .copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp,
                fontFamily = customFont,
            ),
    )
}