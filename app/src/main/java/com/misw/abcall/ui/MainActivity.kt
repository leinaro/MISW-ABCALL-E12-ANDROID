package com.misw.abcall.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.misw.abcall.R
import com.misw.abcall.domain.IncidentDTO
import com.misw.abcall.ui.state.ABCallEvent
import com.misw.abcall.ui.state.MainViewState
import com.misw.abcall.ui.theme.ABCallTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ABCallViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val state by viewModel.state.collectAsState()
            val event by viewModel.event.collectAsState()

            val isRefreshing by viewModel.isRefreshing.collectAsState()
            val isInternetAvailable by viewModel.isInternetAvailable.collectAsState()

            var offlineBannerVisible by rememberSaveable {
                mutableStateOf(true)
            }

            ABCallTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        state = state,
                        event = event,
                        isRefreshing = isRefreshing,
                        isInternetAvailable = isInternetAvailable,
                        launchIntent = { userIntent ->
                            viewModel.onUserIntent(userIntent)
                        },
                        getIncident = { viewModel.getIncident() },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    state: MainViewState = MainViewState(),
    event: ABCallEvent = ABCallEvent.Idle,
    isRefreshing: Boolean = false,
    isInternetAvailable: Boolean = true,
    launchIntent: (UserIntent) -> Unit = {},
    getIncident: () -> IncidentDTO?,
) {
    var offlineBannerVisible by rememberSaveable {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.fillMaxSize()) {
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
        } else {
            MainScreenContent(
                state = state,
                event = event,
                launchIntent = launchIntent,
                getIncident = getIncident,
            )
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