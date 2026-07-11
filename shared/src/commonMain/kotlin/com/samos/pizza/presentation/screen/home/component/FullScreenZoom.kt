package com.samos.pizza.presentation.screen.home.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.dp
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import org.jetbrains.compose.resources.painterResource
import pizza.shared.generated.resources.Res
import pizza.shared.generated.resources.ic_close

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FullScreenZoom(
    currentPizzaUrl: String,
    startZoomScale: Float,
    startZoomAlpha: Float = 1f,
    startZoomPixelY: Float,
    onUnZoom: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isReturning by remember { mutableStateOf(false) }
    var isReadyToFly by remember { mutableStateOf(false) }

    val flightProgress by animateFloatAsState(
        targetValue = if (isReadyToFly && !isReturning) 1f else 0f,
        animationSpec = tween(durationMillis = 350),
        label = "PizzaFlightTransition"
    )

    LaunchedEffect(flightProgress, isReturning) {
        if (isReturning && flightProgress == 0f) {
            onUnZoom()
        }
    }

    val navigationEventState = rememberNavigationEventState(NavigationEventInfo.None)
    NavigationBackHandler(
        state = navigationEventState,
        isBackEnabled = !isReturning,
        onBackCompleted = { isReturning = true }
    )

    var screenCenterY by remember { mutableFloatStateOf(0f) }
    var gestureScale by remember { mutableFloatStateOf(1f) }
    var gestureOffset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f * flightProgress))
            .onGloballyPositioned { coordinates ->
                screenCenterY = coordinates.positionInWindow().y + (coordinates.size.height / 2f)
                isReadyToFly = true
            }
            .pointerInput(flightProgress, isReturning) {
                if (flightProgress < 1f || isReturning) return@pointerInput
                detectTransformGestures { _, pan, zoom, _ ->
                    val oldScale = gestureScale
                    gestureScale = (gestureScale * zoom).coerceIn(0.28f, 7f)

                    if (gestureScale < 0.3f && oldScale >= 0.3f) {
                        isReturning = true
                    }

                    if (gestureScale > 1f) {
                        gestureOffset += pan
                    } else {
                        gestureOffset = Offset.Zero
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        val initialStartOffset = remember(startZoomPixelY, screenCenterY) {
            if (screenCenterY == 0f) 0f else startZoomPixelY + 20f - (screenCenterY - (startZoomPixelY / 2f))
        }

        val targetFullScreenScale = 4.8f

        val finalAlpha =
            if (screenCenterY == 0f) 0f
            else startZoomAlpha

        val currentGestureScale = if (isReturning) 1f else gestureScale

        PizzaImageCore(
            imageUrl = currentPizzaUrl,
            contentDescription = "Animated Full Screen Zoom",
            scale = (startZoomScale + (targetFullScreenScale - startZoomScale) * flightProgress) * currentGestureScale,
            alphaValue = finalAlpha,
            modifier = Modifier.graphicsLayer {
                val flightY = initialStartOffset * (1f - flightProgress)

                translationX = gestureOffset.x * flightProgress
                translationY = flightY + (gestureOffset.y * flightProgress)
            }
        )

        if (flightProgress > 0.5f && !isReturning) {
            IconButton(
                onClick = { isReturning = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 48.dp, end = 24.dp)
                    .graphicsLayer { alpha = (flightProgress - 0.5f) * 2f }
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.1f))
            ) {
                androidx.compose.material3.Icon(
                    painter = painterResource(Res.drawable.ic_close),
                    contentDescription = "Close",
                    tint = Color.Black
                )
            }
        }
    }
}
