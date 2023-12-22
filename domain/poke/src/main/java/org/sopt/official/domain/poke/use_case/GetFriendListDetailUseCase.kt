package org.sopt.official.domain.poke.use_case

import org.sopt.official.domain.poke.entity.ApiResult
import org.sopt.official.domain.poke.entity.apiResult
import org.sopt.official.domain.poke.entity.FriendListDetail
import org.sopt.official.domain.poke.repository.PokeRepository
import org.sopt.official.domain.poke.type.PokeFriendType
import javax.inject.Inject

class GetFriendListDetailUseCase @Inject constructor(
    private val repository: PokeRepository,
) {
    suspend operator fun invoke(
        type: PokeFriendType,
        page: Int,
    ): ApiResult<FriendListDetail> {
        return apiResult {
            repository.getFriendListDetail(
                type = type,
                page = page,
            )
        }
    }
}