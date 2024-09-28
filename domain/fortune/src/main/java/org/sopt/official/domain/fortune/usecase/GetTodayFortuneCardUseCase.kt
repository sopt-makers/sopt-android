package org.sopt.official.domain.fortune.usecase

import org.sopt.official.domain.fortune.model.TodayFortuneCard
import org.sopt.official.domain.fortune.repository.FortuneRepository
import javax.inject.Inject

class GetTodayFortuneCardUseCase @Inject constructor(
    private val fortuneRepository: FortuneRepository,
) {
    suspend operator fun invoke(): TodayFortuneCard = fortuneRepository.fetchTodayFortuneCard()
}
