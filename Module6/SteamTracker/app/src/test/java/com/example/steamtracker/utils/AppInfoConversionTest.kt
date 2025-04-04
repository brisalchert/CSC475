package com.example.steamtracker.utils

import com.example.steamtracker.model.AppInfo
import com.example.steamtracker.room.entities.AppInfoEntity
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class AppInfoConversionTest {
    private lateinit var appInfo: AppInfo
    private lateinit var appInfoEntity: AppInfoEntity

    @Before
    fun setup() {
        appInfo = AppInfo(
            id = 12,
            type = 7,
            name = "name",
            discounted = true,
            discountPercent = 50,
            originalPrice = 4999,
            finalPrice = 2499,
            currency = "USD",
            largeCapsuleImage = "large capsule image",
            smallCapsuleImage = "small capsule image",
            windowsAvailable = true,
            macAvailable = false,
            linuxAvailable = false,
            streamingVideoAvailable = false,
            discountExpiration = 1202020202002L,
            headerImage = "header image",
            headline = "headline",
            controllerSupport = "controller support",
            purchasePackage = 2
        )

        appInfoEntity = AppInfoEntity(
            id = 12,
            categoryId = "category",
            type = 7,
            name = "name",
            discounted = true,
            discountPercent = 50,
            originalPrice = 4999,
            finalPrice = 2499,
            currency = "USD",
            largeCapsuleImage = "large capsule image",
            smallCapsuleImage = "small capsule image",
            windowsAvailable = true,
            macAvailable = false,
            linuxAvailable = false,
            streamingVideoAvailable = false,
            discountExpiration = 1202020202002L,
            headerImage = "header image",
            headline = "headline",
            controllerSupport = "controller support",
            purchasePackage = 2
        )
    }

    @Test
    fun appInfo_toAppInfoEntity_verifyAppInfoEntity() {
        assertEquals(
            appInfoEntity,
            appInfo.toAppInfoEntity("category")
        )
    }

    @Test
    fun appInfoEntity_toAppInfo_verifyAppInfo() {
        assertEquals(
            appInfo,
            appInfoEntity.toAppInfo()
        )
    }
}
