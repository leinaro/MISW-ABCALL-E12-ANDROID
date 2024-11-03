package com.misw.abcall

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.misw.abcall.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ChatBotInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

 /*   @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()*/

    var id = "1b97c36c-c583-4193-acc2-e62028451706"
    @Test
    fun createYConsultarIncidentByChatBot() {
        composeTestRule
            .onNodeWithTag("MainScreen")
            .assertExists()

        composeTestRule
            .onNodeWithTag("SplashScreen")
            .assertIsDisplayed()

        composeTestRule
            .mainClock
            .advanceTimeBy(5000)

        composeTestRule
            .onNodeWithTag("MainScreenContent")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("StartChatBotButton")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("StartChatBotButton")
            .performClick()

        composeTestRule
            .onNodeWithTag("ActivateChatScreen")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("ActivateChatButton")
            .assertIsDisplayed()

        composeTestRule
            .mainClock
            .advanceTimeBy(5000)
        Thread.sleep(1000)

        composeTestRule
            .onNodeWithTag("ActivateChatButton")
            .performClick()

        Thread.sleep(1000)

        composeTestRule
            .mainClock
            .advanceTimeBy(5000)

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Hola! Soy un agente virtual, en que puedo ayudarte?").fetchSemanticsNodes().isNotEmpty()
        }

        Thread.sleep(1000)
        composeTestRule.onNodeWithTag("chatInput")
            .performTextInput("Hola")
        Thread.sleep(1000)

        composeTestRule
            .onNodeWithTag("ChatScreen")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("sendMessage")
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Hola, ¿En qué puedo ayudarte?").fetchSemanticsNodes().isNotEmpty()
        }

        Thread.sleep(1000)
        composeTestRule.onNodeWithTag("chatInput")
            .performTextInput("mi internet no funciona")
        Thread.sleep(1000)

        composeTestRule
            .onNodeWithTag("sendMessage")
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Entendido, quieres registrar el incidente con la siguiente descripción: mi internet no funciona?").fetchSemanticsNodes().isNotEmpty()
        }

        Thread.sleep(1000)
        composeTestRule.onNodeWithTag("chatInput")
            .performTextInput("si")
        Thread.sleep(1000)

        composeTestRule
            .onNodeWithTag("sendMessage")
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Porfavor ingresa tu numero de identificacion sin espacios ni puntos").fetchSemanticsNodes().isNotEmpty()
        }

        Thread.sleep(1000)
        composeTestRule.onNodeWithTag("chatInput")
            .performTextInput("1234567890")
        Thread.sleep(1000)

        composeTestRule
            .onNodeWithTag("sendMessage")
            .performClick()


        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText(
                text = "He creado el incidente con el id",
                substring = true,
                ignoreCase = true
            ).fetchSemanticsNodes().isNotEmpty()
        }

        val messageText = composeTestRule
            .onNodeWithText("He creado el incidente con el id ", substring = true, ignoreCase = true)
            .fetchSemanticsNode()
            .config[SemanticsProperties.Text]
            .firstOrNull()
            ?.text ?: ""

        val regex = "He creado el incidente con el id ([\\w-]+) y se encuentra en estado (\\w+)".toRegex()

        val matchResult = regex.find(messageText)
        val (id, estado) = matchResult?.destructured ?: throw AssertionError("El mensaje no tiene el formato esperado")
        this.id = id

        Thread.sleep(1000)
        composeTestRule.onNodeWithTag("chatInput")
            .performTextInput("quiero consultar un incidente")
        Thread.sleep(1000)

        composeTestRule
            .onNodeWithTag("sendMessage")
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Escribe el número de incidente de quieres consultar").fetchSemanticsNodes().isNotEmpty()
        }

        Thread.sleep(1000)
        composeTestRule.onNodeWithTag("chatInput")
            .performTextInput(id)
        Thread.sleep(1000)

        composeTestRule
            .onNodeWithTag("sendMessage")
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("El incidente $id se encuentra en estado OPEN").fetchSemanticsNodes().isNotEmpty()
        }

    }

    @Test
    fun consultarIncidente() {
        composeTestRule
            .onNodeWithTag("MainScreen")
            .assertExists()

        composeTestRule
            .onNodeWithTag("SplashScreen")
            .assertIsDisplayed()

        composeTestRule
            .mainClock
            .advanceTimeBy(5000)

        composeTestRule
            .onNodeWithTag("MainScreenContent")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("SearchIncidentScreenContent")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("consultarIncidenteEdittext")
            .assertIsDisplayed()

        Thread.sleep(1000)
        composeTestRule.onNodeWithTag("consultarIncidenteEdittext")
            .performTextInput(id)
        Thread.sleep(1000)

        composeTestRule
            .onNodeWithTag("SearchIncidentButton")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("SearchIncidentButton")
            .performClick()

        Thread.sleep(1000)
/*
        composeTestRule
            .onNodeWithTag("incidentDetailScreen")
            .assertIsDisplayed()*/
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText(this.id).fetchSemanticsNodes().isNotEmpty()
        }

        Thread.sleep(1000)
    }
}
