package com.dev2012.berlinclock.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev2012.berlinclock.R
import com.dev2012.berlinclock.ui.theme.BerlinClockTheme
import org.koin.androidx.compose.koinViewModel
import kotlin.time.ExperimentalTime
import java.time.Instant


@OptIn(ExperimentalTime::class)
@Preview(
    showBackground = true
)
@Composable
fun BerlinClockPreview() {
    BerlinClockTheme {
        BerlinClockView(vm = FakeBerlinClockViewModel(Instant.now()))
    }
}


//While I could put these shapes in the class & remember them
// I think this makes it cleaner
val fullRounding = 16.dp
val halfRounding = 8.dp

val circleShape = CircleShape
val centerShape = RoundedCornerShape(corner = CornerSize(halfRounding))
val leftShape = RoundedCornerShape(
    topStart = fullRounding, topEnd = halfRounding, bottomEnd = halfRounding, bottomStart = fullRounding
)
val rightShape = RoundedCornerShape(
    topStart = halfRounding, topEnd = fullRounding, bottomEnd = fullRounding, bottomStart = halfRounding
)

@OptIn(ExperimentalTextApi::class)
val SevenSegmentFamily =
    FontFamily(
        Font(
            R.font.seven_segment,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(950),
                FontVariation.width(30f),
                FontVariation.slant(-6f),
            )
        )
    )

//RowScope for weight()
@Composable
fun RowScope.BerlinClockElement(
    modifier: Modifier = Modifier, shape: RoundedCornerShape, color: Color = MaterialTheme.colorScheme.primary
) {
    Box(
        modifier = modifier
            .weight(1f)
            .background(
                color = color, shape = shape
            )
            .border(width = 2.dp, color = MaterialTheme.colorScheme.outline, shape = shape),
        contentAlignment = Alignment.Center
    ) {}
}

@Composable
fun BerlinClockView(
    vm: BerlinClockUiProvider = koinViewModel<BerlinClockViewModel>()
) {
    val state by vm.uiState.collectAsState()

    val secondSize = 100.dp
    val elementModifier = Modifier.height(50.dp)

    val hourRowModifier = remember { Modifier.fillMaxWidth() }
    val minuteRowModifier = remember { Modifier.fillMaxWidth() }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(secondSize)
                .background(
                    color = if (state.seconds.isOn()) MaterialTheme.colorScheme.secondary else Color.Transparent,
                    shape = circleShape
                )
                .border(width = 2.dp, color = MaterialTheme.colorScheme.outline, shape = circleShape),
            contentAlignment = Alignment.Center
        ) {}
        Row(
            modifier = hourRowModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            repeat(4) { index ->
                BerlinClockElement(
                    modifier = elementModifier,
                    shape = if (index == 0) leftShape else if (index == 3) rightShape else centerShape,
                    color = if (state.hoursAccumulator >= index + 1) MaterialTheme.colorScheme.primary else Color.Transparent,
                )
            }
        }
        Row(
            modifier = hourRowModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            repeat(4) { index ->
                BerlinClockElement(
                    modifier = elementModifier,
                    shape = if (index == 0) leftShape else if (index == 3) rightShape else centerShape,
                    color = if (state.hoursRemainder >= index + 1) MaterialTheme.colorScheme.primary else Color.Transparent,
                )
            }
        }
        Row(
            modifier = minuteRowModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            BerlinClockElement(
                modifier = elementModifier,
                shape = leftShape,
                color = if (state.minutesAccumulator >= 1) MaterialTheme.colorScheme.secondary else Color.Transparent,
            )
            repeat(9) { index ->
                val color = if (state.minutesAccumulator >= index + 2)
                    if ((index + 2) % 3 == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                else Color.Transparent
                BerlinClockElement(
                    modifier = elementModifier,
                    shape = centerShape,
                    color = color,
                )
            }
            BerlinClockElement(
                modifier = elementModifier,
                shape = rightShape,
                color = if (state.minutesAccumulator >= 11) MaterialTheme.colorScheme.secondary else Color.Transparent,
            )
        }
        Row(
            modifier = minuteRowModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            repeat(4) { index ->
                BerlinClockElement(
                    modifier = elementModifier,
                    shape = if (index == 0) leftShape else if (index == 3) rightShape else centerShape,
                    color = if (state.minutesRemainder >= index + 1) MaterialTheme.colorScheme.secondary else Color.Transparent,
                )
            }
        }
        Text(text = state.timeString, fontFamily = SevenSegmentFamily)
    }
}
