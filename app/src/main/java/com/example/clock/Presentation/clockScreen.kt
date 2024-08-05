package com.example.clock.Presentation


import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.clock.R
import kotlinx.coroutines.delay
import java.time.LocalTime
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlinx.coroutines.launch
import com.example.clock.domainLayer.CityTimeZone
import com.example.clock.Presentation.ViewModels.ClockViewModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun clockScreen(navController: NavController, viewModel: ClockViewModule) {
    val selectedCities by viewModel.selectedCities.collectAsState(initial = emptyList())
    Log.d("clockScreenslected","$selectedCities")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clock") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))


                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .padding(8.dp)
                        ) {
                            AnalogClock()
                        }

                        val currentTime = remember { mutableStateOf(LocalTime.now()) }


                        LaunchedEffect(Unit) {
                            while (true) {
                                currentTime.value = LocalTime.now()
                                delay(1000L)
                            }
                        }

                        Text(
                            text = String.format(
                                "%02d:%02d:%02d",
                                currentTime.value.hour,
                                currentTime.value.minute,
                                currentTime.value.second
                            ),
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = 48.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }


                    if (selectedCities.isEmpty()) {
                        // Display message and vector image when no alarms are set
                        item {
                            NoTimeZone()
                        }
                    } else {


                        items(selectedCities, key = { cityTimeZone ->
                            cityTimeZone.city
                        }) { cityTimeZone ->
                            worldClock(
                                cityTimeZone,
                                currentDateTime = LocalDateTime.now(),
                                onDelete = { viewModel.deleteCity(it.city) }
                            )
                        }
                    }
                }


                FloatingActionButton(
                    onClick = {
                        navController.navigate("timeZone")
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 100.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = "Add Country Time"
                    )
                }
            }
        }
    )
}
@Composable
fun AnalogClock(modifier: Modifier = Modifier) {
    val currentTime = remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = LocalTime.now()
            kotlinx.coroutines.delay(1000L)
        }
    }

    val hourRotation by remember { derivedStateOf { (currentTime.value.hour % 12) * 30f + currentTime.value.minute * 0.5f } }
    val minuteRotation by remember { derivedStateOf { currentTime.value.minute * 6f + currentTime.value.second * 0.1f } }
    val secondRotation by remember { derivedStateOf { currentTime.value.second * 6f } }

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            drawRoundRect(
                color = Color.White,
                topLeft = Offset.Zero,
                size = Size(size.width, size.height),
                cornerRadius = CornerRadius(100f)
            )


            for (i in 0..11) {
                val angle = (i * 30f - 90) * (PI / 180f).toFloat()
                val lineStart = Offset(
                    x = size.width / 2 + cos(angle) * size.width * 0.35f,
                    y = size.height / 2 + sin(angle) * size.height * 0.35f
                )
                val lineEnd = Offset(
                    x = size.width / 2 + cos(angle) * size.width * 0.45f,
                    y = size.height / 2 + sin(angle) * size.height * 0.45f
                )
                drawLine(
                    color = Color.Black,
                    start = lineStart,
                    end = lineEnd,
                    strokeWidth = 8f,
                    cap = StrokeCap.Round
                )
            }


            drawLine(
                color = Color.Black,
                start = Offset(size.width / 2, size.height / 2),
                end = Offset(
                    x = size.width / 2 + cos((hourRotation - 90) * (PI / 180)).toFloat() * size.width * 0.2f,
                    y = size.height / 2 + sin((hourRotation - 90) * (PI / 180)).toFloat() * size.height * 0.2f
                ),
                strokeWidth = 16f,
                cap = StrokeCap.Round
            )


            drawLine(
                color = Color.Black,
                start = Offset(size.width / 2, size.height / 2),
                end = Offset(
                    x = size.width / 2 + cos((minuteRotation - 90) * (PI / 180)).toFloat() * size.width * 0.3f,
                    y = size.height / 2 + sin((minuteRotation - 90) * (PI / 180)).toFloat() * size.height * 0.3f
                ),
                strokeWidth = 12f,
                cap = StrokeCap.Round,
            )


            drawLine(
                color = Color.Red,
                start = Offset(size.width / 2, size.height / 2),
                end = Offset(
                    x = size.width / 2 + cos((secondRotation - 90) * (PI / 180)).toFloat() * size.width * 0.4f,
                    y = size.height / 2 + sin((secondRotation - 90) * (PI / 180)).toFloat() * size.height * 0.4f
                ),
                strokeWidth = 8f,
                cap = StrokeCap.Round,

            )
        }
    }
}




@Composable
fun worldClock(cityTimeZone: CityTimeZone, currentDateTime: LocalDateTime, onDelete: (CityTimeZone) -> Unit) {

    val cityTime = remember(cityTimeZone) {
        mutableStateOf(calculateCityTimeclock(cityTimeZone, currentDateTime))
    }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(60000) // Update every minute
            withContext(Dispatchers.Main) {
                cityTime.value = calculateCityTimeclock(cityTimeZone, LocalDateTime.now())
            }
        }
    }

    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            if (offsetX.value > 400 || offsetX.value < -400) {
                                onDelete(cityTimeZone)
                                // Reset offset after deletion
                                offsetX.snapTo(0f)
                            } else {
                                offsetX.animateTo(0f, tween(300))
                            }
                        }
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        coroutineScope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount)
                        }
                        change.consume()
                    }
                )
            }
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
    ) {
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(30.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(cityTimeZone.city, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(cityTimeZone.county, fontSize = 12.sp, color = Color.Gray)
                }
                Text(cityTime.value, fontSize = 16.sp)
            }
        }
    }
}

fun calculateCityTimeclock(cityTimeZone: CityTimeZone, currentDateTime: LocalDateTime): String {
    val zonedDateTime = currentDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(cityTimeZone.timeZone)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return zonedDateTime.format(timeFormatter)
}




@Composable
fun NoTimeZone() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_access_time_24 ), // Replace with your vector image
            contentDescription = "No timeZone",
            modifier = Modifier.size(120.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Time Zone set.",
            fontSize = 18.sp,
            color = Color.LightGray
        )
    }
}