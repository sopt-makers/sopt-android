package org.sopt.official.domain.poke.use_case

import org.sopt.official.domain.poke.entity.ApiResult
import org.sopt.official.domain.poke.entity.PokeFriendOfFriendList
import org.sopt.official.domain.poke.entity.apiResult
import org.sopt.official.domain.poke.repository.PokeRepository
import javax.inject.Inject

class GetPokeFriendOfFriendListUseCase @Inject constructor(
    private val repository: PokeRepository
) {
    suspend operator fun invoke(): ApiResult<List<PokeFriendOfFriendList>> {
        return apiResult {
            repository.getPokeFriendOfFriendList()
        }
    }
}