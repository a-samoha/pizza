package com.samos.pizza.presentation.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pizza.shared.generated.resources.Res
import pizza.shared.generated.resources.add
import pizza.shared.generated.resources.ic_minus
import pizza.shared.generated.resources.ic_plus

private const val COUNTER_BACKGROUND_HEX = 0xFFF3E3DA
private const val ADD_BUTTON_BG_HEX = 0xFF19BFFF
private const val COUNTER_RADIUS_DP = 32
private const val SHADOW_ELEVATION_DP = 2
private const val PRISE_SIZE_DEF = 24
private const val PRISE_SIZE_SMALL = 20

@Composable
fun PizzaOrderBottomBar(
    amount: Int,
    singlePrice: Double,
    modifier: Modifier = Modifier,
    onMinusClick: () -> Unit = {},
    onPlusClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    // Dynamically calculates the final aggregate price based on the selected amount state
    val totalPrice = singlePrice * amount
    // Formats the output text string to always preserve standard decimal point rendering (e.g. $15.99)
    val formattedPrice = "\$${formatKmpDecimal(totalPrice)}"

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Dual-Button Amount Counter Component Group
        Row(
            modifier = Modifier
                .width(144.dp)
                .height(48.dp)
                .background(
                    color = Color(COUNTER_BACKGROUND_HEX),
                    shape = RoundedCornerShape(COUNTER_RADIUS_DP.dp)
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decrement Control Element Anchor
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(COUNTER_RADIUS_DP.dp),
                color = Color.White,
                shadowElevation = SHADOW_ELEVATION_DP.dp
            ) {
                IconButton(
                    onClick = onMinusClick,
                    enabled = amount > 1 // Block subtraction attempts below initial baseline levels
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_minus), // Ensure your resource file maps onto this token
                        contentDescription = "Decrease unit count",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }

            // Numeric Display Segment Layer
            Text(
                text = amount.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            // Increment Control Element Anchor
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(COUNTER_RADIUS_DP.dp),
                color = Color.White,
                shadowElevation = SHADOW_ELEVATION_DP.dp
            ) {
                IconButton(onClick = onPlusClick) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_plus), // Ensure your resource file maps onto this token
                        contentDescription = "Increase unit count",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // 2. Aggregate Final Price Text Field Label Component
        Text(
            text = formattedPrice,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 1,
            fontSize = if (formattedPrice.length <= 9) PRISE_SIZE_DEF.sp else PRISE_SIZE_SMALL.sp,
            style = MaterialTheme.typography.headlineSmall,
        )

        // 3. Primary Cart Dispatch Action Button Container
        Button(
            onClick = onAddClick,
            modifier = Modifier
                .width(86.dp)
                .height(48.dp),
            shape = RoundedCornerShape(27.dp), // Complete pill corner rounding transformation mapping parameters
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(ADD_BUTTON_BG_HEX)
            ),
            contentPadding = PaddingValues(0.dp) // Reset padding behaviors to center explicit string elements neatly
        ) {
            Text(
                text = stringResource(Res.string.add),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.background,
            )
        }
    }
}

private fun formatKmpDecimal(value: Double): String {
    val rounded = (value * 100 + 0.5).toLong() / 100.0
    val wholePart = rounded.toLong()
    val fractionalPart = ((rounded - wholePart) * 100 + 0.5).toInt()

    // Pads a trailing zero if the fractional values falls beneath double digits (e.g. .9 turns into .90)
    val paddedFraction = if (fractionalPart < 10) "0$fractionalPart" else "$fractionalPart"
    return "$wholePart.$paddedFraction"
}