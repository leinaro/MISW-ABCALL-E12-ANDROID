package com.misw.abcall.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.misw.abcall.R
import com.misw.abcall.R.string
import com.misw.abcall.ui.Routes
import com.misw.abcall.ui.UserIntent
import com.misw.abcall.ui.UserIntent.SearchIncident
import com.misw.abcall.ui.theme.ABCallTheme

@Composable
fun SearchIncidentScreenContent(
    launchIntent: (UserIntent)->Unit = {},
) {
    var query by remember { mutableStateOf("") }
    var isError: Boolean? by rememberSaveable { mutableStateOf(null) }
    val charLimit = 5

    fun validate(text: String): Boolean {
        isError = !(text.length > charLimit)
        return text.length > charLimit
    }

    val errorMessage = if (query.isBlank()){
        stringResource(string.required_not_empty)
    }
    else {
        stringResource(string.max_char_limit, charLimit)
    }

    /*val painter = rememberAsyncImagePainter(
        model = Builder(LocalContext.current)
            .data(state.album?.cover)
            .error(drawable.baseline_broken_image_24)
            .size(Size.ORIGINAL)
            .build(),
    )*/
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        /*Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray)
                .aspectRatio(1f),
            contentScale = ContentScale.Inside,
        )*/
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(R.string.search_incident_query),
                )
            },
            label = {
                Text(
                    modifier = Modifier.background(Color(0xFFFEF7FF)),
                    text = stringResource(R.string.search_incident_query),
                )
            },
            value = query,
            isError = isError ?: false,
            supportingText = {
                if (isError == true) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            onValueChange = { query = it }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    if (validate(query)) {
                        launchIntent(SearchIncident(query))
                        return@Button
                    }
                },
                modifier = Modifier
                    .testTag("SearchIncidentButton")

            ) {
                Text(
                    text = stringResource(R.string.search_incident),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchIncidentScreenContentPreview() {
    ABCallTheme {
        SearchIncidentScreenContent()
    }
}
