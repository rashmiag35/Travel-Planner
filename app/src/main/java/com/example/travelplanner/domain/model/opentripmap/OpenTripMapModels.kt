package com.example.travelplanner.domain.model.opentripmap

data class PlaceResponse(
    val xid: String,
    val name: String,
    val dist: Double,
    val rate: Int,
    val kinds: String,
    val point: Point
)

data class Point(
    val lat: Double,
    val lon: Double
)

data class PlaceFeature(
    val id: String,
    val properties: PlaceProperties,
    val geometry: Geometry
)

data class PlaceProperties(
    val xid: String,
    val name: String,
    val dist: Double,
    val rate: Int,
    val kinds: String
)

data class Geometry(
    val coordinates: List<Double>
)

data class PlaceDetails(
    val xid: String,
    val name: String,
    val address: Address? = null,
    val rate: String? = null,
    val wikidata: String? = null,
    val kinds: String? = null,
    val info: Info? = null,
    val image: String? = null,
    val preview: Preview? = null,
    val wikipedia_extracts: WikipediaExtracts? = null
)

data class Address(
    val city: String?,
    val country: String?
)

data class Info(
    val desc: String?
)

data class Preview(
    val source: String?,
    val height: Int?,
    val width: Int?
)

data class WikipediaExtracts(
    val title: String?,
    val text: String?,
    val html: String?
)
