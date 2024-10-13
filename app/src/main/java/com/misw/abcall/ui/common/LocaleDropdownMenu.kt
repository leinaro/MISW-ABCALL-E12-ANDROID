package com.misw.abcall.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.misw.abcall.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocaleDropdownMenu(
    modifier: Modifier = Modifier,
    onLocaleSelected: (String) -> Unit = {},
) {
    val localeOptions = mapOf(
        R.string.en to "en",
//        R.string.fr to "fr",
//        R.string.hi to "hi",
//        R.string.ja to "ja",
        R.string.es to "es",
    ).mapKeys { stringResource(it.key) }
    var selectedLocale by remember { mutableStateOf("en") }

    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, end = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .width(100.dp),
                readOnly = true,
                shape = RoundedCornerShape(16.dp),
                value = selectedLocale.uppercase(),
                onValueChange = { },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                localeOptions.keys.forEach { selectionLocale ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            selectedLocale = localeOptions[selectionLocale] ?: "en"
                            onLocaleSelected(localeOptions[selectionLocale] ?: "en")
                        },
                        text = { Text(selectionLocale) }
                    )
                }
            }
        }
    }
}