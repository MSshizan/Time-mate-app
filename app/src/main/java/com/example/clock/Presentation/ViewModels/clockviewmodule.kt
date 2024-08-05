package com.example.clock.Presentation.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clock.domainLayer.CityTimeZone
import com.example.clock.Database.DataBaseApplicationClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class ClockViewModule: ViewModel() {

    val cityTimeZoneDao = DataBaseApplicationClass.CitiesDataBase.getCitiesdao()

    val selectedCities: Flow<List<CityTimeZone>> = cityTimeZoneDao.getAllSelectedCities()


    fun addSelectedCity(cityTimeZone: CityTimeZone) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingCity = cityTimeZoneDao.getCityByName(cityTimeZone.city)
            if (existingCity == null) {
                cityTimeZoneDao.addCity(cityTimeZone)
            } else {
                Log.d("ClockViewModel", "City ${cityTimeZone.city} already exists in the database")
            }
        }
    }

    fun deleteCity(city:String){
        viewModelScope.launch(Dispatchers.IO) {
            cityTimeZoneDao.deleteCity(city)
        }

    }

   }