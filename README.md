# Travel Planner

A modern Android application that acts as a unified travel command centre — combining real-time weather, destination discovery, and offline-ready itinerary management into a single cohesive experience.

---

## Table of Contents

- [Assumptions](#assumptions)
- [Features](#features)
- [App Flow](#app-flow)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Tech Stack](#tech-stack)
- [API Keys & Security](#api-keys--security)
- [Database Schema](#database-schema)
- [Getting Started](#getting-started)

---

## Assumptions

### 1. Prefetching vs. Syncing
- **Prefetching** means eagerly writing place metadata (name, kinds, lat/lon, XID) into the local Room database at the moment the user taps **+** on an attraction. No background sync is performed; the saved snapshot is treated as the source of truth for offline use.
- **Syncing** (not yet implemented) would be the background process of refreshing saved itineraries with updated remote data after they are stored.

### 2. Location Logic
- **Dashboard weather** is derived from the device's current GPS coordinates, resolved via `LocationManager.getLastKnownLocation()` across all enabled providers, and fetched from OpenWeatherMap on every cold start.
- **Discovery Hub weather** is based on the coordinates of the destination chosen through the Google Places search — not the device's physical location.

### 3. Itinerary Auto-Creation
- An itinerary record is **automatically created** the first time any attraction is saved for a given city. The itinerary name follows the pattern `"<CityName> Trip"`. If an itinerary whose name contains the city name already exists, the new place is appended to it rather than creating a duplicate.

### 4. Offline Behaviour
- The **Itinerary Details** and **Maps** screens work fully offline because all required data (name, category, lat/lon) is persisted in Room at save time.
- The **Dashboard** weather card, **Discovery Hub** forecast, and attraction list all require a live network connection. No caching of network responses is implemented beyond what the operating system may provide.

### 5. API Key Security
- All three API keys (Google Maps/Places, OpenWeatherMap, OpenTripMap) are stored exclusively in the native C++ layer (`native-lib.cpp`) and returned via JNI. They are never present as plain strings in Kotlin/Java bytecode or `gradle.properties`.

### 6. OpenTripMap Fallback
- If the OpenTripMap `/places/radius` call fails (e.g. quota exceeded or network error), the Discovery Hub falls back to a hardcoded set of four mock places so the UI remains usable during development.

---

## Features

| Feature | Description |
|---|---|
| **Location-aware weather** | Reads the device GPS on launch and fetches current conditions (temp, feels-like, humidity, wind, high/low) from OpenWeatherMap |
| **Destination search** | Google Places Autocomplete surfaces city, landmark or country results in real time as the user types |
| **5-day forecast** | Per-destination 3-hourly forecast data collapsed to one card per day with a contextual weather icon |
| **Nearby attractions** | OpenTripMap returns up to 30 points of interest within 1 km of the selected destination, categorised by kind (museums, parks, historic, arts, …) |
| **Itinerary builder** | Tapping the **+** button on any attraction saves it to a named city trip; the itinerary is created automatically on first save |
| **Offline-ready itineraries** | All saved places (name, category, lat/lon, XID) are persisted in the local Room database and readable without a network connection |
| **Map view** | Any saved place can be opened in a full-screen Google Map with a centred marker |
| **Time-based greeting** | The dashboard header greets the user by time of day (morning / afternoon / evening) |
| **Branded permission flow** | A dedicated screen guides the user through location permission and GPS activation before the dashboard loads |
| **NDK-secured API keys** | All three API keys are returned from a native C++ library (`libtravelplanner.so`) — never embedded as plain strings in the APK |

---

## App Flow

```
App Launch
    │
    ▼
┌─────────────────────────────┐
│  Location Permission Screen │  ── requests ACCESS_FINE / COARSE_LOCATION
│  (LocationPermissionScreen) │  ── if GPS off → opens Location Settings
└─────────────┬───────────────┘
              │ permission granted + GPS on
              ▼
┌─────────────────────────────┐
│       Dashboard             │  ── fetches weather for current GPS coords
│    (DashboardScreen)        │  ── lists saved itineraries from Room DB
└──────┬──────────────┬───────┘
       │              │
  "Where to          tap saved
   next?"             trip
       │              │
       ▼              ▼
┌──────────────┐  ┌─────────────────────────┐
│  Destination │  │   Itinerary Details     │  ── loads SavedPlace rows
│  Search      │  │  (ItineraryDetailsScreen)│    from Room (offline-ready)
│  (Search-    │  └────────────┬────────────┘
│  Destination)│               │ tap a place
└──────┬───────┘               ▼
       │              ┌─────────────────┐
  select a place      │   Maps Screen   │  ── Google Map + Marker
       │              │  (MapsScreen)   │
       ▼              └─────────────────┘
┌──────────────────────────┐
│      Discovery Hub       │  ── 5-day forecast (OpenWeatherMap)
│   (DiscoveryHubScreen)   │  ── nearby attractions (OpenTripMap)
└──────────────────────────┘
       │ tap "+"
       ▼
  SavedPlace written to Room DB
  Itinerary auto-created if absent
  "Added … to your trip!" snackbar
```

---

## Architecture

The app follows **MVVM** with a clean separation between **Presentation**, **Domain** and **Data** layers.

```
┌──────────────────────────────────────────────────────────────────────┐
│                        PRESENTATION LAYER                            │
│                                                                      │
│  ┌──────────────┐  ┌───────────────┐  ┌────────────────────────┐   │
│  │  LocationVM  │  │  DashboardVM  │  │  SearchDestinationVM   │   │
│  │  ────────── │  │  ──────────── │  │  ──────────────────── │   │
│  │ permState    │  │ savedItiner-  │  │ predictions            │   │
│  │ weatherData  │  │ aries: Flow   │  │ selectedPlace          │   │
│  │ currentLoc   │  │               │  │ isLoading              │   │
│  └──────┬───────┘  └──────┬────────┘  └──────────┬─────────────┘   │
│         │                 │                       │                  │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                      UI Screens (Compose)                    │   │
│  │  LocationPermission → Dashboard → SearchDestination          │   │
│  │                             ↓              ↓                 │   │
│  │                     ItineraryDetails   DiscoveryHub          │   │
│  │                             ↓              ↓                 │   │
│  │                         MapsScreen   (AttractionItem,        │   │
│  │                                       ForecastRow)           │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  ┌────────────────┐  ┌──────────────────┐                          │
│  │DiscoveryHubVM  │  │ItineraryDetails  │                          │
│  │ ─────────────  │  │VM                │                          │
│  │ forecastData   │  │ ───────────────  │                          │
│  │ nearbyPlaces   │  │ places: List     │                          │
│  │ savedPlaceIds  │  │ <SavedPlace>     │                          │
│  │ isLoading      │  │                  │                          │
│  └────────────────┘  └──────────────────┘                          │
└────────────────────────────┬─────────────────────────────────────────┘
                             │  StateFlow / coroutines
┌────────────────────────────▼─────────────────────────────────────────┐
│                         DOMAIN LAYER                                  │
│                                                                       │
│  Models                          State                               │
│  ─────────────────────────────   ──────────────────────────────────  │
│  WeatherResponse, ForecastItem   PermissionUiState                   │
│  PlaceFeature, PlaceProperties   (Checking | Granted |               │
│  PlaceDetails, SavedPlace        ShowRationale | DeniedPermanently)  │
│  Itinerary                                                            │
└────────────────────────────┬─────────────────────────────────────────┘
                             │
┌────────────────────────────▼─────────────────────────────────────────┐
│                          DATA LAYER                                   │
│                                                                       │
│   NETWORK (Retrofit + OkHttp)         LOCAL (Room)                   │
│   ───────────────────────────         ──────────────────────────     │
│                                                                       │
│   RetrofitClient                      TravelDatabase                 │
│   ├── WeatherApiService               ├── ItineraryDao               │
│   │   GET /weather  (current)         │   getAllItineraries(): Flow  │
│   │   GET /forecast (5-day)           │   insertItinerary()          │
│   │                                   │   deleteItinerary()          │
│   └── OpenTripMapApiService           │   getPlacesForItinerary()    │
│       GET /places/radius              │   insertSavedPlace()         │
│       GET /places/xid/{xid}           │   deleteSavedPlace()         │
│                                       │                               │
│   Google Places SDK (new)             Tables                         │
│   ├── FindAutocompletePredictions     ├── itineraries                │
│   └── FetchPlace                      │   id, name, description,     │
│                                       │   createdAt                  │
│   NativeLib (NDK / C++)               │                               │
│   ├── getMapsApiKey()                 └── saved_places               │
│   ├── getWeatherApiKey()                  id, itineraryId (FK),      │
│   └── getOpenTripMapApiKey()              xid, name, kinds,          │
│                                           latitude, longitude        │
└──────────────────────────────────────────────────────────────────────┘
```

### Navigation Graph

All screen transitions are managed by a single **Jetpack Navigation** `NavHost` inside `MainActivity`.

```
checkLocationPermission
        │ (popUpTo + inclusive)
        ▼
    dashboard
     ├──► searchDestination
     │            └──► discoveryHub/{lat}/{lon}/{name}
     │                        │ (back stack)
     └──► itineraryDetails/{tripName}
                    └──► maps/{lat}/{lon}/{name}
```

---

## Project Structure

```
app/src/main/
├── cpp/
│   ├── CMakeLists.txt
│   └── native-lib.cpp          ← returns API keys from compiled C++ code
│
└── java/com/example/travelplanner/
    ├── app/
    │   └── TravelPlannerApp.kt ← @HiltAndroidApp, Places SDK init
    │
    ├── data/
    │   ├── local/
    │   │   ├── TravelDatabase.kt           ← Room DB (singleton)
    │   │   ├── dao/
    │   │   │   └── ItineraryDao.kt         ← CRUD + Flow queries
    │   │   └── entity/
    │   │       ├── Itinerary.kt            ← id, name, description, createdAt
    │   │       └── SavedPlace.kt           ← id, itineraryId (FK), xid, name, kinds, lat, lon
    │   └── network/
    │       ├── RetrofitClient.kt           ← two Retrofit instances (weather + OTM)
    │       ├── WeatherApiService.kt        ← GET /weather, GET /forecast
    │       └── OpenTripMapApiService.kt    ← GET /places/radius, GET /places/xid/{xid}
    │
    ├── domain/
    │   ├── model/
    │   │   ├── Weather.kt                  ← WeatherResponse, ForecastResponse, …
    │   │   └── opentripmap/
    │   │       └── OpenTripMapModels.kt    ← PlaceFeature, PlaceDetails, …
    │   └── repo/
    │       └── PermissionUiState.kt        ← sealed interface for permission FSM
    │
    ├── presentation/
    │   └── ui/
    │       ├── MainActivity.kt             ← NavHost, single Activity
    │       ├── theme/
    │       │   ├── Color.kt                ← teal / amber / sky brand palette
    │       │   ├── Theme.kt                ← MaterialTheme wrapper (dynamic color off)
    │       │   └── Type.kt                 ← full 13-style M3 typography scale
    │       ├── checklocation/
    │       │   ├── LocationPermissionScreen.kt
    │       │   └── LocationVM.kt           ← permission FSM, GPS fetch, weather fetch
    │       ├── dashboard/
    │       │   ├── DashboardScreen.kt      ← LazyColumn: greeting + weather + search + trips
    │       │   ├── DashboardVM.kt          ← getAllItineraries() as StateFlow
    │       │   └── components/
    │       │       ├── WeatherHeaderCard.kt  ← temp, feels-like, humidity, wind, high/low
    │       │       ├── SearchBarMock.kt      ← tappable pill → SearchDestination
    │       │       ├── TripItemCard.kt       ← trip row with icon badge + chevron
    │       │       └── SavedTripsList.kt     ← empty-state + list (used standalone)
    │       ├── searchdestination/
    │       │   ├── SearchDestination.kt    ← OutlinedTextField + autocomplete list
    │       │   └── SearchDestinationVM.kt  ← Google Places Autocomplete + FetchPlace
    │       ├── discoveryhub/
    │       │   ├── DiscoveryHubScreen.kt   ← LazyColumn: forecast + attractions
    │       │   ├── DiscoveryHubVM.kt       ← loadCityData(), savePlaceToItinerary()
    │       │   └── components/
    │       │       ├── ForecastRow.kt        ← horizontal card strip (5 days)
    │       │       └── AttractionItem.kt     ← place card with category chip + add button
    │       ├── itinerarydetails/
    │       │   ├── ItineraryDetailsScreen.kt ← place list (Room, offline-ready)
    │       │   └── ItineraryDetailsVM.kt     ← loadItineraryDetails() via Flow
    │       └── maps/
    │           └── MapsScreen.kt           ← GoogleMap + single Marker
    │
    └── util/
        └── NativeLib.kt                    ← JNI bridge to libtravelplanner.so
```

---

## Tech Stack

| Category | Library / Tool |
|---|---|
| **Language** | Kotlin |
| **UI** | Jetpack Compose + Material 3 |
| **Navigation** | Jetpack Navigation Compose |
| **Architecture** | MVVM · AndroidViewModel · StateFlow |
| **DI** | Dagger Hilt (+ KSP) |
| **Local DB** | Room 2.x (entities: `itineraries`, `saved_places`) |
| **Networking** | Retrofit 2 + OkHttp (logging interceptor, 60 s timeout) |
| **Serialisation** | Gson converter |
| **Maps** | Google Maps Compose + Play Services Maps |
| **Places** | Google Places SDK (New) — Autocomplete + FetchPlace |
| **Weather API** | OpenWeatherMap `/data/2.5/weather` & `/forecast` |
| **Attractions API** | OpenTripMap `/0.1/en/places/radius` & `/places/xid/{xid}` |
| **Key Security** | Android NDK / CMake — keys stored in native C++ library |
| **Min SDK** | 24 (Android 7.0) |
| **Target / Compile SDK** | 37 |
| **Build** | Gradle Kotlin DSL, Version Catalogs |

---

## API Keys & Security

API keys are **never stored as plain strings** in Java/Kotlin source or `gradle.properties`. They are compiled into a native shared library (`libtravelplanner.so`) via CMake and exposed through JNI:

```
NativeLib.getMapsApiKey()        → Google Maps / Places key
NativeLib.getWeatherApiKey()     → OpenWeatherMap key
NativeLib.getOpenTripMapApiKey() → OpenTripMap key
```

The native library is loaded once at class initialisation:

```kotlin
// util/NativeLib.kt
object NativeLib {
    init { System.loadLibrary("travelplanner") }
    external fun getMapsApiKey(): String
    external fun getWeatherApiKey(): String
    external fun getOpenTripMapApiKey(): String
}
```

The Google Places SDK is initialised with the new Places API in `TravelPlannerApp.onCreate()`:

```kotlin
Places.initializeWithNewPlacesApiEnabled(applicationContext, NativeLib.getMapsApiKey())
```

---

## Database Schema

### `itineraries`

| Column | Type | Notes |
|---|---|---|
| `id` | `INTEGER` PK autoincrement | |
| `name` | `TEXT` | e.g. `"Paris Trip"` |
| `description` | `TEXT` nullable | |
| `createdAt` | `INTEGER` | epoch ms — used for sorting |

### `saved_places`

| Column | Type | Notes |
|---|---|---|
| `id` | `INTEGER` PK autoincrement | |
| `itineraryId` | `INTEGER` FK → `itineraries.id` | CASCADE delete |
| `xid` | `TEXT` | OpenTripMap unique identifier |
| `name` | `TEXT` | display name |
| `kinds` | `TEXT` nullable | comma-separated categories |
| `latitude` | `REAL` | |
| `longitude` | `REAL` | |

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- NDK installed (CMake 3.22.1)
- A `local.properties` file (auto-generated by Android Studio — no manual edits required)

### Build

1. **Clone** the repository.
2. **Add API keys** to the native C++ source file (`app/src/main/cpp/native-lib.cpp`) in the three `extern "C"` functions.
3. **Sync** Gradle.
4. **Run** on a device or emulator with Android 7.0+ (API 24+).

> **Note:** The app requests `ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION` at runtime. A real device with GPS is recommended for accurate weather on the dashboard; the emulator's extended controls can be used to inject a mock location.

> **Note — Google Maps not rendering:** Map tiles will not load because the Maps SDK for Android requires an active billing account on Google Cloud. The **location marker is still placed correctly** using the saved lat/lon. To fix, enable billing and the Maps SDK for Android on the [Google Cloud Console](https://console.cloud.google.com).

> **Note — Destination search not working:** The Google Places API key stored in `native-lib.cpp` may have exceeded its quota. Generate a new key on the [Google Cloud Console](https://console.cloud.google.com) and replace it in the native C++ file.
