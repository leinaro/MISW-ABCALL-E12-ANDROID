package com.misw.abcall.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.misw.abcall.R
import com.misw.abcall.domain.IncidentDTO
import com.misw.abcall.ui.UserIntent.OnNewIntent
import com.misw.abcall.ui.UserIntent.SearchIncident
import com.misw.abcall.ui.state.ABCallEvent
import com.misw.abcall.ui.state.ABCallEvent.NotificationReceived
import com.misw.abcall.ui.state.MainViewState
import com.misw.abcall.ui.theme.ABCallTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: ABCallViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult<String, Boolean>(
        RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(
                this, "Notifications permission granted",
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            Toast.makeText(
                this,
                "FCM can't post notifications without POST_NOTIFICATIONS permission",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            val incidentId = it.getStringExtra("incident_id")
            incidentId?.let {
                viewModel.onUserIntent(OnNewIntent(incidentId))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val state by viewModel.state.collectAsState()
            val event by viewModel.event.collectAsState()

            val isRefreshing by viewModel.isRefreshing.collectAsState()
            val isInternetAvailable by viewModel.isInternetAvailable.collectAsState()


            ABCallTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainScreen(
                        state = state,
                        event = event,
                        isInternetAvailable = isInternetAvailable,
                        launchIntent = { userIntent ->
                            viewModel.onUserIntent(userIntent)
                        },
                        getIncident = { viewModel.getIncident() },
                        intent = intent,
                    )
                }
            }
        }

        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(com.misw.abcall.R.string.default_notification_channel_id)
            val channelName = getString(com.misw.abcall.R.string.app_name)
            val notificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW
                )
            )
        }

        if (intent.extras != null) {
            for (key in intent.extras!!.keySet()) {
                val value = intent.extras!![key]
                Log.d("iarl", "Key: $key Value: $value")
            }
        }

        askNotificationPermission();
    }
    private fun askNotificationPermission() {
        // This is only necessary for API Level > 33 (TIRAMISU)
        if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}


@Composable
fun MainScreen(
    state: MainViewState = MainViewState(),
    event: ABCallEvent = ABCallEvent.Idle,
    isInternetAvailable: Boolean = true,
    launchIntent: (UserIntent) -> Unit = {},
    getIncident: () -> IncidentDTO?,
    intent: Intent? = null,
) {
    var offlineBannerVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val showDialogFromIntent = intent?.getBooleanExtra("showDialog", false)
    val dialogTitle = intent?.getStringExtra("title") ?: "Título"
    val dialogBody = intent?.getStringExtra("body") ?: "Mensaje recibido"
    var incidentId = intent?.getStringExtra("incident_id") ?: ""
    var showDialog by remember { mutableStateOf(showDialogFromIntent ?: false || incidentId.isNotEmpty()) }

    if (event is NotificationReceived) {
        showDialog = true
        incidentId = event.incidentId
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .testTag("MainScreen")) {
        if (isInternetAvailable.not() && offlineBannerVisible) {
            OffLineBanner {
                offlineBannerVisible = false
            }
        }
        var showSplash by remember { mutableStateOf(true) }
        if (showSplash) {
            SplashScreen(
                continueNavigation = {
                    showSplash = false
                }
            )
        }
        else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ){
                MainScreenContent(
                    state = state,
                    event = event,
                    launchIntent = launchIntent,
                    getIncident = getIncident,
                )

                if (showDialog && incidentId.isNotEmpty()) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text(text = dialogTitle) },
                        text = { Text(text = dialogBody) },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDialog = false
                                    launchIntent(
                                        SearchIncident(incidentId)
                                    )
                                }) {
                                Text("Ir a la incidencia")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showDialog = false }) {
                                Text("Cerrar")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ABCallTheme {
        MainScreen(getIncident = { IncidentDTO() })
    }
}

class SnackbarVisualsWithError(
    override val message: String,
    val isError: Boolean,
) : SnackbarVisuals {
    override val actionLabel: String
        get() = if (isError) "OK" else "OK"
    override val withDismissAction: Boolean
        get() = false
    override val duration: SnackbarDuration
        get() = SnackbarDuration.Long
}

@Composable
fun OffLineBanner(
    closeClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(4.dp)
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = stringResource(id = R.string.no_internet_mode),
            color = MaterialTheme.colorScheme.surfaceTint,
        )
        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(4.dp)
                .clickable { closeClick() },
            imageVector = Filled.Close,
            contentDescription = stringResource(R.string.close),
        )
    }
}