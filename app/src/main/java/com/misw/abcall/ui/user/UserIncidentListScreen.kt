package com.misw.abcall.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.misw.abcall.ui.theme.ABCallTheme

@Composable
fun UserIncidentListScreen(
) {
    LazyColumn {
        item {
            Text(
                text = "Mis Incidencias Abiertas",
                style = MaterialTheme.typography.titleLarge,
            )

        }

        items(3) {
            DataBox("Incidente $it")
        }
        item {
            Text(
                text = "Mis Incidencias Cerradas",
                style = MaterialTheme.typography.titleLarge,
            )
        }
        items(3) {
            DataBox("Incidente $it")
        }
    }
}

@Composable
private fun DataBox(title: String, onClick: () -> Unit = {}){
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Color(0xFFFEF7FF))
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
@Preview
fun UserIncidentListScreenPreview() {
    ABCallTheme {
        UserIncidentListScreen()
    }
}