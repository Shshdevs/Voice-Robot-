package com.hotelka.voicerobot.presentation.ui.content.sheets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hotelka.voicerobot.R

@Composable
fun MyRobotsSheet(
    modifier: Modifier,
    isExpanded: Boolean,
    onExpand:() -> Unit
) {
    Card(
        onClick = onExpand,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ),
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(Modifier.padding(vertical = 10.dp, horizontal = 20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    stringResource(R.string.my_robots),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    painterResource(R.drawable.dropdown),
                    "Dropdown",
                    modifier = Modifier.rotate(180f).size(32.dp)
                )
            }
        }
    }
}