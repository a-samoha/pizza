package com.samos.pizza.presentation.screen.home.component

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.samos.pizza.domain.model.PizzaModel.PizzaSize
import com.samos.pizza.presentation.screen.home.viewmodel.HomeIntent
import com.samos.pizza.presentation.screen.home.viewmodel.HomeState
import kotlin.math.absoluteValue

// CAROUSEL
const val CAROUSEL_HEIGHT_DP = 380
private const val INFINITE_LOOP_PAGE_COUNT = 1000
private const val MAIN_PIZZA_SCALE = 1.2f
private const val CAROUSEL_HORIZONTAL_PADDING_DP = 96
private const val CAROUSEL_PIZZA_SIZE_DP = 220
private const val CAROUSEL_SCALE_SHRINK_SPEED = 1.5f
private const val CAROUSEL_SCALE_MIN_LIMIT = 0.65f
private const val CAROUSEL_MAX_ALPHA_REDUCTION = 0.70f
private const val CAROUSEL_ALPHA_MULTIPLIER = 1.0f

private const val SIZE_SCALE_STEP = 0.16f
private const val SIZE_ANIMATION_DURATION_MS = 300

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PizzasCarousel(
    state: HomeState,
    animationProgress: Float,
    modifier: Modifier = Modifier,
    handleIntent: (HomeIntent) -> Unit = {},
) {
    val initialVirtualPage = remember {
        (INFINITE_LOOP_PAGE_COUNT / 2) - ((INFINITE_LOOP_PAGE_COUNT / 2) % state.pizzas.size) + state.currentIndex
    }

    val pagerState = rememberPagerState(
        initialPage = initialVirtualPage,
        pageCount = { INFINITE_LOOP_PAGE_COUNT }
    )

    // Maps the PizzaSize enum token to a relative scale factor deviation modifier
    val targetSizeScaleModifier = remember(state.currentSize) {
        when (state.currentSize) {
            PizzaSize.S -> -SIZE_SCALE_STEP  // Shrinks centered pizza by -0.1f
            PizzaSize.M -> 0f                // Medium stays at baseline MAIN_PIZZA_SCALE
            PizzaSize.L -> SIZE_SCALE_STEP   // Expands centered pizza by +0.1f
        }
    }

    // Smoothly animates the size scale factor shift whenever currentSize updates
    val animatedSizeScaleModifier by animateFloatAsState(
        targetValue = targetSizeScaleModifier,
        animationSpec = tween(durationMillis = SIZE_ANIMATION_DURATION_MS),
        label = "PizzaSizeSmoothTransition"
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { virtualPage ->
            val actualIndex = virtualPage % state.pizzas.size
            handleIntent(HomeIntent.OnSwipe(actualIndex))
        }
    }

    LaunchedEffect(state.currentIndex) {
        val currentActualIndex = pagerState.currentPage % state.pizzas.size
        if (currentActualIndex != state.currentIndex) {
            val targetVirtualPage = pagerState.currentPage + (state.currentIndex - currentActualIndex)
            pagerState.scrollToPage(targetVirtualPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .graphicsLayer {
                val linearVisibility = 1f - animationProgress
                val delayedEasing = CubicBezierEasing(1.0f, 0.0f, 1.0f, 1.0f)
                alpha = delayedEasing.transform(linearVisibility)
            },
        contentPadding = PaddingValues(horizontal = CAROUSEL_HORIZONTAL_PADDING_DP.dp),
        beyondViewportPageCount = 1
    ) { page ->
        val actualPizzaIndex = page % state.pizzas.size
        val pizzaItem = state.pizzas[actualPizzaIndex]

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // Reading state parameters here bypasses the composition phase completely, running smoothly at 120 FPS.
                    val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

                    val centerFactor = (1f - pageOffset).coerceIn(0f, 1f)
                    val baseCenterScale = MAIN_PIZZA_SCALE + (animatedSizeScaleModifier * centerFactor)

                    val scaleReduction = (pageOffset * CAROUSEL_SCALE_SHRINK_SPEED)
                        .coerceIn(0f, CAROUSEL_SCALE_MIN_LIMIT)
                    val scale = baseCenterScale - scaleReduction

                    scaleX = scale
                    scaleY = scale

                    val alphaReduction = (pageOffset * CAROUSEL_ALPHA_MULTIPLIER)
                        .coerceIn(0f, CAROUSEL_MAX_ALPHA_REDUCTION)
                    alpha = 1f - alphaReduction
                },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = pizzaItem.imageUrl,
                contentDescription = "Carousel pizza item: ${pizzaItem.name}",
                modifier = Modifier.size(CAROUSEL_PIZZA_SIZE_DP.dp)
            )
        }
    }
}