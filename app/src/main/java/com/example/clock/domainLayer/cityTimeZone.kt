package com.example.clock.domainLayer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZoneId


@Entity
data class CityTimeZone(
    @PrimaryKey val city:String,
    val county: String,
    @ColumnInfo(name = "time_zone") val timeZone: ZoneId,
    var time: String?=null
)
val cities = listOf(

    CityTimeZone("New York", "USA", ZoneId.of("America/New_York")),
    CityTimeZone("Los Angeles", "USA", ZoneId.of("America/Los_Angeles")),
    CityTimeZone("Chicago", "USA", ZoneId.of("America/Chicago")),
    CityTimeZone("Houston", "USA", ZoneId.of("America/Chicago")),
    CityTimeZone("Phoenix", "USA", ZoneId.of("America/Phoenix")),
    CityTimeZone("Toronto", "Canada", ZoneId.of("America/Toronto")),
    CityTimeZone("Vancouver", "Canada", ZoneId.of("America/Vancouver")),
    CityTimeZone("Montreal", "Canada", ZoneId.of("America/Toronto")),
    CityTimeZone("Calgary", "Canada", ZoneId.of("America/Edmonton")),
    CityTimeZone("Ottawa", "Canada", ZoneId.of("America/Toronto")),
    CityTimeZone("London", "UK", ZoneId.of("Europe/London")),
    CityTimeZone("Manchester", "UK", ZoneId.of("Europe/London")),
    CityTimeZone("Birmingham", "UK", ZoneId.of("Europe/London")),
    CityTimeZone("Edinburgh", "UK", ZoneId.of("Europe/London")),
    CityTimeZone("Glasgow", "UK", ZoneId.of("Europe/London")),
    CityTimeZone("Paris", "France", ZoneId.of("Europe/Paris")),
    CityTimeZone("Marseille", "France", ZoneId.of("Europe/Paris")),
    CityTimeZone("Lyon", "France", ZoneId.of("Europe/Paris")),
    CityTimeZone("Toulouse", "France", ZoneId.of("Europe/Paris")),
    CityTimeZone("Nice", "France", ZoneId.of("Europe/Paris")),
    CityTimeZone("Berlin", "Germany", ZoneId.of("Europe/Berlin")),
    CityTimeZone("Munich", "Germany", ZoneId.of("Europe/Berlin")),
    CityTimeZone("Frankfurt", "Germany", ZoneId.of("Europe/Berlin")),
    CityTimeZone("Hamburg", "Germany", ZoneId.of("Europe/Berlin")),
    CityTimeZone("Cologne", "Germany", ZoneId.of("Europe/Berlin")),
    CityTimeZone("Moscow", "Russia", ZoneId.of("Europe/Moscow")),
    CityTimeZone("Saint Petersburg", "Russia", ZoneId.of("Europe/Moscow")),
    CityTimeZone("Novosibirsk", "Russia", ZoneId.of("Asia/Novosibirsk")),
    CityTimeZone("Yekaterinburg", "Russia", ZoneId.of("Asia/Yekaterinburg")),
    CityTimeZone("Nizhny Novgorod", "Russia", ZoneId.of("Europe/Moscow")),
    CityTimeZone("Beijing", "China", ZoneId.of("Asia/Shanghai")),
    CityTimeZone("Shanghai", "China", ZoneId.of("Asia/Shanghai")),
    CityTimeZone("Chongqing", "China", ZoneId.of("Asia/Shanghai")),
    CityTimeZone("Tianjin", "China", ZoneId.of("Asia/Shanghai")),
    CityTimeZone("Guangzhou", "China", ZoneId.of("Asia/Shanghai")),
    CityTimeZone("Tokyo", "Japan", ZoneId.of("Asia/Tokyo")),
    CityTimeZone("Osaka", "Japan", ZoneId.of("Asia/Tokyo")),
    CityTimeZone("Nagoya", "Japan", ZoneId.of("Asia/Tokyo")),
    CityTimeZone("New York", "USA", ZoneId.of("America/New_York")),
    CityTimeZone("London", "UK", ZoneId.of("Europe/London")),
    CityTimeZone("Tokyo", "Japan", ZoneId.of("Asia/Tokyo")),
    CityTimeZone("Sydney", "Australia", ZoneId.of("Australia/Sydney")),
    CityTimeZone("Kabul", "Afghanistan", ZoneId.of("Asia/Kabul")),
    CityTimeZone("Tirana", "Albania", ZoneId.of("Europe/Tirane")),
    CityTimeZone("Algiers", "Algeria", ZoneId.of("Africa/Algiers")),
    CityTimeZone("Andorra la Vella", "Andorra", ZoneId.of("Europe/Andorra")),
    CityTimeZone("Luanda", "Angola", ZoneId.of("Africa/Luanda")),
    CityTimeZone("Yerevan", "Armenia", ZoneId.of("Asia/Yerevan")),
    CityTimeZone("Canberra", "Australia", ZoneId.of("Australia/Sydney")), // Canberra as an example
    CityTimeZone("Nassau", "Bahamas", ZoneId.of("America/Nassau")),
    CityTimeZone("Manama", "Bahrain", ZoneId.of("Asia/Bahrain")),
    CityTimeZone("Dhaka", "Bangladesh", ZoneId.of("Asia/Dhaka")),
    CityTimeZone("Bridgetown", "Barbados", ZoneId.of("America/Barbados")),
    CityTimeZone("Minsk", "Belarus", ZoneId.of("Europe/Minsk")),
    CityTimeZone("Brussels", "Belgium", ZoneId.of("Europe/Brussels")),
    CityTimeZone("Belmopan", "Belize", ZoneId.of("America/Belize")),
    CityTimeZone("Porto-Novo", "Benin", ZoneId.of("Africa/Porto-Novo")),
    CityTimeZone("Thimphu", "Bhutan", ZoneId.of("Asia/Thimphu")),
    CityTimeZone("Sucre", "Bolivia", ZoneId.of("America/La_Paz")), // Sucre as an example
    CityTimeZone("Sarajevo", "Bosnia and Herzegovina", ZoneId.of("Europe/Sarajevo")),
    CityTimeZone("Gaborone", "Botswana", ZoneId.of("Africa/Gaborone")),
    CityTimeZone("Brasília", "Brazil", ZoneId.of("America/Sao_Paulo")), // Brasília as an example
CityTimeZone("Bandar Seri Begawan", "Brunei", ZoneId.of("Asia/Brunei")),
CityTimeZone("Sofia", "Bulgaria", ZoneId.of("Europe/Sofia")),
CityTimeZone("Ouagadougou", "Burkina Faso", ZoneId.of("Africa/Ouagadougou")),
CityTimeZone("Bujumbura", "Burundi", ZoneId.of("Africa/Bujumbura")),
CityTimeZone("Phnom Penh", "Cambodia", ZoneId.of("Asia/Phnom_Penh")),
CityTimeZone("Yaoundé", "Cameroon", ZoneId.of("Africa/Douala")), // Yaoundé as an example
CityTimeZone("Ottawa", "Canada", ZoneId.of("America/Toronto")), // Ottawa as an example
CityTimeZone("Praia", "Cape Verde", ZoneId.of("Atlantic/Cape_Verde")),
CityTimeZone("Bangui", "Central African Republic", ZoneId.of("Africa/Bangui")),
CityTimeZone("N'Djamena", "Chad", ZoneId.of("Africa/Ndjamena")),
CityTimeZone("Santiago", "Chile", ZoneId.of("America/Santiago")), // Santiago as an example
CityTimeZone("Beijing", "China", ZoneId.of("Asia/Shanghai")), // Beijing as an example
CityTimeZone("Bogotá", "Colombia", ZoneId.of("America/Bogota")), // Bogotá as an example
CityTimeZone("Moroni", "Comoros", ZoneId.of("Indian/Comoro")),
CityTimeZone("Kinshasa", "Democratic Republic of the Congo", ZoneId.of("Africa/Kinshasa")), // Kinshasa as an example
CityTimeZone("San José", "Costa Rica", ZoneId.of("America/Costa_Rica")), // San José as an example
CityTimeZone("Zagreb", "Croatia", ZoneId.of("Europe/Zagreb")),
CityTimeZone("Havana", "Cuba", ZoneId.of("America/Havana")),
CityTimeZone("Nicosia", "Cyprus", ZoneId.of("Asia/Nicosia")), // Nicosia as an example
CityTimeZone("Prague", "Czech Republic", ZoneId.of("Europe/Prague")),
CityTimeZone("Copenhagen", "Denmark", ZoneId.of("Europe/Copenhagen")),
CityTimeZone("Djibouti", "Djibouti", ZoneId.of("Africa/Djibouti")),
CityTimeZone("Roseau", "Dominica", ZoneId.of("America/Dominica")),
CityTimeZone("Santo Domingo", "Dominican Republic", ZoneId.of("America/Santo_Domingo")),
CityTimeZone("Dili", "East Timor", ZoneId.of("Asia/Dili")), // Dili as an example
CityTimeZone("Quito", "Ecuador", ZoneId.of("America/Guayaquil")), // Quito as an example
CityTimeZone("Cairo", "Egypt", ZoneId.of("Africa/Cairo")),
CityTimeZone("San Salvador", "El Salvador", ZoneId.of("America/El_Salvador")),
CityTimeZone("Malabo", "Equatorial Guinea", ZoneId.of("Africa/Malabo")),
CityTimeZone("Asmara", "Eritrea", ZoneId.of("Africa/Asmara")),
CityTimeZone("Tallinn", "Estonia", ZoneId.of("Europe/Tallinn")),
CityTimeZone("Addis Ababa", "Ethiopia", ZoneId.of("Africa/Addis_Ababa")),
CityTimeZone("Suva", "Fiji", ZoneId.of("Pacific/Fiji")),
CityTimeZone("Helsinki", "Finland", ZoneId.of("Europe/Helsinki")),
CityTimeZone("Paris", "France", ZoneId.of("Europe/Paris")),
CityTimeZone("Libreville", "Gabon", ZoneId.of("Africa/Libreville")),
CityTimeZone("Banjul", "Gambia", ZoneId.of("Africa/Banjul")),
CityTimeZone("Tbilisi", "Georgia", ZoneId.of("Asia/Tbilisi")),
CityTimeZone("Berlin", "Germany", ZoneId.of("Europe/Berlin")),
CityTimeZone("Accra", "Ghana", ZoneId.of("Africa/Accra")),
CityTimeZone("Athens", "Greece", ZoneId.of("Europe/Athens")),
CityTimeZone("St. George's", "Grenada", ZoneId.of("America/Grenada")),
CityTimeZone("Guatemala City", "Guatemala", ZoneId.of("America/Guatemala")), // Guatemala City as an example
CityTimeZone("Conakry", "Guinea", ZoneId.of("Africa/Conakry")),
CityTimeZone("Bissau", "Guinea-Bissau", ZoneId.of("Africa/Bissau")),
CityTimeZone("Georgetown", "Guyana", ZoneId.of("America/Guyana")),
CityTimeZone("Port-au-Prince", "Haiti", ZoneId.of("America/Port-au-Prince")),
CityTimeZone("Tegucigalpa", "Honduras", ZoneId.of("America/Tegucigalpa")),
CityTimeZone("Budapest", "Hungary", ZoneId.of("Europe/Budapest")),
CityTimeZone("Reykjavik", "Iceland", ZoneId.of("Atlantic/Reykjavik")),
CityTimeZone("New Delhi", "India", ZoneId.of("Asia/Kolkata")), // New Delhi as an example
CityTimeZone("Jakarta", "Indonesia", ZoneId.of("Asia/Jakarta")), // Jakarta as an example
CityTimeZone("Tehran", "Iran", ZoneId.of("Asia/Tehran")),
CityTimeZone("Baghdad", "Iraq", ZoneId.of("Asia/Baghdad")),
CityTimeZone("Dublin", "Ireland", ZoneId.of("Europe/Dublin")),
CityTimeZone("Jerusalem", "Israel", ZoneId.of("Asia/Jerusalem")),
CityTimeZone("Rome", "Italy", ZoneId.of("Europe/Rome")),
CityTimeZone("Kingston", "Jamaica", ZoneId.of("America/Jamaica")),
CityTimeZone("Tokyo", "Japan", ZoneId.of("Asia/Tokyo")), // Tokyo as an example
CityTimeZone("Amman", "Jordan", ZoneId.of("Asia/Amman")),
CityTimeZone("Nur-Sultan", "Kazakhstan", ZoneId.of("Asia/Almaty")), // Nur-Sultan (Astana) as an example
CityTimeZone("Nairobi", "Kenya", ZoneId.of("Africa/Nairobi")), // Nairobi as an example
CityTimeZone("South Tarawa", "Kiribati", ZoneId.of("Pacific/Tarawa")), // South Tarawa as an example
CityTimeZone("Pristina", "Kosovo", ZoneId.of("Europe/Belgrade")), // Pristina as an example
CityTimeZone("Kuwait City", "Kuwait", ZoneId.of("Asia/Kuwait")),
CityTimeZone("Bishkek", "Kyrgyzstan", ZoneId.of("Asia/Bishkek")),
CityTimeZone("Vientiane", "Laos", ZoneId.of("Asia/Vientiane")),
CityTimeZone("Riga", "Latvia", ZoneId.of("Europe/Riga")),
CityTimeZone("Beirut", "Lebanon", ZoneId.of("Asia/Beirut")),
CityTimeZone("Maseru", "Lesotho", ZoneId.of("Africa/Maseru")),
CityTimeZone("Monrovia", "Liberia", ZoneId.of("Africa/Monrovia")),
CityTimeZone("Tripoli", "Libya", ZoneId.of("Africa/Tripoli")),
CityTimeZone("Vaduz", "Liechtenstein", ZoneId.of("Europe/Vaduz")),
CityTimeZone("Vilnius", "Lithuania", ZoneId.of("Europe/Vilnius")),
CityTimeZone("Luxembourg", "Luxembourg", ZoneId.of("Europe/Luxembourg")),
CityTimeZone("Skopje", "North Macedonia", ZoneId.of("Europe/Skopje")), // Skopje as an example
CityTimeZone("Antananarivo", "Madagascar", ZoneId.of("Indian/Antananarivo")), // Antananarivo as an example
CityTimeZone("Lilongwe", "Malawi", ZoneId.of("Africa/Blantyre")), // Lilongwe as an example
CityTimeZone("Kuala Lumpur", "Malaysia", ZoneId.of("Asia/Kuala_Lumpur")), // Kuala Lumpur as an example
CityTimeZone("Male", "Maldives", ZoneId.of("Indian/Maldives")),
CityTimeZone("Bamako", "Mali", ZoneId.of("Africa/Bamako")),
CityTimeZone("Valletta", "Malta", ZoneId.of("Europe/Malta")),
CityTimeZone("Majuro", "Marshall Islands", ZoneId.of("Pacific/Majuro")), // Majuro as an example
CityTimeZone("Nouakchott", "Mauritania", ZoneId.of("Africa/Nouakchott")),
CityTimeZone("Port Louis", "Mauritius", ZoneId.of("Indian/Mauritius")),
CityTimeZone("Mexico City", "Mexico", ZoneId.of("America/Mexico_City")), // Mexico City as an example
CityTimeZone("Palikir", "Micronesia", ZoneId.of("Pacific/Chuuk")), // Palikir as an example
CityTimeZone("Chisinau", "Moldova", ZoneId.of("Europe/Chisinau")),
CityTimeZone("Monaco", "Monaco", ZoneId.of("Europe/Monaco")),
CityTimeZone("Ulaanbaatar", "Mongolia", ZoneId.of("Asia/Ulaanbaatar")), // Ulaanbaatar as an example
CityTimeZone("Podgorica", "Montenegro", ZoneId.of("Europe/Podgorica")), // Podgorica as an example
CityTimeZone("Rabat", "Morocco", ZoneId.of("Africa/Casablanca")), // Rabat as an example
CityTimeZone("Maputo", "Mozambique", ZoneId.of("Africa/Maputo")),
CityTimeZone("Naypyidaw", "Myanmar", ZoneId.of("Asia/Yangon")), // Naypyidaw as an example
CityTimeZone("Windhoek", "Namibia", ZoneId.of("Africa/Windhoek")),
CityTimeZone("Yaren", "Nauru", ZoneId.of("Pacific/Nauru")), // Yaren as an example
CityTimeZone("Kathmandu", "Nepal", ZoneId.of("Asia/Kathmandu")), // Kathmandu as an example
CityTimeZone("Amsterdam", "Netherlands", ZoneId.of("Europe/Amsterdam")), // Amsterdam as an example
CityTimeZone("Wellington", "New Zealand", ZoneId.of("Pacific/Auckland")), // Wellington as an example
CityTimeZone("Managua", "Nicaragua", ZoneId.of("America/Managua")),
CityTimeZone("Niamey", "Niger", ZoneId.of("Africa/Niamey")),
CityTimeZone("Abuja", "Nigeria", ZoneId.of("Africa/Lagos")), // Abuja as an example
CityTimeZone("Oslo", "Norway", ZoneId.of("Europe/Oslo")),
CityTimeZone("Muscat", "Oman", ZoneId.of("Asia/Muscat")),
CityTimeZone("Islamabad", "Pakistan", ZoneId.of("Asia/Karachi")), // Islamabad as an example
CityTimeZone("Ngerulmud", "Palau", ZoneId.of("Pacific/Palau")), // Ngerulmud as an example
CityTimeZone("Panama City", "Panama", ZoneId.of("America/Panama")), // Panama City as an example
CityTimeZone("Port Moresby", "Papua New Guinea", ZoneId.of("Pacific/Port_Moresby")), // Port Moresby as an example
CityTimeZone("Asunción", "Paraguay", ZoneId.of("America/Asuncion")), // Asunción as an example
CityTimeZone("Lima", "Peru", ZoneId.of("America/Lima")), // Lima as an example
CityTimeZone("Manila", "Philippines", ZoneId.of("Asia/Manila")), // Manila as an example
CityTimeZone("Warsaw", "Poland", ZoneId.of("Europe/Warsaw")),
CityTimeZone("Lisbon", "Portugal", ZoneId.of("Europe/Lisbon")),
CityTimeZone("Doha", "Qatar", ZoneId.of("Asia/Qatar")), // Doha as an example
CityTimeZone("Bucharest", "Romania", ZoneId.of("Europe/Bucharest")),
CityTimeZone("Moscow", "Russia", ZoneId.of("Europe/Moscow")), // Moscow as an example
CityTimeZone("Kigali", "Rwanda", ZoneId.of("Africa/Kigali")),
CityTimeZone("Basseterre", "Saint Kitts and Nevis", ZoneId.of("America/St_Kitts")), // Basseterre as an example
CityTimeZone("Castries", "Saint Lucia", ZoneId.of("America/St_Lucia")), // Castries as an example
CityTimeZone("Kingstown", "Saint Vincent and the Grenadines", ZoneId.of("America/St_Vincent")), // Kingstown as an example
CityTimeZone("Apia", "Samoa", ZoneId.of("Pacific/Apia")), // Apia as an example
CityTimeZone("San Marino", "San Marino", ZoneId.of("Europe/San_Marino")),
CityTimeZone("São Tomé", "São Tomé and Príncipe", ZoneId.of("Africa/Sao_Tome")), // São Tomé as an example
CityTimeZone("Riyadh", "Saudi Arabia", ZoneId.of("Asia/Riyadh")),
CityTimeZone("Dakar", "Senegal", ZoneId.of("Africa/Dakar")),
CityTimeZone("Belgrade", "Serbia", ZoneId.of("Europe/Belgrade")),
CityTimeZone("Victoria", "Seychelles", ZoneId.of("Indian/Mahe")), // Victoria as an example
CityTimeZone("Freetown", "Sierra Leone", ZoneId.of("Africa/Freetown")),
CityTimeZone("Singapore", "Singapore", ZoneId.of("Asia/Singapore")), // Singapore as an example
CityTimeZone("Bratislava", "Slovakia", ZoneId.of("Europe/Bratislava")),
CityTimeZone("Ljubljana", "Slovenia", ZoneId.of("Europe/Ljubljana")),
CityTimeZone("Honiara", "Solomon Islands", ZoneId.of("Pacific/Guadalcanal")), // Honiara as an example
CityTimeZone("Mogadishu", "Somalia", ZoneId.of("Africa/Mogadishu")),
CityTimeZone("Pretoria", "South Africa", ZoneId.of("Africa/Johannesburg")), // Pretoria as an example
CityTimeZone("Seoul", "South Korea", ZoneId.of("Asia/Seoul")), // Seoul as an example
CityTimeZone("Juba", "South Sudan", ZoneId.of("Africa/Juba")),
CityTimeZone("Madrid", "Spain", ZoneId.of("Europe/Madrid")),
CityTimeZone("Colombo", "Sri Lanka", ZoneId.of("Asia/Colombo")), // Colombo as an example
CityTimeZone("Khartoum", "Sudan", ZoneId.of("Africa/Khartoum")),
CityTimeZone("Paramaribo", "Suriname", ZoneId.of("America/Paramaribo")),
CityTimeZone("Stockholm", "Sweden", ZoneId.of("Europe/Stockholm")),
CityTimeZone("Bern", "Switzerland", ZoneId.of("Europe/Zurich")), // Bern as an example
CityTimeZone("Damascus", "Syria", ZoneId.of("Asia/Damascus")),
CityTimeZone("Taipei", "Taiwan", ZoneId.of("Asia/Taipei")), // Taipei as an example
CityTimeZone("Dushanbe", "Tajikistan", ZoneId.of("Asia/Dushanbe")),
CityTimeZone("Dodoma", "Tanzania", ZoneId.of("Africa/Dar_es_Salaam")), // Dodoma as an example
CityTimeZone("Bangkok", "Thailand", ZoneId.of("Asia/Bangkok")), // Bangkok as an example
CityTimeZone("Lomé", "Togo", ZoneId.of("Africa/Lome")),
CityTimeZone("Nuku'alofa", "Tonga", ZoneId.of("Pacific/Tongatapu")), // Nuku'alofa as an example
CityTimeZone("Port of Spain", "Trinidad and Tobago", ZoneId.of("America/Port_of_Spain")), // Port of Spain as an example
CityTimeZone("Tunis", "Tunisia", ZoneId.of("Africa/Tunis")),
CityTimeZone("Ankara", "Turkey", ZoneId.of("Europe/Istanbul")), // Ankara as an example
CityTimeZone("Ashgabat", "Turkmenistan", ZoneId.of("Asia/Ashgabat")),
CityTimeZone("Funafuti", "Tuvalu", ZoneId.of("Pacific/Funafuti")), // Funafuti as an example
CityTimeZone("Kampala", "Uganda", ZoneId.of("Africa/Kampala")),
CityTimeZone("Kyiv", "Ukraine", ZoneId.of("Europe/Kiev")), // Kyiv as an example
CityTimeZone("Abu Dhabi", "United Arab Emirates", ZoneId.of("Asia/Dubai")), // Abu Dhabi as an example
CityTimeZone("London", "United Kingdom", ZoneId.of("Europe/London")), // London as an example
CityTimeZone("Washington, D.C.", "United States", ZoneId.of("America/New_York")), // Washington, D.C. as an example
CityTimeZone("Montevideo", "Uruguay", ZoneId.of("America/Montevideo")), // Montevideo as an example
CityTimeZone("Tashkent", "Uzbekistan", ZoneId.of("Asia/Tashkent")),
CityTimeZone("Port Vila", "Vanuatu", ZoneId.of("Pacific/Efate")), // Port Vila as an example
CityTimeZone("Vatican City", "Vatican City", ZoneId.of("Europe/Vatican")), // Vatican City as an example
CityTimeZone("Caracas", "Venezuela", ZoneId.of("America/Caracas")),
CityTimeZone("Hanoi", "Vietnam", ZoneId.of("Asia/Ho_Chi_Minh")), // Hanoi as an example
CityTimeZone("Sana'a", "Yemen", ZoneId.of("Asia/Aden")), // Sana'a as an example
CityTimeZone("Lusaka", "Zambia", ZoneId.of("Africa/Lusaka")),
CityTimeZone("Harare", "Zimbabwe", ZoneId.of("Africa/Harare"))

)