package com.misw.abcall.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.misw.abcall.R
import com.misw.abcall.ui.Routes.SearchIncident
import com.misw.abcall.ui.theme.ABCallTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ABCallTopAppBar(
    navController: NavController = rememberNavController(),
    onInfoActionClick: () -> Unit = {},
) {
    val baseRoute = listOf(SearchIncident.path)
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    TopAppBar(
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.app_name),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
            )
        },
        navigationIcon = {
            if (currentBackStackEntry?.destination?.route !in baseRoute) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { onInfoActionClick() }) {
                Icon(
                    imageVector = Outlined.Info,
                    contentDescription = "Info",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        )
    )
}

@Composable
@Preview
fun ABCallTopAppBarPreview() {
    ABCallTheme(dynamicColor = false, darkTheme = true) {
        ABCallTopAppBar()
    }
}
