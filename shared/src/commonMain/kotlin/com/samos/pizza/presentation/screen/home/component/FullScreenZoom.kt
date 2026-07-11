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
    val navigationEventState = rememberNavigationEventState(NavigationEventInfo.None) //

    NavigationBackHandler(
        state = navigationEventState,
        isBackEnabled = true,
        onBackCompleted = { onUnZoom() }
    )

    var isReadyToFly by remember { mutableStateOf(false) }

    val flightProgress by animateFloatAsState(
        targetValue = if (isReadyToFly) 1f else 0f,
        animationSpec = tween(durationMillis = 350),
        label = "PizzaFlightTransition"
    )

    var screenCenterY by remember { mutableFloatStateOf(0f) }

    var gestureScale by remember { mutableFloatStateOf(1f) }
    var gestureOffset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f * flightProgress))
            .onGloballyPositioned { coordinates ->
                screenCenterY = coordinates.positionInWindow().y + (coordinates.size.height / 2f)
                isReadyToFly = true
            }
            .pointerInput(flightProgress) {
                if (flightProgress < 1f) return@pointerInput
                detectTransformGestures { _, pan, zoom, _ ->
                    gestureScale = (gestureScale * zoom).coerceIn(1f, 7f)
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
            if (screenCenterY == 0f) 0f else startZoomPixelY - (screenCenterY - (startZoomPixelY / 2f))
        }

        val targetFullScreenScale = 4.8f

        val finalAlpha =
            if (screenCenterY == 0f) 0f
            else startZoomAlpha

        PizzaImageCore(
            imageUrl = currentPizzaUrl,
            contentDescription = "Animated Full Screen Zoom",
            scale = (startZoomScale + 0.02f + (targetFullScreenScale - startZoomScale) * flightProgress) * gestureScale,
            alphaValue = finalAlpha,
            modifier = Modifier.graphicsLayer {
                val flightY = initialStartOffset * (1f - flightProgress)

                translationX = gestureOffset.x * flightProgress
                translationY = flightY + (gestureOffset.y * flightProgress)
            }
        )

        if (flightProgress > 0.5f) {
            IconButton(
                onClick = onUnZoom,
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
