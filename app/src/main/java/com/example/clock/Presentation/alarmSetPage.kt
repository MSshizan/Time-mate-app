package com.example.clock.Presentation

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.clock.Presentation.ViewModels.AlarmViewModule
import com.example.clock.dataLayer.sheduler.alarmSheduler
import com.example.clock.domainLayer.alarmdataClass
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreenSet(
    navController: NavController,
    alarmViewModel: AlarmViewModule,
    alarmId: Int? = null
) {
    var selectedHour by remember { mutableStateOf("12") }
    var selectedMinute by remember { mutableStateOf("59") }
    var selectedAmPm by remember { mutableStateOf("AM") }
    var Label by remember { mutableStateOf("") }
    val selectedMusic by alarmViewModel.selectedMusicUri.observeAsState()
    val context = LocalContext.current


    val alarm by remember {
        derivedStateOf { alarmId?.let { alarmViewModel.getAlarmById(it) } }
    }
    var showDaySelectionDialog by remember {
        mutableStateOf(false)
    }
    val selectedDays = remember {
        mutableStateListOf<String>()
    }




    LaunchedEffect(alarmId) {
        alarm?.let {
            selectedHour = it.hour.toString()
            selectedMinute = it.minute.toString()
            selectedAmPm = it.amPm
            Label = it.label
            selectedDays.clear()
            selectedDays.addAll(it.selectedDays)

        }
    }

    LaunchedEffect(selectedMusic) {
        Log.d("AlarmScreenSetupdating", "Current Selected Music: $selectedMusic")
        if (selectedMusic != null) {

            alarmViewModel.setSelectedMusicUri(selectedMusic)
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(if (alarmId == null) "Set Alarm" else "Update Alarm") },
            modifier = Modifier.fillMaxWidth()
        )
    }, content = { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Hour", fontSize = 16.sp)
                        HoursPicker(
                            selectedValue = selectedHour.toString().padStart(2, '0'),
                            onValueChange = { selectedHour = it })
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Minute", fontSize = 16.sp)
                        minituesPicker(
                            selectedValue = selectedMinute,
                            onValueChange = { selectedMinute = it })
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "AM/PM", fontSize = 16.sp)
                        AmPmPicker(selectedValue = selectedAmPm,
                            onValueChange = { selectedAmPm = it })
                    }
                }

                Spacer(modifier = Modifier.padding(16.dp))

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {

                        navController.navigate("musicSelector")
                    }
                    .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "RingTone", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = if (selectedMusic != null && selectedMusic.toString().isNotEmpty()) {
                            "${selectedMusic.toString()} >"
                        } else {
                            "Default >"
                        }, fontSize = 16.sp
                    )
                }


                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDaySelectionDialog = true

                    }
                    .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Repeat", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    if (selectedDays.size != 7) Text(
                        text = "${selectedDays.size}" + " Days" + " >", fontSize = 16.sp
                    ) else Text(text = "EveryDay", fontSize = 16.sp)
                }


                Row(
                    modifier = Modifier.fillMaxWidth()

                ) {

                    TextField(
                        value = Label,
                        onValueChange = { Label = it },
                        placeholder = { Text("Label") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp)
                            .padding(top = 16.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedLabelColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = Color.Gray
                        ),
                        textStyle = TextStyle(fontSize = 16.sp)
                    )
                }



                Button(
                    onClick = {
                        val newAlarm = alarmdataClass(
                            id = alarm?.id ?: Random.nextInt(),
                            hour = selectedHour.toInt(),
                            minute = selectedMinute.toInt(),
                            amPm = selectedAmPm,
                            isEnabled = true,
                            label = Label,
                            musicUri = selectedMusic?.toString() ?: "",
                            selectedDays = selectedDays.toList()
                        )
                        if (alarmId == null) {
                            alarmViewModel.addAlarm(newAlarm)
                        } else {
                            alarmViewModel.updateAlarm(newAlarm)
                        }
                        val alarmScheduler = alarmSheduler(context)
                        if (newAlarm.isEnabled) {
                            alarmScheduler.setAlarm(newAlarm)
                        } else {
                            alarmScheduler.cancelAlarm(newAlarm)
                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (alarmId == null) "Set Alarm" else "Update Alarm")
                }
            }
        }
    }
    )

    if (showDaySelectionDialog) {
        DaySelectionoption(
            selectedDays = selectedDays,
            onDaySelected = { day ->
                if (selectedDays.contains(day)) {
                    selectedDays.remove(day)
                } else {
                    selectedDays.add(day)
                }
            },
            onDismissRequest = { showDaySelectionDialog = false }
        )
    }
}


@Composable
fun AmPmPicker(
    selectedValue: String, onValueChange: (String) -> Unit
) {
    val options = listOf("", "AM", "PM", "")
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState, modifier = Modifier
            .height(150.dp)
            .width(100.dp)
    ) {
        items(options) { value ->
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value,
                    color = (if (value == selectedValue) Color.Black else Color.LightGray),
                    fontSize = 30.sp
                )
            }
        }
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val centerItemIndex =
                (listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size / 2)
            onValueChange(options[centerItemIndex])
        }
    }
}

@Composable
fun minituesPicker(
    selectedValue: String, onValueChange: (String) -> Unit
) {
    val options = listOf("") + (1..59).map { String.format("%02d", it) } + listOf("")
    val listState = rememberLazyListState()




    LazyColumn(
        state = listState, modifier = Modifier
            .height(150.dp)
            .width(100.dp)
    ) {
        items(options) { value ->
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value,
                    color = (if (value == selectedValue) Color.Black else Color.LightGray),
                    fontSize = 30.sp
                )
            }
        }
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val centerItemIndex =
                (listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size / 2)
            onValueChange(options[centerItemIndex])
        }
    }
}


@Composable
fun HoursPicker(
    selectedValue: String, onValueChange: (String) -> Unit
) {
    val options = listOf("") + (0..12).map { String.format("%02d", it) } + listOf("")
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState, modifier = Modifier
            .height(150.dp)
            .width(100.dp)
    ) {
        items(options) { value ->
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value,
                    color = (if (value == selectedValue) Color.Black else Color.LightGray),
                    fontSize = 30.sp
                )
            }
        }
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val centerItemIndex =
                (listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size / 2)
            onValueChange(options[centerItemIndex])
        }
    }
}

@Composable
fun DaySelectionoption(
    selectedDays: List<String>, onDaySelected: (String) -> Unit, onDismissRequest: () -> Unit
) {
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            shape = MaterialTheme.shapes.medium, shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Select Days", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                days.forEach { day ->
                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = selectedDays.contains(day),
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    onDaySelected(day)
                                } else {
                                    onDaySelected(day)
                                }

                            })
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = day)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { onDismissRequest() }, modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Done")

                }
            }

        }

    }
}

