package com.hotelka.voicerobot.presentation.ui.content.headers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hotelka.voicerobot.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(currentConnection: String, onSettingsClick: () -> Unit) {
    LargeTopAppBar(
        title = {
            Column {
                Text(
                    currentConnection,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    stringResource(R.string.current_connection),
                    fontSize = 16.sp
                )
            }
        },
        modifier = Modifier.clip(
            RoundedCornerShape(
                bottomEnd = 20.dp,
                bottomStart = 20.dp
            )
        ),
        actions = {
            IconButton(
                onClick = onSettingsClick,
            ) {
                Icon(
                    painterResource(R.drawable.settings),
                    "Settings"
                )
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}