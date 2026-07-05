package com.samos.pizza.presentation.screen.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import pizza.shared.generated.resources.Res
import pizza.shared.generated.resources.img_banana_for_scale

private const val BORDER_STROKE_WIDTH_PX = 2
private const val BUTTON_SIZE_DP = 48
private const val BUTTON_ELEVATION_DP = 3


@Composable
fun BananaSizeSelector(
    size: Int, // 0 для S, 1 для M, 2 для L
    onSizeChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.img_banana_for_scale),
            contentDescription = "Banana for scale",
            modifier = Modifier
                //.size(width = 220.dp, height = 120.dp)
                .padding(bottom = 48.dp)
                .graphicsLayer {
                    scaleX = 3f
                    scaleY = 3f
                }
        )

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val sizeLabels = listOf("S", "M", "L")

            sizeLabels.forEachIndexed { index, label ->
                val isSelected = size == index

                Surface(
                    modifier = Modifier
                        .then(
                            if (index == 1) {
                                Modifier.padding(
                                    start = if (index == 1) 32.dp else 0.dp,
                                    top = if (index == 1) 48.dp else 0.dp,
                                    end = if (index == 1) 32.dp else 0.dp,
                                )
                            } else Modifier
                        )
                        .size(if (isSelected) 52.dp else BUTTON_SIZE_DP.dp)
                        .then(
                            if (isSelected) {
                                Modifier.border(
                                    width = BORDER_STROKE_WIDTH_PX.dp,
                                    color = Color.White,
                                    shape = CircleShape
                                )
                            } else Modifier
                        ),
                    shape = RoundedCornerShape(BUTTON_SIZE_DP.dp),
                    color = if (isSelected) Color.Black else MaterialTheme.colorScheme.background,
                    shadowElevation = if (isSelected) 0.dp else BUTTON_ELEVATION_DP.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                }
                /*Surface(
                    modifier = Modifier.size(BUTTON_SIZE_DP.dp),
                    shape = CircleShape,
                    color = Color.Transparent,
                    shadowElevation = if (isSelected) 0.dp else BUTTON_ELEVATION_DP.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .then(
                                if (isSelected) {
                                    Modifier.border(
                                        width = BORDER_STROKE_WIDTH_PX.dp,
                                        color = Color.White,
                                        shape = CircleShape
                                    )
                                } else Modifier
                            )
                            .clip(CircleShape)
                            .background(if (isSelected) Color.Black else Color.White)
                            .clickable { onSizeChanged(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }*/
                /* Box(
                     modifier = Modifier
                         .size(48.dp)
                         .clip(CircleShape)
                         .background(if (isSelected) Color.Black else Color.White)
                         .clickable { onSizeChanged(index) },
                     contentAlignment = Alignment.Center
                 ) {
                     Text(
                         text = label,
                         color = if (isSelected) Color.White else Color.Black,
                         fontSize = 20.sp,
                         fontWeight = FontWeight.SemiBold
                     )
                 }*/
            }
        }
    }
}