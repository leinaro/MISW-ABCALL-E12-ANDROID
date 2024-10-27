package com.misw.abcall.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.misw.abcall.R
import com.misw.abcall.domain.IncidentDTO
import com.misw.abcall.ui.chat.ActivateChatScreen
import com.misw.abcall.ui.chat.ChatMessage
import com.misw.abcall.ui.chat.ChatScreen
import com.misw.abcall.ui.common.ABCallTopAppBar
import com.misw.abcall.ui.common.InfoDialog
import com.misw.abcall.ui.common.LocaleDropdownMenu
import com.misw.abcall.ui.state.ABCallEvent
import com.misw.abcall.ui.state.ABCallEvent.Idle
import com.misw.abcall.ui.state.ABCallEvent.NavigateBack
import com.misw.abcall.ui.state.ABCallEvent.NavigateTo
import com.misw.abcall.ui.state.ABCallEvent.ShowError
import com.misw.abcall.ui.state.ABCallEvent.ShowSuccess
import com.misw.abcall.ui.state.MainViewState
import com.misw.abcall.ui.theme.ABCallTheme
import com.misw.abcall.ui.user.IncidentDetailsScreen
import com.misw.abcall.ui.search.SearchIncidentScreenContent
import com.misw.abcall.ui.user.UserDetailsScreen
import com.misw.abcall.ui.user.UserIncidentListScreen
import kotlinx.coroutines.launch

//@OptIn(ExperimentalMaterialApi::class)
@Composable fun MainScreenContent(
    state: MainViewState,
    event: ABCallEvent = Idle,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    launchIntent: (UserIntent) -> Unit = {},
    getIncident: () -> IncidentDTO?,
) {
    val context = LocalContext.current

    val navController = rememberNavController()

/*    fun navigateTo(route: String) {
        navController.navigate(route)
    }*/

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("albums", "artists", "collectors")
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isInfoDialogVisible by remember { mutableStateOf(false) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    /*
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )*/

    fun showSnackBar(message: String) {
        try {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    visuals = SnackbarVisualsWithError(
                        message = message,
                        isError = true,
                    ),
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        //Do Something
                    }

                    SnackbarResult.Dismissed -> {
                        //Do Something
                    }
                }
            }
        } finally {
            //onDismissSnackBarState()
        }
    }

    LaunchedEffect(key1 = event) {
        when (event) {
            is NavigateBack -> {
                navController.navigateUp()
            }

            is ShowError -> {
                showSnackBar(event.message)
            }

            is ShowSuccess -> {
                showSnackBar(event.message)
            }

            is NavigateTo -> {
                navController.navigate(event.route)
            }

            is Idle -> Unit
        }
    }

    Scaffold(
        floatingActionButton = {
            when (currentBackStackEntry?.destination?.route){
                "chat" -> {}
                else -> {
                    ExtendedFloatingActionButton(
                        onClick = { launchIntent(UserIntent.StartChat) },
                        containerColor = Color(0xFFEADDFF),
                        contentColor = Color(0xFF21005D),
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_support_agent), contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Hablar con un agente")
                    }
                }
            }
        },
        topBar = {
            ABCallTopAppBar(
                navController = navController,
                onInfoActionClick = {
                    isInfoDialogVisible = true
                }
            )
        },
         snackbarHost = {
             SnackbarHost(hostState = snackbarHostState)
         },
/*         bottomBar = {
             ABCallNavigationBar(
                 onItemSelected = { selected ->
                     selectedItem = selected
                     navController.navigate(items[selected])
                 },
                 selected = selectedItem
             )
         },*/
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LocaleDropdownMenu(
                onLocaleSelected = { locale ->
                    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(locale)
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
            )
            NavHost(
                navController = navController,
                startDestination = Routes.SearchIncident.path,
                modifier = Modifier
                    .fillMaxSize()
                //.pullRefresh(pullRefreshState)
            ) {
                composable(Routes.SearchIncident.path) {
                    SearchIncidentScreenContent(
                        launchIntent = launchIntent,
                    )
                }
                composable(
                    route = Routes.IncidentDetails.path,
                    arguments = listOf(
                        navArgument("incidentId") { type = NavType.StringType }
                    )) { backStackEntry ->
                    val incidentId = backStackEntry.arguments?.getInt("incidentId")
                    IncidentDetailsScreen(incident = getIncident())
                }
                composable(
                    route = Routes.UserIncidentList.path,
                    arguments = listOf(
                        navArgument("userId") { type = NavType.StringType }
                    )) { backStackEntry ->
                    val userId = backStackEntry.arguments?.getInt("userId")
                    UserIncidentListScreen()
                }
                composable(
                    route = Routes.UserDetails.path,
                    arguments = listOf(
                        navArgument("userId") { type = NavType.StringType }
                    )) { backStackEntry ->
                    val userId = backStackEntry.arguments?.getInt("userId")
                    UserDetailsScreen(userId = userId, navController = navController)
                }
                composable(
                    route = Routes.ActivateChat.path,
                ) {
                    ActivateChatScreen(
                        launchIntent = launchIntent,
                    )
                }
                composable(
                    route = Routes.Chat.path,
                ) {
                    ChatScreen(
                        launchIntent = launchIntent,
                        list = state.messageList,
                        isTyping = state.isTyping,
                    )
                }
            }
            /*
                        PullRefreshIndicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            refreshing = isRefreshing,
                            state = pullRefreshState,
                        )*/

            SnackbarHost(snackbarHostState) { data ->
                Snackbar {
                    Text(data.visuals.message)
                }
            }
        }
    }

    if (isInfoDialogVisible) {
        InfoDialog(
            onDismissRequest = {
                isInfoDialogVisible = false
            })
    }
    //if (true) {
       // LoadingScreen(userId = userId, navController = navController)
    //}
}

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

