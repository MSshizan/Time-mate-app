package com.example.clock.Presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.clock.domainLayer.CityTimeZone
import com.example.clock.Presentation.ViewModels.ClockViewModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeZoneSelector(
    navController: NavController,
    cities: List<CityTimeZone>,
    onCitySelected: (CityTimeZone) -> Unit,
    viewModel: ClockViewModule

) {


    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Select City") }, modifier = Modifier.fillMaxWidth()
        )
    }, content = { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {


            val coroutineScope = rememberCoroutineScope()
            val currentDateTime by remember { mutableStateOf(LocalDateTime.now()) }
            var centralTimeOffset by remember { mutableStateOf(0) }
            var searchText by remember { mutableStateOf("") }
            var isFocused by remember { mutableStateOf(false) }


            fun filteredCities(searchText: String, cities: List<CityTimeZone>): List<CityTimeZone> {
                return cities.filter {
                    it.city.contains(searchText, ignoreCase = true) ||
                            it.county.contains(searchText, ignoreCase = true)
                }
            }





            LaunchedEffect(key1 = Unit) {
                while (true) {
                    delay(60000)
                    with(coroutineScope) {
                        launch {
                            centralTimeOffset++
                        }
                    }
                }
            }


            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.White)
                ) {
                    OutlinedTextField(

                        value = searchText,
                        onValueChange = {
                            searchText = it
                            isFocused = true },

                        label = { if (!isFocused || searchText.isEmpty()) Text("Search ") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.Black,
                            placeholderColor = Color.Gray,
                            cursorColor = Color.Black,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),


                    )
                    IconButton(
                        onClick = { /*TODO*/ }, modifier = Modifier.padding(end = 16.dp)


                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }


                }
                LazyColumn {
                    items(filteredCities(searchText, cities)) { cityTimeZone ->
                        CityTimeZoneItem(cityTimeZone, currentDateTime){selectedCity ->
                            onCitySelected(selectedCity)
                            viewModel.addSelectedCity(selectedCity)
                            navController.popBackStack()
                        }
                    }

                }
            }
        }
    })
}


@Composable
fun CityTimeZoneItem(cityTimeZone: CityTimeZone, currentDateTime: LocalDateTime, onCitySelected: (CityTimeZone) -> Unit) {
    val cityTime = remember(cityTimeZone) {
        mutableStateOf(calculateCityTime(cityTimeZone, currentDateTime))
    }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(60000)
            withContext(Dispatchers.Main) {
                cityTime.value = calculateCityTime(cityTimeZone, LocalDateTime.now())
            }
        }
    }

    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCitySelected(cityTimeZone)
            }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(cityTimeZone.city, fontSize = 16.sp)
                Text(cityTimeZone.county, fontSize = 10.sp)
            }
            Text(cityTime.value, fontSize = 16.sp)
        }
    }
}

fun calculateCityTime(cityTimeZone: CityTimeZone, currentDateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return currentDateTime.atZone(ZoneId.systemDefault())
        .withZoneSameInstant(cityTimeZone.timeZone)
        .toLocalTime()
        .format(formatter)
}








