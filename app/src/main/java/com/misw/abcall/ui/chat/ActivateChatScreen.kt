package com.misw.abcall.ui.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.misw.abcall.R
import com.misw.abcall.ui.UserIntent
import com.misw.abcall.ui.theme.ABCallTheme
import java.util.Date

@Composable
fun ActivateChatScreen(
    modifier: Modifier = Modifier,
    launchIntent: (UserIntent) ->Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 60.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            border = BorderStroke(width = 2.dp, color = Color(0xFFE3E3E3))
        ) {
            Text(
                modifier = Modifier.padding(28.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                text = stringResource(
                    R.string.hola_presiona_el_boton_de_comenzar_si_estas_de_acuerdo_con_nuestra_politica_de_manejo_de_datos
                )
            )

            Button(
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEADDFF),
                    contentColor = Black
                ),
                modifier = Modifier
                    .testTag("ActivateChatButton")
                    .align(CenterHorizontally)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 22.dp),
                onClick = {
                    launchIntent(UserIntent.OpenChat)
                }
            ) {
                Text(stringResource(R.string.comenzar))
            }

        }

        Image(
            painter = painterResource(id = R.drawable.ic_support_agent),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFFEADDFF))
                .size(56.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-50).dp) // Offset to make it half inside the container
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewActivateChatScreen() {
    ABCallTheme {
        ActivateChatScreen()
    }
}




data class ChatMessage(
    val message: String = "Hola! Soy un agente virtual, en que puedo ayudarte?",
    val isAgent: Boolean = true,
    val time: String = "Livechat ${Date().toLocaleString()}",
    val language: String = "en"
)