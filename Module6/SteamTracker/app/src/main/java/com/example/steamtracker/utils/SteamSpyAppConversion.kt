package com.example.steamtracker.utils

import com.example.steamtracker.model.SteamSpyAppRequest
import com.example.steamtracker.room.entities.SteamSpyAppEntity
import com.example.steamtracker.room.entities.TagEntity

fun SteamSpyAppRequest.toSteamSpyAppEntity(): SteamSpyAppEntity {
    return SteamSpyAppEntity(
        appid = this.appid,
        name = this.name,
        developer = this.developer,
        publisher = this.publisher,
        scoreRank = this.scoreRank,
        positive = this.positive,
        negative = this.negative,
        userscore = this.userscore,
        owners = this.owners,
        averageForever = this.averageForever,
        average2Weeks = this.average2Weeks,
        medianForever = this.medianForever,
        median2Weeks = this.median2Weeks,
        price = this.price,
        initialprice = this.initialprice,
        discount = this.discount,
        ccu = this.ccu,
        languages = this.languages,
        genre = this.genre,
        lastUpdated = System.currentTimeMillis()
    )
}

fun SteamSpyAppEntity.toSteamSpyAppRequest(tags: List<TagEntity>?): SteamSpyAppRequest {
    return SteamSpyAppRequest(
        appid = this.appid,
        name = this.name,
        developer = this.developer,
        publisher = this.publisher,
        scoreRank = this.scoreRank,
        positive = this.positive,
        negative = this.negative,
        userscore = this.userscore,
        owners = this.owners,
        averageForever = this.averageForever,
        average2Weeks = this.average2Weeks,
        medianForever = this.medianForever,
        median2Weeks = this.median2Weeks,
        price = this.price,
        initialprice = this.initialprice,
        discount = this.discount,
        ccu = this.ccu,
        languages = this.languages,
        genre = this.genre,
        tags = tags?.associate { it.tagName to it.tagCount }
    )
}
