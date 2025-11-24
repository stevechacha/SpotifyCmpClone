package com.chachadev.core.network.mapper

import com.chachadev.core.network.dto.ApiProducts
import com.chachadev.core.network.dto.ApiResponse
import model.DomainModel
import model.ProductsDomainModel

/**
 * Extension function to convert DomainModel to ApiResponse
 * Useful when sending data to the API
 */
fun DomainModel.toDto(): ApiResponse {
    return ApiResponse(
        list = list.map { it.toDto() }
    )
}


/**
 * Extension function to convert ProductsDomainModel to ApiProducts
 * Useful when sending individual product data to the API
 */
fun ProductsDomainModel.toDto(): ApiProducts {
    return ApiProducts(
        id = id,
        title = title,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        category = category,
        thumbnail = thumbnail
    )
}

/**
 * Extension function to convert ApiResponse to DomainModel
 * This is the main mapper used when receiving data from the API
 */
fun ApiResponse.toDomain(): DomainModel {
    return DomainModel(
        list = list.map { it.toDomain() }
    )
}

/**
 * Extension function to convert ApiProducts to ProductsDomainModel
 * Maps individual product from API response to domain model
 */
fun ApiProducts.toDomain(): ProductsDomainModel {
    return ProductsDomainModel(
        id = id,
        title = title,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        category = category,
        thumbnail = thumbnail
    )
}
