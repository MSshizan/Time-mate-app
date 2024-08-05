package com.example.clock.Presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clock.domainLayer.CityTimeZone
import com.example.clock.Presentation.ViewModels.AlarmViewModule
import com.example.clock.Presentation.ViewModels.CalenderViewModel
import com.example.clock.Presentation.ViewModels.ClockViewModule
import com.example.clock.Presentation.ViewModels.ReminderViewModel
import com.example.clock.domainLayer.cities


@Composable
fun navigation(
    selectedCities: List<CityTimeZone>,
    viewModule: ClockViewModule,
    alarmViewModule: AlarmViewModule,
    calenderViewModel: CalenderViewModel,
    viewModuleReminder: ReminderViewModel,

    ) {

    val navController = rememberNavController()
    NavHost(navController, startDestination = "navigationBar") {
        composable("clock") { clockScreen(navController = navController, viewModel = viewModule) }
        composable("musicSelector") {
            muscicSelect(
                navController = navController,
                alarmViewModel = alarmViewModule,
                onMusicSelected = { musicUri ->
                    alarmViewModule.setSelectedMusicUri(musicUri)
                }
            )
        }
        composable("alarm") {
            alramScreen(
                navController = navController,
                alarmViewModel = alarmViewModule
            )
        }
        composable("alarmset/{alarmId}") { backStackEntry ->
            val alarmId = backStackEntry.arguments?.getString("alarmId")?.toIntOrNull()
            AlarmScreenSet(
                navController = navController,
                alarmViewModel = alarmViewModule,
                alarmId = alarmId
            )
        }
        composable("calender") { CalendarScreen(navController = navController, viewModel = calenderViewModel) }
        composable("reminder") { ReminderScreen(navController = navController, viewModel = viewModuleReminder) }
        composable("timeZone") {
            TimeZoneSelector(
                navController = navController,
                cities = cities,
                viewModel = viewModule,
                onCitySelected = { cityTimeZone ->

                }
            )
        }
        composable("navigationBar") {
            bottomNavigationScreen(
                navController = navController,
                selectedCities = selectedCities,
                viewModel = viewModule,
                alarmViewModule = alarmViewModule,
                calenderViewModel = calenderViewModel,
                reminderViewModel = viewModuleReminder
            )
        }
        composable("alarmset"){
            AlarmScreenSet(navController=navController, alarmViewModel = alarmViewModule )

        }
        composable("addReminder"){
            AddReminderScreen(navController=navController, viewModel = viewModuleReminder )

        }
        composable("updateReminder/{reminderId}") { backStackEntry ->
            val reminderId = backStackEntry.arguments?.getString("reminderId")?.toInt()
            AddReminderScreen(navController, viewModuleReminder, reminderId)
        }

    }

}
