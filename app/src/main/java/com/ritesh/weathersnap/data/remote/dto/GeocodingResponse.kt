package com.ritesh.weathersnap.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeocodingResponse(
    @SerializedName("results") val results: List<CityDto>?
)

data class CityDto(
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String?,
    @SerializedName("admin1") val admin1: String?,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)
