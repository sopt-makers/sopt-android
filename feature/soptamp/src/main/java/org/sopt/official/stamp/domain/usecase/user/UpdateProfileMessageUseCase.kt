package org.sopt.official.stamp.domain.usecase.user

import org.sopt.official.stamp.domain.repository.UserRepository
import javax.inject.Inject

class UpdateProfileMessageUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(new: String) = userRepository.updateProfileMessage(new)
}
