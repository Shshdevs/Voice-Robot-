package com.hotelka.voicerobot.presentation.ui.content

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.hotelka.voicerobot.presentation.events.HomeScreenEvents
import com.hotelka.voicerobot.presentation.model.HomeScreenUiModel
import com.hotelka.voicerobot.presentation.ui.content.headers.HomeTopBar
import com.hotelka.voicerobot.presentation.ui.content.widgets.PulsatingButton
import com.hotelka.voicerobot.presentation.ui.content.widgets.VoiceWaveform

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    homeUiModel: HomeScreenUiModel,
    onEvent: (HomeScreenEvents) -> Unit
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) onEvent(HomeScreenEvents.OnStartStopMicEvent)
    }
    Box {
        Column(Modifier.fillMaxSize()) {
            HomeTopBar("dsfkjdsfhsjk-021e3") { }
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PulsatingButton(
                    buttonSize = 120.dp,
                    pulseSize = 132.dp,
                    isPulsating = homeUiModel.micIsOn,
                    onClick = {
                        if (hasRecordAudioPermission(context)) {
                            onEvent(HomeScreenEvents.OnStartStopMicEvent)
                        } else {
                            launcher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }
                )

                Spacer(Modifier.height(15.dp))
                Text(
                    homeUiModel.command.replaceFirstChar { it.uppercase() },
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                VoiceWaveform(
                    Modifier.height(55.dp),
                    barWidth = 5.dp,
                    gap = 5.dp,
                    heights = homeUiModel.bars
                )
            }
            Spacer(Modifier.height(50.dp))
        }
//        MyRobotsSheet(
//            Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp),
//            isExpanded = homeUiModel.myRobotsExpanded,
//            onExpand = { onEvent(HomeScreenEvents.OnExpandMyRobots) })
    }
}

fun hasRecordAudioPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED
}