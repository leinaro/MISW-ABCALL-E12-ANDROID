package com.misw.abcall.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.misw.abcall.R
import com.misw.abcall.ui.theme.ABCallTheme

@Composable fun ChatMessageView(
    chatMessage: ChatMessage,
) {
    Row(
        modifier = Modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (chatMessage.isAgent) {
            Icon(
                painter = painterResource(id = R.drawable.ic_support_agent),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEADDFF))
                    .padding(2.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f) // Solo el componente central usa el espacio restante
        ) {
            Text(
                style = MaterialTheme.typography.labelSmall,
                text = chatMessage.time,
                modifier = Modifier
                    .padding(top = 2.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                textAlign = if (chatMessage.isAgent) TextAlign.Start else TextAlign.End,
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 9.dp, horizontal = 14.dp),
                    text = chatMessage.message
                )
            }
        }

        if (!chatMessage.isAgent) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEADDFF))
                    .padding(2.dp)
            )
        }
    }

}

@Composable
@Preview(showBackground = true)
fun PreviewChatMessageView() {
    ABCallTheme {
        ChatMessageView(ChatMessage())
    }
}