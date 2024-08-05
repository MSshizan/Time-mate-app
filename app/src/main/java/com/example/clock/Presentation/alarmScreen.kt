package com.example.clock.Presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clock.Presentation.ViewModels.AlarmViewModule
import com.example.clock.R
import com.example.clock.dataLayer.sheduler.alarmSheduler
import com.example.clock.domainLayer.alarmdataClass
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun alramScreen(navController: NavController, alarmViewModel: AlarmViewModule) {

    val context = LocalContext.current
    val alarms by alarmViewModel.alarms.observeAsState(emptyList())



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alarm") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (alarms.isEmpty()) {
                    NoAlarmsView()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {


                        items(alarms, key = { alarm -> alarm.id }) { alarm ->
                            Alarm(
                                alarm = alarm,
                                alarmViewModel = alarmViewModel,
                                onDelete = { alarmToDelete ->
                                    alarmViewModel.removeAlarm(alarmToDelete)
                                },
                                onEdit = {
                                    navController.navigate("alarmset/${alarm.id}")
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                }

                FloatingActionButton(
                    onClick = {
                        navController.navigate("alarmset")
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            end = 16.dp,
                            bottom = 100.dp
                        )
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
fun Alarm(
    alarm: alarmdataClass,
    alarmViewModel: AlarmViewModule,
    onDelete: (alarmdataClass) -> Unit,
    onEdit: () -> Unit
) {
    val context = LocalContext.current
    val alarmSheduler = alarmSheduler(context)
    var alarmIsOn by remember {
        mutableStateOf(true)
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
                                onDelete(alarm)

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
            .clickable {
                onEdit()
            }
    ) {
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(30.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "${alarm.hour}:${
                            alarm.minute.toString().padStart(2, '0')
                        } ${alarm.amPm}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = alarm.label,
                        fontSize = 14.sp,
                        color = Color.Black,
                    )
                }
                Switch(
                    checked = alarm.isEnabled,
                    onCheckedChange = { isChecked ->
                        val updatedAlarm = alarm.copy(isEnabled = isChecked)
                        alarmViewModel.updateAlarm(updatedAlarm)
                        if (isChecked) {
                            alarmSheduler.setAlarm(updatedAlarm)
                        } else {
                            alarmSheduler.cancelAlarm(updatedAlarm)
                        }
                    }
                )


            }
        }
    }
}
@Composable
fun NoAlarmsView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_alarm_24),
            contentDescription = "No Alarms",
            modifier = Modifier.size(120.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Alarms set.",
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}


