package com.dev2012.berlinclock.ui

import android.annotation.SuppressLint
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev2012.berlinclock.R
import com.dev2012.berlinclock.ui.theme.BerlinClockTheme
import kotlin.time.ExperimentalTime


@SuppressLint("ViewModelConstructorInComposable")
@OptIn(ExperimentalTime::class)
@Preview(
    showBackground = true
)
@Composable
fun BerlinClockPreview() {
    BerlinClockTheme {
        BerlinClockView(vm = BerlinClockViewModel())
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
private fun HourRow(
    modifier: Modifier = Modifier,
    elementModifier: Modifier = Modifier,
    count: () -> Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        val currentCount = count()
        repeat(4) { index ->
            BerlinClockElement(
                modifier = elementModifier,
                shape = if (index == 0) leftShape else if (index == 3) rightShape else centerShape,
                color = if (currentCount >= index + 1) MaterialTheme.colorScheme.primary else Color.Transparent,
            )
        }
    }
}

@Composable
private fun MinuteAccumulatorRow(
    modifier: Modifier = Modifier,
    elementModifier: Modifier = Modifier,
    count: () -> Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        val currentCount = count()
        repeat(11) { index ->
            val color = if (currentCount >= index + 1) {
                if ((index + 1) % 3 == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            } else {
                Color.Transparent
            }
            BerlinClockElement(
                modifier = elementModifier,
                shape = when (index) {
                    0 -> leftShape
                    10 -> rightShape
                    else -> centerShape
                },
                color = color,
            )
        }
    }
}

@Composable
private fun MinuteRemainderRow(
    modifier: Modifier = Modifier,
    elementModifier: Modifier = Modifier,
    count: () -> Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        val currentCount = count()
        repeat(4) { index ->
            BerlinClockElement(
                modifier = elementModifier,
                shape = if (index == 0) leftShape else if (index == 3) rightShape else centerShape,
                color = if (currentCount >= index + 1) MaterialTheme.colorScheme.secondary else Color.Transparent,
            )
        }
    }
}

@Composable
fun BerlinClockView(
    vm: BerlinClockViewModel = viewModel<BerlinClockViewModel>()
) {
    val seconds by vm.secondsFlow.collectAsState()
    val minutesRemainder by vm.minuteRemainderFlow.collectAsState()
    val minutesAccumulator by vm.minuteAccumulatorFlow.collectAsState()
    val hoursRemainder by vm.hoursRemainderFlow.collectAsState()
    val hoursAccumulator by vm.hoursAccumulatorFlow.collectAsState()

    val timeString by vm.stringFlow.collectAsState()
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
                    color = if (seconds.isOn()) MaterialTheme.colorScheme.secondary else Color.Transparent,
                    shape = circleShape
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = circleShape
                ),
            contentAlignment = Alignment.Center
        ) {}

        HourRow(
            modifier = hourRowModifier,
            elementModifier = elementModifier,
            count = { hoursAccumulator }
        )
        HourRow(
            modifier = hourRowModifier,
            elementModifier = elementModifier,
            count = { hoursRemainder }
        )
        MinuteAccumulatorRow(
            modifier = minuteRowModifier,
            elementModifier = elementModifier,
            count = { minutesAccumulator }
        )
        MinuteRemainderRow(
            modifier = minuteRowModifier,
            elementModifier = elementModifier,
            count = { minutesRemainder }
        )
        Text(text = timeString, fontFamily = SevenSegmentFamily)
    }
}