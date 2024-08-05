package com.example.clock.Presentation

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.clock.Presentation.ViewModels.AlarmViewModule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun muscicSelect(
    navController: NavController, alarmViewModel: AlarmViewModule, onMusicSelected: (Uri) -> Unit
) {
    val context = LocalContext.current
    val musicList = remember { getMusicList(context) }
    var selectedMusic by remember { mutableStateOf<String?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var selectedMusicUri by remember { mutableStateOf<Uri?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            // Ensure media player is released when disposing of the composable
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Select Music") }, modifier = Modifier.fillMaxWidth()
        )
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(musicList) { music ->
                    MusicListItem(
                        music = music,
                        selectedMusic = selectedMusic,
                        onMusicSelected = { uri ->
                            // Pause previous playback
                            mediaPlayer?.stop()
                            mediaPlayer?.release()
                            mediaPlayer = null

                            // Play selected music
                            onMusicSelected(uri)
                            mediaPlayer = MediaPlayer.create(context, uri)
                            mediaPlayer?.start()

                            // Update selected music state
                            selectedMusic = music
                            selectedMusicUri = uri
                        })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stop Playback Button
            Button(
                onClick = {
                    if (selectedMusicUri != null) {
                        alarmViewModel.setSelectedMusicUri(selectedMusicUri!!)
                        onMusicSelected(selectedMusicUri!!)
                        navController.popBackStack() // Navigate back
                    }
                },
                enabled = selectedMusicUri != null,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Set Alarm RingTone")
            }
        }
    })
}

@Composable
fun MusicListItem(
    music: String, selectedMusic: String?, onMusicSelected: (Uri) -> Unit
) {
    val context = LocalContext.current
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            val uri = getMusicUri(context, music)
            onMusicSelected(uri)
        }
        .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = music)
        if (music == selectedMusic) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null)
        }
    }
}

// Helper function to get a list of music from device
fun getMusicList(context: Context): List<String> {
    val musicList = mutableListOf<String>()
    val projection = arrayOf(
        MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA
    )
    val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
    val cursor = context.contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null
    )
    cursor?.use { cursor ->
        while (cursor.moveToNext()) {
            val name =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
            val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
            Log.d("MusicSelect", "Found music: $name at path: $path")
            musicList.add(name)
        }
    }

    Log.d("MusicSelect", "Music Not found: ${musicList.size}")
    return musicList
}

// Helper function to get URI of selected music
fun getMusicUri(context: Context, musicName: String): Uri {
    val projection = arrayOf(
        MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA
    )
    val selection = "${MediaStore.Audio.Media.DISPLAY_NAME} = ?"
    val selectionArgs = arrayOf(musicName)
    var musicUri: Uri? = null

    context.contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            val uriColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val uriString = cursor.getString(uriColumnIndex)
            musicUri = Uri.parse(uriString)
        }
    }

    return musicUri ?: Uri.EMPTY
}