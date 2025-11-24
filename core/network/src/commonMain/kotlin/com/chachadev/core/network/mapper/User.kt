package com.chachadev.core.network.mapper

import com.chachadev.core.network.dto.AddressDto
import com.chachadev.core.network.dto.LocationDto
import com.chachadev.core.network.dto.UserDetailsDto
import com.chachadev.core.network.dto.UserRegistrationDto
import model.Account
import model.Address
import model.Location
import model.User

fun UserDetailsDto.toDomain() = User(
    id = id ?: "",
    country = fullName ?: "",
    email = email ?: "",
    fullName = fullName ?: "",
    permission = permission ?: 1,
    username = username ?: "",
    addresses = addresses?.filterNotNull()?.map { it.toDomain() } ?: emptyList(),
    phoneNumber = phoneNumber ?: ""
)

fun Account.toUserRegistrationDto(): UserRegistrationDto {
    return UserRegistrationDto(
        fullName = fullName,
        username = username,
        password = password,
        email = email,
        phone = phone,
        address = address
    )
}


fun AddressDto.toDomain(): Address {
    return Address(
        id = id ?: "",
        location = location?.toDomain(),
        address = address ?: "",
    )
}


fun LocationDto.toDomain() = Location(
    latitude = latitude ?: 0.0,
    longitude = longitude ?: 0.0
)