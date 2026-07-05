package com.samos.pizza.presentation.screen.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.samos.pizza.presentation.base.TransparentSystemBarsEffect
import com.samos.pizza.presentation.screen.home.component.BananaSizeSelector
import com.samos.pizza.presentation.screen.home.component.HomeTopAppBar
import com.samos.pizza.presentation.screen.home.component.PizzaOrderBottomBar
import com.samos.pizza.presentation.screen.home.component.PizzasCarousel
import com.samos.pizza.presentation.screen.home.viewmodel.HomeIntent
import com.samos.pizza.presentation.screen.home.viewmodel.HomeScreenViewModel
import com.samos.pizza.presentation.screen.home.viewmodel.HomeState
import org.koin.compose.viewmodel.koinViewModel

// Configuration constants for the background animation physics and dimensions
private const val TRANSITION_DURATION_MS = 1400
private const val CANVAS_HEIGHT_MULTIPLIER = 1.1f
private const val OVAL_WIDTH_TO_HEIGHT_RATIO = 0.8f
private const val OVAL_VERTICAL_STRETCH_RATIO = 1.4f
private const val FINAL_RESTING_HEIGHT_FRACTION = 0.62f
private const val INITIAL_Y_START_OFFSET = -200f


@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreenContent(
        state = state,
        handleIntent = { viewModel.handleIntent(it) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContent(
    state: HomeState,
    handleIntent: (HomeIntent) -> Unit = {},
) {
    TransparentSystemBarsEffect()

    var isAnimated by remember { mutableStateOf(false) }

    // Interpolates background state layout progression
    val animationProgress by animateFloatAsState(
        targetValue = if (isAnimated) 0f else 1f,
        animationSpec = tween(durationMillis = TRANSITION_DURATION_MS),
        label = "FullScreenToHalfArcTransition"
    )

    LaunchedEffect(Unit) {
        isAnimated = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Renders the background layered canvas geometry
        Canvas(modifier = Modifier.fillMaxSize()) {
            val virtualCanvasHeight = size.height * CANVAS_HEIGHT_MULTIPLIER
            val ovalWidth = virtualCanvasHeight * OVAL_WIDTH_TO_HEIGHT_RATIO
            val ovalHeight = ovalWidth * OVAL_VERTICAL_STRETCH_RATIO

            val targetY = (virtualCanvasHeight * FINAL_RESTING_HEIGHT_FRACTION) - ovalHeight
            val animatedY = targetY + (INITIAL_Y_START_OFFSET - targetY) * animationProgress

            drawArc(
                color = Color(0xFFF3E3DA),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = true,
                topLeft = Offset(
                    x = (size.width - ovalWidth) / 2f,
                    y = animatedY
                ),
                size = Size(width = ovalWidth, height = ovalHeight)
            )
        }

        if (state.pizzas.isNotEmpty()) {
            val currentPizza = state.pizzas[state.currentIndex]

            Scaffold(
                topBar = {
                    HomeTopAppBar(
                        title = currentPizza.name,
                        animationProgress = animationProgress,
                        onBackClick = { handleIntent(HomeIntent.OnBackClick) }
                    )
                },
                containerColor = Color.Transparent,
            ) { innerPadding ->
                /*Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PizzasCarousel(
                        state = state,
                        animationProgress = animationProgress,
                        handleIntent = handleIntent,
                    )
                    BananaSizeSelector(
                        size = state.selectedSize,
                        modifier = Modifier,
                        onSizeChanged = {},
                    )
                    // 2. Animated Content Block managing text description fading state responses
                    AnimatedContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp, top = 40.dp, end = 32.dp),
                        targetState = currentPizza.description,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(durationMillis = 400)) togetherWith
                                    fadeOut(animationSpec = tween(durationMillis = 300))
                        },
                        label = "PizzaDescriptionTransition",
                    ) { targetDescription ->
                        Text(
                            text = targetDescription,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 15.sp,
                            lineHeight = 24.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Start
                        )
                    }
                    PizzaOrderBottomBar(
                        amount = state.amount,
                        singlePrice = currentPizza.variants[0].price,
                        modifier = Modifier
                            //  Applies fluid translation and fade-in synchronization bound to animationProgress parameters
                            .graphicsLayer {
                                // Maximum downward distance offset pixel rate for the hidden out-of-screen start position
                                val maxSlideDistancePx = 500f

                                // When animationProgress is 1f (initial) -> translationY = 500px (completely hidden underneath the screen viewport)
                                // When animationProgress is 0f (resting) -> translationY = 0px (perfectly positioned in place)
                                translationY = maxSlideDistancePx * animationProgress

                                // Converts linear progress (1f to 0f) to linear visibility (0f to 1f)
                                val linearVisibility = 1f - animationProgress

                                // Uses custom easing curve coordinates to hold off the opacity layer reveal process
                                // Prevents the bottom bar elements from blinking into existence prematurely while the full oval handles transitions
                                val delayedEasing = CubicBezierEasing(0.6f, 0.0f, 1.0f, 1.0f)
                                alpha = delayedEasing.transform(linearVisibility)
                            },
                        onMinusClick = {},
                        onPlusClick = {},
                        onAddClick = {},
                    )
                }*/
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding())
                ) {
                    val availableHeight = maxHeight

                    // 1. Scalable Upper/Middle Content Flow Layout Container
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Allocates roughly 38-40% of the viewport height strictly to the carousel frame to avoid vertical squeezing
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(availableHeight * 0.5f),
                            contentAlignment = Alignment.Center
                        ) {
                            PizzasCarousel(
                                state = state,
                                animationProgress = animationProgress,
                                handleIntent = handleIntent,
                            )
                        }

                        // Dedicated slot zone for the custom size picker controller component
                        /*Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(availableHeight * 0.12f)
                                // Synchronized slide-up and premium delayed fade-in effects matching the top bar setup
                                .graphicsLayer {
                                    translationY = 150f * animationProgress
                                    val delayedEasing = CubicBezierEasing(0.7f, 0.0f, 1.0f, 1.0f)
                                    alpha = delayedEasing.transform(1f - animationProgress)
                                },
                            contentAlignment = Alignment.Center
                        ) {}*/
                    }

                    BananaSizeSelector(
                        size = state.selectedSize,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 148.dp)
                            .align(Alignment.Center)
                            .graphicsLayer {
                                translationY = 150f * animationProgress
                                val delayedEasing = CubicBezierEasing(0.7f, 0.0f, 1.0f, 1.0f)
                                alpha = delayedEasing.transform(1f - animationProgress)
                            },
                        onSizeChanged = {},
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .align(Alignment.BottomCenter),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AnimatedContent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 32.dp, end = 32.dp, bottom = 8.dp)
                                .graphicsLayer {
                                    translationY = 200f * animationProgress
                                    val delayedEasing = CubicBezierEasing(0.8f, 0.0f, 1.0f, 1.0f)
                                    alpha = delayedEasing.transform(1f - animationProgress)
                                },
                            targetState = currentPizza.description,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(durationMillis = 200)) togetherWith
                                        fadeOut(animationSpec = tween(durationMillis = 160))
                            },
                            label = "PizzaDescriptionTransition",
                        ) { targetDescription ->
                            Text(
                                text = targetDescription,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Start
                            )
                        }

                        PizzaOrderBottomBar(
                            amount = state.amount,
                            singlePrice = currentPizza.variants[0].price,
                            modifier = Modifier
                                .navigationBarsPadding()
                                .padding(16.dp)
                                .graphicsLayer {
                                    val maxSlideDistancePx = 500f
                                    translationY = maxSlideDistancePx * animationProgress
                                    val linearVisibility = 1f - animationProgress
                                    val delayedEasing = CubicBezierEasing(0.6f, 0.0f, 1.0f, 1.0f)
                                    alpha = delayedEasing.transform(linearVisibility)
                                },
                            onMinusClick = {},
                            onPlusClick = {},
                            onAddClick = {},
                        )
                    }
                }
            }
        }
    }
}


