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
import com.misw.abcall.domain.IncidentDTO
import com.misw.abcall.ui.theme.ABCallTheme

@Composable
fun IncidentDetailsScreen(incidentId: Int? = null, incident: IncidentDTO?) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "Datos del incidente",
            style = MaterialTheme.typography.titleLarge,
        )

        DataBox("Id", incident?.id.orEmpty())
        DataBox("Titulo", incident?.description.orEmpty())
        DataBox("descripcion", incident?.description.orEmpty())
        DataBox("Tipo", incident?.registration_medium.orEmpty())
        DataBox("Estado", incident?.status.orEmpty())
        DataBox("Creación", incident?.date.orEmpty())
        DataBox("Ultima Actualización", incident?.date.orEmpty())

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
        IncidentDetailsScreen(incidentId = 1, incident = null)
    }
}
