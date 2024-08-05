package com.example.clock.Presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import com.example.clock.domainLayer.CityTimeZone
import com.example.clock.Presentation.ViewModels.AlarmViewModule
import com.example.clock.Presentation.ViewModels.CalenderViewModel
import com.example.clock.Presentation.ViewModels.ClockViewModule
import com.example.clock.Presentation.ViewModels.ReminderViewModel
import com.example.clock.R
import com.example.clock.domainLayer.navIteam
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun bottomNavigationScreen(
    navController: NavHostController,
    selectedCities: List<CityTimeZone>,
    viewModel: ClockViewModule,
    alarmViewModule: AlarmViewModule,
    calenderViewModel: CalenderViewModel,
    reminderViewModel: ReminderViewModel
) {


    val naviteam = listOf(
        navIteam("Clock",  ImageVector.vectorResource(id = R.drawable.baseline_access_time_24)),
        navIteam("Alarm", ImageVector.vectorResource(id = R.drawable.baseline_alarm_24)),
        navIteam("Calender" ,ImageVector.vectorResource(id = R.drawable.baseline_calendar_month_24)),
        navIteam("Reminder", ImageVector.vectorResource(id = R.drawable.baseline_event_note_24)),

        )
    var selectedIteam by remember {
        mutableStateOf(0)
    }

    val pagerState = rememberPagerState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val coroutineScope = rememberCoroutineScope()
                naviteam.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = pagerState.currentPage== index,
                        onClick = {
                          coroutineScope.launch {
                              pagerState.animateScrollToPage(index)
                          }

                                  },
                        label = {
                            Text(
                                text = item.label

                            )
                        },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            count = naviteam.size,
            state = pagerState
        ) {page->
            contentScreen(
                modifier = Modifier.padding(paddingValues),
                selectedIndex = page,
                navController = navController,
                selectedCities = selectedCities,
                viewModel = viewModel,
               alarmViewModule =  alarmViewModule,
               calenderViewModel =  calenderViewModel,
                reminderViewModel = reminderViewModel
            )

        }


    }
}



@Composable
fun contentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    navController: NavHostController,
    selectedCities: List<CityTimeZone>,
    viewModel: ClockViewModule,
    alarmViewModule: AlarmViewModule,
    calenderViewModel: CalenderViewModel,
    reminderViewModel: ReminderViewModel
) {
    when (selectedIndex) {
        0 -> clockScreen(navController = navController, viewModel = viewModel)
        1 -> alramScreen(navController = navController, alarmViewModel = alarmViewModule)
        2 -> CalendarScreen(navController = navController, viewModel = calenderViewModel)
        3 -> ReminderScreen(navController = navController, viewModel =reminderViewModel)
    }
}

