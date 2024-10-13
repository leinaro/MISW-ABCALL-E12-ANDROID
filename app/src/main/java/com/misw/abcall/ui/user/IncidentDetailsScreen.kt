package com.misw.abcall.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.misw.abcall.ui.theme.ABCallTheme

@Composable
fun IncidentDetailsScreen(incidentId: Int?) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "Datos del incidente",
            style = MaterialTheme.typography.titleLarge,
        )

        DataBox("Id", "1234QQWERT")
        DataBox("Titulo", "Problemas de internet")
        DataBox("descripcion", "No tengo internet en casa")
        DataBox("Tipo", "Servicio internet")
        DataBox("Estado", "Abierto")
        
    }
}

@Composable
private fun DataBox(title: String, description: String){
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(Color(0xFFFEF7FF))
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
        Text (
            text = description,
        style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun IncidentDetailsScreenPreview() {
    ABCallTheme {
        IncidentDetailsScreen(incidentId = 1)
    }
}
