package com.misw.abcall.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.misw.abcall.R
import com.misw.abcall.ui.TypingIndicator
import com.misw.abcall.ui.UserIntent
import com.misw.abcall.ui.theme.ABCallTheme

@Composable
fun ChatScreen(
    launchIntent: (UserIntent)->Unit = {},
    list: List<ChatMessage> = emptyList(),
    isTyping: Boolean = false
) {
    val listState = rememberLazyListState()

    LaunchedEffect(list.size) {
        listState.animateScrollToItem(list.size)
    }

    Column (
        modifier = Modifier.background(Color(0xFFE3E3E3))
    ){
        RateChat(
            modifier = Modifier,
        )

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = 23.dp),
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(Color(0xFFE3E3E3))
                .padding(vertical = 23.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list) { chatMessage ->
                ChatMessageView(chatMessage)
            }
            if (isTyping){
                item {
                    TypingIndicator()
                }
            }
            item {
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp))
            }
        }

        ChatInputField(
            modifier = Modifier,
            onSendMessage = { message ->
                launchIntent(UserIntent.SendMessage(message))
            }
        )

    }
}

@Composable
@Preview(showBackground = true)
fun PreviewChatScreen() {
    ABCallTheme {
        ChatScreen()
    }
}

@Composable
fun ChatInputField(
    modifier: Modifier = Modifier,
    onSendMessage: (String) -> Unit = {}
) {
    var textState by remember { mutableStateOf(TextFieldValue()) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = textState,
            onValueChange = { textState = it },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = {
                Text(text = stringResource(R.string.escribe_un_mensaje))
                          },
            maxLines = 3
        )

        IconButton(
            onClick = {
                onSendMessage(textState.text)
                textState = TextFieldValue()
            },
            enabled = textState.text.isNotBlank()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Enviar mensaje",
                tint = if (textState.text.isNotBlank()) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }
    }
}

@Composable
@Preview
fun PreviewChatInputField() {
    ABCallTheme {
        ChatInputField(onSendMessage = {})
    }
}