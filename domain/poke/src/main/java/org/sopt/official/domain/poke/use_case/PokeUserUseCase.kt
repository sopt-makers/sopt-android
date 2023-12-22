package org.sopt.official.domain.poke.use_case

import org.sopt.official.domain.poke.entity.ApiResult
import org.sopt.official.domain.poke.entity.apiResult
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.repository.PokeRepository
import javax.inject.Inject

class PokeUserUseCase @Inject constructor(
    private val repository: PokeRepository,
) {
    suspend operator fun invoke(
        userId: Int,
        message: String,
    ): ApiResult<PokeUser> {
        return apiResult {
            repository.pokeUser(
                userId = userId,
                message = message,
            )
        }
    }
}