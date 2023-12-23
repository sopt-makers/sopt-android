package org.sopt.official.data.mapper

import org.sopt.official.data.model.poke.response.PokeNotificationResponse
import org.sopt.official.domain.entity.poke.PokeNotificationItem

class PokeNotificationItemMapper {
    fun toPokeNotificationItem(responseItem: PokeNotificationResponse.PokeNotification) : PokeNotificationItem {
        return PokeNotificationItem(
            userId = responseItem.userId,
            profileImage = responseItem.profileImage,
            name = responseItem.name,
            message = responseItem.message,
            generation = responseItem.generation,
            part = responseItem.part,
            pokeNum = responseItem.pokeNum,
            relationName = responseItem.relationName,
            mutual = responseItem.mutual,
            isFirstMeet = responseItem.isFirstMeet,
            isAlreadyPoke = responseItem.isAlreadyPoke
        )
    }
}