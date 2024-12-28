package com.musfick.albumfetcher.features.user.data.network.dto

data class UserDto(
    val address: AddressDto,
    val company: CompanyDto,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val username: String,
    val website: String
)