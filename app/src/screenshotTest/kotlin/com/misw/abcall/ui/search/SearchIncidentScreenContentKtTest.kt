package com.misw.abcall.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.misw.abcall.domain.IncidentDTO
import com.misw.abcall.ui.Language
import com.misw.abcall.ui.LanguageSelectionComponent
import com.misw.abcall.ui.MainScreen
import com.misw.abcall.ui.MainScreenContent
import com.misw.abcall.ui.SplashScreen
import com.misw.abcall.ui.chat.ActivateChatScreen
import com.misw.abcall.ui.chat.ChatInputField
import com.misw.abcall.ui.chat.ChatMessage
import com.misw.abcall.ui.chat.ChatMessageView
import com.misw.abcall.ui.chat.ChatScreen
import com.misw.abcall.ui.chat.RateChat
import com.misw.abcall.ui.common.ABCallTopAppBar
import com.misw.abcall.ui.common.InfoDialog
import com.misw.abcall.ui.state.MainViewState
import com.misw.abcall.ui.theme.ABCallTheme
import com.misw.abcall.ui.user.IncidentDetailsScreen
import com.misw.abcall.ui.user.UserIncidentListScreen

class SearchIncidentScreenContentKtTest {
/*
    @Preview(showBackground = true)
    @Composable
    fun SearchIncidentScreenContentPreview() {
        ABCallTheme {
            SearchIncidentScreenContent()
        }
    }*/

    @Composable
    @Preview
    fun UserIncidentListScreenPreview() {
        ABCallTheme {
            UserIncidentListScreen()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ABCallTheme {
            MainScreen(getIncident = { IncidentDTO() })
        }
    }
/*
    @Composable
    @Preview
    fun SplashScreenPreview() {
        SplashScreen()
    }*/

    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        ABCallTheme(dynamicColor = false, darkTheme = true) {
            MainScreenContent(
                state = MainViewState(),
                getIncident = { IncidentDTO() }
            )
        }
    }
/*
    @Composable
    @Preview
    fun ABCallTopAppBarPreview() {
        ABCallTheme(dynamicColor = false, darkTheme = true) {
            ABCallTopAppBar()
        }
    }*/


    @Preview(showBackground = true)
    @Composable
    fun LanguagePreviewScreen() {
        ABCallTheme(dynamicColor = false, darkTheme = true) {
            LanguageSelectionComponent()
        }
    }

    @Composable
    @Preview(showBackground = true)
    fun PreviewActivateChatScreen() {
        ABCallTheme {
            ActivateChatScreen()
        }
    }


    @Composable
    @Preview(showBackground = true)
    fun PreviewChatMessageView() {
        ABCallTheme {
            ChatMessageView(ChatMessage())
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
    @Preview
    fun PreviewChatInputField() {
        ABCallTheme {
            ChatInputField(onSendMessage = {})
        }
    }

    @Composable
    @Preview(showBackground = true)
    fun PreviewRateChat() {
        ABCallTheme {
            RateChat()
        }
    }

    @Composable
    @Preview
    fun InfoDialogPreview() {
        InfoDialog(
            onDismissRequest = {},
        )
    }

    @Composable
    @Preview(showBackground = true, showSystemUi = true)
    fun IncidentDetailsScreenPreview() {
        ABCallTheme {
            IncidentDetailsScreen(incidentId = 1, incident = null)
        }
    }
}