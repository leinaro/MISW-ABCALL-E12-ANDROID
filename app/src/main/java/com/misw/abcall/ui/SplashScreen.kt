package com.misw.abcall.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec.RawRes
import com.airbnb.lottie.compose.rememberLottieComposition
import com.misw.abcall.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    continueNavigation: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = null) {
        scope.launch {
            delay(5000L)
            continueNavigation()
        }
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .testTag("SplashScreen"),
    ) {
        val composition by rememberLottieComposition(RawRes(R.raw.panda))
        LottieAnimation(
            modifier = Modifier.fillMaxWidth(),
            composition = composition,
            contentScale = ContentScale.Inside,
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge,
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelSmall,
            text = "Version v2.0.0-02-11-2024",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelSmall,
            text = "by Tears Of The Devs",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
@Preview
fun SplashScreenPreview() {
    SplashScreen()
}