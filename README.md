# Travel Planner - Digital Companion

A modern Android application designed to eliminate travel anxiety by providing a unified "Command Center" for destination discovery, contextual planning, and guaranteed offline access.

## 🎯 Project Goal
The primary objective is to provide travelers with a reliable, single-app experience to discover new cities, plan itineraries based on real-time weather and local attractions, and ensure all data is accessible even without an internet connection.

### Core Objectives:
*   **Data Harmony:** Combine Google Places, OpenTripMap, and OpenWeatherMap into a consistent UI.
*   **Offline-First:** Use "Prefetching" to ensure that once a trip is saved, it is fully functional without a data connection.
*   **Contextual Intelligence:** Help users plan by showing weather and attractions together.

---

## 💡 Assumptions & Definitions

### 1. Prefetching vs. Syncing
*   **Prefetching:** fetching places metadata into local Room database
*   **Syncing:** The background process of keeping saved itineraries up-to-date.

### 2. Location Logic
*   **Dashboard Weather:** Based on the device's current GPS location.
*   **Discovery Weather:** Based on the coordinates of the destination selected via the search bar.

---

## 📱 App Flow & Screen Layouts

### 1. Home Dashboard
*   **Role:** The launchpad for navigation and current context.
*   **Layout:**
    *   **Header:** Dynamic Weather Card (Current location temp/condition).
    *   **Search Bar:** "Where to next?" (Navigate to Search).
    *   **Body:** "My Saved Trips" - A list of itineraries stored in Room DB.
    *   **Indicators:** Offline-ready status icons for each saved trip.

### 2. Destination Search
*   **Role:** Entry point for discovery.
*   **Layout:**
    *   Full-screen Search Input.
    *   Real-time suggestions (Google Places Autocomplete).
    *   Selection navigates to the **Discovery Hub**.

### 3. Discovery Hub
*   **Role:** The planning workspace where data sources merge.
*   **Layout:**
    *   **City Overview:** Selected city name + 5-day weather forecast (OpenWeather).
    *   **Exploration Section:** Categories of nearby attractions (Museums, Parks, Landmarks) from OpenTripMap.
    *   **Interaction:** Users can tap attractions to see details or add them to a draft itinerary.

### 4. Itinerary View (Offline Ready)
*   **Role:** The user's finalized travel plan for a specific destination.
*   **Layout:**
    *   **Saved Places List:** Displays only the attractions explicitly added by the user.
    *   **Offline Data:** Metadata for these places is loaded from the local **Room Database**.
    *   **Map Integration:** Pins for all selected locations.

### 5. Offline Experience
*   **Role:** Reliability in dead zones.
*   **Layout:**
    *   If no internet is detected, the app restricts navigation to "Saved Trips."
    *   Loads all data (Place Details) directly from the **Room Database** instead of Retrofit services.

---

## 🛠 Tech Stack (Compatibility)
*   **Min SDK:** 24 (Android 7.0) - required for Places API (New).
*   **UI:** Jetpack Compose with Material 3.
*   **Local Storage:** Room Database (for Offline Access/Prefetching).
*   **Networking:** Retrofit + OkHttp (Weather & OpenTripMap).
*   **SDKs:** Google Places SDK (New).
*   **Concurrency:** Kotlin Coroutines & Flow.
