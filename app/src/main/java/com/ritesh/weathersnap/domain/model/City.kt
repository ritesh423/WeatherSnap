package com.ritesh.weathersnap.domain.model

data class City(
    val name: String,
    val country: String?,
    val latitude: Double,
    val longitude: Double
) {
    val displayName: String
        get() = country?.let { "$name, $it" } ?: name
}
