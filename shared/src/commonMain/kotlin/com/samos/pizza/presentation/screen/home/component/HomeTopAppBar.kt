package com.samos.pizza.presentation.screen.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pizza.shared.generated.resources.Res
import pizza.shared.generated.resources.ic_arrow_back
import pizza.shared.generated.resources.ic_favorite
import pizza.shared.generated.resources.top_bar_category_title

private const val HIDE_TOP_BAR_OFFSET_PX = -400f
private const val BUTTON_SIZE_DP = 48
private const val BUTTON_ELEVATION_DP = 16

@Composable
fun HomeTopAppBar(
    title: String,
    animationProgress: Float,
    onBackClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
) {
    // Custom container structure instead of standard TopAppBar to allow wrapContentHeight and avoid clipping shadows
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .statusBarsPadding()
            .graphicsLayer {
                // When animationProgress is 1f (initial state) -> translationY = -400px (fully hidden off-screen)
                // When animationProgress is 0f (final state) -> translationY = 0px (perfectly positioned in place)
                translationY = HIDE_TOP_BAR_OFFSET_PX * animationProgress

                // Optional premium detail: alpha dims from invisible to visible alongside the translation glide
                alpha = 1f - animationProgress
            }
            .padding(start = 24.dp, top = 18.dp, end = 24.dp, bottom = 18.dp)
    ) {
        // 1. Navigation Button (Left Side Component Layer)
        Surface(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(BUTTON_SIZE_DP.dp)
                .align(Alignment.CenterStart),
            shape = RoundedCornerShape(BUTTON_SIZE_DP.dp),
            color = MaterialTheme.colorScheme.background,
            shadowElevation = BUTTON_ELEVATION_DP.dp
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = "Icon Back",
                    tint = Color.Unspecified,
                )
            }
        }

        // 2. Centered Text Column Layer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = (BUTTON_SIZE_DP + 18).dp) // Allocates safe spacing zones to avoid overlapping text bounds onto buttons
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.top_bar_category_title),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
        }

        // 3. Actions Layer Group (Right Side Component Layer)
        Surface(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(BUTTON_SIZE_DP.dp)
                .align(Alignment.CenterEnd), // Safely pin to the right middle section bounds
            shape = RoundedCornerShape(BUTTON_SIZE_DP.dp),
            color = MaterialTheme.colorScheme.background,
            shadowElevation = BUTTON_ELEVATION_DP.dp
        ) {
            IconButton(onClick = onLikeClick) {
                Icon(
                    painter = painterResource(Res.drawable.ic_favorite),
                    contentDescription = "Icon Add to Favorites",
                    tint = Color.Unspecified,
                )
            }
        }
    }
}
