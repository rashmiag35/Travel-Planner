package com.example.travelplanner.domain.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val coord: Coord,
    val weather: List<WeatherInfo>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val rain: Rain? = null,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

data class Coord(
    val lon: Double,
    val lat: Double
)

data class WeatherInfo(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int? = null,
    val grnd_level: Int? = null
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double? = null
)

data class Rain(
    @SerializedName("1h") val oneH: Double? = null
)

data class Clouds(
    val all: Int
)

data class Sys(
    val type: Int? = null,
    val id: Int? = null,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: CityInfo
)

data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<WeatherInfo>,
    @SerializedName("dt_txt") val dtTxt: String
)

data class CityInfo(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val timezone: Int
)
