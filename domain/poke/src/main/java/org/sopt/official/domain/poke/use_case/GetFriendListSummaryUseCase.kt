package org.sopt.official.domain.poke.use_case

import org.sopt.official.domain.poke.entity.ApiResult
import org.sopt.official.domain.poke.entity.apiResult
import org.sopt.official.domain.poke.entity.FriendListSummary
import org.sopt.official.domain.poke.repository.PokeRepository
import javax.inject.Inject

class GetFriendListSummaryUseCase @Inject constructor(
    private val repository: PokeRepository,
) {
    suspend operator fun invoke(): ApiResult<FriendListSummary> {
        return apiResult {
            repository.getFriendListSummary()
        }
    }
}