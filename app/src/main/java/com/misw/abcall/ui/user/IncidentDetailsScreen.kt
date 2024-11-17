package com.misw.abcall.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.misw.abcall.R
import com.misw.abcall.domain.IncidentDTO
import com.misw.abcall.ui.theme.ABCallTheme

@Composable
fun IncidentDetailsScreen(incidentId: Int? = null, incident: IncidentDTO?) {
    Column(
        modifier = Modifier
            .testTag("incidentDetailScreen")
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = stringResource(R.string.datos_del_incidente),
            style = MaterialTheme.typography.titleLarge,
        )

        DataBox(stringResource(R.string.id), incident?.id.orEmpty())
        DataBox(stringResource(R.string.titular), incident?.description.orEmpty())
        DataBox(stringResource(R.string.descripcion), incident?.description.orEmpty())
        DataBox(stringResource(R.string.tipo), incident?.registration_medium.orEmpty())
        DataBox(stringResource(R.string.estado), incident?.status.orEmpty())
        DataBox(stringResource(R.string.creaci_n), incident?.date.orEmpty())
        DataBox(stringResource(R.string.ultima_actualizaci_n), incident?.date.orEmpty())

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(70.dp))

    }
}

@Composable
private fun DataBox(title: String, description: String){
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
