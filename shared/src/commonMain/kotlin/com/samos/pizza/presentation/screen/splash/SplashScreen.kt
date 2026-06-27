package com.samos.pizza.presentation.screen.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import pizza.shared.generated.resources.Res
import pizza.shared.generated.resources.img_pizza_frame_0
import pizza.shared.generated.resources.img_pizza_frame_1
import pizza.shared.generated.resources.img_pizza_frame_2
import pizza.shared.generated.resources.img_pizza_frame_3
import pizza.shared.generated.resources.img_pizza_frame_4
import pizza.shared.generated.resources.img_pizza_frame_5
import pizza.shared.generated.resources.img_pizza_frame_6
import pizza.shared.generated.resources.img_pizza_frame_7
import pizza.shared.generated.resources.img_pizza_frame_8

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = koinViewModel(),
) {

    // Tracks how many slices of pizza are currently unlocked (-1 to 8)
    var visibleFramesCount by remember { mutableStateOf(-1) }

    // Tracks whether the animation is currently building up (true) or fading away (false)
    var isBuildingUp by remember { mutableStateOf(true) }

    // Static array reference storing our ordered image resources
    val pizzaFrames = remember {
        listOf(
            Res.drawable.img_pizza_frame_0,
            Res.drawable.img_pizza_frame_1,
            Res.drawable.img_pizza_frame_2,
            Res.drawable.img_pizza_frame_3,
            Res.drawable.img_pizza_frame_4,
            Res.drawable.img_pizza_frame_5,
            Res.drawable.img_pizza_frame_6,
            Res.drawable.img_pizza_frame_7,
            Res.drawable.img_pizza_frame_8,
        )
    }

    // Infinite loop managing the progressive breakdown and buildup state cycles
    LaunchedEffect(Unit) {
        while (true) {
            if (isBuildingUp) {
                // Progressively reveal slices one by one
                for (i in pizzaFrames.indices) {
                    visibleFramesCount = i
                    delay(60)
                }
                // Pause briefly once the full pizza is assembled
                delay(200)
                isBuildingUp = false
            } else {
                // Progressively fade out slices one by one in reverse order
                for (i in pizzaFrames.indices.reversed()) {
                    visibleFramesCount = i - 1
                    delay(60)
                }
                // Pause briefly once the screen is completely cleared
                delay(300)
                isBuildingUp = true
            }
        }
    }

    // Screen wrapper filling viewport with a solid white background color
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        // Stack all frames layout-wise on top of each other inside the same Box anchor
        pizzaFrames.forEachIndexed { index, drawableRes ->
            // Condition checks if this specific slice's turn to appear has arrived
            val targetAlpha = if (index <= visibleFramesCount) 1f else 0f

            // Handles smooth transitions individually for every layered slice asset
            val animatedAlpha by animateFloatAsState(
                targetValue = targetAlpha,
                animationSpec = tween(
                    durationMillis = 350 // Adjusted duration for rapid and responsive state changes
                ),
                label = "PizzaSliceAlpha_$index"
            )

            // Render the asset only if its opacity layer has crossed the invisible threshold
            if (animatedAlpha > 0f) {
                Image(
                    painter = painterResource(drawableRes),
                    contentDescription = "Layered pizza slice number $index",
                    modifier = Modifier
                        .size(200.dp)
                        .alpha(animatedAlpha) // Applies the separate animated state target values
                )
            }
        }
    }
}

/**
 * One more nice animation variant
 */
/*@Composable
fun SplashScreen() {
    // Tracks how many slices of pizza are currently unlocked (-1 to 7)
    var visibleFramesCount by remember { mutableStateOf(-1) }

    // Tracks whether the animation is currently building up (true) or fading away (false)
    var isBuildingUp by remember { mutableStateOf(true) }

    // Static array reference storing our ordered image resources
    val pizzaFrames = remember {
        listOf(
            Res.drawable.img_pizza_frame_1,
            Res.drawable.img_pizza_frame_2,
            Res.drawable.img_pizza_frame_3,
            Res.drawable.img_pizza_frame_4,
            Res.drawable.img_pizza_frame_5,
            Res.drawable.img_pizza_frame_6,
            Res.drawable.img_pizza_frame_7,
            Res.drawable.img_pizza_frame_8,
        )
    }

    // Infinite loop managing the progressive buildup and simultaneous fade-out cycles
    LaunchedEffect(Unit) {
        while (true) {
            if (isBuildingUp) {
                // Progressively reveal slices one by one
                for (i in pizzaFrames.indices) {
                    visibleFramesCount = i
                    delay(60)
                }
                // Pause briefly once the full pizza is completely assembled
                delay(200) // Increased slightly so the user can enjoy the full pizza before it vanishes
                isBuildingUp = false
            } else {
                // FIXED: Instant reset to -1 forces all layered frames to trigger targetAlpha = 0f simultaneously
                visibleFramesCount = -1

                // Wait for the duration of the fade-out animation before restarting the loop
                delay(350)

                // Pause briefly while the screen is completely cleared
                delay(200)
                isBuildingUp = true
            }
        }
    }

    // Screen wrapper filling viewport with a solid white background color
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        // Stack all frames layout-wise on top of each other inside the same Box anchor
        pizzaFrames.forEachIndexed { index, drawableRes ->
            // Condition checks if this specific slice's turn to appear has arrived
            val targetAlpha = if (index <= visibleFramesCount) 1f else 0f

            // Handles smooth transitions individually for every layered slice asset
            val animatedAlpha by animateFloatAsState(
                targetValue = targetAlpha,
                animationSpec = tween(
                    durationMillis = 350 // Handled uniformly during sequential buildup and simultaneous fade-out
                ),
                label = "PizzaSliceAlpha_$index"
            )

            // Render the asset only if its opacity layer has crossed the invisible threshold
            if (animatedAlpha > 0f) {
                Image(
                    painter = painterResource(drawableRes),
                    contentDescription = "Layered pizza slice number $index",
                    modifier = Modifier
                        .size(200.dp)
                        .alpha(animatedAlpha) // Applies the separate animated state target values
                )
            }
        }
    }
}*/
