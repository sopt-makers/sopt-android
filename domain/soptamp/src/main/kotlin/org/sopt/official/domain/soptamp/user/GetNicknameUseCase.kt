package org.sopt.official.domain.soptamp.user

import org.sopt.official.domain.soptamp.repository.UserRepository
import javax.inject.Inject

class GetNicknameUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke() = repository.nickname
}
