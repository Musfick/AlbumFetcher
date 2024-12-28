package com.musfick.albumfetcher.features.user.data.network.dto

data class AddressDto(
    val city: String,
    val geo: GeoDto,
    val street: String,
    val suite: String,
    val zipcode: String
)