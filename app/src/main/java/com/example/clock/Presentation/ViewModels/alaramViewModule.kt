package com.example.clock.Presentation.ViewModels
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clock.dataLayer.Repositories.AlarmRepository
import com.example.clock.dataLayer.sheduler.alarmSheduler
import com.example.clock.domainLayer.alarmdataClass


class AlarmViewModule(private val alarmRepository: AlarmRepository, private val alarmSheduler: alarmSheduler): ViewModel() {


    private val _alarms = MutableLiveData<List<alarmdataClass>>(emptyList())
    val alarms:LiveData<List<alarmdataClass>> = _alarms

    private val _selectedMusicUri = MutableLiveData<Uri?>()
    val selectedMusicUri: LiveData<Uri?> = _selectedMusicUri

    init {
        _alarms.value = alarmRepository.loadAlarm()
        _alarms.value?.forEach { alarm ->
            if (alarm.isEnabled) {
                alarmSheduler.setAlarm(alarm)
            }
        }
    }



    fun addAlarm(alarm: alarmdataClass) {
        _alarms.value = _alarms.value?.toMutableList()?.apply {
            add(alarm)
        }
        alarmRepository.saveAlarms(_alarms.value ?: emptyList())
        alarmSheduler.setAlarm(alarm)
    }

    fun updateAlarm(alarm: alarmdataClass){
        _alarms.value=_alarms.value?.map {
            if(it.id==alarm.id)alarm else it
        }
        alarmRepository.saveAlarms(_alarms.value ?: emptyList())
        if (alarm.isEnabled) {
            alarmSheduler.setAlarm(alarm)
        } else {
            alarmSheduler.cancelAlarm(alarm)
        }
        Log.d("AlarmUpdate", "Updating alarm with ID: ${alarm.id}, Music URI: ${alarm.musicUri}")
    }

    fun removeAlarm(alarm: alarmdataClass){
        _alarms.value = _alarms.value?.filter { it.id != alarm.id }
        alarmRepository.saveAlarms(_alarms.value ?: emptyList())
        alarmSheduler.cancelAlarm(alarm)
    }
    fun setSelectedMusicUri(uri: Uri?) {
        _selectedMusicUri.value = uri
        Log.d("AlarmViewModel", "Selected Music URI: $uri")
    }
    fun getAlarmById(id: Int): alarmdataClass? {
        return _alarms.value?.find { it.id == id }
    }

}