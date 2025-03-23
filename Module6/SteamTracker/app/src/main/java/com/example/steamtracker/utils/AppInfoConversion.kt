package com.example.steamtracker.utils

import com.example.steamtracker.model.AppInfo
import com.example.steamtracker.room.entities.AppInfoEntity

fun AppInfo.toAppInfoEntity(categoryId: String): AppInfoEntity {
    return AppInfoEntity(
        id = this.id,
        categoryId = categoryId,
        type = this.type,
        name = this.name,
        discounted = this.discounted,
        discountPercent = this.discountPercent,
        originalPrice = this.originalPrice,
        finalPrice = this.finalPrice,
        currency = this.currency,
        largeCapsuleImage = this.largeCapsuleImage,
        smallCapsuleImage = this.smallCapsuleImage,
        windowsAvailable = this.windowsAvailable,
        macAvailable = this.macAvailable,
        linuxAvailable = this.linuxAvailable,
        streamingVideoAvailable = this.streamingVideoAvailable,
        discountExpiration = this.discountExpiration,
        headerImage = this.headerImage,
        headline = this.headline,
        controllerSupport = this.controllerSupport,
        purchasePackage = this.purchasePackage
    )
}

fun AppInfoEntity.toAppInfo(): AppInfo {
    return AppInfo(
        id = this.id,
        type = this.type,
        name = this.name,
        discounted = this.discounted,
        discountPercent = this.discountPercent,
        originalPrice = this.originalPrice,
        finalPrice = this.finalPrice,
        currency = this.currency,
        largeCapsuleImage = this.largeCapsuleImage,
        smallCapsuleImage = this.smallCapsuleImage,
        windowsAvailable = this.windowsAvailable,
        macAvailable = this.macAvailable,
        linuxAvailable = this.linuxAvailable,
        streamingVideoAvailable = this.streamingVideoAvailable,
        discountExpiration = this.discountExpiration,
        headerImage = this.headerImage,
        headline = this.headline,
        controllerSupport = this.controllerSupport,
        purchasePackage = this.purchasePackage
    )
}

fun List<AppInfo>.toAppInfoEntityList(categoryId: String): List<AppInfoEntity> {
    return this.map { it.toAppInfoEntity(categoryId) }
}

fun List<AppInfoEntity>.toAppInfoList(): List<AppInfo> {
    return this.map { it.toAppInfo() }
}
