package com.example.clock.Presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clock.R
import com.example.clock.domainLayer.Reminder
import com.example.clock.Presentation.ViewModels.ReminderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(navController: NavController, viewModel: ReminderViewModel) {
    val reminders by viewModel.reminders.collectAsState()
    val selectedReminders = remember { mutableStateOf<Set<Reminder>>(emptySet()) }
    val showDeleteButton = remember { mutableStateOf(false) }
    val context = LocalContext.current



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Reminder") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        floatingActionButton = {
            if (showDeleteButton.value) {
                FloatingActionButton(
                    onClick = {
                        selectedReminders.value.forEach { reminder ->
                            viewModel.deleteReminder(reminder)
                        }
                        selectedReminders.value = emptySet()
                        showDeleteButton.value = false
                    },
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = Color.White,
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 72.dp) // Adjust padding to be above bottom bar
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_delete_24),
                        contentDescription = "Delete"
                    )
                }
            } else {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("addReminder")
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 72.dp) // Adjust padding to avoid overlap with delete button
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = "Add Reminder"
                    )
                }
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if(reminders.isEmpty()){
                        item {
                            NoReminder()
                        }
                    }else {
                        items(reminders) { reminder ->
                            ReminderItem(
                                reminder = reminder,
                                isSelected = selectedReminders.value.contains(reminder),
                                onClick = {
                                    if (selectedReminders.value.isEmpty()) {
                                        navController.navigate("updateReminder/${reminder.id}")
                                    } else {
                                        val newSelectedSet = selectedReminders.value.toMutableSet()
                                        if (newSelectedSet.contains(reminder)) {
                                            newSelectedSet.remove(reminder)
                                        } else {
                                            newSelectedSet.add(reminder)
                                        }
                                        selectedReminders.value = newSelectedSet
                                        showDeleteButton.value = newSelectedSet.isNotEmpty()
                                    }
                                },
                                onLongClick = {
                                    selectedReminders.value = setOf(reminder)
                                    showDeleteButton.value = true
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReminderItem(reminder: Reminder, isSelected: Boolean, onClick: () -> Unit, onLongClick: () -> Unit) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else Color.White
    )
    val elevation by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 0.dp
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            ),
        color = backgroundColor,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = reminder.title)
            Text(text = reminder.date.toString())
        }
    }
}

@Composable
fun NoReminder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_event_busy_24), // Replace with your vector image
            contentDescription = "No Alarms",
            modifier = Modifier.size(120.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Reminders.",
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}

