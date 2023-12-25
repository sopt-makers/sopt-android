package org.sopt.official.domain.poke.use_case

import org.sopt.official.domain.poke.entity.ApiResult
import org.sopt.official.domain.poke.entity.PokeNotificationList
import org.sopt.official.domain.poke.entity.apiResult
import org.sopt.official.domain.poke.repository.PokeRepository
import javax.inject.Inject

class GetPokeNotificationListUseCase @Inject constructor(
    private val repository: PokeRepository
) {
    suspend operator fun invoke(
        page: Int,
    ): ApiResult<PokeNotificationList> {
        return apiResult {
            repository.getPokeNotificationList(page)
        }
    }
}