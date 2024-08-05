package com.example.clock.Presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.LocalTime
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.example.clock.R
import com.example.clock.domainLayer.Reminder
import com.example.clock.Presentation.ViewModels.ReminderViewModel
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderScreen(
    navController: NavController,
    viewModel: ReminderViewModel,
    reminderId: Int? = null
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now()) }
    var time by remember { mutableStateOf(LocalTime.now()) }
    var isNotified by remember { mutableStateOf(false) }
    val isUpdate = reminderId != null
    val reminderState by viewModel.reminders.collectAsState()

    LaunchedEffect(reminderId) {
        reminderId?.let {
            reminderState.find { it.id == reminderId }?.let { reminder ->
                title = reminder.title ?: ""
                description = reminder.description ?: ""
                date = reminder.date ?: LocalDate.now()
                time = reminder.time ?: LocalTime.now()
                isNotified = reminder.isOn
            }
        }
    }


    androidx.compose.material3.Scaffold(topBar = {
        androidx.compose.material3.TopAppBar(
            title = { androidx.compose.material3.Text(if (isUpdate) "Update Reminder" else "Add Reminder")},
            modifier = Modifier.fillMaxWidth()


        )
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            TextField(value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(100.dp)),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = colorResource(id = R.color.white))
            )
            Spacer(modifier = Modifier.height(25.dp))
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.height(25.dp))
            DatePicker(date = date, onDateSelected = { date = it })
            Spacer(modifier = Modifier.height(25.dp))
            TimePicker(time = time, onTimeSelected = { time = it })
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()) {
                Checkbox(checked = isNotified, onCheckedChange = { isNotified = it })
                Text(text = "Set Notification")
            }
            Spacer(modifier = Modifier.height(50.dp))
            Button(
                onClick = {
                    val newReminder = Reminder(0, title, description, date, time, isNotified)
                    if (isUpdate) {
                        viewModel.updateReminder(newReminder.copy(id = reminderId!!))
                    } else {
                        viewModel.addReminder(newReminder)
                    }
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.purple_500 ))
            ) {
                Text(if (isUpdate) "Update Reminder" else "Add Reminder")
            }
        }
    }
    )

}


@Composable
fun DatePicker(date: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    calendar.set(date.year, date.monthValue - 1, date.dayOfMonth)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Button(modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
        backgroundColor = Color.White
    ), onClick = { datePickerDialog.show() }) {
        Text("Select Date: ${date.toString()}")
    }
}

@Composable
fun TimePicker(time: LocalTime, onTimeSelected: (LocalTime) -> Unit) {
    val context = LocalContext.current
    val timePickerDialog = TimePickerDialog(
        context, { _, hourOfDay, minute ->
            onTimeSelected(LocalTime.of(hourOfDay, minute))
        }, time.hour, time.minute, true
    )

    Button(modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
        backgroundColor = Color.White
    ), onClick = { timePickerDialog.show() }) {
        Text("Select Time: ${time.toString()}")
    }
}

