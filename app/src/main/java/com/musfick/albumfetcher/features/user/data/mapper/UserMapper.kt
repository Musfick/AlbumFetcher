package com.musfick.albumfetcher.features.user.data.mapper

import com.musfick.albumfetcher.features.user.data.network.dto.UserDto
import com.musfick.albumfetcher.features.user.domain.model.Address
import com.musfick.albumfetcher.features.user.domain.model.Company
import com.musfick.albumfetcher.features.user.domain.model.Geo
import com.musfick.albumfetcher.features.user.domain.model.User

fun UserDto.toModel() = User(
    address = Address(
        city = this.address.city,
        geo = Geo(
            lat = this.address.geo.lat,
            lng = this.address.geo.lng
        ),
        street = this.address.street,
        suite = this.address.suite,
        zipcode = this.address.zipcode
    ),
    company = Company(
        bs = this.company.bs,
        catchPhrase = this.company.catchPhrase,
        name = this.company.name
    ),
    email = this.email,
    id = this.id,
    name = this.name,
    phone = this.phone,
    username = this.username,
    website = this.website
)