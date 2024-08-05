package com.example.clock.Presentation
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.clock.Presentation.ViewModels.CalenderViewModel
import com.example.clock.R
import com.example.clock.domainLayer.Reminder
import com.example.clock.domainLayer.entity.HolidayEntity
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(navController: NavController, viewModel: CalenderViewModel) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    val selectedCountry by viewModel.selectedCountry.observeAsState("US")
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val errorMessage by viewModel.errorMessage.collectAsState()
    var showSnackbar by remember { mutableStateOf(false) }


    LaunchedEffect(currentYearMonth) {
        viewModel.fetchAllHolidays(selectedCountry, currentYearMonth.year)
    }

    LaunchedEffect(errorMessage) {
        showSnackbar = errorMessage != null
    }



    val countryCodeMap = mapOf(
        "Andorra" to "AD", "Albania" to "AL", "Armenia" to "AM", "Argentina" to "AR",
        "Austria" to "AT", "Australia" to "AU", "Åland Islands" to "AX", "Bosnia and Herzegovina" to "BA",
        "Barbados" to "BB", "Belgium" to "BE", "Bulgaria" to "BG", "Benin" to "BJ",
        "Bolivia" to "BO", "Brazil" to "BR", "Bahamas" to "BS", "Botswana" to "BW",
        "Belarus" to "BY", "Belize" to "BZ", "Canada" to "CA", "Switzerland" to "CH",
        "Chile" to "CL", "China" to "CN", "Colombia" to "CO", "Costa Rica" to "CR",
        "Cuba" to "CU", "Cyprus" to "CY", "Czechia" to "CZ", "Germany" to "DE",
        "Denmark" to "DK", "Dominican Republic" to "DO", "Ecuador" to "EC", "Estonia" to "EE",
        "Egypt" to "EG", "Spain" to "ES", "Finland" to "FI", "Faroe Islands" to "FO",
        "France" to "FR", "Gabon" to "GA", "United Kingdom" to "GB", "Grenada" to "GD",
        "Georgia" to "GE", "Guernsey" to "GG", "Gibraltar" to "GI", "Greenland" to "GL",
        "Gambia" to "GM", "Greece" to "GR", "Guatemala" to "GT", "Guyana" to "GY",
        "Hong Kong" to "HK", "Honduras" to "HN", "Croatia" to "HR", "Haiti" to "HT",
        "Hungary" to "HU", "Indonesia" to "ID", "Ireland" to "IE", "Isle of Man" to "IM",
        "Iceland" to "IS", "Italy" to "IT", "Jersey" to "JE", "Jamaica" to "JM",
        "Japan" to "JP", "South Korea" to "KR", "Kazakhstan" to "KZ", "Liechtenstein" to "LI",
        "Lesotho" to "LS", "Lithuania" to "LT", "Luxembourg" to "LU", "Latvia" to "LV",
        "Morocco" to "MA", "Monaco" to "MC", "Moldova" to "MD", "Montenegro" to "ME",
        "Madagascar" to "MG", "North Macedonia" to "MK", "Mongolia" to "MN", "Montserrat" to "MS",
        "Malta" to "MT", "Mexico" to "MX", "Mozambique" to "MZ", "Namibia" to "NA",
        "Niger" to "NE", "Nigeria" to "NG", "Nicaragua" to "NI", "Netherlands" to "NL",
        "Norway" to "NO", "New Zealand" to "NZ", "Panama" to "PA", "Peru" to "PE",
        "Papua New Guinea" to "PG", "Poland" to "PL", "Puerto Rico" to "PR", "Portugal" to "PT",
        "Paraguay" to "PY", "Romania" to "RO", "Serbia" to "RS", "Russia" to "RU",
        "Sweden" to "SE", "Singapore" to "SG", "Slovenia" to "SI", "Svalbard and Jan Mayen" to "SJ",
        "Slovakia" to "SK", "San Marino" to "SM", "Suriname" to "SR", "El Salvador" to "SV",
        "Tunisia" to "TN", "Turkey" to "TR", "Ukraine" to "UA", "United States" to "US",
        "Uruguay" to "UY", "Vatican City" to "VA", "Venezuela" to "VE", "Vietnam" to "VN",
        "South Africa" to "ZA", "Zimbabwe" to "ZW"
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendar") },
                actions = {
                    Box {
                        IconButton(onClick = { isDropdownExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Select Country")
                        }
                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false }
                        ) {
                            countryCodeMap.forEach { (countryName, countryCode) ->
                                androidx.compose.material.DropdownMenuItem(onClick = {
                                    viewModel.setSelectedCountry(countryCode)
                                    isDropdownExpanded = false
                                    viewModel.fetchAllHolidays(selectedCountry, currentYearMonth.year)
                                }) {
                                    Text(text = countryName)
                                }
                            }
                        }
                        if (showSnackbar && errorMessage != null) {
                            Snackbar(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(16.dp),
                                content = {
                                    Text(text = errorMessage ?: "")
                                },
                                action = {
                                    TextButton(onClick = { showSnackbar = false  }) {
                                        Text("DISMISS")
                                    }
                                }
                            )
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    CalendarView(
                        yearMonth = currentYearMonth,
                        selectedDate = selectedDate,
                        onDateSelected = { date ->
                            selectedDate = date
                        },
                        onMonthChanged = { newYearMonth ->
                            currentYearMonth = newYearMonth
                        },
                        holidays = viewModel.holidays,
                        reminders = viewModel.reminders
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    EventsList(
                        viewModel.getHolidaysForMonth(currentYearMonth),
                        viewModel.getRemindersForMonth(currentYearMonth)
                        )
                }

            }
        }
    )
}

