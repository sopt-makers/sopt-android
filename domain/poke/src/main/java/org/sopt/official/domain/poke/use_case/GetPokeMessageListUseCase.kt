package org.sopt.official.domain.poke.use_case

import org.sopt.official.domain.poke.entity.ApiResult
import org.sopt.official.domain.poke.entity.PokeMessageList
import org.sopt.official.domain.poke.entity.apiResult
import org.sopt.official.domain.poke.repository.PokeRepository
import org.sopt.official.domain.poke.type.PokeMessageType
import javax.inject.Inject

class GetPokeMessageListUseCase @Inject constructor(
    private val repository: PokeRepository,
) {
    suspend operator fun invoke(
        messageType: PokeMessageType,
    ): ApiResult<PokeMessageList> {
        return apiResult {
            repository.getPokeMessageList(
                messageType = messageType,
            )
        }
    }
}