package com.misw.abcall.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.misw.abcall.R
import com.misw.abcall.ui.theme.ABCallTheme

@Composable
fun RateChat(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth(),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        ){
        Icon(
            painter = painterResource(id = R.drawable.ic_support_agent),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(48.dp)
                .border(width = 2.dp, color = Color(0xFFE3E3E3))
                .padding(12.dp)
        )
            Text(
                text = stringResource(R.string.how_would_you_rate_this_chat),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(start = 16.dp)
            )

        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.ic_good),
                contentDescription = null,
                tint = Color(0xFF454B58)
            )
        }
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.ic_bad),
                contentDescription = null,
                tint = Color(0xFF454B58)
            )
        }

    }
}

@Composable
@Preview(showBackground = true)
fun PreviewRateChat() {
    ABCallTheme {
        RateChat()
    }
}