@Composable
fun CalendarView(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (YearMonth) -> Unit,
    holidays: StateFlow<Map<LocalDate, List<HolidayEntity>>>,
    reminders: StateFlow<List<Reminder>>
) {
    val holidayMap by holidays.collectAsState()
    val reminderList by reminders.collectAsState()
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek.value % 7


    val reminderMap = reminderList.groupBy { it.date }


    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { onMonthChanged(yearMonth.minusMonths(1)) }) {
                Text(text = "<", fontSize = 20.sp)
            }
            Text(
                text = "${yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${yearMonth.year}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { onMonthChanged(yearMonth.plusMonths(1)) }) {
                Text(text = ">", fontSize = 20.sp)
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        }
        LazyColumn {
            items((daysInMonth + firstDayOfWeek + 6) / 7) { weekIndex ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (dayIndex in 0..6) {
                        val dayNumber = weekIndex * 7 + dayIndex - firstDayOfWeek + 1
                        if (dayNumber in 1..daysInMonth) {
                            val dayDate = yearMonth.atDay(dayNumber)
                            val isHoliday = holidayMap.containsKey(dayDate)
                            val isReminder = reminderMap.containsKey(dayDate)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                                    .background(
                                        if (dayDate == selectedDate) Color.Gray
                                        else if (isHoliday) Color.Red
                                        else if(isReminder) Color.Green
                                        else Color.Transparent
                                    )
                                    .clickable {
                                        onDateSelected(dayDate)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayNumber.toString(),
                                    fontSize = 16.sp,
                                    color = if (isHoliday) Color.White else Color.Black
                                )
                            }
                        } else {
                            // Fill empty cells with an empty Box
                            Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun EventsList(events: List<HolidayEntity>, reminders: List<Reminder>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text ="Events on the selected Month",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding( start = 16.dp, end = 16.dp,bottom = 80.dp)
        ) {

            if (events.isEmpty() && reminders.isEmpty()) {
                // Display message and vector image when no alarms are set
                item {
                    NoEvents()
                }
            } else {
                items(events) { event ->
                    EventItem(event)
                }
                items(reminders) { reminders ->
                    ReminderItem(reminders)
                }
            }
        }
    }
}

@Composable
fun EventItem(event: HolidayEntity) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding( vertical = 5.dp)
    ) {
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(event.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Date: ${event.date}", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun ReminderItem(reminder: Reminder) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(reminder.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Date: ${reminder.date}", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun NoEvents() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_festival_24), // Replace with your vector image
            contentDescription = "No Alarms",
            modifier = Modifier.size(120.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Events.",
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}