package org.sopt.official.domain.soptamp.user

import org.sopt.official.domain.soptamp.repository.UserRepository
import javax.inject.Inject

class UpdateProfileMessageUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(new: String) = userRepository.updateProfileMessage(new)
}
