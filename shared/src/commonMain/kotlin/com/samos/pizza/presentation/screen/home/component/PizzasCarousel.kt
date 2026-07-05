package com.samos.pizza.presentation.screen.home.component

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.samos.pizza.presentation.screen.home.viewmodel.HomeIntent
import com.samos.pizza.presentation.screen.home.viewmodel.HomeState
import kotlin.math.absoluteValue

// CAROUSEL
private const val INFINITE_LOOP_PAGE_COUNT = 1000
private const val MAIN_PIZZA_SCALE = 1.2f
private const val CAROUSEL_HEIGHT_DP = 380
private const val CAROUSEL_HORIZONTAL_PADDING_DP = 96
private const val CAROUSEL_PIZZA_SIZE_DP = 220
private const val CAROUSEL_SCALE_SHRINK_SPEED = 1.5f
private const val CAROUSEL_SCALE_MIN_LIMIT = 0.65f
private const val CAROUSEL_MAX_ALPHA_REDUCTION = 0.70f
private const val CAROUSEL_ALPHA_MULTIPLIER = 1.0f

@Composable
fun PizzasCarousel(
    state: HomeState,
    animationProgress: Float,
    handleIntent: (HomeIntent) -> Unit = {},
) {
    val initialVirtualPage = remember {
        (INFINITE_LOOP_PAGE_COUNT / 2) - ((INFINITE_LOOP_PAGE_COUNT / 2) % state.pizzas.size) + state.currentIndex
    }

    val pagerState = rememberPagerState(
        initialPage = initialVirtualPage,
        pageCount = { INFINITE_LOOP_PAGE_COUNT }
    )

    // Observes user swipe interactions to emit dynamic OnSwipe intent events back to ViewModel
    LaunchedEffect(pagerState) {
        //  Using settledPage instead of currentPage ensures stable frame index tracking
        snapshotFlow { pagerState.settledPage }.collect { virtualPage ->
            val actualIndex = virtualPage % state.pizzas.size

            // Emits the intent event for ALL changes, ensuring index 0 is fully supported and processed
            handleIntent(HomeIntent.OnSwipe(actualIndex))
        }
    }

    // Sync programmatic index modifications enforced directly from incoming external State layers
    LaunchedEffect(state.currentIndex) {
        val currentActualIndex = pagerState.currentPage % state.pizzas.size
        if (currentActualIndex != state.currentIndex) {
            val targetVirtualPage =
                pagerState.currentPage + (state.currentIndex - currentActualIndex)
            pagerState.scrollToPage(targetVirtualPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(CAROUSEL_HEIGHT_DP.dp)
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

        val pageOffset =
            ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val baseCenterScale = MAIN_PIZZA_SCALE

                    // Increase the multiplier to enforce rapid shrinkage on side views as they move away from the center anchor
                    val dynamicScaleMultiplier = CAROUSEL_SCALE_SHRINK_SPEED
                    val maxReductionLimit = CAROUSEL_SCALE_MIN_LIMIT

                    val scaleReduction = (pageOffset * dynamicScaleMultiplier)
                        .coerceIn(0f, maxReductionLimit)
                    val scale = baseCenterScale - scaleReduction

                    scaleX = scale
                    scaleY = scale

                    // Standard alpha transparency fade calculation remains untouched
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
