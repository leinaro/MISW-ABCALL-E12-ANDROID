package com.misw.abcall.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun LanguageSelectionComponent(
    selectedLanguage: String? = null,
    launchIntent: (UserIntent)->Unit = {},
    navController: NavController = rememberNavController()
) {
    val context = LocalContext.current
    val deviceLocale = context.resources.configuration.locales.get(0)

    var currentLocale by remember { mutableStateOf(
        Language.getLanguageByCode(selectedLanguage) ?: Language.getCurrentLanguage(context)
    ) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(items = Language.allowedLocales) { language ->
                if (currentLocale == language) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        onClick = {
                            currentLocale = language
                            navController.navigateUp()
                            //launchIntent.invoke(UserIntent.UpdateLanguage(language.code))
                            //Language.setLocale(context = context, localeCode = language.code)
                        },
                        content = {
                            Text(text = stringResource(id = language.titleRes))
                        })
                } else {
                    FilledTonalButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        onClick = {
                            currentLocale = language
                            launchIntent.invoke(UserIntent.UpdateLanguage(language.code))
                            Language.setLocale(context = context, localeCode = language.code)
                            navController.navigateUp()
                        },
                        content = {
                            Text(text = stringResource(id = language.titleRes))
                        })
                }

            }
        }
    }
}