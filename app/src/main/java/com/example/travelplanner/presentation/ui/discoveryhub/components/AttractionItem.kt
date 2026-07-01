package com.example.travelplanner.presentation.ui.discoveryhub.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travelplanner.domain.model.opentripmap.Geometry
import com.example.travelplanner.domain.model.opentripmap.PlaceFeature
import com.example.travelplanner.domain.model.opentripmap.PlaceProperties
import java.util.Locale

@Composable
fun AttractionItem(
    place: PlaceFeature,
    isAdded: Boolean = false,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isAdded)
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = place.properties.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                val categoryLabel = place.properties.kinds
                    .split(",")
                    .firstOrNull { it.isNotBlank() }
                    ?.trim()
                    ?.replace("_", " ")
                    ?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    } ?: ""

                if (categoryLabel.isNotEmpty()) {
                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                text = categoryLabel,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        modifier = Modifier.padding(top = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                            labelColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ),
                        border = null
                    )
                }
            }

            FilledTonalIconButton(
                onClick = onAddClick,
                enabled = !isAdded,
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = if (isAdded)
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                    else
                        MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                if (isAdded) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Added",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add to Itinerary",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AttractionItemPreview() {
    val mockPlace = PlaceFeature(
        id = "1",
        properties = PlaceProperties(
            xid = "W123",
            name = "Eiffel Tower",
            dist = 100.0,
            rate = 3,
            kinds = "historic,monuments"
        ),
        geometry = Geometry(coordinates = listOf(2.2945, 48.8584))
    )
    MaterialTheme {
        Column {
            AttractionItem(place = mockPlace, isAdded = false, onAddClick = {})
            AttractionItem(place = mockPlace, isAdded = true, onAddClick = {})
        }
    }
}
