package org.sopt.official.domain.fortune.usecase

import org.sopt.official.domain.fortune.model.TodayFortuneWord
import org.sopt.official.domain.fortune.repository.FortuneRepository
import javax.inject.Inject

class GetTodayFortuneUseCase @Inject constructor(
    private val fortuneRepository: FortuneRepository,
    private val getTodayDateUseCase: GetTodayDateUseCase,
) {

    suspend operator fun invoke(): TodayFortuneWord = fortuneRepository.fetchTodayFortuneWord(getTodayDateUseCase())
}
