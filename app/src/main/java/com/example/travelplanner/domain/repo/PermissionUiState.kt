package com.example.travelplanner.domain.repo

sealed interface PermissionUiState {
    object Checking : PermissionUiState
    object Granted : PermissionUiState
    object ShowRationale : PermissionUiState
    object DeniedPermanently : PermissionUiState
}