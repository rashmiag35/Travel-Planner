package com.example.travelplanner.presentation.ui.discoveryhub.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = place.properties.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Text(
                    text = place.properties.kinds.replace("_", " ")
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingContent = {
                IconButton(onClick = onAddClick, enabled = !isAdded) {
                    if (isAdded) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Added",
                            tint = Color(0xFF4CAF50)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add to Itinerary"
                        )
                    }
                }
            }
        )
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
        AttractionItem(place = mockPlace, isAdded = true, onAddClick = {})
    }
}
